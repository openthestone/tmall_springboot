package com.service;

import com.alibaba.fastjson.JSONObject;
import com.dao.ProductDao;
import com.util.Page4Navigator;
import com.util.TypeOfValue;
import com.util.ZipFilesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.pojo.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

@Service
public class ProductService {
    @Autowired
    ProductDao productDao;

    public Page4Navigator<Product> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA = productDao.findAll(pageable);

        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }



    public List<Product> queryFile(List<Map<String, String>> selectValue) {
        Map<String, String> map = new HashMap<>();
        String dataType = "";
        String sizeX = "";
        String sizeY = "";
        String sizeZ = "";
        String strainX = "";
        String strainY = "";
        String nx = "";
        String ny = "";
        String elecX = "";
        String elecY = "";
        String elecZ = "";
        for (Map<String, String> stringStringMap : selectValue) {
            switch (stringStringMap.get("attribute")) {
                case "id_0":
                    dataType = stringStringMap.get("value");
                    break;
                case "id_1":
                    sizeX = stringStringMap.get("value");
                    break;
                case "id_2":
                    sizeY = stringStringMap.get("value");
                    break;
                case "id_3":
                    sizeZ = stringStringMap.get("value");
                    break;
                case "id_4":
                    strainX = stringStringMap.get("value");
                    break;
                case "id_5":
                    strainY = stringStringMap.get("value");
                    break;
                case "id_6":
                    nx = stringStringMap.get("value");
                    break;
                case "id_7":
                    ny = stringStringMap.get("value");
                    break;
                case "id_8":
                    elecX = stringStringMap.get("value");
                    break;
                case "id_9":
                    elecY = stringStringMap.get("value");
                    break;
                case "id_10":
                    elecZ = stringStringMap.get("value");
                    break;
            }
        }
        List<Product> products = productDao.find(dataType, sizeX, sizeY, sizeZ, strainX, strainY, nx, ny, elecX, elecY, elecZ);
        return products;
    }
//    public Page4Navigator<Product> listFile(int start, int size, int navigatePages) {
//        Sort sort = new Sort(Sort.Direction.DESC, "id");
//        Pageable pageable = new PageRequest(start, size, sort);
//     //   Page pageFromJPA = productDao.find(dataType, sizeX, sizeY, sizeZ, strainX, strainY, nX, nY, elecX, elecY, elecZ, pageable);
//
//        return new Page4Navigator<>(pageFromJPA, navigatePages);
//    }

    public List<Product> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return productDao.findAll(sort);
    }


    public void add(Product bean) {
        productDao.save(bean);
    }

    public void delete(int id) {
        productDao.delete(id);
    }

    public Product get(int id) {
        Product c = productDao.findOne(id);
        return c;
    }

    public void update(Product bean) {
        productDao.update(bean.getId(), bean.getDataType(), bean.getSizeX(), bean.getSizeY(), bean.getSizeZ(), bean.getStrainX(), bean.getStrainY(), bean.getNX(), bean.getNY(),
                bean.getElecX(), bean.getElecY(), bean.getElecZ(), bean.getXY_Fig(), bean.getXZ_Fig(), bean.getXYZ_Fig(), bean.getDataType());
//        productDao.save(bean);
    }

    public List<Product> search(String dataType, String sizeX, String sizeY, String sizeZ, String strainX, String strainY, String nx, String ny, String elecX, String elecY,
                                String elecZ, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        List<Product> products = productDao.findByDataTypeAndSizeXAndSizeYAndSizeZAndStrainXAndStrainYAndNxAndNyAndElecXAndElecYAndElecZ(dataType, sizeX, sizeY, sizeZ, strainX, strainY,
                nx, ny, elecX, elecY, elecZ, pageable);
        return products;
    }


    public void downloadMulti(Collection<Product> products, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<File> files = new ArrayList<>();

        for (Product product : products) {
            File file = new File(product.getData_File());
            if (file.exists()) {
                files.add(file);
            } else {
                throw new Exception("文件 :" + product.getData_File() + "不存在,请联系管理员！");
            }
            File xy_Fig = new File(product.getXY_Fig());
            if (xy_Fig.exists()) {
                files.add(xy_Fig);
            }
            File xz_Fig = new File(product.getXZ_Fig());
            if (xz_Fig.exists()) {
                files.add(xz_Fig);
            }
            File xyz_Fig = new File(product.getXYZ_Fig());
            if (xyz_Fig.exists()) {
                files.add(xyz_Fig);
            }
        }

        if (files.isEmpty()) {
            throw new Exception("当前选择文件不存在，请联系管理员！");
        } else {
            String tempName = "temp.zip";
            String path = "./data_fig/temp/" + tempName;
            //压缩
            ZipFilesUtil.createZipFiles(files, path, response);
            //下载
            ZipFilesUtil.downloadFile(new File(path), tempName, request, response);
        }
    }
    public void upload(){

    }
}
