package com.ring.welkin.common.hibernate.autoddl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = HibernateProperties.PREFIX)
public class HibernateProperties extends Properties {
    private static final long serialVersionUID = -5760550205150368756L;
    public static final String PREFIX = "hibernate";

    public Properties renderProperties() {
        Properties properties = new Properties();
        if (!isEmpty()) {
            this.forEach((k, v) -> {
                properties.put(PREFIX.concat(".").concat(k.toString()), v);
            });
        }
        return properties;
    }
}
