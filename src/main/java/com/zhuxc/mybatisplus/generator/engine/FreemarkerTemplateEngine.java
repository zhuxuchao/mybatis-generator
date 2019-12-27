package com.zhuxc.mybatisplus.generator.engine;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.zhuxc.mybatisplus.generator.config.builder.ConfigBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * @author zhuxuchao
 * @date 2019-12-27 0:37
 */
public class FreemarkerTemplateEngine extends AbstractTemplateEngine {
    private Configuration configuration;

    public FreemarkerTemplateEngine() {
    }

    @Override
    public FreemarkerTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        this.configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        this.configuration.setDefaultEncoding(ConstVal.UTF8);
        this.configuration.setClassForTemplateLoading(com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine.class, "/");
        return this;
    }

    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        Template template = this.configuration.getTemplate(templatePath);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        Throwable var6 = null;

        try {
            template.process(objectMap, new OutputStreamWriter(fileOutputStream, ConstVal.UTF8));
        } catch (Throwable var15) {
            var6 = var15;
            throw var15;
        } finally {
            if (fileOutputStream != null) {
                if (var6 != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable var14) {
                        var6.addSuppressed(var14);
                    }
                } else {
                    fileOutputStream.close();
                }
            }

        }

        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }

    @Override
    public String templateFilePath(String filePath) {
        return filePath + ".ftl";
    }
}
