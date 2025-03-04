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
    public Page4Navigator<Product> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
        start = start < 0 ? 0 : start;
        //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return productService.list(start, size, 5);
    }

    /**
     * 标准表添加数据
     * @param bean 数据对象
     * @return
     * @throws Exception
     */
    @PostMapping("/add_products")
    public Object add(Product bean, HttpServletRequest request) throws Exception {

        String name = request.getParameter("name");
        int dataType = Integer.parseInt(request.getParameter("dataType"));
        float sizeX = Float.parseFloat(request.getParameter("sizeX"));
        float sizeY = Float.parseFloat(request.getParameter("sizeY"));
        float sizeZ = Float.parseFloat(request.getParameter("sizeZ"));
        float strainX = Float.parseFloat(request.getParameter("strainX"));
        float strainY = Float.parseFloat(request.getParameter("strainY"));
        float nx = Float.parseFloat(request.getParameter("nx"));
        float ny = Float.parseFloat(request.getParameter("ny"));
        float elecX = Float.parseFloat(request.getParameter("elecX"));
        float elecY = Float.parseFloat(request.getParameter("elecY"));
        float elecZ = Float.parseFloat(request.getParameter("elecZ"));
        String xy_Fig = request.getParameter("xy_Fig");
        String xz_Fig = request.getParameter("xz_Fig");
        String xyz_Fig = request.getParameter("xyz_Fig");
        String data_File = request.getParameter("data_File");
        bean.setName(name);
        bean.setDataType(dataType);
        bean.setSizeX(sizeX);
        bean.setSizeY(sizeY);
        bean.setSizeZ(sizeZ);
        bean.setStrainX(strainX);
        bean.setStrainY(strainY);
        bean.setNX(nx);
        bean.setNY(ny);
        bean.setElecX(elecX);
        bean.setElecY(elecY);
        bean.setElecZ(elecZ);
        bean.setXY_Fig(xy_Fig);
        bean.setXZ_Fig(xz_Fig);
        bean.setXYZ_Fig(xyz_Fig);
        bean.setData_File(data_File);
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
    public List<Product> fileN(@RequestBody List<Map<String, String>> body) {
        return productService.queryFile(body);
    }
    /**
     * 文件下载
     *
     * @Body ids 下载文件id集合
     * @return
     */
    @PostMapping("/download")
    public void fileMultiDownload(@RequestBody Collection<Product> products, HttpServletRequest request, HttpServletResponse response) throws Exception {
        productService.downloadMulti(products, request, response);
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
        return productService.get(id);
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
        if (img != null) {
            ImageIO.write(img, "jpg", imageFile);
        }
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
        int dataType = Integer.parseInt(request.getParameter("dataType"));
        float sizeX = Float.parseFloat(request.getParameter("sizeX"));
        float sizeY = Float.parseFloat(request.getParameter("sizeY"));
        float sizeZ = Float.parseFloat(request.getParameter("sizeZ"));
        float strainX = Float.parseFloat(request.getParameter("strainX"));
        float strainY = Float.parseFloat(request.getParameter("strainY"));
        float nx = Float.parseFloat(request.getParameter("nx"));
        float ny = Float.parseFloat(request.getParameter("ny"));
        float elecX = Float.parseFloat(request.getParameter("elecX"));
        float elecY = Float.parseFloat(request.getParameter("elecY"));
        float elecZ = Float.parseFloat(request.getParameter("elecZ"));
        String xy_Fig = request.getParameter("xy_Fig");
        String xz_Fig = request.getParameter("xz_Fig");
        String xyz_Fig = request.getParameter("xyz_Fig");
        String data_File = request.getParameter("data_File");
        bean.setName(name);
        bean.setDataType(dataType);
        bean.setSizeX(sizeX);
        bean.setSizeY(sizeY);
        bean.setSizeZ(sizeZ);
        bean.setStrainX(strainX);
        bean.setStrainY(strainY);
        bean.setNX(nx);
        bean.setNY(ny);
        bean.setElecX(elecX);
        bean.setElecY(elecY);
        bean.setElecZ(elecZ);
        bean.setXY_Fig(xy_Fig);
        bean.setXZ_Fig(xz_Fig);
        bean.setXYZ_Fig(xyz_Fig);
        bean.setData_File(data_File);

        productService.update(bean);
        return Result.success();
    }

    // 新增接口：生成二维相图
    @GetMapping("/phaseDiagram")
    public void phaseDiagram(
            @RequestParam String fixedAttr1,
            @RequestParam Float fixedValue1,
            @RequestParam String fixedAttr2,
            @RequestParam Float fixedValue2,
            @RequestParam String varAttr1,
            @RequestParam Float varMin1,
            @RequestParam Float varMax1,
            @RequestParam String varAttr2,
            @RequestParam Float varMin2,
            @RequestParam Float varMax2,
            HttpServletResponse response) throws IOException {
        // 查询符合条件的产品数据
        List<Product> products = productService.getProductsForPhaseDiagram(
                fixedAttr1, fixedValue1, fixedAttr2, fixedValue2,
                varAttr1, varMin1, varMax1, varAttr2, varMin2, varMax2);
        // 生成相图图片
        BufferedImage image = productService.generatePhaseDiagramImage(
                products, varAttr1, varAttr2, varMin1, varMax1, varMin2, varMax2);
        response.setContentType("image/png");
        ImageIO.write(image, "png", response.getOutputStream());
    }
}
