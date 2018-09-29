package com.canalmina.mina.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yihang.lv 2018/9/29、16:21
 */
@Data
@Component
@ConfigurationProperties(prefix = "mina")
public class MinaProperties {
    private int port;
}
