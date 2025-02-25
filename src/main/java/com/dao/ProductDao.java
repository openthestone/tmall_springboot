package com.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pojo.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {


    List<Product> findByDataTypeAndSizeXAndSizeYAndSizeZAndStrainXAndStrainYAndNXAndNYAndElecXAndElecYAndElecZ(String dataType, String sizeX, String sizeY, String sizeZ, String strainX, String strainY, String nX, String nY
            , String elecX, String elecY, String elecZ, Pageable pageable);

    //                    {{bean.dataType}}
    //                    {{bean.sizeX}}
    //                    {{bean.sizeY}}
    //                    {{bean.sizeZ}}
    //                    {{bean.strainX}}
    //                    {{bean.strainY}}
    //                    {{bean.nX}}
    //                    {{bean.nY}}
    //                    {{bean.elecX}}
    //                    {{bean.elecY}}
    //                    {{bean.elecZ}}
    @Query(value = "select * from product where if(?1 !='',dataType=?1,1=1) and if(?2 !='',sizeX=?2,1=1) and if(?3 !='',sizeY=?3,1=1) and if(?4 !='',sizeZ=?4,1=1)" +
            " and if(?5 !='',strainX=?5,1=1) and if(?6 !='',strainY=?6,1=1) and if(?7 !='',nX=?7,1=1) and if(?8 !='',nY=?8,1=1)" +
            " and if(?9 !='',elecX=?9,1=1) and if(?10 !='',elecY=?10,1=1) and if(?11 !='',elecZ=?11,1=1)"
            , nativeQuery = true)
    List<Product> find(String dataType, String sizeX, String sizeY, String sizeZ, String strainX, String strainY, String nX, String nY, String elecX, String elecY,
                       String elecZ);

}
