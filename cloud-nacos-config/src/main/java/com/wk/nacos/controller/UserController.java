package com.wk.nacos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wk.nacos.config.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description :
 * @Author : wukong
 */
@RefreshScope
@RestController
public class UserController {
    @Autowired
    private UserConfig userConfig;

    /**
     * 默认99
     */
    @Value("${province.city.area.name:99}")
    private String name;

    @GetMapping("/test")
    public String get() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(userConfig);
    }

    @GetMapping("/testName")
    public String testName() {
        return name;
    }


}
