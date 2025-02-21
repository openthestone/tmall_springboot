package com.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pojo.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {
    List<Product> findBySizexAndSizeyAndSizezAndNXAndNYAndStrainXAndStrainYAndElecXAndElecYAndElecZ(String sizex, String sizey, String sizez, String strainX, String strainY, String NX, String NY
            , String ElecX, String ElecY, String ElecZ, Pageable pageable);

    //                    {{bean.sizex}}
    //                    {{bean.sizey}}
    //                    {{bean.sizez}}
    //                    {{bean.strainX}}
    //                    {{bean.strainY}}
    //                    {{bean.nX}}
    //                    {{bean.nY}}
    //                    {{bean.elecX}}
    //                    {{bean.elecY}}n
    //                    {{bean.elecZ}}
    @Query(value = "select ID from product where if(?1 !='',sizex=?1,1=1) and if(?2 !='',sizey=?2,1=1) and if(?3 !='',sizez=?3,1=1)" +
            " and if(?4 !='',strainX=?4,1=1) and if(?5 !='',strainY=?5,1=1) and if(?6 !='',nX=?6,1=1) and if(?7 !='',nY=?7,1=1)" +
            " and if(?8 !='',ElecX=?8,1=1) and if(?9 !='',ElecY=?9,1=1) and if(?10 !='',ElecZ=?10,1=1)"
            , nativeQuery = true)
//    List<Product> find(String sizex, String sizey, String sizez, String strainX, String strainY, String nX, String nY, String elecX, String elecY,
//                       String elecZ, Pageable pageable);
    List<Integer> find(String sizex, String sizey, String sizez, String strainX, String strainY, String nX, String nY, String elecX, String elecY,
                       String elecZ);

}
