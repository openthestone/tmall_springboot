package com.web;


import com.pojo.Product;
import com.pojo.TempProduct;
import com.service.TempProductService;
import com.util.Page4Navigator;
import com.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@RestController
public class TempProductController {
    @Autowired
    TempProductService tempProductService;
    /**
     * 临时表分页显示数据
     *
     * @param start 起始页
     * @param size 每次显示页码号
     * @return
     * @throws Exception
     */
    @GetMapping("/temp_products")
    public Page4Navigator<TempProduct> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
        start = start < 0 ? 0 : start;
        //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return tempProductService.list(start, size, 5);
    }

    /**
     * 临时表删除数据
     * @param id 数据id
     * @return
     * @throws Exception
     */
    @DeleteMapping("/temp_products/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception {
        tempProductService.delete(id);
        return null;
    }

    /**
     * 从临时数据库中获取数据
     * @param id 数据id
     * @return
     * @throws Exception
     */
    @GetMapping("/temp_products/{id}")
    public TempProduct get(@PathVariable("id") int id) throws Exception {
        return tempProductService.get(id);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam("string") String str) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = "./temp_data_fig/temp_" + str + "/" + fileName;
        File dest = new File(filePath);
        Files.copy(file.getInputStream(), dest.toPath());
        return filePath;
    }

    /**
     * 上传数据至临时表
     * @param bean product对象
     * @return 上传对象
     * @throws Exception
     */
    @PostMapping("/add_temp_products")
    public Object add(TempProduct bean, HttpServletRequest request) throws Exception {
        String method = request.getParameter("method");
        String name = request.getParameter("name");
        String systemType = request.getParameter("systemType");
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
        String data_Fig = request.getParameter("data_Fig");
        String data_File = request.getParameter("data_File");
        bean.setMethod(method);
        bean.setName(name);
        bean.setSystemType(systemType);
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
        bean.setData_Fig(data_Fig);
        bean.setData_File(data_File);
        tempProductService.add(bean);
        // saveOrUpdateImageFile(bean, image, request);
        return Result.success();
    }
    /**
     * 编辑临时表
     * @param bean 修改数据对象
     * @param request 非必须
     * @return
     * @throws Exception
     */
    @PostMapping("/temp_products/{id}")
    public Object update(TempProduct bean, HttpServletRequest request) throws Exception {
        String method = request.getParameter("method");
        String name = request.getParameter("name");
        String systemType = request.getParameter("systemType");
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
        String data_Fig = request.getParameter("data_Fig");
        String data_File = request.getParameter("data_File");
        bean.setMethod(method);
        bean.setName(name);
        bean.setSystemType(systemType);
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
        bean.setData_Fig(data_Fig);
        bean.setData_File(data_File);

        tempProductService.update(bean);
        return Result.success();
    }
}
