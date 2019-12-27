package com.zhuxc.mybatisplus.generator.config;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhuxuchao
 * @date 2019-12-26
 */
@Configuration
@ConfigurationProperties(prefix = "generator.datasource.config")
public class DefaultDataSourceConfig extends DataSourceConfig {
}
