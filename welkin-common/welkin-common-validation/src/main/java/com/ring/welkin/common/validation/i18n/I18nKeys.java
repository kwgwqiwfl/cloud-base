package com.ring.welkin.common.validation.i18n;

public interface I18nKeys {

	public static final String RightFormat = "javax.validation.constraints.RightFormat.message";
    // javax定义校验消息
    public static final String AssertFalse = "javax.validation.constraints.AssertFalse.message";
	public static final String AssertTrue = "javax.validation.constraints.AssertTrue.message";
	public static final String DecimalMax = "javax.validation.constraints.DecimalMax.message";
	public static final String DecimalMin = "javax.validation.constraints.DecimalMin.message";
	public static final String Digits = "javax.validation.constraints.Digits.message";
	public static final String Email = "javax.validation.constraints.Email.message";
	public static final String Future = "javax.validation.constraints.Future.message";
	public static final String FutureOrPresent = "javax.validation.constraints.FutureOrPresent.message";
	public static final String Max = "javax.validation.constraints.Max.message";
	public static final String Min = "javax.validation.constraints.Min.message";
	public static final String Negative = "javax.validation.constraints.Negative.message";
	public static final String NegativeOrZero = "javax.validation.constraints.NegativeOrZero.message";
	public static final String NotBlank = "javax.validation.constraints.NotBlank.message";
	public static final String NotEmpty = "javax.validation.constraints.NotEmpty.message";
	public static final String NotNull = "javax.validation.constraints.NotNull.message";
	public static final String Null = "javax.validation.constraints.Null.message";
	public static final String Past = "javax.validation.constraints.Past.message";
	public static final String PastOrPresent = "javax.validation.constraints.PastOrPresent.message";
	public static final String Pattern = "javax.validation.constraints.Pattern.message";
	public static final String Positive = "javax.validation.constraints.Positive.message";
	public static final String PositiveOrZero = "javax.validation.constraints.PositiveOrZero.message";
	public static final String Size = "javax.validation.constraints.Size.message";

	// hibernate 定义校验消息
	public static final String CreditCardNumber = "org.hibernate.validator.constraints.CreditCardNumber.message";
	public static final String Currency = "org.hibernate.validator.constraints.Currency.message";
	public static final String EAN = "org.hibernate.validator.constraints.EAN.message";
	public static final String HibernateEmail = "org.hibernate.validator.constraints.Email.message";
	public static final String Length = "org.hibernate.validator.constraints.Length.message";
	public static final String CodePointLength = "org.hibernate.validator.constraints.CodePointLength.message";
	public static final String LuhnCheck = "org.hibernate.validator.constraints.LuhnCheck.message";
	public static final String Mod10Check = "org.hibernate.validator.constraints.Mod10Check.message";
	public static final String Mod11Check = "org.hibernate.validator.constraints.Mod11Check.message";
	public static final String ModCheck = "org.hibernate.validator.constraints.ModCheck.message";
	public static final String HibernateNotBlank = "org.hibernate.validator.constraints.NotBlank.message";
	public static final String HibernateNotEmpty = "org.hibernate.validator.constraints.NotEmpty.message";
	public static final String ParametersScriptAssert = "org.hibernate.validator.constraints.ParametersScriptAssert.message";
	public static final String Range = "org.hibernate.validator.constraints.Range.message";
	public static final String ScriptAssert = "org.hibernate.validator.constraints.ScriptAssert.message";
	public static final String URL = "org.hibernate.validator.constraints.URL.message";
	public static final String TimeDurationMax = "org.hibernate.validator.constraints.time.DurationMax.message";
	public static final String TimeDurationMin = "org.hibernate.validator.constraints.time.DurationMin.message";

	// 自定义校验消息
	public static final String Cron = "com.ring.welkin.common.validation.constraints.Cron.message";
	public static final String EnumValue = "com.ring.welkin.common.validation.constraints.EnumValue.message";
	public static final String MustIn = "com.ring.welkin.common.validation.constraints.MustIn.message";
	public static final String Password = "com.ring.welkin.common.validation.constraints.Password.message";
	public static final String Phone = "com.ring.welkin.common.validation.constraints.Phone.message";
	public static final String IP = "com.ring.welkin.common.validation.constraints.IP.message";

	// passy校验消息
	public static final String PASS_HISTORY_VIOLATION = "HISTORY_VIOLATION";
	public static final String PASS_ILLEGAL_WORD = "ILLEGAL_WORD";
	public static final String PASS_ILLEGAL_WORD_REVERSED = "ILLEGAL_WORD_REVERSED";
	public static final String PASS_ILLEGAL_DIGEST_WORD = "ILLEGAL_DIGEST_WORD";
	public static final String PASS_ILLEGAL_DIGEST_WORD_REVERSED = "ILLEGAL_DIGEST_WORD_REVERSED";
	public static final String PASS_ILLEGAL_MATCH = "ILLEGAL_MATCH";
	public static final String PASS_ALLOWED_MATCH = "ALLOWED_MATCH";
	public static final String PASS_ILLEGAL_CHAR = "ILLEGAL_CHAR";
	public static final String PASS_ALLOWED_CHAR = "ALLOWED_CHAR";
	public static final String PASS_ILLEGAL_QWERTY_SEQUENCE = "ILLEGAL_QWERTY_SEQUENCE";
	public static final String PASS_ILLEGAL_ALPHABETICAL_SEQUENCE = "ILLEGAL_ALPHABETICAL_SEQUENCE";
	public static final String PASS_ILLEGAL_NUMERICAL_SEQUENCE = "ILLEGAL_NUMERICAL_SEQUENCE";
	public static final String PASS_ILLEGAL_USERNAME = "ILLEGAL_USERNAME";
	public static final String PASS_ILLEGAL_USERNAME_REVERSED = "ILLEGAL_USERNAME_REVERSED";
	public static final String PASS_ILLEGAL_WHITESPACE = "ILLEGAL_WHITESPACE";
	public static final String PASS_ILLEGAL_NUMBER_RANGE = "ILLEGAL_NUMBER_RANGE";
	public static final String PASS_INSUFFICIENT_UPPERCASE = "INSUFFICIENT_UPPERCASE";
	public static final String PASS_INSUFFICIENT_LOWERCASE = "INSUFFICIENT_LOWERCASE";
	public static final String PASS_INSUFFICIENT_ALPHABETICAL = "INSUFFICIENT_ALPHABETICAL";
	public static final String PASS_INSUFFICIENT_DIGIT = "INSUFFICIENT_DIGIT";
	public static final String PASS_INSUFFICIENT_SPECIAL = "INSUFFICIENT_SPECIAL";
	public static final String PASS_INSUFFICIENT_CHARACTERISTICS = "INSUFFICIENT_CHARACTERISTICS";
	public static final String PASS_INSUFFICIENT_COMPLEXITY = "INSUFFICIENT_COMPLEXITY";
	public static final String PASS_INSUFFICIENT_COMPLEXITY_RULES = "INSUFFICIENT_COMPLEXITY_RULES";
	public static final String PASS_SOURCE_VIOLATION = "SOURCE_VIOLATION";
	public static final String PASS_TOO_LONG = "TOO_LONG";
	public static final String PASS_TOO_SHORT = "TOO_SHORT";
	public static final String PASS_USER_PWD_IS_INITIAL = "USER_PWD_IS_INITIAL";
	public static final String PASS_USER_PWD_IS_EXPIRED = "USER_PWD_IS_EXPIRED";

}
