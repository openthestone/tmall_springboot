package com.service;

import com.dao.TempProductDao;
import com.pojo.Product;
import com.pojo.TempProduct;
import com.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TempProductService {
    @Autowired
    TempProductDao tempProductDao;

    public Page4Navigator<TempProduct> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA = tempProductDao.findAll(pageable);

        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    public List<TempProduct> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return tempProductDao.findAll(sort);
    }


    public void delete(int id) {
        tempProductDao.delete(id);
    }

    public TempProduct get(int id) {
        TempProduct c = tempProductDao.findOne(id);
        return c;
    }
    public void add(TempProduct bean) {
        tempProductDao.save(bean);
    }
    public void update(TempProduct bean) {
        tempProductDao.save(bean);
    }

}
