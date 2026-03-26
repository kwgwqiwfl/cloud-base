package com.ring.welkin.common.core.crypto.decoder;

import com.ring.welkin.common.core.crypto.CryptoDecoder;
import com.ring.welkin.common.core.crypto.CryptoType;
import com.ring.welkin.common.core.crypto.delegater.CryptorDelegater;
import com.ring.welkin.common.utils.ICollections;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认的无处理的数据解密器
 *
 * @param <T> 解密数据类型
 * @author cloud
 * @date 2021年3月10日 下午4:59:28
 */
public class StringSetCryptoDecoder implements CryptoDecoder<Set<String>> {

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> decode(Object obj, String secretKey, boolean wrapped, CryptoType cryptoType,
                              CryptorDelegater cryptorDelegater) {
        Set<String> set = (Set<String>) obj;
        if (ICollections.hasElements(set)) {
            return set.stream().map(t -> cryptorDelegater.decrypt(t, secretKey, wrapped, cryptoType))
                .collect(Collectors.toSet());
        }
        return set;
    }

}
