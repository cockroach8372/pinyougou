package com.pinyougou.manager.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController  {
    @Reference
    private BrandService brandService;
     @RequestMapping("/findAll.do")
    public List<TbBrand> findAll(){
         return   brandService.findAll();

    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(Integer page,Integer size){
         return  brandService.findPage(page,size);
    }
    @RequestMapping("/add.do")
    public Result add(@RequestBody TbBrand tbBrand){
        try {
            brandService.add(tbBrand);
            Result result = new Result(true,"添加成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Result result = new Result(false, "添加失败");
            return result;
        }

    }
    @RequestMapping("/findOne.do")
    public TbBrand findOne(Long id){
          return brandService.findOne(id);
    }
    @RequestMapping("/update.do")
    public Result update(@RequestBody TbBrand tbBrand){
         if (!StringUtils.isEmpty(tbBrand.getFirstChar())&&!StringUtils.isEmpty(tbBrand.getName())&&tbBrand.getFirstChar().length()==1){
             brandService.update(tbBrand);
             return new Result(true,"更新成功");
         }else {
             return new Result(false,"更新失败");
         }
    }
     @RequestMapping("/delete.do")
    public Result delete(Long [] ids){
         if (!StringUtils.isEmpty(ids.toString())){
             brandService.delete(ids);
             return new Result(true,"删除成功");
         }else {
             return new Result(false,"删除失败");
         }
     }
    @RequestMapping("/search.do")
    public PageResult search(@RequestBody TbBrand tbBrand,Integer page,Integer size){
        return  brandService.findPage(tbBrand,page,size);
    }

    @RequestMapping("/selectOptionList")
    public List<Map<String,Object>>selectOptionList(){
        return brandService.selectOptionList();
    }




}
