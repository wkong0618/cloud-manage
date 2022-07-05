package com.wk.rabbitmq.config;

import com.wk.rabbitmq.enums.RouterKeyEnum;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : exchange queue config
 * @Author : wukong
 */
@Configuration
public class ExchangeQueueConfig {


    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    //--------------定义交换机--------------
    /**
     * 直连交换机
     */
    @Bean("directExchange")
    public DirectExchange directExchange(){
        return new DirectExchange(rabbitMqProperties.getExchange().getDirect(), true, false);
    }

    /**
     * 延迟消息交换机
     * @return
     */
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(rabbitMqProperties.getExchange().getDelay(),
                "x-delayed-message", true, false, args);
    }

    /**
     * 广播交换机
     * @return
     */
    @Bean("fanoutExchange")
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(rabbitMqProperties.getExchange().getFanout(), true, false);
    }

    /**
     * topic交换机
     * @return
     */
    @Bean("topicExchange")
    public TopicExchange topicExchange(){
        return new TopicExchange(rabbitMqProperties.getExchange().getTopic(), true, false);
    }

    //--------------定义队列--------------

    /**
     * 直连队列
     * @return
     */
    @Bean("directQueue")
    public Queue directQueue(){
        return new Queue(rabbitMqProperties.getQueue().getDirectQueue(), true);
    }

    /**
     * 延迟队列
     * @return
     */
    @Bean
    public Queue delayQueue() {
        return new Queue(rabbitMqProperties.getQueue().getDelayQueue(), true);
    }

    /**
     * 广播队列
     * @return
     */
    @Bean
    public Queue fanoutQueue() {
        return new Queue(rabbitMqProperties.getQueue().getFanoutQueue(), true);
    }

    /**
     * 广播队列2
     * @return
     */
    @Bean
    public Queue fanoutQueue2() {
        return new Queue(rabbitMqProperties.getQueue().getFanoutQueue2(), true);
    }

    /**
     * TOPIC队列
     * @return
     */
    @Bean
    public Queue topicQueue() {
        Map<String,Object> params = new HashMap<>(8);
        //设置队列的过期时间 10s
        params.put("x-message-ttl",1000000);
        //声明当前队列绑定的死信交换机
        params.put("x-dead-letter-exchange",DL_TOPIC_EXCHANGE);
        //声明当前队列的死信路由键
        params.put("x-dead-letter-routing-key",DL_ROUTING_KEY);
        return QueueBuilder.durable(rabbitMqProperties.getQueue().getTopicQueue())
                .withArguments(params).build();
    }


    //---------------定义绑定关系---------------

    @Bean
    public Binding bindingDelay() {
        return BindingBuilder.bind(delayQueue())
                .to(delayExchange())
                .with(RouterKeyEnum.DELAY_ROUTER_KEY.getVal())
                .noargs();
    }

    @Bean
    public Binding bindDirect(){
        return BindingBuilder.bind(directQueue()).to(directExchange())
                .with(RouterKeyEnum.DIRECT_ROUTER_KEY.getVal());
    }

    @Bean
    public Binding bindFanout(){
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding bindFanout2(){
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    @Bean
    public Binding bindTopic(){
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with(RouterKeyEnum.TOPIC_ROUTER_KEY.getVal());
    }


    //----------------定义错误交换器和队列----------------

    private static final String ERROR_TOPIC_EXCHANGE = "error-topic-exchange";
    private static final String ERROR_QUEUE = "error-queue";
    private static final String ERROR_ROUTING_KEY = "error-routing-key";

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

    //----------------死信队列-----------------

    private static final String DL_TOPIC_EXCHANGE = "dl-topic-exchange";
    private static final String DL_QUEUE = "dl-queue";
    private static final String DL_ROUTING_KEY = "dl-routing-key";


    /**
     * 创建交换机
     * @return
     */
    @Bean
    public TopicExchange dlTopicExchange(){
        return new TopicExchange(DL_TOPIC_EXCHANGE,true,false);
    }

    /**
     * 创建队列
     * @return
     */
    @Bean
    public Queue dlQueue(){
        return new Queue(DL_QUEUE,true);
    }

    /**
     * 队列与交换机进行绑定
     * @param dlQueue
     * @param dlTopicExchange
     * @return
     */
    @Bean
    public Binding bindingDlQueueAndExchange(Queue dlQueue, TopicExchange dlTopicExchange){
        return BindingBuilder.bind(dlQueue).to(dlTopicExchange).with(DL_ROUTING_KEY);
    }



}
