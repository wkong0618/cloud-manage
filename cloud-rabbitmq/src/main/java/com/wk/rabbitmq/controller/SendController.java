package com.wk.rabbitmq.controller;

import cn.hutool.json.JSONUtil;
import com.wk.rabbitmq.config.RabbitMqProperties;
import com.wk.rabbitmq.enums.RouterKeyEnum;
import com.wk.rabbitmq.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Description : mq message send
 * @Author : wukong
 */
@RestController
@Slf4j
public class SendController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @PostMapping("/sendDelayMessage")
    public String sendDelayMessage(@RequestBody  User user){
        CorrelationData correlation = new CorrelationData("设置：" + UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(rabbitMqProperties.getExchange().getDelay(),
                RouterKeyEnum.DELAY_ROUTER_KEY.getVal(),
                user, message -> {
                    message.getMessageProperties().setHeader("x-delay", user.getDelayMilliseconds());
                    return message;
                }, correlation);
        log.info("发送延迟消息：{}", JSONUtil.toJsonStr(user));
        return "已发送";
    }

    @PostMapping("/sendFanoutMessage")
    public String sendFanoutMessage(@RequestBody  User user){
        CorrelationData correlation = new CorrelationData("设置：" + UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(rabbitMqProperties.getExchange().getFanout(),
                RouterKeyEnum.FANOUT_ROUTER_KEY.getVal(),
                user, correlation);
        log.info("发送广播消息：{}", JSONUtil.toJsonStr(user));
        return "已发送广播消息";
    }

    @PostMapping("/sendDirectMessage")
    public String sendDirectMessage(@RequestBody  User user){
        CorrelationData correlation = new CorrelationData("设置：" + UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(rabbitMqProperties.getExchange().getDirect(),
                RouterKeyEnum.DIRECT_ROUTER_KEY.getVal(),
                user, correlation);
        log.info("发送直连消息：{}", JSONUtil.toJsonStr(user));
        return "已发送直连消息";
    }

    @PostMapping("/sendTopicMessage")
    public String sendTopicMessage(@RequestBody  User user){
        CorrelationData correlation = new CorrelationData("设置：" + UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(rabbitMqProperties.getExchange().getTopic(),
                "topic.123",
                user, correlation);
        log.info("发送topic消息：{}", JSONUtil.toJsonStr(user));
        return "已发送topic消息";
    }

    @PostMapping("/sendCommonDelayMessage")
    public String sendCommonDelayMessage(@RequestBody  User user){
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            //设置消息持久化
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            messageProperties.setExpiration(user.getDelayMilliseconds() + "");
            return message;
        };

        rabbitTemplate.convertAndSend(rabbitMqProperties.getExchange().getTopic(), "topic.123",
                user,  messagePostProcessor);
        log.info("发送正常延迟(消息ttl)消息：{}", JSONUtil.toJsonStr(user));
        return "已发送正常延迟(消息ttl)消息";
    }



}
