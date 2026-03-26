package com.ring.welkin.common.utils.crypto;

import com.ring.welkin.common.utils.Assert;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

@Slf4j
public class AESUtil {

	// 默认字符集
	private static final String DEFAULT_CARSET = "UTF-8";
	// 加密方式
	private static final String AES = "AES";
	// 使用的加密算法
	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
	// 加密结果转换成多少进制的字符串
	private static final int RADIX = 16;

	public static String encryptNoSha1prng(String content, String key) {
		return aesEncryptDecrypt(content, key, true, false);
	}

	public static String decryptNoSha1prng(String content, String key) {
		return aesEncryptDecrypt(content, key, false, false);
	}

	public static String encrypt(String content, String key) {
		return aesEncryptDecrypt(content, key, true, true);
	}

	public static String decrypt(String content, String key) {
		return aesEncryptDecrypt(content, key, false, true);
	}

	private static String aesEncryptDecrypt(String content, String key, boolean isEncode, boolean isSha1prng) {
		Assert.isNotBlank(content, "'content' can not be blank!");
		Assert.isNotBlank(key, "'key' can not be blank!");

		try {
			KeyGenerator kgen = KeyGenerator.getInstance(AES);
			SecretKeySpec secretKeySpec = null;
			if (isSha1prng) {
				SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
				secureRandom.setSeed(key.getBytes());
				kgen.init(128, secureRandom);
				SecretKey secretKey = kgen.generateKey();
				secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), AES);
			} else {
				kgen.init(128);
				secretKeySpec = new SecretKeySpec(key.getBytes(), AES);
			}

			Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
			if (isEncode) {
				cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
				byte[] encodArroy = cipher.doFinal(content.getBytes(DEFAULT_CARSET));
				return bytesToHex(encodArroy);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
				byte[] stringToByte = hexToByte(content, RADIX);
				return new String(cipher.doFinal(stringToByte));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	// Byte[] ->十六进制
	private static String bytesToHex(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 将指定进制的字符串转换成byte数组
	 *
	 * @param hexStr
	 * @param radix  指定是多少进制，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return
	 */
	private static byte[] hexToByte(String hexStr, int radix) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), radix);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), radix);
			result[i] = (byte) (high * radix + low);
		}
		return result;
	}

	// TEST
	public static void main(String[] args) throws Exception {
		// 密钥 (需要前端和后端保持一致)
		// String KEY = "82e5ee761e9fa2f6";
		String KEY = "infoaeskey123456";
		System.out.println(Arrays.toString(KEY.getBytes()));
		String content = "admin";
		System.out.println("加密前：" + content);
		System.out.println("加密密钥和解密密钥：" + KEY);
		String encrypt = encrypt(content, KEY);
		System.out.println("加密后：" + encrypt);
		String decrypt = decrypt(encrypt, KEY);
		System.out.println("解密后：" + decrypt);

		encrypt = "3cde4fd05c58aee9937bfb2db12c9a91";
		System.out.println(decryptNoSha1prng(encrypt, KEY));
	}
}
