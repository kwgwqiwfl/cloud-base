package com.ring.welkin.common.core.passay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.Serializable;
import java.util.List;

/**
 * 密码信息
 *
 * @author cloud
 * @date 2019年9月24日 上午11:33:20
 */
@Data
@NoArgsConstructor
@ApiModel
public class PwdInfo implements Serializable {
	private static final long serialVersionUID = 7809909789618956073L;

	/**
	 * 密码模式：简单模式和严格模式
	 *
	 * @author cloud
	 * @date 2019年9月24日 上午11:43:12
	 */
	public static enum PwdMode {
		SIMPLE, STRICT;
	}

	/**
	 * 模式：SIMPLE/STRICT
	 */
	@ApiModelProperty("模式：SIMPLE/STRICT")
	private PwdMode mode = PwdMode.SIMPLE;

	/**
	 * 是否需要修改
	 */
	@ApiModelProperty("是否需要修改")
	private boolean needChange;

	/**
	 * 是否是第一次登录
	 */
	@ApiModelProperty("是否是第一次登录")
	private boolean firstLogin;

	@ApiModelProperty("首次登陆修改密码")
	private boolean changePwdOnFirstLogin = false;

	/**
	 * 是否过期
	 */
	@ApiModelProperty("是否过期")
	private boolean expired;

	/**
	 * 是否是初始密码
	 */
	@ApiModelProperty("是否是初始密码")
	private boolean inited;

	/**
	 * 提示消息
	 */
	@ApiModelProperty("提示消息")
	private String msg;

	@ApiModelProperty("过期提示消息")
	private List<String> expireMsgs;

	public String getMsg() {
		MessageSourceAccessor messages = PassayMessageSource.getAccessor();
		if (inited) {
			return messages.getMessage("USER_PWD_IS_INITIAL",
					"Your password is the initial password. Please change it first.");
		}
		if (expired) {
			return messages.getMessage("USER_PWD_IS_EXPIRED", "Your password has expired. Please change it first.");
		}
		if (firstLogin) {
			return messages.getMessage("USER_PWD_IS_FIRSTLOGIN", "Your are first login. Please change password first.");
		}
		return msg;
	}

	public boolean isNeedChange() {
		if (!this.needChange) {
			this.needChange = (changePwdOnFirstLogin && firstLogin) || expired || inited;
		}
		return this.needChange;
	}

	public PwdInfo(PwdMode mode,boolean changePwdOnFirstLogin, boolean firstLogin, boolean expired, boolean inited) {
		this.mode = mode;
		this.changePwdOnFirstLogin = changePwdOnFirstLogin;
		this.firstLogin = firstLogin;
		this.expired = expired;
		this.inited = inited;
	}

}
