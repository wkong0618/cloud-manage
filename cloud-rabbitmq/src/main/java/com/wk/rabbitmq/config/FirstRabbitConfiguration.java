package com.wk.rabbitmq.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.ImmediateRequeueMessageRecoverer;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecovererWithConfirms;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Description : 第一个rabbitMq源
 * @Author : wukong
 */
@Configuration
@ConfigurationProperties("spring.rabbitmq.first")
public class FirstRabbitConfiguration extends AbstractRabbitConfiguration{

    @Autowired
    private RetryRuleConfig ruleConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Bean(name = "firstConnectionFactory")
    @Primary
    public ConnectionFactory secondConnectionFactory() {
        return super.connectionFactory();
    }

    /**
     * 在消费端转换JSON消息
     * 监听类都要加上containerFactory属性
     * @param connectionFactory
     * @return
     */
    @Bean("firstFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(@Qualifier("firstConnectionFactory")  ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        /**
         * MANUAL:手动确认会在失败重试后队列依然会存在未确认消息
         * AUTO: 自动确认会在失败重试以后自动确认消息(队列中该消息被丢弃)
         */
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setAutoStartup(true);
        factory.setAdviceChain(
                RetryInterceptorBuilder
                        .stateless()
                        //拒绝并且不会将消息重新发回队列
                        .recoverer(messageRecoverer())
                        .retryOperations(ruleConfig.retryTemplate())
                        .build()
        );
        return factory;
    }

    @Bean(value = "firstRabbitAdmin")
    public RabbitAdmin firstRabbitAdmin(@Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    @Bean(name = "firstRabbitTemplate")
    @Primary
    public RabbitTemplate firstRabbitTemplate(@Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    /**
     * 消息转换器
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(om);
    }

    @Override
    public MessageRecoverer messageRecoverer() {
        //重试失败的消息会立马回到原队列中继续消费重试知道正常消费
        //return new ImmediateRequeueMessageRecoverer();

        /**
         * 重新发布消息(发布到其它交换器和队列)
         * - 对于重试之后仍然异常的消息，
         *   可以采用 RepublishMessageRecoverer，
         *   将消息发送到其他的队列中，再专门针对新的队列进行处理
         * - 还可以采用死信队列处理重试失败的消息(常用的方式)
         */
        /*return new RepublishMessageRecoverer(rabbitTemplate,
                ErrorTopicExchangeQueueConfig.ERROR_TOPIC_EXCHANGE,
                ErrorTopicExchangeQueueConfig.ERROR_ROUTING_KEY);*/

        //带有确认类型的重新发布消息(发布到其它交换器和队列)
        /*return new RepublishMessageRecovererWithConfirms(rabbitTemplate,
                ErrorTopicExchangeQueueConfig.ERROR_TOPIC_EXCHANGE,
                ErrorTopicExchangeQueueConfig.ERROR_ROUTING_KEY, CachingConnectionFactory.ConfirmType.CORRELATED);*/
        return super.messageRecoverer();
    }
}
