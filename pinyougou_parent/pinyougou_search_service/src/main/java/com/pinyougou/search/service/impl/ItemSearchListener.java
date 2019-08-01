package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;


public class ItemSearchListener  implements MessageListener {
    @Autowired
    private ItemSearchService searchService;
    @Override
    public void onMessage(Message message) {
        try {
            String text = ((TextMessage) message).getText();
            System.out.println("监听到消息"+text);
            List<TbItem> itemList = JSON.parseArray(text, TbItem.class);
            searchService.importList(itemList);
            System.out.println("导入到solr索引库");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
