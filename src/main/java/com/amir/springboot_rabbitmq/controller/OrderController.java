package com.amir.springboot_rabbitmq.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/place/{orderId}")
    public String placeOrder(@PathVariable("orderId") String orderId) {
        rabbitTemplate.convertAndSend("order_exchange","order_routing",orderId);
        return "下单成功";
    }
}
