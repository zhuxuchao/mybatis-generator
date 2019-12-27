package com.zhuxc.mybatisplus.generator.config;

import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.zhuxc.mybatisplus.generator.config.builder.ConfigBuilder;

import java.io.File;

/**
 * @author zhuxuchao
 * @date 2019-12-27 0:25
 */
public interface IFileCreate {
    boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath);

    default void checkDir(String filePath) {
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist) {
            file.getParentFile().mkdir();
        }

    }
}
