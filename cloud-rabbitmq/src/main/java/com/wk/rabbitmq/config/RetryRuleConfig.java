package com.wk.rabbitmq.config;

import com.wk.rabbitmq.exceptions.BusinessException;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 重试规则配置
 * 重试并不是 RabbitMQ 重新发送了消息到了队列，仅仅是消费者内部进行了重试，
 * 换句话说就是重试跟mq没有任何关系。
 * 若一旦捕获了异常，在自动 ack 模式下，就相当于消息正确处理了，消息直接被确认掉了，不会触发重试
 *
 * @Author : wukong
 */
@Configuration
public class RetryRuleConfig {
    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    /**
     * 重试对象
     *
     * @return
     */
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOffPolicy());
        retryTemplate.setRetryPolicy(retryPolicy());
        return retryTemplate;
    }

    /**
     * 重试规则
     *
     * @return
     */
    @Bean
    public SimpleRetryPolicy retryPolicy() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>(8);
        //设置重试异常和是否重试
        boolean isRetry = rabbitMqProperties.getRetry().isOpenRetry();
        retryableExceptions.put(AmqpException.class, isRetry);
        retryableExceptions.put(BusinessException.class, isRetry);
        //TODO 异常重试
        //重试次数 - 默认5次
        Integer maxAttempts = rabbitMqProperties.getRetry().getMaxAttempts();
        maxAttempts = maxAttempts == null ? 5 : maxAttempts;
        return new SimpleRetryPolicy(maxAttempts, retryableExceptions);
    }

    /**
     * 重试间隔时间
     *
     * @return
     */
    @Bean
    public ExponentialBackOffPolicy backOffPolicy() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        //重试初始间隔时间 - 默认2秒
        Long initialInterval = rabbitMqProperties.getRetry().getInitialInterval();
        initialInterval = initialInterval == null || initialInterval == 0 ? 2000L : initialInterval;
        backOffPolicy.setInitialInterval(initialInterval);
        //重试最大间隔时间 - 默认最大10秒
        Long maxInterval = rabbitMqProperties.getRetry().getMaxInterval();
        maxInterval = maxInterval == null || maxInterval == 0 ? 10000L : maxInterval;
        backOffPolicy.setMaxInterval(maxInterval);
        //间隔时间乘子，间隔时间*乘子=下一次的间隔时间，最大不能超过设置的最大间隔时间
        Double multiplier = rabbitMqProperties.getRetry().getMultiplier();
        multiplier = multiplier == null ? 0d : multiplier;
        backOffPolicy.setMultiplier(multiplier);
        return backOffPolicy;
    }
}
