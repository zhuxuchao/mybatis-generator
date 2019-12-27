package com.zhuxc.mybatisplus.generator.config;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.zhuxc.mybatisplus.generator.consts.Constants;
import com.zhuxc.mybatisplus.generator.consts.Module;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuxuchao
 * @date 2019-12-26
 */
@Configuration
@ConfigurationProperties(prefix = "generator.global.config")
public class DefaultGlobalConfig extends GlobalConfig {
    private boolean multiModule = false;
    private String modulePrefix = "";
    private Map<Module, String> moduleNames;

    public DefaultGlobalConfig() {
        super();
        setOutputDir(System.getProperty("user.dir") + File.separator);
        setDateType(DateType.ONLY_DATE);
        moduleNames = new HashMap<>();
        moduleNames.put(Module.mapper, modulePrefix + "mapper");
        moduleNames.put(Module.entity, modulePrefix + "entity");
        moduleNames.put(Module.service, modulePrefix + "service");
        moduleNames.put(Module.service_impl, modulePrefix + "service-impl");
        moduleNames.put(Module.controller, modulePrefix + "web");
    }

    public boolean isMultiModule() {
        return multiModule;
    }

    public void setMultiModule(boolean multiModule) {
        this.multiModule = multiModule;
    }

    public String getModulePrefix() {
        return modulePrefix;
    }

    public void setModulePrefix(String modulePrefix) {
        this.modulePrefix = modulePrefix;
    }

    public Map<Module, String> getModuleNames() {
        return moduleNames;
    }

    public void setModuleNames(Map<Module, String> moduleNames) {
        this.moduleNames = moduleNames;
    }

    public String getModuleJavaSrcPath(Module module) {
        String moduleName = moduleNames.get(module);
        return this.getOutputDir() + moduleName + File.separator + Constants.JAVA_SRC_ROOT;
    }
    public String getModuleResourceSrcPath(Module module) {
        String moduleName = moduleNames.get(module);
        return this.getOutputDir() + moduleName + File.separator + Constants.RESOURCE_SRC_ROOT;
    }
}
