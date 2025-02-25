package com.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pojo.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {


    List<Product> findByDataTypeAndSizeXAndSizeYAndSizeZAndStrainXAndStrainYAndNXAndNYAndElecXAndElecYAndElecZ(String dataType, String sizeX, String sizeY, String sizeZ, String strainX, String strainY, String nx, String ny
            , String elecX, String elecY, String elecZ, Pageable pageable);

    //                    {{bean.dataType}}
    //                    {{bean.sizeX}}
    //                    {{bean.sizeY}}
    //                    {{bean.sizeZ}}
    //                    {{bean.strainX}}
    //                    {{bean.strainY}}
    //                    {{bean.nx}}
    //                    {{bean.ny}}
    //                    {{bean.elecX}}
    //                    {{bean.elecY}}
    //                    {{bean.elecZ}}
    @Query(value = "select * from Product where if(?1 !='',dataType=?1,1=1) and if(?2 !='',sizeX=?2,1=1) and if(?3 !='',sizeY=?3,1=1) and if(?4 !='',sizeZ=?4,1=1)" +
            " and if(?5 !='',strainX=?5,1=1) and if(?6 !='',strainY=?6,1=1) and if(?7 !='',nx=?7,1=1) and if(?8 !='',ny=?8,1=1)" +
            " and if(?9 !='',elecX=?9,1=1) and if(?10 !='',elecY=?10,1=1) and if(?11 !='',elecZ=?11,1=1)"
            , nativeQuery = true)
    List<Product> find(String dataType, String sizeX, String sizeY, String sizeZ, String strainX, String strainY, String nx, String ny, String elecX, String elecY,
                       String elecZ);

    @Transactional
    @Modifying()
    @Query(value = "update Product p set p.dataType=?2, p.sizeX=?3, p.sizeY=?4, p.sizeZ=?5, p.strainX=?6, p.strainY=?7, p.nx=?8, p.ny=?9, p.elecX=?10," +
            " p.elecY=?11, p.elecZ=?12, p.xy_Fig=?13, p.xz_Fig=?14, p.xyz_Fig=?15, p.data_File=?16 where p.id = ?1")
    void update(Integer id, String dataType, String sizeX, String sizeY, String sizeZ, String strainX, String strainY, String nx, String ny, String elecX, String elecY,
                String elecZ, String xy_Fig, String xz_Fig, String xyz_Fig, String data_File);
}
