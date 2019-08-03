package com.itheima.ssm.service;

import com.itheima.ssm.Product;

import java.util.List;

public interface IProductService {
    public List<Product> findAll();


    void save(Product product);
}
