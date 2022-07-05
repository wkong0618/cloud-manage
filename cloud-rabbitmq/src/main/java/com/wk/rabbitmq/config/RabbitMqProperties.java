package com.wk.rabbitmq.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description : rabbit配置
 * @Author : wukong
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "mq.channel")
public class RabbitMqProperties {
    ExchangeProperties exchange;
    QueueProperties queue;
    RetryEntity retry;

    /**
     * 交换机定义
     */
    @Getter
    @Setter
    public static class ExchangeProperties {
        //延迟
        String delay;
        //直连
        String direct;
        //广播
        String fanout;
        //topic
        String topic;
    }

    /**
     * 队列定义
     */
    @Getter
    @Setter
    public static class QueueProperties {
        String delayQueue;
        String directQueue;
        String fanoutQueue;
        String fanoutQueue2;
        String topicQueue;
    }

    @Getter
    @Setter
    public static class RetryEntity {
        //是否开启重试
        boolean openRetry;
        //重试初始间隔时间
        Long initialInterval;
        //重试最大间隔时间
        Long maxInterval;
        //间隔时间乘子，间隔时间*乘子=下一次的间隔时间，最大不能超过设置的最大间隔时间
        Double multiplier;
        //最大重试次数
        Integer maxAttempts;

    }
}
