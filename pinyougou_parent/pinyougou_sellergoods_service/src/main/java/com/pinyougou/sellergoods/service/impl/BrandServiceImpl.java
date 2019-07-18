package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {
     @Autowired
    private TbBrandMapper brandMapper;
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<TbBrand> tbBrands = (Page<TbBrand>) brandMapper.selectByExample(null);
        PageResult pageResult = new PageResult(tbBrands.getTotal(), tbBrands.getResult());
        return pageResult;
    }

    @Override
    public void add(TbBrand tbBrand) {
        brandMapper.insert(tbBrand);
    }

    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update( TbBrand tbBrand) {
        brandMapper.updateByPrimaryKey(tbBrand);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids.length>0&&ids!=null){
            for (Long id : ids) {
                brandMapper.deleteByPrimaryKey(id);
            }
        }

    }

    @Override
    public PageResult findPage(TbBrand tbBrand, int page, int size) {
        PageHelper.startPage(page,size);
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(tbBrand.getName())){
            criteria.andNameLike("%"+tbBrand.getName()+"%");
        }
        if (!StringUtils.isEmpty(tbBrand.getFirstChar())){
            criteria.andFirstCharEqualTo(tbBrand.getFirstChar());
        }


        Page<TbBrand> tbBrands = (Page<TbBrand>) brandMapper.selectByExample(example);
        PageResult pageResult = new PageResult(tbBrands.getTotal(), tbBrands.getResult());
        return pageResult;
    }

    @Override
    public List<Map<String, Object>> selectOptionList() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        List<TbBrand> tbBrands = brandMapper.selectByExample(null);

        for (TbBrand tbBrand : tbBrands) {
            Map map=new HashMap();
            map.put("id",tbBrand.getId());
            map.put("text",tbBrand.getName());
            list.add(map);
        }


        return  list;
    }

}
