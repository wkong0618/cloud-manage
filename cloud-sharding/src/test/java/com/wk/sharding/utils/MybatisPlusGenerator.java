package com.wk.sharding.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;

/**
 * @Description : mybatis生成
 * @Author : wukong
 */
public class MybatisPlusGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf8", "root", "root")
                .globalConfig(builder -> {
                    builder.author("wk") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(System.getProperty("user.dir") + "/cloud-sharding" +"/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.wk") // 设置父包名
                            .moduleName("sharding") // 设置父包模块名
                            // .service()  // 设置自定义service路径,不设置就是默认路径
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") +"/src/main/resources/mapper/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_")
                            .controllerBuilder()
                            .enableRestStyle()
                            // 设置自动填充的时间字段
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .addTableFills(
                            new Column("create_time", FieldFill.INSERT),
                                    new Column("update_time", FieldFill.INSERT_UPDATE))
                    ; // 设置过滤表前缀

                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
