package com.ring.welkin.common.core.ml;

import com.ring.welkin.common.core.result.ErrMsg;
import com.ring.welkin.common.core.result.ErrType;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.ToString;

@ApiModel
@Getter
@ToString
public class MResponse<T> extends AbstractMResponse<T> {
    private static final long serialVersionUID = -5597707689816894840L;

    // 私有构造，外部只能用静态方法
    private MResponse(Integer status, String message, T content) {
        super(status, message, content);
    }

    // ====================== 成功 ======================
    public static <T> MResponse<T> ok() {
        return new MResponse<>(200, "success", null);
    }

    public static <T> MResponse<T> ok(T content) {
        return new MResponse<>(200, "success", content);
    }

    // ====================== 失败 ======================
    public static <T> MResponse<T> error(ErrMsg errMsg) {
        return new MResponse<>(errMsg.getStatus(), errMsg.getMessage(), null);
    }

    public static <T> MResponse<T> error(int status, String message) {
        return new MResponse<>(status, message, null);
    }

    // ====================== 常用快速响应 ======================
    public static <T> MResponse<T> badRequest() {
        return error(ErrType.BAD_REQUEST);
    }

    public static <T> MResponse<T> unauthorized() {
        return error(ErrType.UNAUTHORIZED);
    }

    public static <T> MResponse<T> forbidden() {
        return error(ErrType.FORBIDDEN);
    }

    public static <T> MResponse<T> notFound() {
        return error(ErrType.NOT_FOUND);
    }

    public static <T> MResponse<T> serverError() {
        return error(ErrType.INTERNAL_SERVER_ERROR);
    }
}
