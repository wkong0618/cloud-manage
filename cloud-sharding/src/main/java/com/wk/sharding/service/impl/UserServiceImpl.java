package com.wk.sharding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wk.sharding.entity.User;
import com.wk.sharding.mapper.UserMapper;
import com.wk.sharding.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wk
 * @since 2022-07-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User qryById(Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id)
        .eq(User::getIsactive, true);
        User user =baseMapper.selectOne(queryWrapper);
        return user;
    }
}
