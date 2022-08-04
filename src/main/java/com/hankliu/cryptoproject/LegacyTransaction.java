/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hankliu.cryptoproject;

import com.hankliu.cryptoproject.util.EIP55;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Sign;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/**
 *
 * @author hank.liu
 */
public class LegacyTransaction implements Transaction {

    BigInteger nonce;
    BigInteger gasPrice;
    BigInteger gasLimit;
    String to;
    BigInteger value;
    String data;
    BigInteger v;
    String r = "";
    String s = "";

    public void setR(String r) {
        this.r = r;
    }

    public void setS(String s) {
        this.s = s;
    }

    public LegacyTransaction(Ethereum eth, String fromPublicKey, String to, double value, String data) {
        System.out.println("From:");
        String from = EIP55.encode(fromPublicKey);
        System.out.println(from + System.getProperty("line.separator"));

        eth.getBalance(from);

        this.nonce = eth.getNonce(from);

        this.gasPrice = eth.getGasPrice();

        this.to = to;

        this.value = Convert.toWei(new BigDecimal(value), Convert.Unit.ETHER).toBigInteger();
        System.out.println("value: " + System.getProperty("line.separator") + value + System.getProperty("line.separator"));

        this.data = data;

        this.gasLimit = eth.getGasLimit(from, this.to, this.gasPrice, this.value, this.data);

        this.v = eth.getChain().getChainId();
    }

    @Override
    public String encodeRlp() {
        byte[] rlp = RlpEncoder.encode(new RlpList(
                RlpString.create(nonce),
                RlpString.create(gasPrice),
                RlpString.create(gasLimit),
                RlpString.create(Numeric.hexStringToByteArray(to)),
                RlpString.create(value),
                RlpString.create(Numeric.hexStringToByteArray(data)),
                RlpString.create(v),
                RlpString.create(Numeric.hexStringToByteArray(r)),
                RlpString.create(Numeric.hexStringToByteArray(s))
        ));
        return Hex.toHexString(rlp);
    }

    @Override
    public String toSignTransaction(Sign.SignatureData sign) {
        System.out.println("R: " + Hex.toHexString(sign.getR()));
        System.out.println("S: " + Hex.toHexString(sign.getS()));
        System.out.println("V: " + Hex.toHexString(sign.getV()));

        // composeSignedTransacton
        // v = v(27 or 28) + chainId * 2 + 8;
        this.v = new BigInteger(sign.getV()).add(this.v.multiply(new BigInteger("2"))).add(new BigInteger("8"));
        this.s = Hex.toHexString(sign.getS());
        this.r = Hex.toHexString(sign.getR());

        return this.encodeRlp();
    }

}
