package com.wk.sharding.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wk.sharding.entity.User;
import com.wk.sharding.mapper.UserMapper;
import com.wk.sharding.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shardingsphere.api.hint.HintManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        HintManager.clear();
        HintManager hintManager = HintManager.getInstance();
        Map<String, Object> shardingMap = new HashMap<>();
        shardingMap.put("startCreateTime", "2022-07-02");
        shardingMap.put("endCreateTime", "2022-07-06");
        hintManager.addTableShardingValue("user", JSONUtil.toJsonStr(shardingMap));
        User user =baseMapper.selectOne(queryWrapper);
        return user;
    }
}
