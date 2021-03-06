package com.wk.sharding.controller;


import com.wk.sharding.entity.User;
import com.wk.sharding.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wk
 * @since 2022-07-05
 */
@RestController
@RequestMapping("/sharding/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/qryById")
    public User qryOneUser(@RequestParam("id") Long id){
        return userService.qryById(id);
    }

    @PostMapping("/addUser")
    public Boolean addUser(){
        User user = new User();
        user.setAge(1);
        user.setName("wk");
        return userService.save(user);
    }
}
