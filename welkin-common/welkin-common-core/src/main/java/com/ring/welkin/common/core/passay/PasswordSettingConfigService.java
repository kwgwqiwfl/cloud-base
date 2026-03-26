package com.ring.welkin.common.core.passay;

public interface PasswordSettingConfigService {

	/**
	 * 获取密码设定策略
	 *
	 * @return 密码设定策略
	 */
	default PasswordSettingConfig getPasswordSettingConfig() {
		return new PasswordSettingConfig();
	}
}
