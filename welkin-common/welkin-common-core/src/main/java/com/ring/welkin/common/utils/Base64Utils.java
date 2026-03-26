package com.ring.welkin.common.utils;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.regex.Pattern;

public class Base64Utils {

    public static final Encoder ENCODER = Base64.getEncoder();
    public static final Decoder DECODER = Base64.getDecoder();

    public static boolean check(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    public static String encodeBasicToken(String username, String password) {
        return "Basic " + encode(username + ":" + password);
    }

    public static String encode(String src) {
        return ENCODER.encodeToString(src.getBytes());
    }

    public static String encode(byte[] src) {
        return ENCODER.encodeToString(src);
    }

    public static String decode(byte[] src) {
        return new String(DECODER.decode(src));
    }

    public static String decode(String src) {
        return new String(DECODER.decode(src));
    }

    public static void main(String[] args) {
        String str = "welkin:123456";
        System.out.println(check(str));
        String encode = encode(str);
        System.out.println(encode);
        System.out.println(check(encode));
        System.out.println(decode(encode));
    }
}
