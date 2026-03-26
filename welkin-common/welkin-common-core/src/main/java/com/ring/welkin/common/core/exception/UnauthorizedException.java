package com.ring.welkin.common.core.exception;

import com.ring.welkin.common.core.result.ErrMsg;

public class UnauthorizedException extends BizException {
	private static final long serialVersionUID = 5816871071003848385L;

	public UnauthorizedException(ErrMsg errMsg) {
		super(errMsg);
	}

	public UnauthorizedException(ErrMsg errMsg, Throwable cause) {
		super(errMsg, cause);
	}

	public UnauthorizedException(ErrMsg errMsg, String customMessage) {
		super(errMsg, customMessage);
	}

	public UnauthorizedException(ErrMsg errMsg, String customMessage, Throwable cause) {
		super(errMsg, customMessage, cause);
	}

	protected UnauthorizedException(ErrMsg errMsg, String customMessage, Object[] args) {
		super(errMsg, customMessage, args);
	}

	public UnauthorizedException(ErrMsg errMsg, Object[] args) {
		super(errMsg, args);
	}

	protected UnauthorizedException(ErrMsg errMsg, String customMessage, Object[] args, Throwable cause) {
		super(errMsg, customMessage, args, cause);
	}
}
