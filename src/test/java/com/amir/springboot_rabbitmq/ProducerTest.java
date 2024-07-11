package com.amir.springboot_rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

@SpringBootTest
public class ProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessage(){
        rabbitTemplate.convertAndSend("boot_topic_exchange","message",
                "topic_exchange_message".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void sendMessageNotConfirm(){
        rabbitTemplate.convertAndSend("boot_topic_exchange1","message",
                "topic_exchange_message".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testConfirm(){
        // 添加回调，监听消息是否成功投递到交换机
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 配置信息
             * @param ack 交换机是否成功收到了消息
             * @param message 错误信息
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String message) {
                if (ack){
                    System.out.println("交换机成功收到了消息");
                }else {
                    System.out.println("交换机没有收到消息，error: " + message);
                }
            }
        });
        rabbitTemplate.convertAndSend("boot_topic_exchange","my",
                "topic_exchange_message".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testReturn(){
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                System.out.println("交换机没有收到消息，error: " + returnedMessage.getMessage());
            }
        });
        rabbitTemplate.convertAndSend("boot_topic_exchange","message",
                "topic_exchange_message".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void sendTtlMessage(){
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration("5000"); // 5s

        Message message = new Message("topic_exchange_message".getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend("topic_exchange_message", "boot_topic_exchange",message);
    }

    @Test
    public void sendPriorityMessage(){
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration("5000"); // 5s
        messageProperties.setPriority(10); // 设置消息的优先级

        Message message = new Message("topic_exchange_message".getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend("topic_exchange_message", "boot_topic_exchange",message);
    }
}
