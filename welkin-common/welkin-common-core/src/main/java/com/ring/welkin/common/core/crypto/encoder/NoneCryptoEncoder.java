package com.ring.welkin.common.core.crypto.encoder;

import com.ring.welkin.common.core.crypto.CryptoEncoder;
import com.ring.welkin.common.core.crypto.CryptoType;
import com.ring.welkin.common.core.crypto.delegater.CryptorDelegater;

/**
 * 默认的无处理的数据加密器
 *
 * @param <T> 数据类型
 * @author cloud
 * @date 2021年3月10日 下午4:59:28
 */
public class NoneCryptoEncoder implements CryptoEncoder<Object> {

    @Override
    public Object encode(Object obj, String secretKey, boolean wrapped, CryptoType cryptoType,
                         CryptorDelegater cryptorDelegater) {
        return obj;
    }

}
