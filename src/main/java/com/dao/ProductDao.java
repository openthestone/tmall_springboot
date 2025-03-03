package com.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pojo.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {

//    List<Product> findBySizeXAndSizeYAndSizeZAndStrainXAndStrainYAndNxAndNyAndElecXAndElecYAndElecZ(
//          Float sizeX, Float sizeY, Float sizeZ, Float strainX,
//          Float strainY, Float nx, Float ny, Float elecX, Float elecY, Float elecZ,
//          Pageable pageable);

    @Query(value = "select * from Product where " +
            "abs(sizeX - ?1) < 0.0001 " +
            "and abs(sizeY - ?2) < 0.0001 " +
            "and abs(sizeZ - ?3) < 0.0001 " +
            "and abs(strainX - ?4) < 0.0001 " +
            "and abs(strainY - ?5) < 0.0001 " +
            "and abs(nx - ?6) < 0.0001 " +
            "and abs(ny - ?7) < 0.0001 " +
            "and abs(elecX - ?8) < 0.0001 " +
            "and abs(elecY - ?9) < 0.0001 " +
            "and abs(elecZ - ?10) < 0.0001 " +
            "order by id desc"
            , nativeQuery = true)
    List<Product> findWithoutDataType(
            Float sizeX, Float sizeY, Float sizeZ, Float strainX, Float strainY,
            Float nx, Float ny, Float elecX, Float elecY, Float elecZ);

    @Query(value = "select * from Product where if(?1 is not null, dataType=?1, 1=1) " +
            "and if(?2 is not null, abs(sizeX - ?2) < 0.0001, 1=1) " +
            "and if(?3 is not null, abs(sizeY - ?3) < 0.0001, 1=1) " +
            "and if(?4 is not null, abs(sizeZ - ?4) < 0.0001, 1=1) " +
            "and if(?5 is not null, abs(strainX - ?5) < 0.0001, 1=1) " +
            "and if(?6 is not null, abs(strainY - ?6) < 0.0001, 1=1) " +
            "and if(?7 is not null, abs(nx - ?7) < 0.0001, 1=1) " +
            "and if(?8 is not null, abs(ny - ?8) < 0.0001, 1=1) " +
            "and if(?9 is not null, abs(elecX - ?9) < 0.0001, 1=1) " +
            "and if(?10 is not null, abs(elecY - ?10) < 0.0001, 1=1) " +
            "and if(?11 is not null, abs(elecZ - ?11) < 0.0001, 1=1)"
            , nativeQuery = true)
    List<Product> find(
            Integer dataType, Float sizeX, Float sizeY, Float sizeZ, Float strainX, Float strainY,
            Float nx, Float ny, Float elecX, Float elecY, Float elecZ);

    @Transactional
    @Modifying()
    @Query(value = "update Product p set " +
            "p.dataType=?2, p.sizeX=?3, p.sizeY=?4, p.sizeZ=?5, " +
            "p.strainX=?6, p.strainY=?7, p.nx=?8, p.ny=?9, " +
            "p.elecX=?10, p.elecY=?11, p.elecZ=?12, " +
            "p.xy_Fig=?13, p.xz_Fig=?14, p.xyz_Fig=?15, p.data_File=?16 " +
            "where p.id = ?1")
    void update(Integer id, Integer dataType, Float sizeX, Float sizeY, Float sizeZ,
                Float strainX, Float strainY, Float nx, Float ny, Float elecX, Float elecY,
                Float elecZ, String xy_Fig, String xz_Fig, String xyz_Fig, String data_File);

    @Query(value = "select * from Product where " +
            "((:fixedAttr1 = 'sizeZ' and abs(sizeZ - :fixedValue1) < 0.0001) or " +
            " (:fixedAttr1 = 'strainX' and abs(strainX - :fixedValue1) < 0.0001) or " +
            " (:fixedAttr1 = 'strainY' and abs(strainY - :fixedValue1) < 0.0001) or " +
            " (:fixedAttr1 = 'elecZ' and abs(elecZ - :fixedValue1) < 0.0001)) " +
            "and " +
            "((:fixedAttr2 = 'sizeZ' and abs(sizeZ - :fixedValue2) < 0.0001) or " +
            " (:fixedAttr2 = 'strainX' and abs(strainX - :fixedValue2) < 0.0001) or " +
            " (:fixedAttr2 = 'strainY' and abs(strainY - :fixedValue2) < 0.0001) or " +
            " (:fixedAttr2 = 'elecZ' and abs(elecZ - :fixedValue2) < 0.0001)) " +
            "and " +
            "((:varAttr1 = 'sizeZ' and sizeZ between :varMin1 - 0.0001 and :varMax1 + 0.0001) or " +
            " (:varAttr1 = 'strainX' and strainX between :varMin1 - 0.0001 and :varMax1 + 0.0001) or " +
            " (:varAttr1 = 'strainY' and strainY between :varMin1 - 0.0001 and :varMax1 + 0.0001) or " +
            " (:varAttr1 = 'elecZ' and elecZ between :varMin1 - 0.0001 and :varMax1 + 0.0001)) " +
            "and " +
            "((:varAttr2 = 'sizeZ' and sizeZ between :varMin2 - 0.0001 and :varMax2 + 0.0001) or " +
            " (:varAttr2 = 'strainX' and strainX between :varMin2 - 0.0001 and :varMax2 + 0.0001) or " +
            " (:varAttr2 = 'strainY' and strainY between :varMin2 - 0.0001 and :varMax2 + 0.0001) or " +
            " (:varAttr2 = 'elecZ' and elecZ between :varMin2 - 0.0001 and :varMax2 + 0.0001))"
            , nativeQuery = true)
    List<Product> findPhaseDiagramProducts(@Param("fixedAttr1") String fixedAttr1,
                                           @Param("fixedValue1") Float fixedValue1,
                                           @Param("fixedAttr2") String fixedAttr2,
                                           @Param("fixedValue2") Float fixedValue2,
                                           @Param("varAttr1") String varAttr1,
                                           @Param("varMin1") Float varMin1,
                                           @Param("varMax1") Float varMax1,
                                           @Param("varAttr2") String varAttr2,
                                           @Param("varMin2") Float varMin2,
                                           @Param("varMax2") Float varMax2);

    @Query(value = "from Product p where p.xy_Fig =?1")
    Product findByXY_Fig(String xy_Fig);
    @Query(value = "from Product p where p.xz_Fig =?1")
    Product findByXZ_Fig(String xz_Fig);
}
