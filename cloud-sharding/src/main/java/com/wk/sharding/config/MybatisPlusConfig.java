package com.wk.sharding.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description : MybatisPlusConfig
 * @Author : wukong
 * @Date : 2022/7/5 11:55 下午
 */
@Configuration
public class MybatisPlusConfig {
    /**
     * 老版本，目前已失效
     * @return
     */
    /**@Bean
    public PaginationInterceptor paginationInterceptor() {
    PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    return paginationInterceptor;
    }*/


    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
