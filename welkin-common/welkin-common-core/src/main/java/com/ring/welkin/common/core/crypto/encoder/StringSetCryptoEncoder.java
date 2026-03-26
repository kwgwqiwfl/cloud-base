package com.ring.welkin.common.core.crypto.encoder;

import com.ring.welkin.common.core.crypto.CryptoEncoder;
import com.ring.welkin.common.core.crypto.CryptoType;
import com.ring.welkin.common.core.crypto.delegater.CryptorDelegater;
import com.ring.welkin.common.utils.ICollections;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认的无处理的数据加密器
 *
 * @param <T> 数据类型
 * @author cloud
 * @date 2021年3月10日 下午4:59:28
 */
public class StringSetCryptoEncoder implements CryptoEncoder<Set<String>> {

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> encode(Object obj, String secretKey, boolean wrapped, CryptoType cryptoType,
                              CryptorDelegater cryptorDelegater) {
        Set<String> set = (Set<String>) obj;
        if (ICollections.hasElements(set)) {
            return set.stream().map(t -> cryptorDelegater.encrypt(t, secretKey, wrapped, cryptoType))
                .collect(Collectors.toSet());
        }
        return set;
    }

}
