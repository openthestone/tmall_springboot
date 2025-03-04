package com.web;


import com.pojo.NonStandardProduct;
import com.service.NonStandardProductService;
import com.util.Page4Navigator;
import com.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class NonStandardProductController {
    @Autowired
    NonStandardProductService nonStandardProductService;

    @GetMapping("/non_standard_products")
    public Page4Navigator<NonStandardProduct> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
        start = start < 0 ? 0 : start;
        return nonStandardProductService.list(start, size, 5);
    }

    @PostMapping("/non_standard_queryData")
    public List<NonStandardProduct> fileN(@RequestBody List<Map<String, String>> body) {
        return nonStandardProductService.queryFile(body);
    }

    @PostMapping("/non_standard_download")
    public void fileMultiDownload(@RequestBody Collection<NonStandardProduct> products, HttpServletRequest request, HttpServletResponse response) throws Exception {
        nonStandardProductService.downloadMulti(products, request, response);
    }

    @DeleteMapping("/non_standard_products/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception {
        nonStandardProductService.delete(id);
        return null;
    }

    @GetMapping("/non_standard_products/{id}")
    public NonStandardProduct get(@PathVariable("id") int id) throws Exception {
        return nonStandardProductService.get(id);
    }

    @PostMapping("/add_non_standard_products")
    public Object add(NonStandardProduct bean, HttpServletRequest request) throws Exception {
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
        nonStandardProductService.add(bean);
        return Result.success();
    }

    @PostMapping("/non_standard_products/{id}")
    public Object update(NonStandardProduct bean, HttpServletRequest request) throws Exception {
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
        nonStandardProductService.update(bean);
        return Result.success();
    }
}
