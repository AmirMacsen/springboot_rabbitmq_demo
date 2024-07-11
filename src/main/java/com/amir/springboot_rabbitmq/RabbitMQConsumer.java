package com.amir.springboot_rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLOutput;

//@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "boot_queue")
    public void receiveMessage(Message message, Channel channel) throws IOException {
        // 消息投递序号，表示消息是第几次被投递的，每次投递+1
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            int a = 1/0;
            // 消息确认 1. 消息投递序号 2. 是否可以签收多个消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 拒签消息 1.消息投递序号 2.是否可以签收多个消息 3.是否重回队列
            System.out.println("消息消费失败");
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
