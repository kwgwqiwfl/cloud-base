package com.ring.welkin.common.core.crypto.cryptors;

import com.ring.welkin.common.core.crypto.CryptoType;
import com.ring.welkin.common.core.crypto.annotation.MappedCryptoType;
import com.ring.welkin.common.utils.crypto.AESUtil;

/**
 * AES加密的字符串加密解密处理器
 *
 * @author cloud
 * @date 2019年11月21日 上午9:49:56
 */
@MappedCryptoType(CryptoType.AES)
public class AesCryptor extends AbstractCryptor {

	public AesCryptor() {
		super("a$", "~", "AES(", ")");
	}

	@Override
	String decryptCiphertext(String ciphertext, String secretKey) {
		return AESUtil.decrypt(ciphertext, secretKey);
	}

	@Override
	String encryptPlaintext(String plaintext, String secretKey) {
		return AESUtil.encrypt(plaintext, secretKey);
	}
}
