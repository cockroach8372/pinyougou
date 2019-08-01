package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;

@Component
public class PageListener  implements MessageListener {
   @Autowired
   private ItemPageService itemPageService;
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage= (TextMessage) message;
        try {
            String text = textMessage.getText();
            System.out.println("接受到的消息为"+text);
            boolean b = itemPageService.genItemHtml(Long.parseLong(text));
            System.out.println("页面生成："+b);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
