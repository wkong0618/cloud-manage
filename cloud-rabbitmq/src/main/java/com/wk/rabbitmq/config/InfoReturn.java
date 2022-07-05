package com.wk.rabbitmq.config;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description : 交换机消息没成功分发到队列
 * -- rabbitTemplate 需要设置mandatory属性为true
 * @Author : wukong
 */
@Slf4j
@Component
public class InfoReturn implements RabbitTemplate.ReturnsCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 需要给ConfirmCallback赋值 不然不会走回调方法，默认是null
     */
    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机发送消息到队列失败执行此方法
     * @param returned
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("交换机发送消息到队列失败, info:{}", JSONUtil.toJsonStr(returned));
    }
}
