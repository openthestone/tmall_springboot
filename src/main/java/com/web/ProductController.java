package com.web;

import com.pojo.Product;
import com.service.ProductService;
import com.util.ImageUtil;
import com.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public Page4Navigator<Product> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        Page4Navigator<Product> page = productService.list(start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return page;
    }

    @PostMapping("/products")
    public Object add(Product bean) throws Exception {
        productService.add(bean);
        // saveOrUpdateImageFile(bean, image, request);
        return bean;
    }

    @PostMapping("/queryData")
    public List<Integer> fileN(@RequestBody List<Map<String, String>> body) {
        return productService.queryFile(body);
    }
    /**
     * 批量文件下载
     *
     * @param ids 文件id集合
     * @return
     */
    @PostMapping("/download")
    public void fileMultiDownload(@RequestBody Collection<Integer> ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        productService.downloadMulti(ids, request, response);
    }
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

    @PostMapping("/products/{id}")
    public Object update(Product bean, HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        String sizex = request.getParameter("sizex");
        String sizey = request.getParameter("sizey");
        String sizez = request.getParameter("sizez");
        String NX = request.getParameter("NX");
        String NY = request.getParameter("NY");
        String strainX = request.getParameter("strainX");
        String strainY = request.getParameter("strainY");
        String ElecX = request.getParameter("ElecX");
        String ElecY = request.getParameter("ElecY");
        String ElecZ = request.getParameter("ElecZ");
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
        return bean;
    }
}
