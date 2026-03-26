package com.ring.welkin.common.core.crypto;

import com.ring.welkin.common.core.crypto.cryptors.Cryptor;
import com.ring.welkin.common.core.crypto.delegater.CryptorDelegater;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

/**
 * 加密解密处理器组合器
 *
 * @author cloud
 * @date 2023年3月21日 上午9:53:15
 */
@Getter
@AllArgsConstructor
public class CryptorCombinator {
    /**
     * 秘钥
     */
    private final String secretKey;
    /**
     * 加解密器代理器
     */
    private final CryptorDelegater delegater;

    public void register(Cryptor cryptor) {
        this.delegater.register(cryptor);
    }

    public void register(Cryptor... cryptors) {
        this.delegater.register(cryptors);
    }

    public void register(Collection<Cryptor> cryptors) {
        this.delegater.register(cryptors);
    }

    public String encrypt(String plaintext, boolean wrapped, CryptoType cryptoType) {
        return this.delegater.encrypt(plaintext, this.secretKey, wrapped, cryptoType);
    }

    public String decrypt(String ciphertext, boolean wrapped, CryptoType cryptoType) {
        return this.delegater.decrypt(ciphertext, this.secretKey, wrapped, cryptoType);
    }

}
