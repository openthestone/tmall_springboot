package com.web;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pojo.Product;
import com.pojo.User;
import com.service.ProductService;
import com.service.UserService;
import com.util.Page4Navigator;
import com.util.Result;
import com.util.ZipFilesUtil;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@RestController
public class ForeRESTController {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

//    @GetMapping("/fore")
//    public Page4Navigator<Product> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
//        start = start < 0 ? 0 : start;
//        Page4Navigator<Product> page = productService.list(start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
//        return page;
//    }
    @PostMapping("/foreRegister")
    public Object register(@RequestBody User user,HttpSession session) {
        String name =  user.getName();
        String password = user.getPassword();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);

        if(exist){
            String message ="用户名已经被使用,不能使用";
            return Result.fail(message);
        }
        user.setPassword(password);
        userService.add(user);
        session.setAttribute("user", user);
        return Result.success();
    }
    @PostMapping("/foreLogin")
    public Object login(@RequestBody User userParam, HttpSession session) {
        String name =  userParam.getName();
        name = HtmlUtils.htmlEscape(name);
        User user =userService.get(name,userParam.getPassword());
        if(null==user){
            String message ="账号密码错误";
            return Result.fail(message);
        }
        else{
            session.setAttribute("user", user);
            return Result.success();
        }
    }
    @PostMapping("/foreSearch")
    public Object search(@RequestParam(value = "sizeX") Float sizeX, @RequestParam(value = "sizeY") Float sizeY,
                         @RequestParam(value = "sizeZ") Float sizeZ, @RequestParam(value = "strainX") Float strainX,
                         @RequestParam(value = "strainY") Float strainY, @RequestParam(value = "nx") Float nx,
                         @RequestParam(value = "ny") Float ny, @RequestParam(value = "elecX") Float elecX,
                         @RequestParam(value = "elecY") Float elecY, @RequestParam(value = "elecZ") Float elecZ) {
        return productService.search(sizeX, sizeY, sizeZ, strainX, strainY, nx, ny, elecX, elecY, elecZ);
    }

    @PostMapping("/foreImageSearch")
    public Object imageSearch(@RequestParam("file") MultipartFile file,
                              @RequestParam("imageType") String imageType,
                              @RequestParam("baseSection") String baseSection) {
        try {
            // 保存上传的图片到临时目录
            String tempDir = System.getProperty("java.io.tmpdir");
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File tempFile = new File(tempDir, filename);
            file.transferTo(tempFile);
            // 调用Service方法进行图片匹配（以下方法会调用Python脚本，并返回匹配的产品记录）
            Product matchedProduct = productService.imageSearch(tempFile, imageType, baseSection);
            // 删除临时文件
            tempFile.delete();
            return matchedProduct;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("图片匹配失败：" + e.getMessage());
        }
    }

    // 还原为上一版本的下载接口：接收完整的产品对象数组
    @PostMapping(value="/gjw/download", consumes="application/json")
    public void downloadFile(@RequestBody Collection<Product> products,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        if (products == null || products.isEmpty()) {
            throw new Exception("没有选择产品");
        }
        // 这里按上一版本处理，下载时以第一个产品记录为例
        Product product = products.iterator().next();
        if (product == null) {
            throw new Exception("产品不存在");
        }
        // 构造文件列表：从产品记录中取出三视图和数据文件（注意真实路径已上传时保存到数据库中）
        java.util.List<File> filesToDownload = new java.util.ArrayList<>();
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

        // 调用 Service 层方法（或直接使用工具类）压缩文件并下载
        productService.downloadMulti(products, request, response);
    }
}