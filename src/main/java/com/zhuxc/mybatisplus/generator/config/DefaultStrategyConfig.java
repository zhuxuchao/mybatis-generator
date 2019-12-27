package com.zhuxc.mybatisplus.generator.config;

import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhuxuchao
 * @date 2019-12-26
 */
@Configuration
@ConfigurationProperties(prefix = "generator.strategy.config")
public class DefaultStrategyConfig extends StrategyConfig {

    public DefaultStrategyConfig() {
        this.setNaming(NamingStrategy.underline_to_camel);
        this.setColumnNaming(NamingStrategy.underline_to_camel);
        this.setEntityLombokModel(true);
        this.setEntityBuilderModel(true);
        this.setRestControllerStyle(true);
        this.setSuperEntityColumns("id");
    }
}
