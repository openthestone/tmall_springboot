import os
import shutil
from sympy import *


def calResult(conditionA, conditionB, conditionC):
    # the dump is in the unit of microNewton / nanometer ** 2
    dump1 = -conditionA * 1e-6 * 1e18
    dump2 = -conditionB * 1e-6 * 1e18
    dump3 = -conditionC * 1e-6 * 1e18
    # the parameters for STO and PTO
    c11P = 1.76e11
    c12P = 7.937e10
    c11S = 3.36e11
    c12S = 1.07e11
    # the superlattice's parameters would be the mean of two materials
    c11 = c11P * 1 / 3 + c11S * 2 / 3
    c12 = c12P * 1 / 3 + c12S * 2 / 3

    eta1, eta2, eta3 = symbols('eta1 eta2 eta3')

    f1 = c11 * eta1 + c12 * (eta2 + eta3) - dump1
    f2 = c11 * eta2 + c12 * (eta1 + eta3) - dump2
    f3 = c11 * eta3 + c12 * (eta1 + eta2) - dump3

    result = linsolve([f1, f2, f3], (eta1, eta2, eta3))

    a, b, c = result.args[0]

    return f"{a} {b} {c} "


def reviseInput(path, rowNum, content):
    with open(path, 'r+') as f:
        flist = f.readlines()
        flist[rowNum] = content
    with open(path, 'w') as f:
        f.writelines(flist)


if __name__ == "__main__":
    source_path = os.path.relpath("origin")
    
    with open('data-test.csv', 'w+') as f:
        f.write("id, name, sizeX, sizeY, sizeZ, strainX, strainY, NX, NY, elecX, elecY, elecZ, XY_fig, XZ_fig, data_file\n")

        count = 0
        for i in range(11):  # 单层周期数 4-24
            single_layer = 4 + 2 * i
            single_layer = round(single_layer)
            
            for j in range(11):  # Z方向电场 -10V 到 10V
                voltage = -10 + 2 * j
                voltage_Norm = round(voltage * 10 / 13 * 10000) / 10000
                
                for k in range(6):  # X方向衬底 3.8-4.0
                    x_sub = round((3.8 + 0.04 * k) * 10000) / 10000
                    
                    for l in range(6):  # Y方向衬底 3.8-4.0
                        y_sub = round((3.8 + 0.04 * l) * 10000) / 10000
                        if x_sub < y_sub:
                            continue
                        
                        folderName = f"thick{single_layer}voltage{voltage}xsub{x_sub}ysub{y_sub}"
                        count += 1
                        f.write(f"{count},{folderName},40,40,{round(0.4 * (30 + single_layer * 3 + 30), 1)},{x_sub},{y_sub},{single_layer},{single_layer},0,0,{voltage},./XY_fig/XY_{folderName}-20000.dat.jpg,./XZ_fig/XZ_{folderName}-20000.dat.jpg,./XYZ/XYZ_{folderName}-20000-compress8.npz\n")

                        image_path = f"./data_package_incomplete/XZ_fig/XZ_{folderName}-20000.dat.jpg"
                        if not os.path.exists(image_path):
                            print(image_path)

                        # 创建任务目录
                        target_path = os.path.relpath(folderName)
                        if not os.path.exists(target_path):
                            os.makedirs(target_path)

                        if os.path.exists(source_path):
                            shutil.copytree(source_path, target_path, dirs_exist_ok=True)

                        # 修改输入文件
                        reviseInput(f"{folderName}/inputN.in", 4, f"{single_layer} {single_layer}\n")
                        reviseInput(f"{folderName}/inputN.in", 6, f"{single_layer * 3} 30 0 0\n")
                        reviseInput(f"{folderName}/inputN.in", 2, f"100 100 {30 + single_layer * 3 + 30}\n")
                        reviseInput(f"{folderName}/inputN.in", 3, f"40 40 {round(0.4 * (30 + single_layer * 3 + 30) * 10) / 10}\n")
                        reviseInput(f"{folderName}/V-3.sh", 1, f"#SBATCH -J PhaseHTDB-{folderName}-DGSH\n")
                        reviseInput(f"{folderName}/inputN.in", 15, f"3.905 3.9574 {x_sub} {y_sub}\n")
                        reviseInput(f"{folderName}/inputN.in", 24, f"{voltage_Norm} 0.0 0.01 0.6 0.4 0.0\n")

                        # 提交任务
                        os.system(f'cd {folderName} && sbatch V-3.sh && cd ..')
