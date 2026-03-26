package com.ring.welkin.common.core.crypto.cryptors;

import com.ring.welkin.common.core.crypto.CryptoType;
import com.ring.welkin.common.core.crypto.annotation.MappedCryptoType;
import com.ring.welkin.common.utils.crypto.TripDESUtil;

/**
 * TripDES加密的字符串加密解密处理器
 *
 * @author cloud
 * @date 2019年11月21日 上午9:49:56
 */
@MappedCryptoType(CryptoType.DESEDE)
public class DESedeCryptor extends AbstractCryptor {

    public DESedeCryptor() {
        super("de$", "~");
    }

    @Override
    String decryptCiphertext(String ciphertext, String secretKey) {
        return TripDESUtil.decrypt(ciphertext, secretKey);
    }

    @Override
    String encryptPlaintext(String plaintext, String secretKey) {
        return TripDESUtil.encrypt(plaintext, secretKey);
    }

}
