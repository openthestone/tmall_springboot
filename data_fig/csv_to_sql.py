import os
import csv


csv_reader = csv.reader(open("data.csv"))
for row in csv_reader:
	sql = "insert into `product` (`id`, `name`, `dataType`, `sizeX`, `sizeY`, `sizeZ`, `strainX`, `strainY`, `nx`, `ny`, `elecX`, `elecY`, `elecZ`, `xy_Fig`, `xz_Fig`, `xyz_Fig`, `data_File`) values("
	sql += "'" + row[0] + "','" + row[1] + "','"
	label = -1
	for i in range(0, 10):
		file = "./XY_label_fig/" + row[12][9:-4] + "-" + str(i) + row[12][-4:]
		if os.path.exists(file):
			label = i
			break
	sql += str(label) + "','" + row[2] + "','" + row[3] + "','" + row[4] + "','" + row[5] + "','" + row[6] + "','" + row[7] + "','" + row[8] + "','" + row[9] + "','" + row[10] + "','" + row[11] + "','"
	sql += "./data_fig/XY_label_fig/" + row[12][9:-4] + "-" + str(label) + row[12][-4:] + "','" + "./data_fig/XZ_label_fig/" + row[13][9:-4] + "-" + str(label) + row[13][-4:] + "','"
	sql += "./data_fig/XYZ_label_fig/" + row[14][6:-4] + "-" + str(label) + ".png" + "','" + "./data_fig/XYZ_label/" + row[14][6:-4] + "-" + str(label) + row[14][-4:] +"');\n"
	with open("temp.txt", "a") as f:
		f.write(sql)
