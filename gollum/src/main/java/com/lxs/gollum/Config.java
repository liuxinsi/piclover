package com.lxs.gollum;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 加载配置。
 *
 * @author liuxinsi
 * @date 2018/9/11 17:51
 */
@Configuration
@PropertySource(value = "classpath:application.properties", encoding = "utf-8")
public class Config {
    private static final String DIR_PREFIX = "file:///";
    @Value("${spring.resources.static-locations}")
    @Setter
    private String saveDir;

    public String getSaveDir() {
        if (saveDir.startsWith(DIR_PREFIX)) {
            saveDir = saveDir.replace(DIR_PREFIX, "");
        }
        return saveDir;
    }
}
