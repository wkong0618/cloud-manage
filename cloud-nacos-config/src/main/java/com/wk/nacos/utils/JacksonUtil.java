package com.wk.nacos.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @Description : jackson工具类
 * @Author : wukong
 */
@Slf4j
public class JacksonUtil {
    private static ObjectMapper MAPPER;
    private static ObjectMapper YAML_MAPPER;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDefaultPropertyInclusion( JsonInclude.Include.NON_NULL);
        objectMapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.configure( SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.setDateFormat( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") );
        objectMapper.setTimeZone( TimeZone.getTimeZone( "GMT+8" ) );
        MAPPER = objectMapper;

        YAML_MAPPER = new ObjectMapper(new YAMLFactory());
    }

    /**
     * yaml字符串转json 字符串
     * @param yamlStr
     * @return
     */
    public static String yaml2JsonStr(String yamlStr) {
        try {
            Object obj = YAML_MAPPER.readValue(yamlStr, Object.class);
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.info("yamlStr->jsonStr 异常, yamlStr:{}", yamlStr, e);
        }
        return null;
    }

}
