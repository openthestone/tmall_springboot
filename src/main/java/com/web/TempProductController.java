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
        tempProductService.update(bean);
        return Result.success();
    }
}
