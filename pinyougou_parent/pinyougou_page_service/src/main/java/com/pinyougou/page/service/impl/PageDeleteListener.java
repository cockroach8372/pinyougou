package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;

@Component
public class PageDeleteListener  implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;
    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage= (ObjectMessage) message;
            Long [] goodids= (Long[]) objectMessage.getObject();
            System.out.println("收到消息。。"+goodids);
            itemPageService.deleItemHteml(goodids);
            System.out.println("删除结果："+goodids);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
