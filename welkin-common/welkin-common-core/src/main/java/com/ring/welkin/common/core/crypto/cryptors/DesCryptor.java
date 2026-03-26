package com.ring.welkin.common.core.crypto.cryptors;

import com.ring.welkin.common.core.crypto.CryptoType;
import com.ring.welkin.common.core.crypto.annotation.MappedCryptoType;
import com.ring.welkin.common.utils.crypto.DESUtil;

/**
 * AES加密的字符串加密解密处理器
 *
 * @author cloud
 * @date 2019年11月21日 上午9:49:56
 */
@MappedCryptoType(CryptoType.DES)
public class DesCryptor extends AbstractCryptor {

    public DesCryptor() {
        super("d$", "~");
    }

    @Override
    String decryptCiphertext(String ciphertext, String secretKey) {
        return DESUtil.decrypt(ciphertext, secretKey);
    }

    @Override
    String encryptPlaintext(String plaintext, String secretKey) {
        return DESUtil.encrypt(plaintext, secretKey);
    }

}
