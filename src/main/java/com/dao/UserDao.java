package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pojo.User;

public interface UserDao extends JpaRepository<User,Integer>{
    User findByName(String name);
    User getByNameAndPassword(String name, String password);
}