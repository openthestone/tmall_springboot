package com.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pojo.NonStandardProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


public interface NonStandardProductDao extends JpaRepository<NonStandardProduct, Integer>, JpaSpecificationExecutor<NonStandardProduct> {

    @Query(value = "select * from non_standard_product where " +
            "if(?1 is not null, method = ?1, 1=1) " +
            "and if(?2 is not null, systemType = ?2, 1=1) " +
            "and if(?3 is not null, dataType = ?3, 1=1) " +
            "and if(?4 is not null, sizeX > ?4 - 0.0001, 1=1) " +
            "and if(?5 is not null, sizeX < ?5 + 0.0001, 1=1) " +
            "and if(?6 is not null, sizeY > ?6 - 0.0001, 1=1) " +
            "and if(?7 is not null, sizeY < ?7 + 0.0001, 1=1) " +
            "and if(?8 is not null, sizeZ > ?8 - 0.0001, 1=1) " +
            "and if(?9 is not null, sizeZ < ?9 + 0.0001, 1=1) " +
            "and if(?10 is not null, strainX > ?10 - 0.0001, 1=1) " +
            "and if(?11 is not null, strainX < ?11 + 0.0001, 1=1) " +
            "and if(?12 is not null, strainY > ?12 - 0.0001, 1=1) " +
            "and if(?13 is not null, strainY < ?13 + 0.0001, 1=1) " +
            "and if(?14 is not null, nx > ?14 - 0.0001, 1=1) " +
            "and if(?15 is not null, nx < ?15 + 0.0001, 1=1) " +
            "and if(?16 is not null, ny > ?16 - 0.0001, 1=1) " +
            "and if(?17 is not null, ny < ?17 + 0.0001, 1=1) " +
            "and if(?18 is not null, elecX > ?18 - 0.0001, 1=1) " +
            "and if(?19 is not null, elecX < ?19 + 0.0001, 1=1) " +
            "and if(?20 is not null, elecY > ?20 - 0.0001, 1=1) " +
            "and if(?21 is not null, elecY < ?21 + 0.0001, 1=1) " +
            "and if(?22 is not null, elecZ > ?22 - 0.0001, 1=1)" +
            "and if(?23 is not null, elecZ < ?23 + 0.0001, 1=1)"
            , nativeQuery = true)
    List<NonStandardProduct> find(
            String method, String systemType, Integer dataType, Float sizeX_min, Float sizeX_max,
            Float sizeY_min, Float sizeY_max, Float sizeZ_min, Float sizeZ_max,
            Float strainX_min, Float strainX_max, Float strainY_min, Float strainY_max,
            Float nx_min, Float nx_max, Float ny_min, Float ny_max, Float elecX_min,
            Float elecX_max, Float elecY_min, Float elecY_max, Float elecZ_min, Float elecZ_max);

}
