package com.ring.welkin.common.core.exception;

import com.ring.welkin.common.core.result.ErrMsg;

public class NetworkAuthenticationRequiredException extends BizException {
    private static final long serialVersionUID = 8230905892917927484L;

    public NetworkAuthenticationRequiredException(ErrMsg errMsg) {
        super(errMsg);
    }

    public NetworkAuthenticationRequiredException(ErrMsg errMsg, Throwable cause) {
        super(errMsg, cause);
    }

    public NetworkAuthenticationRequiredException(ErrMsg errMsg, String customMessage) {
        super(errMsg, customMessage);
    }

    public NetworkAuthenticationRequiredException(ErrMsg errMsg, String customMessage, Throwable cause) {
        super(errMsg, customMessage, cause);
    }

    protected NetworkAuthenticationRequiredException(ErrMsg errMsg, String customMessage, Object[] args) {
        super(errMsg, customMessage, args);
    }

    public NetworkAuthenticationRequiredException(ErrMsg errMsg, Object[] args) {
        super(errMsg, args);
    }

    protected NetworkAuthenticationRequiredException(ErrMsg errMsg, String customMessage, Object[] args,
                                                     Throwable cause) {
        super(errMsg, customMessage, args, cause);
    }
}
