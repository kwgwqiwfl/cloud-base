package com.ring.welkin.common.quartz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = { "com.ring.welkin" })
@PropertySource(value = { "classpath:/application-test.properties" })
public class QuartzTestStarter {
    public static void main(String[] args) {
        log.info("JAVA CLASS PATH = " + System.getProperty("java.class.path"));
        SpringApplication.run(QuartzTestStarter.class, args);
    }
}
