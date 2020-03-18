package com.zhuxc.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.zhuxc.mybatisplus.generator.config.DefaultDataSourceConfig;
import com.zhuxc.mybatisplus.generator.config.DefaultGlobalConfig;
import com.zhuxc.mybatisplus.generator.config.DefaultPackageConfig;
import com.zhuxc.mybatisplus.generator.config.DefaultStrategyConfig;
import com.zhuxc.mybatisplus.generator.config.builder.ConfigBuilder;
import com.zhuxc.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeneratorApplicationTests {

    @Autowired
    private DefaultGlobalConfig globalConfig;
    @Autowired
    private DefaultPackageConfig packageConfig;
    @Autowired
    private DefaultStrategyConfig strategyConfig;
    @Autowired
    private DefaultDataSourceConfig dataSourceConfig;

    @Test
    public void generate() {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        TemplateConfig templateConfig = new TemplateConfig();
//        InjectionConfig injectionConfig = new InjectionConfig() {
//            @Override
//            public void initMap() {
//
//            }
//        };
//        List<FileOutConfig> fileOutConfigList = new ArrayList<>();
//        fileOutConfigList.add(new FileOutConfig(templateConfig.getXml() + ".ftl") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return globalConfig.getModuleResourceSrcPath(Module.mapper) + File.separator + packageConfig.getMapper() + File.separator
//                        + tableInfo.getMapperName() + StringPool.DOT_XML;
//            }
//        });
//        injectionConfig.setFileOutConfigList(fileOutConfigList);
//        mpg.setCfg(injectionConfig);
        mpg.setConfig(new ConfigBuilder(packageConfig, dataSourceConfig, strategyConfig, templateConfig, globalConfig));
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
