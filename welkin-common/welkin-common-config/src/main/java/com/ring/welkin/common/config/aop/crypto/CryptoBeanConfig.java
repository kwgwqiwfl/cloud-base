package com.ring.welkin.common.config.aop.crypto;

import com.ring.welkin.common.config.aop.crypto.CryptoBeanConfig.CryptoProperties;
import com.ring.welkin.common.core.crypto.CryptorCombinator;
import com.ring.welkin.common.core.crypto.delegater.CryptorDelegater;
import com.ring.welkin.common.core.crypto.delegater.DefaultCryptorDelegater;
import com.ring.welkin.common.core.crypto.method.CryptoConfig;
import com.ring.welkin.common.core.crypto.method.CryptoMethodInvoker;
import com.ring.welkin.common.core.crypto.method.DefaultCryptoMethodInvoker;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 接口参数和结果加密解密切面处理
 *
 * @author cloud
 * @date 2019年11月21日 上午11:08:14
 */
@Configuration
@EnableConfigurationProperties(CryptoProperties.class)
public class CryptoBeanConfig {

	@Bean
	@ConditionalOnMissingBean(value = CryptoMethodInvoker.class)
	public CryptoMethodInvoker cryptoMethodInvoker(final CryptoConfig config) {
		return new DefaultCryptoMethodInvoker(config.getSecretKey());
	}

	@Bean
	@ConditionalOnMissingBean(value = CryptorDelegater.class)
	public CryptorDelegater cryptorDelegater() {
		return new DefaultCryptorDelegater();
	}

	@Bean
	@ConditionalOnMissingBean(value = CryptorCombinator.class)
	public CryptorCombinator cryptorCombinator(CryptoConfig config, CryptorDelegater cryptorDelegater) {
		return new CryptorCombinator(config.getSecretKey(), cryptorDelegater);
	}

	@Setter
	@Getter
	@ConfigurationProperties(prefix = CryptoProperties.PREFIX)
	public static final class CryptoProperties extends CryptoConfig {
		public static final String PREFIX = "crypto";
	}
}
