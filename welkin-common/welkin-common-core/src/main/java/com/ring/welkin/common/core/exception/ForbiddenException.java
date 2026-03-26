package com.ring.welkin.common.core.exception;

import com.ring.welkin.common.core.result.ErrMsg;

public class ForbiddenException extends BizException {
    private static final long serialVersionUID = 5816871071003848385L;

    public ForbiddenException(ErrMsg errMsg) {
        super(errMsg);
    }

    public ForbiddenException(ErrMsg errMsg, Throwable cause) {
        super(errMsg, cause);
    }

    public ForbiddenException(ErrMsg errMsg, String customMessage) {
        super(errMsg, customMessage);
    }

    public ForbiddenException(ErrMsg errMsg, String customMessage, Throwable cause) {
        super(errMsg, customMessage, cause);
    }

    protected ForbiddenException(ErrMsg errMsg, String customMessage, Object[] args) {
        super(errMsg, customMessage, args);
    }

    public ForbiddenException(ErrMsg errMsg, Object[] args) {
        super(errMsg, args);
    }

    protected ForbiddenException(ErrMsg errMsg, String customMessage, Object[] args, Throwable cause) {
        super(errMsg, customMessage, args, cause);
    }
}
