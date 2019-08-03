package com.itheima.ssm.controller;

import com.itheima.ssm.Product;
import com.itheima.ssm.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
      @Autowired
     private IProductService iProductService;

    @RequestMapping("/findAll.do")
    public ModelAndView findAll(){
        ModelAndView modelAndView = new ModelAndView();
        List<Product> productList = iProductService.findAll();
        modelAndView.addObject("productList",productList);
        modelAndView.setViewName("product-list1");
        return modelAndView;
    }
    @RequestMapping("/save.do")
    public String save(Product product){
          iProductService.save(product);
          return "redirect:findAll.do";

    }
}
