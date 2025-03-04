package com.service;

import com.dao.ProductDao;
import com.util.Page4Navigator;
import com.util.ZipFilesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.pojo.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.*;

import java.awt.image.BufferedImage;
import java.util.List;
import javax.imageio.ImageIO;

@Service
public class ProductService {
    @Autowired
    ProductDao productDao;

    public Page4Navigator<Product> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Product> pageFromJPA = productDao.findAll(pageable);

        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }



    public List<Product> queryFile(List<Map<String, String>> selectValue) {
        Integer dataType = null;
        Float sizeX = null;
        Float sizeY = null;
        Float sizeZ = null;
        Float strainX = null;
        Float strainY = null;
        Float nx = null;
        Float ny = null;
        Float elecX = null;
        Float elecY = null;
        Float elecZ = null;
        for (Map<String, String> m : selectValue) {
            switch (m.get("attribute")) {
                case "id_0":
                    dataType = Integer.parseInt(m.get("value"));
                    break;
                case "id_1":
                    sizeX = Float.parseFloat(m.get("value"));
                    break;
                case "id_2":
                    sizeY = Float.parseFloat(m.get("value"));
                    break;
                case "id_3":
                    sizeZ = Float.parseFloat(m.get("value"));
                    break;
                case "id_4":
                    strainX = Float.parseFloat(m.get("value"));
                    break;
                case "id_5":
                    strainY = Float.parseFloat(m.get("value"));
                    break;
                case "id_6":
                    nx = Float.parseFloat(m.get("value"));
                    break;
                case "id_7":
                    ny = Float.parseFloat(m.get("value"));
                    break;
                case "id_8":
                    elecX = Float.parseFloat(m.get("value"));
                    break;
                case "id_9":
                    elecY = Float.parseFloat(m.get("value"));
                    break;
                case "id_10":
                    elecZ = Float.parseFloat(m.get("value"));
                    break;
            }
        }
        return productDao.find(dataType, sizeX, sizeY, sizeZ, strainX, strainY, nx, ny, elecX, elecY, elecZ);
    }

    public List<Product> list() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return productDao.findAll(sort);
    }

    public void add(Product bean) {
        productDao.save(bean);
    }

    public void delete(int id) {
        productDao.delete(id);
    }

    public Product get(int id) {
        return productDao.findOne(id);
    }

    public void update(Product bean) {
        productDao.update(bean.getId(), bean.getDataType(), bean.getSizeX(), bean.getSizeY(), bean.getSizeZ(), bean.getStrainX(), bean.getStrainY(), bean.getNX(), bean.getNY(),
                bean.getElecX(), bean.getElecY(), bean.getElecZ(), bean.getXY_Fig(), bean.getXZ_Fig(), bean.getXYZ_Fig(), bean.getData_File());
    }

    public List<Product> search(Float sizeX, Float sizeY, Float sizeZ, Float strainX, Float strainY, Float nx, Float ny, Float elecX, Float elecY,
                                Float elecZ) {
        return productDao.findWithoutDataType(sizeX, sizeY, sizeZ, strainX, strainY, nx, ny, elecX, elecY, elecZ);
    }


    // 上一版本下载方法：接收完整产品对象集合，打包下载对应文件
    public void downloadMulti(Collection<Product> products,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        // 这里只以第一个产品为例，实际可扩展为多产品下载
        Product product = products.iterator().next();
        List<File> filesToDownload = new ArrayList<>();
        if (product.getXY_Fig() != null && !product.getXY_Fig().isEmpty()) {
            File xyFile = new File(product.getXY_Fig());
            if (xyFile.exists()) {
                filesToDownload.add(xyFile);
            }
        }
        if (product.getXZ_Fig() != null && !product.getXZ_Fig().isEmpty()) {
            File xzFile = new File(product.getXZ_Fig());
            if (xzFile.exists()) {
                filesToDownload.add(xzFile);
            }
        }
        if (product.getXYZ_Fig() != null && !product.getXYZ_Fig().isEmpty()) {
            File xyzFile = new File(product.getXYZ_Fig());
            if (xyzFile.exists()) {
                filesToDownload.add(xyzFile);
            }
        }
        if (product.getData_File() != null && !product.getData_File().isEmpty()) {
            File dataFile = new File(product.getData_File());
            if (dataFile.exists()) {
                filesToDownload.add(dataFile);
            }
        }
        if (filesToDownload.isEmpty()) {
            throw new Exception("所选产品没有记录任何文件");
        }
        // 调用工具类压缩下载
        File zipFile = ZipFilesUtil.createZip(filesToDownload);
        ZipFilesUtil.downloadFile(zipFile, zipFile.getName(), request, response);
        zipFile.delete();
    }

    public List<Product> getProductsForPhaseDiagram(String fixedAttr1, Float fixedValue1,
                                                    String fixedAttr2, Float fixedValue2,
                                                    String varAttr1, Float varMin1, Float varMax1,
                                                    String varAttr2, Float varMin2, Float varMax2) {
        return productDao.findPhaseDiagramProducts(fixedAttr1, fixedValue1, fixedAttr2, fixedValue2,
                varAttr1, varMin1, varMax1,
                varAttr2, varMin2, varMax2);
    }

    // 生成二维相图，横轴为 varAttr1，纵轴为 varAttr2（示例代码，颜色依据 DataType 0-9）
    public BufferedImage generatePhaseDiagramImage(List<Product> products,
                                                   String varAttr1, String varAttr2,
                                                   float ignoredVarMin1, float ignoredVarMax1,
                                                   float ignoredVarMin2, float ignoredVarMax2) {
        int width = 600, height = 600;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // 填充背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        if (products == null || products.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.drawString("No data available", width/2 - 50, height/2);
            g2d.dispose();
            return image;
        }

        // 求出数据的最小值与最大值
        float actualMin1 = Float.MAX_VALUE, actualMax1 = -Float.MAX_VALUE;
        float actualMin2 = Float.MAX_VALUE, actualMax2 = -Float.MAX_VALUE;
        for (Product p : products) {
            float val1 = getAttrValue(p, varAttr1);
            float val2 = getAttrValue(p, varAttr2);
            if(val1 < actualMin1) actualMin1 = val1;
            if(val1 > actualMax1) actualMax1 = val1;
            if(val2 < actualMin2) actualMin2 = val2;
            if(val2 > actualMax2) actualMax2 = val2;
        }
        // 如果只有唯一值，则扩展范围以便居中显示
        if (actualMin1 == actualMax1) {
            actualMin1 -= 1;
            actualMax1 += 1;
        } else {
            float margin1 = (actualMax1 - actualMin1) * 0.1f;
            actualMin1 -= margin1;
            actualMax1 += margin1;
        }
        if (actualMin2 == actualMax2) {
            actualMin2 -= 1;
            actualMax2 += 1;
        } else {
            float margin2 = (actualMax2 - actualMin2) * 0.1f;
            actualMin2 -= margin2;
            actualMax2 += margin2;
        }

        // 新增代码：确保横轴（varAttr1）的范围至少达到一个最小值
        float minRange1 = 0.1f; // 可根据实际数据调整阈值，例如0.1
        if ((actualMax1 - actualMin1) < minRange1) {
            float mid = (actualMax1 + actualMin1) / 2.0f;
            actualMin1 = mid - minRange1 / 2.0f;
            actualMax1 = mid + minRange1 / 2.0f;
        }

        // 同理，确保纵轴（varAttr2）的范围至少达到一个最小值
        float minRange2 = 0.1f; // 可根据实际数据调整阈值
        if ((actualMax2 - actualMin2) < minRange2) {
            float mid = (actualMax2 + actualMin2) / 2.0f;
            actualMin2 = mid - minRange2 / 2.0f;
            actualMax2 = mid + minRange2 / 2.0f;
        }

        // 设置坐标区：将坐标系放在图片左下区域，占70%宽高
        int plotWidth = (int)(width * 0.70);
        int plotHeight = (int)(height * 0.70);
        int plotX = 60;  // 左侧边距
        int plotY = height - 60 - plotHeight; // 底部边距60像素

        // 绘制坐标系边框
        g2d.setColor(Color.BLACK);
        g2d.drawRect(plotX, plotY, plotWidth, plotHeight);

        // 刻度设置：预留边缘 marginTick，保证刻度不紧贴边框
        int marginTick = 10;
        int tickStartX = plotX + marginTick;
        int tickEndX = plotX + plotWidth - marginTick;
        int tickRangeX = tickEndX - tickStartX;
        int tickStartY = plotY + plotHeight - marginTick;  // 下边起始（纵轴）
        int tickEndY = plotY + marginTick;
        int tickRangeY = tickStartY - tickEndY;

        // 采集所有数据点对应的横轴和纵轴值，确保所有出现的点在坐标轴上有刻度
        TreeSet<Float> uniqueX = new TreeSet<>();
        TreeSet<Float> uniqueY = new TreeSet<>();
        for (Product p : products) {
            uniqueX.add(getAttrValue(p, varAttr1));
        }
        // 如果唯一值超过1个，则直接以数据轴上已有的值为刻度
        List<Float> ticksX;
        if (uniqueX.size() > 1) {
            ticksX = new ArrayList<>(uniqueX);
        } else { // 只有一个唯一值则扩展上下范围生成均匀刻度
            ticksX = new ArrayList<>();
            for (int i = 0; i <= 5; i++) {
                float val = actualMin1 + (actualMax1 - actualMin1) * i / 5f;
                ticksX.add(val);
            }
        }

        for (Product p : products) {
            uniqueY.add(getAttrValue(p, varAttr2));
        }
        List<Float> ticksY;
        if (uniqueY.size() > 1) {
            ticksY = new ArrayList<>(uniqueY);
        } else {
            ticksY = new ArrayList<>();
            for (int i = 0; i <= 5; i++) {
                float val = actualMin2 + (actualMax2 - actualMin2) * i / 5f;
                ticksY.add(val);
            }
        }

        // 绘制横轴刻度及其数值（使用10号字体，确保边缘不靠近坐标框）
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
        for (Float tick : ticksX) {
            int x = tickStartX + (int)(((tick - actualMin1) / (actualMax1 - actualMin1)) * tickRangeX);
            int y = plotY + plotHeight;
            g2d.drawLine(x, y, x, y + 5);
            String s = String.format("%.2f", tick);
            int strWidth = g2d.getFontMetrics().stringWidth(s);
            g2d.drawString(s, x - strWidth/2, y + 15);
        }

        // 绘制纵轴刻度及其数值
        for (Float tick : ticksY) {
            int y = tickStartY - (int)(((tick - actualMin2) / (actualMax2 - actualMin2)) * tickRangeY);
            g2d.drawLine(plotX - 5, y, plotX, y);
            String s = String.format("%.2f", tick);
            int strWidth = g2d.getFontMetrics().stringWidth(s);
            g2d.drawString(s, plotX - strWidth - 10, y + 3);
        }

        // 绘制坐标轴属性标签，横轴标签用较大字体；纵轴标签采用旋转，并使用保存还原变换的方法
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        int xLabelWidth = g2d.getFontMetrics().stringWidth(varAttr1);
        g2d.drawString(varAttr1, plotX + plotWidth/2 - xLabelWidth/2, plotY + plotHeight + 40);

        // 保存当前变换
        AffineTransform oldTransform = g2d.getTransform();
        // 将坐标系平移到纵轴左侧，取坐标区域左侧距离（如 plotX - 30）和纵轴中点位置（plotY + plotHeight/2）
        g2d.translate(plotX - 40, plotY + plotHeight/2);
        // 逆时针旋转90度（-PI/2）
        g2d.rotate(-Math.PI/2);
        // 绘制纵轴标签，使其水平居中
        g2d.drawString(varAttr2, -g2d.getFontMetrics().stringWidth(varAttr2) / 2, 0);
        // 恢复原始变换
        g2d.setTransform(oldTransform);

        // 定义 10 中颜色对应 DataType
        Color[] colors = { Color.BLUE, Color.GREEN, Color.RED, Color.ORANGE, Color.MAGENTA,
                Color.CYAN, Color.PINK, Color.YELLOW, Color.LIGHT_GRAY, Color.DARK_GRAY };

        // 绘制数据点
        for (Product p : products) {
            float xVal = getAttrValue(p, varAttr1);
            float yVal = getAttrValue(p, varAttr2);
            int xPos = tickStartX + (int)(((xVal - actualMin1) / (actualMax1 - actualMin1)) * tickRangeX);
            int yPos = tickStartY - (int)(((yVal - actualMin2) / (actualMax2 - actualMin2)) * tickRangeY);
            int dataType = p.getDataType();
            g2d.setColor(colors[dataType % colors.length]);
            g2d.fillOval(xPos - 3, yPos - 3, 6, 6);
        }

        // 绘制右上角 DataType 图例：缩小尺寸并分为两列显示（左列显示 0~4，右列显示 5~9）
        int legendX = plotX + plotWidth + 20;
        int legendY = 30;
        int colWidth = 40; // 每列宽度
        int rowHeight = 10; // 每行高度
        // 绘制图例边框（宽度两列合计加间隔）
        int legendWidth = colWidth * 2 + 10;
        int legendHeight = 5 * rowHeight + 20; // 每列5行，加上标题区
        g2d.setColor(Color.BLACK);
        g2d.drawRect(legendX, legendY, legendWidth, legendHeight);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 9));
        g2d.drawString("DataType", legendX + 5, legendY + 12);
        // 分两列显示
        for (int i = 0; i < 10; i++) {
            int col = (i < 5) ? 0 : 1;
            int row = (i < 5) ? i : i - 5;
            int boxX = legendX + 5 + col * (colWidth);
            int boxY = legendY + 15 + row * rowHeight;
            g2d.setColor(colors[i]);
            g2d.fillRect(boxX, boxY, 8, 8);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(boxX, boxY, 8, 8);
            g2d.drawString(String.valueOf(i), boxX + 12, boxY + 8);
        }

        g2d.dispose();
        return image;
    }

    private float getAttrValue(Product p, String attr) {
        if (attr.equalsIgnoreCase("sizeZ")) {
            return p.getSizeZ();
        } else if (attr.equalsIgnoreCase("strainX")) {
            return p.getStrainX();
        } else if (attr.equalsIgnoreCase("strainY")) {
            return p.getStrainY();
        } else if (attr.equalsIgnoreCase("elecZ")) {
            return p.getElecZ();
        }
        return 0;
    }

    /**
     * 调用 Python 图片匹配脚本，将上传的实验图片、图片类型和Base类型传入
     * 并根据匹配结果查询 Product 表返回匹配记录
     */
    public Product imageSearch(File imageFile, String imageType, String baseSection) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("python",
                "data_fig/code/image_match.py",
                imageFile.getAbsolutePath(),
                imageType,
                baseSection);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // 读取Python脚本的输出
        Scanner sc = new Scanner(process.getInputStream());
        StringBuilder outputBuilder = new StringBuilder();
        while(sc.hasNextLine()){
            outputBuilder.append(sc.nextLine()).append("\n");
        }
        sc.close();
        int exitCode = process.waitFor();
        String bestMatch = getMatch(outputBuilder, exitCode);

        Product product = null;
        if(baseSection.equals("XY"))
            product = productDao.findByXY_Fig("./data_fig/XY_label_fig/" + bestMatch);
        else if(baseSection.equals("XZ"))
            product = productDao.findByXZ_Fig("./data_fig/XZ_label_fig/" + bestMatch);
        if(product == null){
            throw new Exception("数据库中未找到与匹配结果对应的记录");
        }
        return product;
    }

    private static String getMatch(StringBuilder outputBuilder, int exitCode) throws Exception {
        String output = outputBuilder.toString().trim();

        if(exitCode != 0 || output.startsWith("ERROR:")){
            throw new Exception("Python脚本出错: " + output);
        }

        // 取出最后一行作为文件名
        String[] lines = output.split("\n");
        String bestMatch = "";
        // 从最后一行开始查找非空行
        for (int i = lines.length - 1; i >= 0; i--) {
            if (!lines[i].trim().isEmpty()) {
                bestMatch = lines[i].trim();
                break;
            }
        }
        if(bestMatch.isEmpty()){
            throw new Exception("未获得匹配结果");
        }
        return bestMatch;
    }
}
