package com.wk.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description : 消息确认 - 是否发送到交换机
 * 发送方确认机制->确认生产者是否成功发送消息到交换机
 * @Author : wukong
 */
@Slf4j
@Component
public class InfoConfirm implements RabbitTemplate.ConfirmCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 需要给ConfirmCallback赋值 不然不会走回调方法，默认是null
     */
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 监听消息是否发送到交换机
     * @param correlationData 当前消息的唯一性,发送消息的时候填充
     * @param ack 消息投递到broker 的状态，true表示成功
     * @param cause 失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            log.info("消息发送到交换机, id:{}", correlationData.getId());
            if(null == correlationData.getReturned()){
                log.info("消息被确认");
            } else {
                log.info("message = {}",new String(correlationData.getReturned().getMessage().getBody()));
            }
        } else {
            log.info("消息发送到交换机失败, id:{}, cause:{}",
                    correlationData.getId(), cause);
            if(null == correlationData.getReturned()) {
                log.info("消息异常");
            } else {
                byte[] body = correlationData.getReturned().getMessage().getBody();
                log.info("message = {}",new String(body));
            }
        }
    }
}
