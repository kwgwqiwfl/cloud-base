package com.ring.welkin.common.core.passay;

import java.util.List;

import org.passay.EnglishSequenceData;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 密码设定策略
 */
@Data
@ApiModel
public class PasswordSettingConfig {

	@ApiModelProperty(value = "是否启用密码设定策略，默认true", required = false)
	private boolean enabled = true;

	@ApiModelProperty(value = "首次登陆需要重置密码，默认false", required = false)
	private boolean resetPwdOnFirstLogin = false;

	@ApiModelProperty(value = "系统用户初始密码", required = false)
	private String initPassword = "RFKJ123456";

	/********************* 密码格式验证 **********************/
	@ApiModelProperty(value = "密码是否允许空格，默认false", required = false)
	private boolean whitespaceEnabled = false;

	@ApiModelProperty(value = "是否开启密码长度限制，检查minLength和maxLength，默认true", required = false)
	private boolean lengthEnabled = true;

	@ApiModelProperty(value = "密码最小长度，默认8", required = false)
	private int minLength = 8;

	@ApiModelProperty(value = "密码最大长度，默认30", required = false)
	private int maxLength = 30;

	@ApiModelProperty(value = "是否开启字符特征规则，默认false", required = false)
	private boolean characteristicsEnabled = false;

	@ApiModelProperty(value = "字符特征规则的类型个数（大写字母、小写字母、数字、特殊字符，共4种），默认3种", required = false)
	private int numCharacteristics = 3;

	@ApiModelProperty(value = "字符特征规则的类型：数字类型的最小个数，默认1", required = false)
	private int numDigitCharacters = 1;

	@ApiModelProperty(value = "字符特征规则的类型：小写字符最小个数，默认1", required = false)
	private int numLowercaseCharacters = 1;

	@ApiModelProperty(value = "字符特征规则的类型：大写字符最小个数，默认1", required = false)
	private int numUppercaseCharacters = 1;

	@ApiModelProperty(value = "字符特征规则的类型：特殊字符最小个数，默认1", required = false)
	private int numSpecialCharacters = 1;

	@ApiModelProperty(value = "是否开启非法序列规则，默认false", required = false)
	private boolean illegalSequenceEnabled = false;

	@ApiModelProperty(value = "非法序列规则：类型，可选Alphabetical（字母序列），Numerical（数字序列），USQwerty（键盘序列），默认USQwerty", required = false)
	private EnglishSequenceData illegalSequenceData = EnglishSequenceData.USQwerty;

	@ApiModelProperty(value = "非法序列规则：序列最大长度，默认3", required = false)
	private int illegalSequenceLength = 3;

	@ApiModelProperty(value = "非法序列规则：包装顺序，默认true", required = false)
	private boolean illegalSequenceWrap = true;

	@ApiModelProperty(value = "是否开启历史密码规则，默认false", required = false)
	private boolean historyEnabled = false;

	@ApiModelProperty(value = "是否开启历史密码用户名校验规则，默认false", required = false)
	private boolean usernameEnabled = false;

	public List<String> getRuleMessages() {
		List<String> msgs = Lists.newArrayList();
		if (enabled) {
			// 空格限制
			if (whitespaceEnabled) {
				msgs.add("密码不允许存在空格");
			}

			// 长度限制
			if (lengthEnabled) {
				msgs.add(String.format("密码长度必须大于等于%d且小于等于%d", minLength, maxLength));
			}

			// 字符限制
			if (characteristicsEnabled) {
				StringBuffer sbf = new StringBuffer(String.format("密码必须包含大写字母、小写字母、数字、特殊字符中的%d种", numCharacteristics));

				if (numDigitCharacters > 0) {
					sbf.append(",").append(String.format("至少%d个数字", numDigitCharacters));
				}
				if (numLowercaseCharacters > 0) {
					sbf.append(",").append(String.format("至少%d个小写字符", numLowercaseCharacters));
				}
				if (numUppercaseCharacters > 0) {
					sbf.append(",").append(String.format("至少%d个大写字符", numUppercaseCharacters));
				}
				if (numSpecialCharacters > 0) {
					sbf.append(",").append(String.format("至少%d个特殊字符", numSpecialCharacters));
				}
				msgs.add(sbf.toString());
			}

			// 字符序列
			if (illegalSequenceEnabled && illegalSequenceLength > 1) {
				String typeName = "";
				switch (illegalSequenceData) {
				case Alphabetical:
					typeName = "字母序列";
					break;
				case Numerical:
					typeName = "数字序列";
					break;
				case USQwerty:
				default:
					typeName = "键盘序列";
					break;
				}
				msgs.add(String.format("密码中连续的%s不能超过%d个", typeName, illegalSequenceLength));
			}

			// 历史密码
			if (historyEnabled) {
				msgs.add("密码不能与历史密码相同");
			}

			// 用户名
			if (usernameEnabled) {
				msgs.add("密码不能与用户名相同");
			}
		}
		return msgs;
	}
}
