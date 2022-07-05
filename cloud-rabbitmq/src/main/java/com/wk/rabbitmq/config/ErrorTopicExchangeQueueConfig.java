package com.wk.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description : 错误交换机和队列
 * @Author : wukong
 */
@Configuration
public class ErrorTopicExchangeQueueConfig {
    //----------------定义错误交换器和队列----------------

    public static final String ERROR_TOPIC_EXCHANGE = "error-topic-exchange";
    public static final String ERROR_QUEUE = "error-queue";
    public static final String ERROR_ROUTING_KEY = "error-routing-key";

    /**
     * 创建异常交换机
     * @return
     */
    @Bean
    public TopicExchange errorTopicExchange(){
        return new TopicExchange(ERROR_TOPIC_EXCHANGE,true,false);
    }

    /**
     * 创建异常队列
     * @return
     */
    @Bean
    public Queue errorQueue(){
        return new Queue(ERROR_QUEUE,true);
    }

    /**
     * 队列与交换机进行绑定
     * @param errorQueue
     * @param errorTopicExchange
     * @return
     */
    @Bean
    public Binding bindingErrorQueueAndExchange(Queue errorQueue, TopicExchange errorTopicExchange){
        return BindingBuilder.bind(errorQueue).to(errorTopicExchange).with(ERROR_ROUTING_KEY);
    }
}
