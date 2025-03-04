package com.service;

import com.dao.NonStandardProductDao;
import com.pojo.NonStandardProduct;
import com.util.Page4Navigator;
import com.util.ZipFilesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class NonStandardProductService {
    @Autowired
    NonStandardProductDao nonStandardProductDao;

    public Page4Navigator<NonStandardProduct> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<NonStandardProduct> pageFromJPA = nonStandardProductDao.findAll(pageable);

        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    public List<NonStandardProduct> queryFile(List<Map<String, String>> selectValue) {
        String method = null;
        String systemType = null;
        Integer dataType = null;
        Float sizeX_min = null;
        Float sizeX_max = null;
        Float sizeY_min = null;
        Float sizeY_max = null;
        Float sizeZ_min = null;
        Float sizeZ_max = null;
        Float strainX_min = null;
        Float strainX_max = null;
        Float strainY_min = null;
        Float strainY_max = null;
        Float nx_min = null;
        Float nx_max = null;
        Float ny_min = null;
        Float ny_max = null;
        Float elecX_min = null;
        Float elecX_max = null;
        Float elecY_min = null;
        Float elecY_max = null;
        Float elecZ_min = null;
        Float elecZ_max = null;
        for (Map<String, String> m : selectValue) {
            switch (m.get("attribute")) {
                case "method": {
                    method = m.get("value");
                    break;
                }
                case "systemType": {
                    systemType = m.get("value");
                    break;
                }
                case "dataType": {
                    dataType = Integer.parseInt(m.get("value"));
                    break;
                }
                case "sizeXMin": {
                    sizeX_min = Float.parseFloat(m.get("value"));
                    break;
                }
                case "sizeXMax": {
                    sizeX_max = Float.parseFloat(m.get("value"));
                    break;
                }
                case "sizeYMin": {
                    sizeY_min = Float.parseFloat(m.get("value"));
                    break;
                }
                case "sizeYMax": {
                    sizeY_max = Float.parseFloat(m.get("value"));
                    break;
                }
                case "sizeZMin": {
                    sizeZ_min = Float.parseFloat(m.get("value"));
                    break;
                }
                case "sizeZMax": {
                    sizeZ_max = Float.parseFloat(m.get("value"));
                    break;
                }
                case "strainXMin": {
                    strainX_min = Float.parseFloat(m.get("value"));
                    break;
                }
                case "strainXMax": {
                    strainX_max = Float.parseFloat(m.get("value"));
                    break;
                }
                case "strainYMin": {
                    strainY_min = Float.parseFloat(m.get("value"));
                    break;
                }
                case "strainYMax": {
                    strainY_max = Float.parseFloat(m.get("value"));
                    break;
                }
                case "nxMin": {
                    nx_min = Float.parseFloat(m.get("value"));
                    break;
                }
                case "nxMax": {
                    nx_max = Float.parseFloat(m.get("value"));
                    break;
                }
                case "nyMin": {
                    ny_min = Float.parseFloat(m.get("value"));
                    break;
                }
                case "nyMax": {
                    ny_max = Float.parseFloat(m.get("value"));
                    break;
                }
                case "elecXMin": {
                    elecX_min = Float.parseFloat(m.get("value"));
                    break;
                }
                case "elecXMax": {
                    elecX_max = Float.parseFloat(m.get("value"));
                    break;
                }
                case "elecYMin": {
                    elecY_min = Float.parseFloat(m.get("value"));
                    break;
                }
                case "elecYMax": {
                    elecY_max = Float.parseFloat(m.get("value"));
                    break;
                }
                case "elecZMin": {
                    elecZ_min = Float.parseFloat(m.get("value"));
                    break;
                }
                case "elecZMax": {
                    elecZ_max = Float.parseFloat(m.get("value"));
                    break;
                }
            }
        }
        return nonStandardProductDao.find(method, systemType, dataType, sizeX_min, sizeX_max,
                sizeY_min, sizeY_max, sizeZ_min, sizeZ_max, strainX_min, strainX_max,
                strainY_min, strainY_max, nx_min, nx_max, ny_min, ny_max, elecX_min, elecX_max,
                elecY_min, elecY_max, elecZ_min, elecZ_max);
    }

    public void downloadMulti(Collection<NonStandardProduct> products,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        // 这里只以第一个产品为例，实际可扩展为多产品下载
        NonStandardProduct product = products.iterator().next();
        List<File> filesToDownload = new ArrayList<>();
        if (product.getXY_Fig() != null && !product.getXY_Fig().isEmpty()) {
            File xyFile = new File(product.getXY_Fig());
            if (xyFile.exists()) {
                filesToDownload.add(xyFile);
            }
        }
        if (product.getXZ_Fig() != null && !product.getXZ_Fig().isEmpty()) {
            File xzFile = new File(product.getXZ_Fig());
            if (xzFile.exists()) {
                filesToDownload.add(xzFile);
            }
        }
        if (product.getXYZ_Fig() != null && !product.getXYZ_Fig().isEmpty()) {
            File xyzFile = new File(product.getXYZ_Fig());
            if (xyzFile.exists()) {
                filesToDownload.add(xyzFile);
            }
        }
        if (product.getData_Fig() != null && !product.getData_Fig().isEmpty()) {
            File figFile = new File(product.getData_Fig());
            if (figFile.exists()) {
                filesToDownload.add(figFile);
            }
        }
        if (product.getData_File() != null && !product.getData_File().isEmpty()) {
            File dataFile = new File(product.getData_File());
            if (dataFile.exists()) {
                filesToDownload.add(dataFile);
            }
        }
        if (filesToDownload.isEmpty()) {
            throw new Exception("所选产品没有记录任何文件");
        }
        // 调用工具类压缩下载
        File zipFile = ZipFilesUtil.createZip(filesToDownload);
        ZipFilesUtil.downloadFile(zipFile, zipFile.getName(), request, response);
        zipFile.delete();
    }

    public List<NonStandardProduct> list() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return nonStandardProductDao.findAll(sort);
    }


    public void delete(int id) {
        nonStandardProductDao.delete(id);
    }

    public NonStandardProduct get(int id) {
        return nonStandardProductDao.findOne(id);
    }
    public void add(NonStandardProduct bean) {
        nonStandardProductDao.save(bean);
    }
    public void update(NonStandardProduct bean) {
        nonStandardProductDao.save(bean);
    }
}
