package com.ring.welkin.common.config.threadpool;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = ThreadPoolProperties.PREFIX)
public class ThreadPoolProperties {
    public static final String PREFIX = "thread.pool";


    private boolean enabled = false;

    private int corePoolSize = 10;
    private int maxPoolSize = 30;
    private int keepAliveSeconds = 600;
    private int queueCapacity = 99999;
    private boolean allowCoreThreadTimeOut = false;
    private String threadNamePrefix = "thread-pool-";

}
