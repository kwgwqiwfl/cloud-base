package com.ring.welkin.common.core.passay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.HistoryRule;
import org.passay.PasswordData;
import org.passay.PasswordData.HistoricalReference;
import org.passay.PasswordData.Reference;
import org.passay.PasswordData.SourceReference;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.SourceRule;

import com.ring.welkin.common.core.passay.PassayConfig.Character;
import com.ring.welkin.common.core.passay.PassayConfig.CharacterCharacteristics;
import com.ring.welkin.common.core.passay.PassayConfig.History;
import com.ring.welkin.common.core.passay.PassayConfig.IllegalSequence;
import com.ring.welkin.common.core.passay.PassayConfig.Length;
import com.ring.welkin.common.core.passay.PassayConfig.Username;
import com.ring.welkin.common.core.passay.PassayConfig.Whitespace;
import com.ring.welkin.common.utils.ICollections;

/**
 * 密码检查器
 *
 * @author cloud
 * @date 2019年9月23日 下午12:05:19
 */
public class PassayUtils {

	/**
	 * 获取密码生成器
	 */
	private static final PasswordGenerator GENERATOR = new PasswordGenerator();

	/**
	 * 获取密码验证器
	 *
	 * @param rules 验证规则
	 * @return 密码验证器
	 */
	private static PasswordValidator getValidator(List<Rule> rules) {
		return new PasswordValidator(new PassayPropertiesMessageResolver(PassayMessageSource.getAccessor()), rules);
	}

	/**
	 * 检查密码有效性
	 *
	 * @param username              用户名，用于检查密码域用户名是否重复
	 * @param newPwd                新密码，用于检验新密码格式
	 * @param passwordSettingConfig 密码设定策略
	 * @return 检验结果
	 */
	public static RuleResult validate(String username, String newPwd, PasswordSettingConfig passwordSettingConfig) {
		return validate(username, null, newPwd, passwordSettingConfig);
	}

	/**
	 * 检查密码有效性
	 *
	 * @param username              用户名，用于检查密码域用户名是否重复
	 * @param oldPwd                旧密码，用于检查是否与老密码重复
	 * @param newPwd                新密码，用于检验新密码格式
	 * @param passwordSettingConfig 密码设定策略
	 * @return 检验结果
	 */
	public static RuleResult validate(String username, String oldPwd, String newPwd,
			PasswordSettingConfig passwordSettingConfig) {
		return validate(username, passwordSettingConfig.getInitPassword(), oldPwd, newPwd,
				convertToPassayConfig(passwordSettingConfig));
	}

	/**
	 * 检查密码有效性
	 *
	 * @param username     用户名，用于检查密码域用户名是否重复
	 * @param newPwd       新密码，用于检验新密码格式
	 * @param passayConfig 规则设置
	 * @return 检验结果
	 */
	public static RuleResult validate(String username, String newPwd, PassayConfig passayConfig) {
		return validate(username, null, newPwd, passayConfig);
	}

	/**
	 * 检查密码有效性
	 *
	 * @param username     用户名，用于检查密码域用户名是否重复
	 * @param oldPwd       旧密码，用于检查是否与老密码重复
	 * @param newPwd       新密码，用于检验新密码格式
	 * @param passayConfig 规则设置
	 * @return 检验结果
	 */
	public static RuleResult validate(String username, String oldPwd, String newPwd, PassayConfig passayConfig) {
		return validate(username, null, oldPwd, newPwd, passayConfig);
	}

	/**
	 * 检查密码有效性
	 *
	 * @param username     用户名，用于检查密码域用户名是否重复
	 * @param sourcePwd    原始密码，用于检查是否与初始密码重复
	 * @param oldPwd       旧密码，用于检查是否与老密码重复
	 * @param newPwd       新密码，用于检验新密码格式
	 * @param passayConfig 规则设置
	 * @return 检验结果
	 */
	public static RuleResult validate(String username, String sourcePwd, String oldPwd, String newPwd,
			PassayConfig passayConfig) {
		return validate(generatePasswordData(username, sourcePwd, oldPwd, newPwd), passayConfig);
	}

	private static PasswordData generatePasswordData(String username, String sourcePwd, String oldPwd, String newPwd) {
		PasswordData passwordData = new PasswordData(username, newPwd);
		List<Reference> references = new ArrayList<>();
		if (StringUtils.isNoneBlank(sourcePwd)) {
			references.add(new SourceReference("source", sourcePwd));
		}
		if (StringUtils.isNoneBlank(oldPwd)) {
			references.add(new HistoricalReference(oldPwd));
		}
		if (ICollections.hasElements(references)) {
			passwordData.setPasswordReferences(references);
		}
		return passwordData;
	}

	/**
	 * 检查密码有效性
	 *
	 * @param passwordData 密码数据
	 * @param passayConfig 规则设置
	 * @return 检验结果
	 */
	public static RuleResult validate(PasswordData passwordData, PassayConfig passayConfig) {
		return validate(passwordData, passayConfig.getEffectRules());
	}

	/**
	 * 检查密码有效性
	 *
	 * @param passwordData 密码数据
	 * @param rules        规则列表
	 * @return 检验结果
	 */
	public static RuleResult validate(PasswordData passwordData, List<Rule> rules) {
		PasswordValidator validator = getValidator(rules);
		RuleResult result = validator.validate(passwordData);
		if (!result.isValid()) {
			List<String> messages = validator.getMessages(result);
			throw new IllegalArgumentException(String.join("\n", messages));
		}
		return result;
	}

	/**
	 * 检查密码有效性
	 *
	 * @param passwordData 密码数据
	 * @param rules        规则列表
	 * @return 检验结果
	 */
	public static RuleResult validate(PasswordData passwordData, Rule... rules) {
		return validate(passwordData, Arrays.asList(rules));
	}

	/**
	 * 检查密码有效性
	 *
	 * @param passwordData          密码数据
	 * @param passwordSettingConfig 密码全局设置信息
	 * @return 检验结果
	 */
	public static RuleResult validate(PasswordData passwordData, PasswordSettingConfig passwordSettingConfig) {
		return validate(passwordData, convertToPassayConfig(passwordSettingConfig));
	}

	private static PassayConfig convertToPassayConfig(PasswordSettingConfig passwordSettingConfig) {
		PassayConfig passayConfig = new PassayConfig();
		// 空格策略
		boolean whitespaceEnabled = passwordSettingConfig.isWhitespaceEnabled();
		if (whitespaceEnabled) {
			passayConfig.setWhitespace(new Whitespace(whitespaceEnabled));
		}

		// 长度策略
		boolean lengthEnabled = passwordSettingConfig.isLengthEnabled();
		if (lengthEnabled) {
			int minLength = passwordSettingConfig.getMinLength();
			int maxLength = passwordSettingConfig.getMaxLength();
			passayConfig.setLength(new Length(lengthEnabled, minLength, maxLength));
		}

		// 字符类型策略
		boolean characteristicsEnabled = passwordSettingConfig.isCharacteristicsEnabled();
		if (characteristicsEnabled) {
			int numCharacteristics = passwordSettingConfig.getNumCharacteristics();
			List<Character> characters = new ArrayList<>();
			int numDigitCharacters = passwordSettingConfig.getNumDigitCharacters();
			if (numDigitCharacters > 0) {
				characters.add(new Character(EnglishCharacterData.Digit, numDigitCharacters));
			}
			int numLowercaseCharacters = passwordSettingConfig.getNumLowercaseCharacters();
			if (numLowercaseCharacters > 0) {
				characters.add(new Character(EnglishCharacterData.LowerCase, numLowercaseCharacters));
			}
			int numUppercaseCharacters = passwordSettingConfig.getNumUppercaseCharacters();
			if (numUppercaseCharacters > 0) {
				characters.add(new Character(EnglishCharacterData.UpperCase, numUppercaseCharacters));
			}
			int numSpecialCharacters = passwordSettingConfig.getNumSpecialCharacters();
			if (numSpecialCharacters > 0) {
				characters.add(new Character(EnglishCharacterData.Special, numSpecialCharacters));
			}
			passayConfig.setCharacterCharacteristics(
					new CharacterCharacteristics(characteristicsEnabled, numCharacteristics, characters));
		}

		// 字符序列策略
		boolean illegalSequenceEnabled = passwordSettingConfig.isIllegalSequenceEnabled();
		if (illegalSequenceEnabled) {
			EnglishSequenceData illegalSequenceData = passwordSettingConfig.getIllegalSequenceData();
			int illegalSequenceLength = passwordSettingConfig.getIllegalSequenceLength();
			boolean illegalSequenceWrap = passwordSettingConfig.isIllegalSequenceWrap();
			passayConfig.setIllegalSequence(new IllegalSequence(illegalSequenceEnabled, illegalSequenceData,
					illegalSequenceLength, illegalSequenceWrap));
		}

		// 密码历史策略
		boolean historyEnabled = passwordSettingConfig.isHistoryEnabled();
		if (historyEnabled) {
			passayConfig.setHistory(new History(historyEnabled));
		}

		// 用户名策略
		boolean usernameEnabled = passwordSettingConfig.isUsernameEnabled();
		if (usernameEnabled) {
			passayConfig.setUsername(new Username(usernameEnabled));
		}
		return passayConfig;
	}

	/**
	 * 生成密码
	 *
	 * @param length 密码长度
	 * @param rules  密码规则
	 * @return 生成的密码
	 */
	public static String generate(int length, CharacterRule... rules) {
		return GENERATOR.generatePassword(length, rules);
	}

	/**
	 * 生成密码
	 *
	 * @param length 密码长度
	 * @param rules  密码规则
	 * @return 生成的密码
	 */
	public static String generate(int length, List<CharacterRule> rules) {
		return GENERATOR.generatePassword(length, rules);
	}

	/**
	 * 生成密码
	 *
	 * @param length 密码长度
	 * @return 生成的密码
	 */
	public static String generate(int length) {
		CharacterRule upperCases = new CharacterRule(EnglishCharacterData.UpperCase);
		CharacterRule lowerCases = new CharacterRule(EnglishCharacterData.LowerCase);
		CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit);
		// CharacterRule special = new CharacterRule(EnglishCharacterData.Special);
		return generate(length, upperCases, lowerCases, digits/* , special */);
	}

	public static void main(String[] args) {
		String generate = PassayUtils.generate(10);
		System.out.println(generate);

		SourceRule sourceRule = new SourceRule();
		HistoryRule historyRule = new HistoryRule();
		PasswordValidator validator = new PasswordValidator(sourceRule, historyRule);
		PasswordData password = new PasswordData("password@1235");
		password.setPasswordReferences(new PasswordData.SourceReference("source", "password"),
				new PasswordData.HistoricalReference("password@123"));
		RuleResult result = validator.validate(password);

		if (result.isValid()) {
			System.out.println("Password validated.");
		} else {
			System.out.println("Invalid Password: " + validator.getMessages(result));
		}
	}

}
