package com.ring.welkin.common.core.result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 错误信息
 *
 * @author: cloud
 * @date: 2019年4月23日 下午2:53:43
 */
@Setter
@Getter
@Builder
public class SimpleErrMsg implements ErrMsg {

	private Integer status;
	private String message;

	@Override
	public String getCode() {
		if (status == null) {
			return null;
		}
		return status.toString();
	}

	public SimpleErrMsg errMsg(Integer status, String message) {
		setStatus(status);
		setMessage(message);
		return this;
	}

	public SimpleErrMsg errMsg(ErrMsg errMsg, String message) {
		setStatus(errMsg.getStatus());
		setMessage(StringUtils.defaultString(message, errMsg.getMessage()));
		return this;
	}

}
