package com.amir.springboot_rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    // 订单交换机和队列
    private final String ORDER_EXCHANGE = "order_exchange";
    private final String ORDER_QUEUE = "order_queue";
    // 过期订单交换机和队列
    private final String EXPIRE_EXCHANGE = "expire_exchange";
    private final String EXPIRE_QUEUE = "expire_queue";


    // 过期订单交换机
    @Bean(EXPIRE_EXCHANGE)
    public Exchange deadExchange(){
        return ExchangeBuilder
                .topicExchange(EXPIRE_EXCHANGE)
                .durable(true)
                .build();
    }
    // 过期订单队列
    @Bean(EXPIRE_QUEUE)
    public Queue deadQueue(){
        return QueueBuilder
                .durable(EXPIRE_QUEUE)
                .build();
    }
    // 将过期订单队列绑定到交换机
    @Bean
    public Binding bindDeadQueue(@Qualifier(EXPIRE_EXCHANGE) Exchange exchange,@Qualifier(EXPIRE_QUEUE) Queue queue){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("expire_routing")
                .noargs();
    }


    // 订单交换机
    @Bean(ORDER_EXCHANGE)
    public Exchange normalExchange(){
        return ExchangeBuilder
                .topicExchange(ORDER_EXCHANGE)
                .durable(true)
                .build();
    }
    // 订单队列
    @Bean(ORDER_QUEUE)
    public Queue normalQueue(){
        return QueueBuilder
                .durable(ORDER_QUEUE)
                .ttl(10000) // 存活时间为10s,模拟30min
                .deadLetterExchange(EXPIRE_EXCHANGE) // 绑定死信交换机
                .deadLetterRoutingKey("expire_routing") // 死信交换机的路由关键字
                .build();
    }
    // 将订单队列绑定到交换机
    @Bean
    public Binding bindNormalQueue(@Qualifier(ORDER_EXCHANGE) Exchange exchange,@Qualifier(ORDER_QUEUE) Queue queue){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("order_routing")
                .noargs();
    }
}
