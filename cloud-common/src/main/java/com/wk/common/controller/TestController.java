package com.wk.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description : test
 * @Author : wukong
 * @Date : 2022/7/3 11:43 下午
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "Hello world";
    }
}
