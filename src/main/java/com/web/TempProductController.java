package com.web;


import com.pojo.Product;
import com.pojo.TempProduct;
import com.service.TempProductService;
import com.util.Page4Navigator;
import com.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public Page4Navigator<TempProduct> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        Page4Navigator<TempProduct> page = tempProductService.list(start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return page;
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
//        File  imageFolder= new File(request.getServletContext().getRealPath("img/product"));
//        File  fileFolder= new File(request.getServletContext().getRealPath("file/product"));
//        File imagfile = new File(imageFolder,id+".jpg");
//        File file = new File(fileFolder,id+".in");
//        imagfile.delete();
//        file.delete();
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
        TempProduct bean = tempProductService.get(id);
        return bean;
    }

    /**
     * 上传数据至临时表
     * @param bean product对象
     * @return 上传对象
     * @throws Exception
     */
    @PostMapping("/add_temp_products")
    public Object add(TempProduct bean) throws Exception {
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
        String name = request.getParameter("name");
        String dataType = request.getParameter("dataType");
        String sizeX = request.getParameter("sizeX");
        String sizeY = request.getParameter("sizeY");
        String sizeZ = request.getParameter("sizeZ");
        String strainX = request.getParameter("strainX");
        String strainY = request.getParameter("strainY");
        String nX = request.getParameter("nX");
        String nY = request.getParameter("nY");
        String elecX = request.getParameter("elecX");
        String elecY = request.getParameter("elecY");
        String elecZ = request.getParameter("elecZ");
        String xY_Fig = request.getParameter("xY_Fig");
        String xZ_Fig = request.getParameter("xZ_Fig");
        String xYZ_Fig = request.getParameter("xYZ_Fig");
        String data_File = request.getParameter("data_File");
        bean.setName(name);
        bean.setDataType(dataType);
        bean.setSizeX(sizeX);
        bean.setSizeY(sizeY);
        bean.setSizeZ(sizeZ);
        bean.setStrainX(strainX);
        bean.setStrainY(strainY);
        bean.setNX(nX);
        bean.setNY(nY);
        bean.setElecX(elecX);
        bean.setElecY(elecY);
        bean.setElecZ(elecZ);
        bean.setElecZ(elecZ);
        bean.setXY_Fig(xY_Fig);
        bean.setXZ_Fig(xZ_Fig);
        bean.setXYZ_Fig(xYZ_Fig);
        bean.setData_File(data_File);

//        if(image!=null) {
//            saveOrUpdateImageFile(bean, image, request);
//        }
        tempProductService.update(bean);
        return Result.success();
    }
}
