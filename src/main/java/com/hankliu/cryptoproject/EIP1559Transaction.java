/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hankliu.cryptoproject;

import com.hankliu.cryptoproject.model.Pair;
import com.hankliu.cryptoproject.util.EIP55;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/**
 *
 * @author hank.liu
 */
public class EIP1559Transaction implements Transaction {

    BigInteger chainId;
    BigInteger nonce;
    BigInteger gasTipCap; // maxPriorityFee
    BigInteger gasFeeCap; // maxFee
    BigInteger gasLimit;
    String to;
    BigInteger value;
    String data;

    public EIP1559Transaction(Ethereum eth, String fromPublicKey, String to, double value, String data) {
        System.out.println("From:");
        String from = EIP55.encode(fromPublicKey);
        System.out.println(from + System.getProperty("line.separator"));

        eth.getBalance(from);

        this.chainId = eth.getChain().getChainId();

        this.nonce = eth.getNonce(from);

        eth.getBlockNumber();

        Pair<BigInteger, BigInteger> tipPair = eth.getFeeHistory();
        this.gasTipCap = tipPair.first;
        System.out.println("gasTipCap: " + gasTipCap);
        BigInteger baseFee = tipPair.second;
        System.out.println("baseFee: " + baseFee);

        eth.getMaxPriorityFeePerGas();

        this.gasFeeCap = baseFee.multiply(new BigInteger("2")).add(baseFee);

        this.to = to;

        this.value = Convert.toWei(new BigDecimal(value), Convert.Unit.ETHER).toBigInteger();
        System.out.println("value: " + System.getProperty("line.separator") + value + System.getProperty("line.separator"));

        this.data = data;

        this.gasLimit = eth.getGasLimit(from, this.to, this.gasFeeCap, this.value, this.data);
    }

    @Override
    public String encodeRlp() {
        byte[] rlp = RlpEncoder.encode(new RlpList(
                RlpString.create(chainId),
                RlpString.create(nonce),
                RlpString.create(gasTipCap),
                RlpString.create(gasFeeCap),
                RlpString.create(gasLimit),
                RlpString.create(Numeric.hexStringToByteArray(to)),
                RlpString.create(value),
                RlpString.create(Numeric.hexStringToByteArray(data)),
                new RlpList() // accessList
        ));
        return "02" + Hex.toHexString(rlp);
    }

    @Override
    public String toSignTransaction(SignatureData sign) {
        System.out.println("R: " + Hex.toHexString(sign.getR()));
        System.out.println("S: " + Hex.toHexString(sign.getS()));
        System.out.println("V: " + Hex.toHexString(sign.getV()));

        // composeSignedTransacton
        byte[] rlp = RlpEncoder.encode(new RlpList(
                RlpString.create(chainId),
                RlpString.create(nonce),
                RlpString.create(gasTipCap),
                RlpString.create(gasFeeCap),
                RlpString.create(gasLimit),
                RlpString.create(Numeric.hexStringToByteArray(to)),
                RlpString.create(value),
                RlpString.create(Numeric.hexStringToByteArray(data)),
                new RlpList(), // accessList
                RlpString.create(sign.getV()),
                RlpString.create(sign.getR()),
                RlpString.create(sign.getS())
        ));
        return "02" + Hex.toHexString(rlp);
    }
}
