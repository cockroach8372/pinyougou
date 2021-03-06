package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 */
public interface BrandService {
    public List<TbBrand> findAll();
    public PageResult findPage(int page,int size);
    public void add(TbBrand tbBrand);
    public TbBrand findOne(Long id);
    public void update(TbBrand tbBrand);
    public void delete(Long [] ids);
    public PageResult findPage(TbBrand tbBrand,int page,int size);
    List<Map<String,Object>>selectOptionList();
}
