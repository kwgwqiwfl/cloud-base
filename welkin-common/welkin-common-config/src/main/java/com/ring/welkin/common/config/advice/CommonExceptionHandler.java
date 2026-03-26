package com.ring.welkin.common.config.advice;

import com.ring.welkin.common.core.exception.BizException;
import com.ring.welkin.common.core.exception.ForbiddenException;
import com.ring.welkin.common.core.exception.NetworkAuthenticationRequiredException;
import com.ring.welkin.common.core.exception.UnauthorizedException;
import com.ring.welkin.common.core.result.ErrMsg;
import com.ring.welkin.common.core.result.ErrType;
import com.ring.welkin.common.core.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Set;

/**
 * 系统异常统一处理通知
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

	@Autowired
	@Nullable
	private MessageSourceAccessor accessor;

	/**
	 * 没有捕获处理的异常
	 *
	 * @param e 未捕获异常对象
	 * @return 未捕获异常消息报文
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@Order(1)
	public Response<?> uncaughtExceptionHandler(Exception e) {
		log.error(e.getMessage(), e);
		return Response.error(ErrType.SERVICE_UNAVAILABLE, e.getMessage()).details(e.toString()).build();
	}

	@ResponseBody
	@ExceptionHandler({ IllegalArgumentException.class, ServerWebInputException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@Order(0)
	public Response<?> illegalArgumentExceptionHandler(Exception e) {
		log.error(e.getMessage(), e);
		return Response.error(ErrType.BAD_REQUEST, getIllegalArgumentMessage(e.getMessage())).details(e.toString())
				.build();
	}

	/**
	 * 业务处理异常信息
	 *
	 * @param e 业务异常对象
	 * @return 业务异常消息报文
	 */
	@ResponseBody
	@ExceptionHandler(BizException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@Order(-1)
	public Response<?> bizExceptionHandler(BizException e) {
		return handleBizException(e);
	}

	private <T extends BizException> Response<?> handleBizException(T e) {
		log.error(e.getMessage(), e);
		Response<?> result = null;

		Throwable cause = e.getCause();
		ErrMsg errMsg = e.getErrMsg();
		String customMessage = e.getCustomMessage();

		String message = null;
		try {
			if (accessor != null) {
				message = accessor.getMessage(errMsg.getCode(), e.getArgs(), e.getCustomMessage(),
						LocaleContextHolder.getLocale());
			}
		} catch (Exception e1) {
			// do nothing
		}

		// 默认拼接错误信息
		if (StringUtils.isNotBlank(customMessage)) {
			if (StringUtils.isNotBlank(message)) {
				message += ":" + customMessage;
			} else {
				message = customMessage;
			}
		}

		if (cause != null) {
			if (cause instanceof BizException) {
				BizException c = (BizException) cause;
				result = Response
						.error(errMsg.getStatus(),
								StringUtils.defaultIfEmpty(message, "UNKNOWN ERROR:" + errMsg.getStatus()))
						.details(c.toString()).build();
			} else {
				result = Response.error(errMsg.getStatus(), cause.getMessage()).details(cause.toString()).build();
			}
		} else {
			result = Response
					.error(errMsg.getStatus(),
							StringUtils.defaultIfEmpty(message, "UNKNOWN ERROR:" + errMsg.getStatus()))
					.details(e.toString()).build();
		}
		if (result != null) {
			return result;
		}
		return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getMessage()).details(e.toString()).build();
	}

	@ResponseBody
	@ExceptionHandler({ DataAccessException.class, SQLIntegrityConstraintViolationException.class })
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@Order(-2)
	public Response<?> dataAccessException(Exception e) {
		log.error(e.getMessage(), e);
		String message = null;
		Throwable cause = e.getCause();
		try {
			if (accessor != null) {
				if (cause != null) {
					message = accessor.getMessage(cause.getClass().getCanonicalName(), LocaleContextHolder.getLocale());
				} else {
					message = accessor.getMessage(e.getClass().getCanonicalName(), LocaleContextHolder.getLocale());
				}
			}
		} catch (Exception e1) {
			// do nothing
		}
		message = StringUtils.defaultIfEmpty(message, e.getMessage());
		return Response.serviceUnavailable().message(message).details(e.toString()).build();
	}

	@ResponseBody
	@ExceptionHandler(value = { BindException.class, WebExchangeBindException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@Order(-3)
	public Response<?> webExchangeBindException(BindingResult e) {
		FieldError fieldError = e.getFieldError();
		String objectName = fieldError.getObjectName();
		StringBuffer buff = new StringBuffer();
		buff.append("[");
		if (StringUtils.isNotEmpty(objectName)) {
			buff.append(objectName);
		}
		String field = fieldError.getField();
		if (StringUtils.isNotEmpty(objectName)) {
			buff.append(".").append(field);
		}
		buff.append("] ").append(fieldError.getDefaultMessage());
		return Response.badRequest().message(getIllegalArgumentMessage(buff.toString())).details(e.toString()).build();
	}

	@ResponseBody
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@Order(-4)
	public Response<?> validationException(ValidationException e) {
		String message = null;
		if (e instanceof ConstraintViolationException) {
			ConstraintViolationException exs = (ConstraintViolationException) e;
			Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
			for (ConstraintViolation<?> item : violations) {
				Path propertyPath = item.getPropertyPath();
				message = item.getMessage();
				if (propertyPath instanceof PathImpl) {
					PathImpl p = (PathImpl) propertyPath;
					NodeImpl leafNode = p.getLeafNode();
					switch (leafNode.getKind()) {
					case PARAMETER:
						message = "[" + leafNode.getName() + "]" + message;
						break;
					case PROPERTY:
						message = "[" + leafNode.getParent().getName() + "." + leafNode.getName() + "]" + message;
						break;
					default:
						break;
					}
				}
				break;// 拿第一条错误信息即可，满足快速失败就行
			}
		} else {
			message = e.getMessage();
		}
		return Response.badRequest().message(getIllegalArgumentMessage(message)).details(e.toString()).build();
	}

	@ResponseBody
	@ExceptionHandler(value = { UnauthorizedException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@Order(-4)
	public Response<?> unauthorizedException(UnauthorizedException e) {
		return handleBizException(e);
	}

	@ResponseBody
	@ExceptionHandler(value = {ForbiddenException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@Order(-5)
	public Response<?> forbiddenException(ForbiddenException e) {
		return handleBizException(e);
	}

	@ResponseBody
	@ExceptionHandler(value = {NetworkAuthenticationRequiredException.class})
	@ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
	@Order(-6)
	public Response<?> networkAuthenticationRequiredException(NetworkAuthenticationRequiredException e) {
		return handleBizException(e);
	}

	protected String getIllegalArgumentMessage(String message) {
		return StringUtils.join(accessor.getMessage("IllegalArgument.prefix", LocaleContextHolder.getLocale()), ":",
				message);
	}
}
