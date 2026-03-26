package com.ring.welkin.common.core.crypto.annotation;

import com.ring.welkin.common.core.crypto.CryptoType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappedCryptoType {
    CryptoType value();
}
