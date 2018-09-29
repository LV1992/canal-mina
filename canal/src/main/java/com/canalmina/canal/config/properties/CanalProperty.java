package com.canalmina.canal.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yihang.lv 2018/9/29、9:56
 */
@Data
@Component
@ConfigurationProperties(prefix = "canal")
public class CanalProperty {
    private String host;
    private int port;
    private String destination;
    private String username;
    private String password;
}
