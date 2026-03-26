package com.ring.welkin.common.core.crypto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CryptoType {
	NONE("NONE(", ")"), AES("AES(", ")"), DES("DES(", ")"), DESEDE("DESEDE(", ")"), BASE64("BASE64(", ")");

	private final String prefix;
	private final String suffix;
}
