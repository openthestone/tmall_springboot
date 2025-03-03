package com.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFilesUtil {

    /**
     * 将多个文件压缩成一个临时ZIP文件
     * @param files 要压缩的文件列表
     * @return 临时ZIP文件
     * @throws IOException
     */
    public static File createZip(List<File> files) throws IOException {
        // 创建临时ZIP文件
        File zipFile = File.createTempFile("download_", ".zip");
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()))) {
            byte[] buffer = new byte[1024];
            for (File file : files) {
                if (file.exists() && file.isFile()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        zos.putNextEntry(new ZipEntry(file.getName()));
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                    }
                }
            }
        }
        return zipFile;
    }


    /**
     * 根据输入的文件与输出流对文件进行打包
     */
    private static void zipFile(File inputFile, ZipOutputStream outputStream) {
        try {
            if (inputFile.exists()) {
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    outputStream.putNextEntry(entry);

                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, nNumber);
                    }

                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                zipFile(file, outputStream);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 把接受的全部文件打成压缩包
     */
    private static void zipFile(List<File> files, ZipOutputStream outputStream) {
        for (File file1 : files) {
            zipFile(file1, outputStream);
        }
    }


    /**
     * 将文件写入HttpServletResponse中进行下载
     * @param file 要下载的文件
     * @param fileName 下载时显示的文件名
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @throws IOException
     */
    public static void downloadFile(File file, String fileName,
                                    HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            os.flush();
        }
    }

    /**
     * 处理文件名乱码和浏览器兼容问题
     *
     * @param fileName
     * @param request
     * @param response
     * @throws UnsupportedEncodingException
     */
    private static void generate(String fileName, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("success", "true");

        String userAgent = request.getHeader("User-Agent");
        String formFileName;
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            // 针对IE或者以IE为内核的浏览器：
            formFileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            formFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }

        //如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
        response.setHeader("Content-Disposition", "attachment;filename=" + formFileName);
    }

}
