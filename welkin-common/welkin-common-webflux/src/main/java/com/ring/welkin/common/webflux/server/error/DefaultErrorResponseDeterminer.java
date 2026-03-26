package com.ring.welkin.common.webflux.server.error;

import com.ring.welkin.common.core.exception.BizException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

/**
 * 默认响应状态码抉择器
 *
 * @author cloud
 * @date 2021年1月26日 下午5:33:35
 */
public class DefaultErrorResponseDeterminer implements ErrorResponseDeterminer {

    @Override
    public HttpStatus determineHttpStatus(ServerHttpResponse response, Throwable error) {
        if (error instanceof BizException) {
            return HttpStatus.OK;
        }
        return null;
    }

    @Override
    public String determineMessage(ServerHttpResponse response, Throwable error) {
        if (error instanceof BizException) {
            return error.getMessage();
        }
        return null;
    }

}
