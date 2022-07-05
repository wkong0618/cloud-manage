package com.wk.rabbitmq.consumer.topic;

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
 * @Description : topic信息消费
 * @Author : wukong
 */
@Component
@RabbitListener(queues = "${mq.channel.queue.topicQueue}",
        containerFactory = "firstFactory")
@Slf4j
public class TopicCustomer {
    @RabbitHandler
    public void process(User user, Message message, Channel channel) throws IOException {
        log.info("topic-收到广播消息:{}", JSONUtil.toJsonStr(user));
        /**
         * 异常,重试五次后会发送到死信队列.
         * 如果ack模式是手动ack，那么需要调用channe.nack或者channe.reject方法，同时设置requeue=false才会将异常消息发送到死信队列中
         *
         */
        //int i = 10/0;
        try {
            //设置队列过期时间为10s,模拟执行业务20s,则消息过期会自动到死信队列
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if (user == null) {
                log.warn("收到topic消息为空");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        } catch (Exception e) {
            //异常拒绝(如果绑定了死信队列则会发送到死信队列)
            //channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
        //异常重试
        //throw new BusinessException(0, "重试");
    }
}
