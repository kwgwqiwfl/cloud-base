package com.ring.welkin.common.feign;

import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Configuration(proxyBeanMethods = true)
public class FeignClientConfiguration {

    @Value("${feign.client.config.default.connect-timeout:60000}")
    private int connectTimeout;

    @Value("${feign.client.config.default.read-timeout:60000}")
    private int readTimeout;

    private ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;

    @Bean
    Request.Options options() {
        return new Request.Options(connectTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, true);
    }

    @Bean
    Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    Decoder feignFormDecoder() {
        return new SpringDecoder(messageConverters, new EmptyObjectProvider<>());
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    TransmitTokenFeighClientIntercepter transmitTokenFeighClientIntercepter() {
        return new TransmitTokenFeighClientIntercepter();
    }

    class EmptyObjectProvider<T> implements ObjectProvider<T> {

        @Override
        public T getObject(Object... args) throws BeansException {
            return null;
        }

        @Override
        public T getIfAvailable() throws BeansException {
            return null;
        }

        @Override
        public T getIfUnique() throws BeansException {
            return null;
        }

        @Override
        public T getObject() throws BeansException {
            return null;
        }

        @Override
        public void forEach(Consumer action) {
            // do nothing
        }
    }
}
