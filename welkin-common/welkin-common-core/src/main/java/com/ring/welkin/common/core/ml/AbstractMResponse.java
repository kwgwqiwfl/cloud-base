package com.ring.welkin.common.core.ml;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

@ApiModel
@Getter
public abstract class AbstractMResponse<T> {
    private Integer status;
    private String message;
    private T data;

    public AbstractMResponse(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}