package com.web;

import com.pojo.Product;
import com.service.ProductService;
import com.util.ImageUtil;
import com.util.Page4Navigator;
import com.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    /**
     * 标准表分页显示数据
     *
     * @param start 起始页
     * @param size 每次显示页码号
     * @return
     * @throws Exception
     */
    @GetMapping("/products")
    public Page4Navigator<Product> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        Page4Navigator<Product> page = productService.list(start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return page;
    }

    /**
     * 标准表添加数据
     * @param bean 数据对象
     * @return
     * @throws Exception
     */
    @PostMapping("/add_products")
    public Object add(Product bean) throws Exception {
        productService.add(bean);
        // saveOrUpdateImageFile(bean, image, request);
        return bean;
    }

    /**
     * 数据广场搜索数据
     * @param body
     * @return 数据文件下载列表
     */
    @PostMapping("/queryData")
    public List<Integer> fileN(@RequestBody List<Map<String, String>> body) {
        return productService.queryFile(body);
    }
    /**
     * 文件下载
     *
     * @Body ids 下载文件id集合
     * @return
     */
    @PostMapping("/download")
    public void fileMultiDownload(@RequestBody Collection<Integer> ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        productService.downloadMulti(ids, request, response);
    }

    /**
     * 标准表删除数据
     * @param id 数据id
     * @param request 选择性提供
     * @return
     * @throws Exception
     */
    @DeleteMapping("/products/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception {
        productService.delete(id);
//        File  imageFolder= new File(request.getServletContext().getRealPath("img/product"));
//        File  fileFolder= new File(request.getServletContext().getRealPath("file/product"));
//        File imagfile = new File(imageFolder,id+".jpg");
//        File file = new File(fileFolder,id+".in");
//        imagfile.delete();
//        file.delete();
        return null;
    }

    /**
     * 从标准数据库中获取数据
     * @param id 数据id
     * @return
     * @throws Exception
     */
    @GetMapping("/products/{id}")
    public Product get(@PathVariable("id") int id) throws Exception {
        Product bean = productService.get(id);
        return bean;
    }

    public void saveOrUpdateImageFile(Product bean, MultipartFile image, HttpServletRequest request)
            throws IOException {
        File imageFolder = new File(request.getServletContext().getRealPath("img/product"));
        File fileFolder = new File(request.getServletContext().getRealPath("file/product"));
        File file = new File(fileFolder, bean.getId() + ".in");
        File imageFile = new File(imageFolder, bean.getId() + ".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (!imageFile.getParentFile().exists())
            imageFile.getParentFile().mkdirs();
        image.transferTo(imageFile);
        BufferedImage img = ImageUtil.change2jpg(imageFile);
        ImageIO.write(img, "jpg", imageFile);
    }

    /**
     *
     * @param bean
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/products/{id}")
    public Object update(Product bean, HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        String sizex = request.getParameter("sizex");
        String sizey = request.getParameter("sizey");
        String sizez = request.getParameter("sizez");
        String NX = request.getParameter("nX");
        String NY = request.getParameter("nY");
        String strainX = request.getParameter("strainX");
        String strainY = request.getParameter("strainY");
        String ElecX = request.getParameter("elecX");
        String ElecY = request.getParameter("elecY");
        String ElecZ = request.getParameter("elecZ");
        String dataType = request.getParameter("dataType");
        String Doi = request.getParameter("Doi");
        String SupplmentaryInfo = request.getParameter("SupplmentaryInfo");
        bean.setName(name);
        bean.setSizex(sizex);
        bean.setSizey(sizey);
        bean.setSizez(sizez);
        bean.setnX(NX);
        bean.setnY(NY);
        bean.setStrainX(strainX);
        bean.setStrainY(strainY);
        bean.setElecX(ElecX);
        bean.setElecY(ElecY);
        bean.setElecZ(ElecZ);
        bean.setDataType(dataType);
        bean.setDoi(Doi);
        bean.setSupplmentaryInfo(SupplmentaryInfo);

//        if(image!=null) {
//            saveOrUpdateImageFile(bean, image, request);
//        }
        productService.update(bean);
        return Result.success();
    }

    /**
     * 上传文件
     * @param file 文件上传所接收的就是MultipartFile，如果涉及到多文件上传，那么改为List即可。
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String upload(@RequestPart MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = fileName;
        File dest = new File(filePath);
        Files.copy(file.getInputStream(), dest.toPath());
        return "Upload file success : " + dest.getAbsolutePath();
    }

}
