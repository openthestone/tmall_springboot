package com.dao;

import com.pojo.TempProduct;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TempProductDao extends JpaRepository<TempProduct, Integer> {

}
