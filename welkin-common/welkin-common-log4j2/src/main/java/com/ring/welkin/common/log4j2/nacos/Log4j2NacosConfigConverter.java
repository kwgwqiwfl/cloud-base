package com.ring.welkin.common.log4j2.nacos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.nacos.api.config.convert.NacosConfigConverter;

import lombok.extern.slf4j.Slf4j;

/**
 * nacos 配置转化器，将nacos配置文件内容转化为输入流用于构造{@code ConfigurationSource}
 */
@Slf4j
public class Log4j2NacosConfigConverter implements NacosConfigConverter<InputStream> {
    @Override
    public boolean canConvert(Class<InputStream> targetType) {
        return true;
    }

    @Override
    public InputStream convert(String config) {
        if (StringUtils.isEmpty(config)) {
            log.info("The received config content is empty!");
            return null;
        }
        log.info("==============log4j config info====================");
        log.info(config);
        log.info("===================================================");
        return new ByteArrayInputStream(config.getBytes(Charset.defaultCharset()));
    }
}