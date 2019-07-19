package cn.itcast.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Destination;

@Component
public class QueueProducer {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination queueTextDestination;

    public void sendMessage(final String text){
          jmsTemplate.send(queueTextDestination, new MessageCreator() {
              public Message createMessage(Session session) throws JMSException {
                  return session.createTextMessage(text);
              }
          });

    }
}
