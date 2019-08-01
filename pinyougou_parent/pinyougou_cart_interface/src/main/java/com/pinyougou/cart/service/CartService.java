package com.pinyougou.cart.service;

import com.pinyougou.group.Cart;

import java.util.List;

public interface CartService {
     public List<Cart>addGoodsToCartList(List<Cart>cartList, Long skuId,Integer num);
     public List<Cart> findCartListFromRedis(String username);
     public void saveCartListToRedis(String username,List<Cart> cartList);
     public  List<Cart> mergeCartList(List<Cart>cartList1,List<Cart>cartList2);
}
