package com.wk.nacos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @Description :
 * @Author : wukong
 */
@Data
@ConfigurationProperties(prefix = "configdata.user")
public class UserConfig {
    private String name;
    private Integer age;
    private Map<String, Object> map;
    private List<User> users;

    @Data
    public static class User {
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
