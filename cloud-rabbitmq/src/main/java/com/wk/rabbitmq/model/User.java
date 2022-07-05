package com.wk.rabbitmq.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 用户信息
 * @Author : wukong
 */
@Data
public class User implements Serializable {
    private String id;
    private String name;
    private int age;

    private long delayMilliseconds;
}
