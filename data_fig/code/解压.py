
import numpy as np
import os

# 方法一：压缩成int8的数据类型
def compress8(path):
    # 获取除了抬头的数据
    data = np.loadtxt(path, skiprows=1)
    # 获取抬头数据
    header = np.loadtxt(path, max_rows=1)
    # 仅选取极化数据
    data_right = data[:, 3:6]
    # 获取data_right的最大值和最小值
    max = np.max(data_right)
    min = np.min(data_right)
    # 把数据自适应缩放转为-128-127的区间范围，方便转化为int8类型来缩减体积（比double64会缩小8倍）
    data_right = ((data_right - min) / (max - min) * 255 - 128)
    data_right = data_right.astype(np.int8)
    # 把抬头数据也转化为int16
    header = header.astype(np.int16)
    # 保存压缩参数min和max
    compress_factor = np.zeros(2)
    compress_factor[0] = max
    compress_factor[1] = min
    # 把抬头数据和极化数据整合
    np.savez_compressed(path[:-4] + "-compress8.npz", header=header, data_right=data_right, compress_factor=compress_factor)

# 方法二：压缩成int16的数据类型
def compress16(path):
    # 获取除了抬头的数据
    data = np.loadtxt(path, skiprows=1)
    # 获取抬头数据
    header = np.loadtxt(path, max_rows=1)
    # 仅选取极化数据
    data_right = data[:, 3:6]
    # 获取data_right的最大值和最小值
    max = np.max(data_right)
    min = np.min(data_right)
    # 把数据自适应缩放转为-32768-32767的区间范围，方便转化为int16类型来缩减体积（比double64会缩小4倍）
    data_right = ((data_right - min) / (max - min) * 65535 - 32768)
    data_right = data_right.astype(np.int16)
    # 把抬头数据也转化为int16
    header = header.astype(np.int16)
    # 保存压缩参数min和max
    compress_factor = np.zeros(2)
    compress_factor[0] = max
    compress_factor[1] = min
    # 把抬头数据和极化数据整合
    np.savez_compressed(path + "-compress16.npz", header=header, data_right=data_right, compress_factor=compress_factor)


# 方法二：采用半精度的方式进行保存
def compressf16(path):
    # 获取除了抬头的数据
    data = np.loadtxt(path, skiprows=1)
    # 获取抬头数据
    header = np.loadtxt(path, max_rows=1)
    # 仅选取极化数据
    data_right = data[:, 3:6]
    # 转化为半精度格式
    data_right = data_right.astype(np.float16)
    # 把抬头数据转化为int16
    header = header.astype(np.int16)
    # 把抬头数据和极化数据整合
    np.savez_compressed(path + "-compressf16.npz", header=header, data_right=data_right)

def decompressf16(path):
    npzfile = np.load(path)
    # 读取size情况
    size = npzfile["header"]
    # 读取处理后的数据
    data_right = npzfile["data_right"]
    # 转换为int64总行数避免超出的情况
    size = size.astype(np.int64)
    data_left = np.zeros((size[0] * size[1] * size[2], 3))
    for i in range(size[0]):
        for j in range(size[1]):
            for k in range(size[2]):
                data_left[i * size[1] * size[2] + j * size[2] + k, 0] = i + 1
                data_left[i * size[1] * size[2] + j * size[2] + k, 1] = j + 1
                data_left[i * size[1] * size[2] + j * size[2] + k, 2] = k + 1
    # 合并获得整体数据
    data_total = np.concatenate((data_left, data_right), axis=1)
    # 输出结果
    with open(path + "-decompress.in", "w") as f:
        f.write(str(size[0]) + "  " + str(size[1]) + "  " + str(size[2]) + "\n")
        np.savetxt(f, data_total, fmt=["%d", "%d", "%d", "%.5e", "%.5e", "%.5e"])


def decompress8(path):
    npzfile = np.load(path)
    # 读取size情况
    size = npzfile["header"]
    # 读取压缩参数
    compress_factor = npzfile["compress_factor"]
    # 读取处理后的数据
    data_right = npzfile["data_right"]
    # 转换为int64总行数避免超出的情况
    size = size.astype(np.int64)
    data_left = np.zeros((size[0] * size[1] * size[2], 3))
    print(size)
    print(len(data_right))
    for i in range(size[0]):
        for j in range(size[1]):
            for k in range(size[2]):
                data_left[i * size[1] * size[2] + j * size[2] + k, 0] = i + 1
                data_left[i * size[1] * size[2] + j * size[2] + k, 1] = j + 1
                data_left[i * size[1] * size[2] + j * size[2] + k, 2] = k + 1
    # 读取压缩参数Max
    max = compress_factor[0]
    # 读取压缩参数Min
    min = compress_factor[1]
    # 反处理得到原数据
    data_right = (data_right + 128) / 255 * (max - min) + min
    # 合并获得整体数据
    data_total = np.concatenate((data_left, data_right), axis=1)
    # 输出文件名，替换 '.npz' 为 '.in'
    output_filename = path.replace('.npz', '') + '.in'
    # 输出结果
    with open(output_filename, "w") as f:
        f.write(str(size[0]) + "  " + str(size[1]) + "  " + str(size[2]) + "\n")
        np.savetxt(f, data_total, fmt=["%d", "%d", "%d", "%.5e", "%.5e", "%.5e"])


def decompress16(path):
    npzfile = np.load(path)
    # 读取size情况
    size = npzfile["header"]
    # 读取压缩参数
    compress_factor = npzfile["compress_factor"]
    # 读取处理后的数据
    data_right = npzfile["data_right"]
    # 转换为int64总行数避免超出的情况
    size = size.astype(np.int64)
    data_left = np.zeros((size[0] * size[1] * size[2], 3))
    for i in range(size[0]):
        for j in range(size[1]):
            for k in range(size[2]):
                data_left[i * size[1] * size[2] + j * size[2] + k, 0] = i + 1
                data_left[i * size[1] * size[2] + j * size[2] + k, 1] = j + 1
                data_left[i * size[1] * size[2] + j * size[2] + k, 2] = k + 1
    # 读取压缩参数Max
    max = compress_factor[0]
    # 读取压缩参数Min
    min = compress_factor[1]
    # 反处理得到原数据
    data_right = (data_right + 32768) / 65535 * (max - min) + min
    # 合并获得整体数据
    data_total = np.concatenate((data_left, data_right), axis=1)
    # 输出结果
    with open(path + "-decompress.in", "w") as f:
        f.write(str(size[0]) + "  " + str(size[1]) + "  " + str(size[2]) + "\n")
        np.savetxt(f, data_total, fmt=["%d", "%d", "%d", "%.5e", "%.5e", "%.5e"])

# 衡量一下误差的情况
def verify(path1, path2):
    # 获取除了抬头的数据
    data1 = np.loadtxt(path1, skiprows=1, dtype=np.float64)
    # 仅选取极化数据
    data1_right = data1[:, 3:6]

    # 获取除了抬头的数据
    data2 = np.loadtxt(path2, skiprows=1, dtype=np.float64)
    # 仅选取极化数据
    data2_right = data2[:, 3:6]

    diff_data = np.abs(data1_right - data2_right)
    print("max_differ = " + "{:.8f}".format(np.max(diff_data)))
    print("average_differ = " + "{:.8f}".format(np.mean(diff_data)))
def GetNpzFiles(path):
    """
    获取给定路径下所有的 .npz 文件
    """
    files = [f for f in os.listdir(path) if f.endswith('.npz')]
    return [os.path.join(path, f) for f in files]

def DecompressFilesInFolder(folder_path):
    """
    解压指定文件夹内的所有使用 compress8 方法压缩的 npz 文件
    """
    files = GetNpzFiles(folder_path)
    for file in files:
        decompress8(file)

if __name__ == "__main__":
    folder_path = r"E:\学校\科研项目\data_fig"
    DecompressFilesInFolder(folder_path)