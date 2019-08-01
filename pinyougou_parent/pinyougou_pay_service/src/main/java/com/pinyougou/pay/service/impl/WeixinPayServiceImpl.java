package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;
import util.HttpClient;

import java.util.HashMap;
import java.util.Map;
@Service
public class WeixinPayServiceImpl  implements WeixinPayService {
    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private  String partner;
    @Value("${partnerkey}")
    private String partnerkey;

    /**
     * 生成二维码。订单号。金额
     */
    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        //封装请求参数
        Map param=new HashMap();
        param.put("appid",appid);
        param.put("mch_id",partner);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("body","品优购");
        param.put("out_trade_no",out_trade_no);
        param.put("total_fee",total_fee);
        param.put("spbill_create_ip","127.0.0.1");
        param.put("notify_url","http://test.itcast.cn");//回调地址随便写，用的方式二，回调地址没用到
        param.put("trade_type","NATIVE");

        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println(xmlParam);
            HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();//发送请求
            String content = httpClient.getContent();//获得结果
            System.out.println("content"+content);
            Map<String, String> xmlMap = WXPayUtil.xmlToMap(content);
            System.out.println("xmlmap"+xmlMap);
            Map<String, String> map=new HashMap<>();
            map.put("total_fee",total_fee);
            map.put("out_trade_no",out_trade_no);

            map.put("code_url",xmlMap.get("code_url"));

          return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap();
    }

    /**
     * 查询订单状态
     * @param out_trade_no
     * @return
     */
    @Override
    public Map queryPayStatus(String out_trade_no) {
        Map param=new HashMap();
        param.put("appid",appid);
        param.put("mch_id",partner);
        param.put("out_trade_no",out_trade_no);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        String url="https://api.mch.weixin.qq.com/pay/orderquery";

        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println("传到微信的数据："+xmlParam);
            HttpClient httpClient=new HttpClient(url);
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();//发送请求
            String result = httpClient.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            System.out.println("调动查询API返回结果："+result);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Map closePay(String out_trade_no) {
        //1.封装参数
        Map param=new HashMap();
        param.put("appid", appid);
        param.put("mch_id", partner);
        param.put("out_trade_no", out_trade_no);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            //2.发送请求
            HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/closeorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();

            //3.获取结果
            String xmlResult = httpClient.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(xmlResult);
            System.out.println("调动查询API返回结果："+xmlResult);

            return map;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
