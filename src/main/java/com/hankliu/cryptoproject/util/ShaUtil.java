/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hankliu.cryptoproject.util;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author Hank Liu <kccc418@gmail.com>
 */
public class ShaUtil {

    public final static String Keccak256(byte[] input) {
        Keccak.Digest256 digest = new Keccak.Digest256();
        byte[] messageDigest = digest.digest(input);
        return Hex.toHexString(messageDigest);
    }
}
