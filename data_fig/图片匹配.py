import os
import sys
import pickle
import torch
import numpy as np
import re
from torchvision import models, transforms
from PIL import Image, ImageTk, ImageOps, ImageEnhance
import tkinter as tk
from tkinter import filedialog, messagebox
from tqdm import tqdm
from torch.utils.data import Dataset, DataLoader
from torch import nn, optim
import multiprocessing

##############################################
# 模型训练、加载、特征提取、特征库构建等部分
##############################################

def train_contrast_model():
    """
    训练对比度适应模型：
    使用预训练的 ResNet18，在实验数据集（根据文件名中的 TEM/PFM 标签）上微调，
    将模型最后一层全连接层修改为二分类输出。
    训练完成后保存模型参数到 contrast_model.pth。
    """
    # 定义数据集，自动根据文件名包含 TEM 或 PFM 打标签
    class ContrastDataset(Dataset):
        def __init__(self, folders, transform=None):
            self.image_paths = []
            self.labels = []  # TEM: 0, PFM: 1
            self.transform = transform
            for folder in folders:
                for fname in os.listdir(folder):
                    if fname.lower().endswith(('.png', '.jpg', '.jpeg')):
                        path = os.path.join(folder, fname)
                        if "TEM" in fname.upper():
                            self.image_paths.append(path)
                            self.labels.append(0)
                        elif "PFM" in fname.upper():
                            self.image_paths.append(path)
                            self.labels.append(1)
        def __len__(self):
            return len(self.image_paths)
        def __getitem__(self, idx):
            image = Image.open(self.image_paths[idx]).convert('RGB')
            label = self.labels[idx]
            if self.transform:
                image = self.transform(image)
            return image, label

    # 定义数据预处理及数据增强操作
    train_transform = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.RandomHorizontalFlip(),
        transforms.ColorJitter(brightness=0.2, contrast=0.2),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406],
                             std=[0.229, 0.224, 0.225])
    ])

    # 训练数据文件夹（需根据实际路径修改）
    contrast_folders = [
        r".\experiment_fig\skyrmion",
        r".\experiment_fig\vortex"
    ]

    dataset = ContrastDataset(contrast_folders, transform=train_transform)
    dataloader = DataLoader(dataset, batch_size=32, shuffle=True, num_workers=0)

    # 使用预训练的 ResNet18，并将全连接层修改为二分类输出
    model_contrast = models.resnet18(pretrained=True)
    in_features = model_contrast.fc.in_features
    model_contrast.fc = nn.Linear(in_features, 2)

    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model_contrast = model_contrast.to(device)

    criterion = nn.CrossEntropyLoss()
    optimizer = optim.Adam(model_contrast.parameters(), lr=1e-4)

    num_epochs = 50
    model_contrast.train()
    for epoch in range(num_epochs):
        running_loss = 0.0
        for images, labels in dataloader:
            images = images.to(device)
            labels = labels.to(device)
            optimizer.zero_grad()
            outputs = model_contrast(images)
            loss = criterion(outputs, labels)
            loss.backward()
            optimizer.step()
            running_loss += loss.item() * images.size(0)
        epoch_loss = running_loss / len(dataset)
        print(f"Epoch {epoch+1}/{num_epochs}, Loss: {epoch_loss:.4f}")
    torch.save(model_contrast.state_dict(), "contrast_model.pth")
    print("Contrast adaptation model saved to contrast_model.pth")
    return model_contrast

def load_contrast_model(device):
    """
    加载训练好的对比度适应模型。
    """
    model_contrast = models.resnet18(pretrained=True)
    in_features = model_contrast.fc.in_features
    model_contrast.fc = nn.Linear(in_features, 2)
    model_contrast.load_state_dict(torch.load("contrast_model.pth", map_location=device))
    model_contrast.eval()
    return model_contrast

def build_feature_extractor(model_contrast, device):
    """
    构建特征提取器，将训练好的对比度适应模型的全连接层替换为 Identity，
    以提取中间特征向量。
    """
    feature_extractor = models.resnet18(pretrained=False)
    in_features = feature_extractor.fc.in_features
    feature_extractor.fc = torch.nn.Identity()
    state_dict = model_contrast.state_dict()
    # 只加载除 fc 层外的权重
    filtered_dict = {k: v for k, v in state_dict.items() if not k.startswith("fc.")}
    feature_extractor.load_state_dict(filtered_dict, strict=False)
    feature_extractor = feature_extractor.to(device)
    feature_extractor.eval()
    return feature_extractor

def adjust_contrast_with_category(image, category):
    """
    根据实验图片类型（TEM 或 PFM）调整对比度和色彩。
    TEM：转换为灰度图后均衡化，再使用 colorize 将图像映射为白色背景和红色高光。
    PFM：转换为灰度图后均衡化，再使用 colorize 采用较柔和的红色映射。
    """
    if category.upper() == "TEM":
        gray = image.convert('L')
        equalized = ImageOps.equalize(gray)
        colored = ImageOps.colorize(equalized, black="white", white="red")
        return colored
    elif category.upper() == "PFM":
        gray = image.convert('L')
        equalized = ImageOps.equalize(gray)
        colored = ImageOps.colorize(equalized, black="white", white="#FFCCCC")
        return colored
    else:
        return image

def extract_features(image_path, category, feature_extractor, device):
    """
    针对单张图片进行预处理和特征提取，并进行L2归一化。
    如果指定了 category，则使用相应预处理；否则自动根据文件名判断。
    """
    extract_transform = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406],
                             std=[0.229, 0.224, 0.225])
    ])
    image = Image.open(image_path)
    if category is not None:
        image = adjust_contrast_with_category(image, category)
    else:
        filename = os.path.basename(image_path)
        if "TEM" in filename.upper():
            image = adjust_contrast_with_category(image, "TEM")
        elif "PFM" in filename.upper():
            image = adjust_contrast_with_category(image, "PFM")
    image = image.convert('RGB')
    image = extract_transform(image).unsqueeze(0).to(device)
    with torch.no_grad():
        feature = feature_extractor(image)
    feat_np = feature.cpu().numpy().flatten()
    norm = np.linalg.norm(feat_np)
    if norm > 0:
        feat_np = feat_np / norm
    return feat_np

def build_feature_dict(folder, feature_extractor, device):
    """
    遍历指定文件夹，对每张图片提取特征，并构建特征库字典，键为文件名，值为特征向量。
    """
    feature_dict = {}
    file_list = os.listdir(folder)
    for fname in tqdm(file_list, desc=f"Processing {os.path.basename(folder)}"):
        fpath = os.path.join(folder, fname)
        try:
            feature_dict[fname] = extract_features(fpath, None, feature_extractor, device)
        except Exception as e:
            print(f"Error processing {fpath}: {e}")
    return feature_dict

def match_image(exp_image_path, base_section, category, feature_extractor, device, base_features):
    """
    在指定的基准特征库中匹配实验图片，返回最佳匹配文件名及匹配分数。
    """
    exp_feature = extract_features(exp_image_path, category, feature_extractor, device)
    best_score = -1
    best_match = None
    for fname, feat in base_features.items():
        score = np.dot(exp_feature, feat) / (np.linalg.norm(exp_feature) * np.linalg.norm(feat) + 1e-8)
        if score > best_score:
            best_score = score
            best_match = fname
    return best_match, best_score

##############################################
# 辅助函数：文件名转换
# 将基准文件名转换为目标截面文件名，XYZ转换时去除“.dat”、插入“-compress8”、扩展名固定为“.png”
##############################################
def convert_filename(filename, target_section, base_section):
    base, ext = os.path.splitext(filename)
    if target_section == "XYZ":
        new_ext = ".png"
        if base.startswith(base_section + "_"):
            base_body = base[len(base_section)+1:]
        else:
            base_body = base
        base_body = base_body.replace(".dat", "")
        pos = base_body.rfind("-")
        if pos != -1:
            new_base = base_body[:pos] + "-compress8" + base_body[pos:]
        else:
            new_base = base_body + "-compress8"
        return "XYZ_" + new_base + new_ext
    else:
        new_ext = ext
        if base.startswith(base_section + "_"):
            new_base = target_section + base[len(base_section):]
        else:
            new_base = target_section + "_" + base
        return new_base + new_ext

##############################################
# 辅助函数：解析文件名提取参数信息
##############################################
def parse_simulation_filename(filename):
    base = os.path.splitext(filename)[0]
    m_thick = re.search(r"thick(\d+)", base)
    m_voltage = re.search(r"voltage([-+]?\d+(?:\.\d+)?)", base)
    m_xsub = re.search(r"xsub([\d\.]+)", base)
    m_ysub = re.search(r"ysub([\d\.]+)", base)
    thick = m_thick.group(1) if m_thick else "N/A"
    voltage = m_voltage.group(1) if m_voltage else "N/A"
    xsub = m_xsub.group(1) if m_xsub else "N/A"
    ysub = m_ysub.group(1) if m_ysub else "N/A"
    return f"Thick: {thick}, Voltage: {voltage}, xsub: {xsub}, ysub: {ysub}"

##############################################
# 辅助函数：计算多图平均特征
##############################################
def average_features(image_paths, feature_extractor, device):
    feats = []
    for path in image_paths:
        feat = extract_features(path, None, feature_extractor, device)
        feats.append(feat)
    avg_feat = np.mean(feats, axis=0)
    norm = np.linalg.norm(avg_feat)
    if norm > 0:
        avg_feat = avg_feat / norm
    return avg_feat

##############################################
# 改进后的交互界面
# 支持单图/多图输入，多图模式下左侧显示所有保存的实验图片（缩略图），
# Save按钮及保存数量放在第二行，
# 浏览图片后立即在左侧显示预览，
# 右侧显示匹配的三幅模拟图片及解析参数信息
##############################################
def run_gui(feature_extractor, device, xy_features, xz_features, xy_folder, xz_folder, xyz_folder):
    root = tk.Tk()
    root.title("Image Matching")

    # 第一行控制区域
    control_frame = tk.Frame(root)
    control_frame.pack(side=tk.TOP, fill=tk.X, padx=10, pady=5)

    tk.Label(control_frame, text="Input Mode:").grid(row=0, column=0, padx=5, pady=5)
    input_mode_var = tk.StringVar(value="Single")
    input_mode_menu = tk.OptionMenu(control_frame, input_mode_var, "Single", "Multiple")
    input_mode_menu.grid(row=0, column=1, padx=5, pady=5)

    # Browse Image按钮，点击后立即显示预览到左侧
    browse_button = tk.Button(control_frame, text="Browse Image", command=lambda: browse_image())
    browse_button.grid(row=0, column=2, padx=5, pady=5)

    tk.Label(control_frame, text="Image Type:").grid(row=0, column=3, padx=5, pady=5)
    exp_type_var = tk.StringVar(value="TEM")
    exp_type_menu = tk.OptionMenu(control_frame, exp_type_var, "TEM", "PFM")
    exp_type_menu.grid(row=0, column=4, padx=5, pady=5)

    tk.Label(control_frame, text="Base Section:").grid(row=0, column=5, padx=5, pady=5)
    section_var = tk.StringVar(value="XY")
    section_menu = tk.OptionMenu(control_frame, section_var, "XY", "XZ")
    section_menu.grid(row=0, column=6, padx=5, pady=5)

    match_button = tk.Button(control_frame, text="Match", command=lambda: match_action())
    match_button.grid(row=0, column=7, padx=5, pady=5)

    clean_button = tk.Button(control_frame, text="Clean", command=lambda: clean_display())
    clean_button.grid(row=0, column=8, padx=5, pady=5)

    # 第二行控制区域：Save按钮和保存数量显示（仅针对Multiple模式）
    save_button = tk.Button(control_frame, text="Save", command=lambda: save_image())
    save_button.grid(row=1, column=1, padx=5, pady=5)
    saved_label = tk.Label(control_frame, text="Saved: 0")
    saved_label.grid(row=1, column=2, padx=5, pady=5)

    # 用于保存当前选择图片路径和已保存图片列表
    exp_image_path = tk.StringVar()
    saved_image_paths = []
    current_preview = [None]  # 使用列表保存当前预览路径以避免nonlocal问题

    # 左侧显示区域：Experimental Images
    left_panel = tk.LabelFrame(root, text="Experimental Images")
    left_panel.pack(side=tk.LEFT, fill=tk.BOTH, expand=True, padx=10, pady=10)
    multi_image_frame = tk.Frame(left_panel)
    multi_image_frame.pack(fill=tk.BOTH, expand=True)

    # 右侧显示区域：Matched Simulated Images（仅创建一次）
    right_panel = tk.LabelFrame(root, text="Matched Simulated Images")
    right_panel.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True, padx=10, pady=10)
    sim_frame = tk.Frame(right_panel)
    sim_frame.pack(fill=tk.BOTH, expand=True)
    sim_image_label_1 = tk.Label(sim_frame)
    sim_image_label_1.grid(row=0, column=0, padx=5, pady=5)
    sim_image_label_2 = tk.Label(sim_frame)
    sim_image_label_2.grid(row=0, column=1, padx=5, pady=5)
    sim_image_label_3 = tk.Label(sim_frame)
    sim_image_label_3.grid(row=0, column=2, padx=5, pady=5)
    param_label = tk.Label(right_panel, text="")
    param_label.pack(pady=5)

    # 当单图模式时更新左侧显示区域为当前图片预览
    def update_left_panel_single(path):
        for widget in multi_image_frame.winfo_children():
            widget.destroy()
        try:
            img = Image.open(path)
            img.thumbnail((400, 400))
            photo = ImageTk.PhotoImage(img)
            label = tk.Label(multi_image_frame, image=photo)
            label.image = photo
            label.pack()
        except Exception as e:
            print(f"Error displaying single image: {e}")

    # 当多图模式时更新左侧显示区域，显示已保存图片及当前预览（如果存在）
    def update_left_panel_multiple():
        for widget in multi_image_frame.winfo_children():
            widget.destroy()
        for path in saved_image_paths:
            try:
                img = Image.open(path)
                img.thumbnail((150, 150))
                photo = ImageTk.PhotoImage(img)
                lbl = tk.Label(multi_image_frame, image=photo)
                lbl.image = photo
                lbl.pack(side=tk.LEFT, padx=5, pady=5)
            except Exception as e:
                print(f"Error displaying saved image {path}: {e}")
        if current_preview[0] and current_preview[0] not in saved_image_paths:
            try:
                img = Image.open(current_preview[0])
                img.thumbnail((150, 150))
                photo = ImageTk.PhotoImage(img)
                lbl = tk.Label(multi_image_frame, image=photo, bg="yellow")
                lbl.image = photo
                lbl.pack(side=tk.LEFT, padx=5, pady=5)
            except Exception as e:
                print(f"Error displaying preview image {current_preview[0]}: {e}")

    # 浏览图片后立即显示预览
    def browse_image():
        mode = input_mode_var.get()
        path = filedialog.askopenfilename(title="Select Experimental Image",
                                          filetypes=[("Image files", "*.png;*.jpg;*.jpeg")])
        if path:
            exp_image_path.set(path)
            if mode == "Multiple":
                current_preview[0] = path
                update_left_panel_multiple()
            else:
                update_left_panel_single(path)

    # Save按钮：仅在Multiple模式下生效，将当前预览图片保存
    def save_image():
        if input_mode_var.get() == "Multiple":
            path = exp_image_path.get()
            if path and current_preview[0]:
                saved_image_paths.append(current_preview[0])
                saved_label.config(text=f"Saved: {len(saved_image_paths)}")
                update_left_panel_multiple()
                messagebox.showinfo("Save", "Image saved.")
                current_preview[0] = None
        else:
            messagebox.showinfo("Info", "Save is only available in Multiple mode.")

    # Match按钮：根据单图或多图模式计算匹配特征，然后根据基准截面构造对应三幅模拟图片的路径，并显示
    def match_action():
        mode = input_mode_var.get()
        if mode == "Multiple":
            if not saved_image_paths:
                messagebox.showerror("Error", "No images saved!")
                return
            avg_feat = average_features(saved_image_paths, feature_extractor, device)
        else:
            file_path = exp_image_path.get()
            if not file_path:
                messagebox.showerror("Error", "No image selected!")
                return
            avg_feat = extract_features(file_path, exp_type_var.get(), feature_extractor, device)

        base_section = section_var.get()
        if base_section == "XY":
            base_features = xy_features
        elif base_section == "XZ":
            base_features = xz_features
        else:
            messagebox.showerror("Error", "Invalid base section!")
            return

        best_score = -1
        best_match = None
        for fname, feat in base_features.items():
            score = np.dot(avg_feat, feat) / (np.linalg.norm(avg_feat) * np.linalg.norm(feat) + 1e-8)
            if score > best_score:
                best_score = score
                best_match = fname

        if not best_match:
            messagebox.showinfo("Match Result", "No matching simulated image found!")
            return

        # 根据基准截面构造对应的XY、XZ和XYZ文件名
        if base_section == "XY":
            fname_xy = best_match
            fname_xz = convert_filename(best_match, "XZ", "XY")
        elif base_section == "XZ":
            fname_xz = best_match
            fname_xy = convert_filename(best_match, "XY", "XZ")
        fname_xyz = convert_filename(best_match, "XYZ", base_section)

        path_xy = os.path.join(xy_folder, fname_xy)
        path_xz = os.path.join(xz_folder, fname_xz)
        path_xyz = os.path.join(xyz_folder, fname_xyz)
        display_simulated_images(path_xy, path_xz, path_xyz)

    # Clean按钮：清空左侧和右侧所有显示内容及保存列表
    def clean_display():
        for widget in multi_image_frame.winfo_children():
            widget.destroy()
        param_label.config(text="")
        exp_image_path.set("")
        saved_image_paths.clear()
        saved_label.config(text="Saved: 0")
        sim_image_label_1.config(image='')
        sim_image_label_2.config(image='')
        sim_image_label_3.config(image='')
        param_label.config(text="")

    def average_features(image_paths, feature_extractor, device):
        feats = []
        for path in image_paths:
            feat = extract_features(path, None, feature_extractor, device)
            feats.append(feat)
        avg_feat = np.mean(feats, axis=0)
        norm = np.linalg.norm(avg_feat)
        if norm > 0:
            avg_feat = avg_feat / norm
        return avg_feat

    # 显示匹配到的三幅模拟图片及解析参数
    def display_simulated_images(path1, path2, path3):
        try:
            img1 = Image.open(path1)
            img1.thumbnail((200, 200))
            photo1 = ImageTk.PhotoImage(img1)
            sim_image_label_1.config(image=photo1)
            sim_image_label_1.image = photo1
        except Exception as e:
            print(f"Error loading {path1}: {e}")

        try:
            img2 = Image.open(path2)
            img2.thumbnail((200, 200))
            photo2 = ImageTk.PhotoImage(img2)
            sim_image_label_2.config(image=photo2)
            sim_image_label_2.image = photo2
        except Exception as e:
            print(f"Error loading {path2}: {e}")

        try:
            img3 = Image.open(path3)
            img3.thumbnail((200, 200))
            photo3 = ImageTk.PhotoImage(img3)
            sim_image_label_3.config(image=photo3)
            sim_image_label_3.image = photo3
        except Exception as e:
            print(f"Error loading {path3}: {e}")

        fname = os.path.basename(path1)
        params = parse_simulation_filename(fname)
        param_label.config(text=params)

    # 更新单图模式下左侧显示区域
    def update_left_panel_single(path):
        for widget in multi_image_frame.winfo_children():
            widget.destroy()
        try:
            img = Image.open(path)
            img.thumbnail((400, 400))
            photo = ImageTk.PhotoImage(img)
            label = tk.Label(multi_image_frame, image=photo)
            label.image = photo
            label.pack()
        except Exception as e:
            print(f"Error displaying single image: {e}")

    root.mainloop()

##############################################
# 主程序入口
##############################################
if __name__ == '__main__':
    multiprocessing.freeze_support()
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    if not os.path.exists("contrast_model.pth"):
        model_contrast = train_contrast_model()
    else:
        print("Loading contrast adaptation model from contrast_model.pth")
        model_contrast = load_contrast_model(device)
    feature_extractor = build_feature_extractor(model_contrast, device)
    xy_folder = r".\XY_label_fig"
    xz_folder = r".\XZ_label_fig"
    xyz_folder = r".\XYZ_label_fig"

    if os.path.exists("xy_features.pkl"):
        print("Loading XY features from cache...")
        with open("xy_features.pkl", "rb") as f:
            xy_features = pickle.load(f)
    else:
        print("Building XY simulated image feature library...")
        xy_features = build_feature_dict(xy_folder, feature_extractor, device)
        with open("xy_features.pkl", "wb") as f:
            pickle.dump(xy_features, f)

    if os.path.exists("xz_features.pkl"):
        print("Loading XZ features from cache...")
        with open("xz_features.pkl", "rb") as f:
            xz_features = pickle.load(f)
    else:
        print("Building XZ simulated image feature library...")
        xz_features = build_feature_dict(xz_folder, feature_extractor, device)
        with open("xz_features.pkl", "wb") as f:
            pickle.dump(xz_features, f)

    run_gui(feature_extractor, device, xy_features, xz_features, xy_folder, xz_folder, xyz_folder)
