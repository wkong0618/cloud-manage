package com.wk.sharding.service;

import com.wk.sharding.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wk
 * @since 2022-07-05
 */
public interface IUserService extends IService<User> {

    User qryById(Long id);
}
