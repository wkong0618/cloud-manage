package com.wk.rabbitmq.config;

import lombok.Data;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;

/**
 * @Description : rabbit基础配置
 * @Author : wukong
 */
@Data
public abstract class AbstractRabbitConfiguration {
    protected String host;
    protected int port;
    protected String username;
    protected String password;
    protected String virtualHost;
    protected String  publisherConfirmType;
    protected boolean  publisherReturns;


    protected ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(publisherReturns);
        return connectionFactory;
    }

    public MessageRecoverer messageRecoverer(){
        //默认
        return new RejectAndDontRequeueRecoverer();
    }

}
