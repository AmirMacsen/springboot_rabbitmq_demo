package com.amir.springboot_rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    // 监听队列
    @RabbitListener(queues = "expire_queue")
    public void listenMessage(String orderId){
        System.out.println("查询"+orderId+"号订单的状态，如果已支付则无需处理，如果未支付则需要回退库存");
    }
}
