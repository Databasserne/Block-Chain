package com.databasserne.controller.security;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;

public class Sha3 {

    private static Size DEFAULT = Size.S224;

    public static String encode(String s) throws Exception {
        if(s == null || s.equals("")) throw new Exception("Bad Request");
        SHA3.DigestSHA3 md = new SHA3.DigestSHA3(DEFAULT.getValue());

        try {
            md.update(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }

        byte[] digest = md.digest();
        return encodeToString(digest);
    }

    private static String encodeToString(byte[] bytes) {
        return Hex.toHexString(bytes);
    }

    protected enum Size {
        S224(224),
        S256(256),
        S384(384),
        S512(512);

        int bits = 0;

        Size(int bits) {
            this.bits = bits;
        }

        public int getValue() {
            return this.bits;
        }
    }
}
