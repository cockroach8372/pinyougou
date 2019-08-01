package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.group.Cart;
import entity.Result;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference(timeout=6000)
    private CartService cartService;
    @Autowired
    private HttpServletResponse response;

    @Autowired
     private HttpServletRequest request;

    @RequestMapping("/name")
    public Map findLoginUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("登录的名字"+name);
        Map map = new HashMap();
        map.put("loginName",name);
        return  map;
    }
    //从cookie中提取购物车
     @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
         //当前登录人账号
         String username = SecurityContextHolder.getContext().getAuthentication().getName();
         System.out.println("当前登录人："+username);
         String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
         if (cartListString == null || cartListString.equals("") || cartListString.equals("null")){
               cartListString="[]";
         }
         List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
         if(username.equals("anonymousUser")){
             System.out.println("从cookie中提取购物车");
             return cartList_cookie;
         }else{//如果已登录
             //获取redis购物车
             List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
             if(cartList_cookie.size()>0){//判断当本地购物车中存在数据
                 //得到合并后的购物车
                 List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);
                 //将合并后的购物车存入redis
                 cartService.saveCartListToRedis(username, cartList);
                 //本地购物车清除
                 util.CookieUtil.deleteCookie(request, response, "cartList");
                 System.out.println("执行了合并购物车的逻辑");
                 return cartList;
             }
             return cartList_redis;
         }


     }
       @RequestMapping("/addGoodsToCartList")
       @CrossOrigin(origins = "http://localhost:9105",allowCredentials = "true")
     public Result  addGoodsToCartList(Long itemId,Integer num){
//        response.setHeader("Access-Control-Allow-Origin","http://localhost:9105");
//        response.setHeader("Access-Control-Allow-Credentials","true");
           //当前登录人账号
           String username = SecurityContextHolder.getContext().getAuthentication().getName();
           System.out.println("当前登录人："+username);
         try {
             List<Cart> cartList = findCartList();
             cartList= cartService.addGoodsToCartList(cartList, itemId, num);
             if(username.equals("anonymousUser")){
                 String cartListString = JSON.toJSONString(cartList);
                 CookieUtil.setCookie(request,response,"cartList",cartListString,3600*24,"UTF-8");
                 System.out.println("向cookie中存储数据");

             }else {
                   cartService.saveCartListToRedis(username,cartList);
             }
             return  new Result(true,"存入购物车成功");
         } catch (Exception e) {
             e.printStackTrace();
             return  new Result(false,"存入购物车失败");
         }
     }





}
