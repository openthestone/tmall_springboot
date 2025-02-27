package com.service;

import com.dao.ProductDao;
import com.util.Page4Navigator;
import com.util.ZipFilesUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
        Page<Product> pageFromJPA = productDao.findAll(pageable);

        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }



    public List<Product> queryFile(List<Map<String, String>> selectValue) {
//        Map<String, String> map = new HashMap<>();
        int dataType = -1;
        float sizeX = 0.0F;
        float sizeY = 0.0F;
        float sizeZ = 0.0F;
        float strainX = 0.0F;
        float strainY = 0.0F;
        float nx = 0.0F;
        float ny = 0.0F;
        float elecX = 0.0F;
        float elecY = 0.0F;
        float elecZ = 0.0F;
        for (Map<String, String> m : selectValue) {
            switch (m.get("attribute")) {
                case "id_0":
                    dataType = Integer.parseInt(m.get("value"));
                    break;
                case "id_1":
                    sizeX = Float.parseFloat(m.get("value"));
                    break;
                case "id_2":
                    sizeY = Float.parseFloat(m.get("value"));
                    break;
                case "id_3":
                    sizeZ = Float.parseFloat(m.get("value"));
                    break;
                case "id_4":
                    strainX = Float.parseFloat(m.get("value"));
                    break;
                case "id_5":
                    strainY = Float.parseFloat(m.get("value"));
                    break;
                case "id_6":
                    nx = Float.parseFloat(m.get("value"));
                    break;
                case "id_7":
                    ny = Float.parseFloat(m.get("value"));
                    break;
                case "id_8":
                    elecX = Float.parseFloat(m.get("value"));
                    break;
                case "id_9":
                    elecY = Float.parseFloat(m.get("value"));
                    break;
                case "id_10":
                    elecZ = Float.parseFloat(m.get("value"));
                    break;
            }
        }
        return productDao.find(dataType, sizeX, sizeY, sizeZ, strainX, strainY, nx, ny, elecX, elecY, elecZ);
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
        return productDao.findOne(id);
    }

    public void update(Product bean) {
        productDao.update(bean.getId(), bean.getDataType(), bean.getSizeX(), bean.getSizeY(), bean.getSizeZ(), bean.getStrainX(), bean.getStrainY(), bean.getNX(), bean.getNY(),
                bean.getElecX(), bean.getElecY(), bean.getElecZ(), bean.getXY_Fig(), bean.getXZ_Fig(), bean.getXYZ_Fig(), bean.getData_File());
//        productDao.save(bean);
    }

    public List<Product> search(Float sizeX, Float sizeY, Float sizeZ, Float strainX, Float strainY, Float nx, Float ny, Float elecX, Float elecY,
                                Float elecZ, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        System.out.println(sizeX);
        return productDao.findBySizeXAndSizeYAndSizeZAndStrainXAndStrainYAndNxAndNyAndElecXAndElecYAndElecZ(sizeX, sizeY, sizeZ, strainX, strainY,
                nx, ny, elecX, elecY, elecZ, pageable);
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
