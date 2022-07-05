package com.wk.rabbitmq.consumer.delay;

import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import com.wk.rabbitmq.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description : 延迟消费者
 * @Author : wukong
 */
@Component
@RabbitListener(queues = "${mq.channel.queue.delayQueue}",
        containerFactory = "firstFactory")
@Slf4j
public class Delay2Consumer {
    @RabbitHandler
    public void process(User user, Message message, Channel channel) throws IOException {
        try {
            log.info("delay2-收到消息:{}", JSONUtil.toJsonStr(user));
            if (user == null) {
                log.warn("收到延迟消息为空");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        } catch (Exception e) {
        }
        //异常重试
        //throw new BusinessException(0, "重试");
    }
}
