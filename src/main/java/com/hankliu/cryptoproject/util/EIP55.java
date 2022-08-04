package com.hankliu.cryptoproject.util;

import org.bitcoinj.core.ECKey;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.math.ec.ECPoint;

/**
 *
 * @author Hank Liu <kccc418@gmail.com>
 */
public class EIP55 {

    public static String encode(String publicKey) {
        ECKey ecKey = ECKey.fromPublicOnly(Hex.decode(publicKey));
        ECPoint ecp = ecKey.getPubKeyPoint();

        StringBuilder sb = new StringBuilder();
        sb.append(Padding.addZeroForNum(ecp.getRawXCoord().toString(), 64));
        sb.append(Padding.addZeroForNum(ecp.getRawYCoord().toString(), 64));

        String publicKeyHash = ShaUtil.Keccak256(Hex.decode(sb.toString()));
        String address = publicKeyHash.substring(publicKeyHash.length() - 40, publicKeyHash.length()).toLowerCase();

        String hashed_address = ShaUtil.Keccak256(address.getBytes());
        byte[] byte_hashed_address = Hex.decode(hashed_address);
        String encodeAddress = "";
        for (int i = 0; i < address.length(); i++) {
            char c = address.charAt(i);
            if (c >= 'a' && c <= 'f') {
                if (checkNibble(byte_hashed_address, i)) {
                    encodeAddress += String.valueOf(c).toUpperCase();
                } else {
                    encodeAddress += String.valueOf(c);
                }
            } else {
                encodeAddress += c;
            }
        }
        return "0x" + encodeAddress;
    }

    private static boolean checkNibble(byte[] byte_hashed_address, int index) {
        byte temp = byte_hashed_address[index / 2];
        int nibble;
        if (index % 2 == 0) {
            nibble = (temp >> 4) & 0x0f;
        } else {
            nibble = temp & 0x0f;
        }
        return nibble > 7;
    }
}
