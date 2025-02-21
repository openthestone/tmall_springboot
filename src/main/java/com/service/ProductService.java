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
    @Value("${fileProps.filePath}")
    private String filePath;

    public Page4Navigator<Product> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA = productDao.findAll(pageable);

        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }



    public List<Integer> queryFile(List<Map<String, String>> selectValue) {
        Map<String, String> map = new HashMap<>();
        String sizex = "";
        String sizey = "";
        String sizez = "";
        String strainX = "";
        String strainY = "";
        String nX = "";
        String nY = "";
        String elecX = "";
        String elecY = "";
        String elecZ = "";
        for (Map<String, String> stringStringMap : selectValue) {
            switch (stringStringMap.get("attribute")) {
                case "id_0":
                    sizex = stringStringMap.get("value");
                    break;
                case "id_1":
                    sizey = stringStringMap.get("value");
                    break;
                case "id_2":
                    sizez = stringStringMap.get("value");
                    break;
                case "id_3":
                    strainX = stringStringMap.get("value");
                    break;
                case "id_4":
                    strainY = stringStringMap.get("value");
                    break;
                case "id_5":
                    nX = stringStringMap.get("value");
                    break;
                case "id_6":
                    nY = stringStringMap.get("value");
                    break;
                case "id_7":
                    elecX = stringStringMap.get("value");
                    break;
                case "id_8":
                    elecY = stringStringMap.get("value");
                    break;
                case "id_9":
                    elecZ = stringStringMap.get("value");
                    break;
            }
        }
        List<Integer> products = productDao.find(sizex, sizey, sizez, strainX, strainY, nX, nY, elecX, elecY, elecZ);
        return products;
    }
//    public Page4Navigator<Product> listFile(int start, int size, int navigatePages) {
//        Sort sort = new Sort(Sort.Direction.DESC, "id");
//        Pageable pageable = new PageRequest(start, size, sort);
//     //   Page pageFromJPA = productDao.find(sizex, sizey, sizez, strainX, strainY, nX, nY, elecX, elecY, elecZ, pageable);
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
        productDao.save(bean);
    }

    public List<Product> search(String sizex, String sizey, String sizez, String strainX, String strainY, String NX, String NY, String elecX, String elecY,
                                String elecZ, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        List<Product> products = productDao.findBySizexAndSizeyAndSizezAndNXAndNYAndStrainXAndStrainYAndElecXAndElecYAndElecZ(sizex, sizey, sizez, strainX, strainY, NX, NY, elecX, elecY, elecZ
                , pageable);
        System.out.println(products);
        return products;
    }


    public void downloadMulti(Collection<Integer> ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<File> files = new ArrayList<>();

        for (Integer id : ids) {
            File file = new File(filePath + "/" + id+".txt");
            if (file.exists()) {
                files.add(file);
            } else {
                throw new Exception("文件 :" +id + ".txt 不存在,请联系管理员!");
            }
        }

        if (files.isEmpty()) {
            throw new Exception("当前选择文件不存在,请联系管理员!");
        } else {
            String tempName = "temp.zip";
            String path = filePath + "/" + tempName;
            //压缩
            ZipFilesUtil.createZipFiles(files, path, response);
            //下载
            ZipFilesUtil.downloadFile(new File(path), tempName, request, response);
        }
    }
    public void upload(){

    }
}
