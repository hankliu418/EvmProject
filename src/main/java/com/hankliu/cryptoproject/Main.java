/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hankliu.cryptoproject;

import com.google.common.collect.ImmutableList;
import com.hankliu.cryptoproject.Ethereum.ChainList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;

/**
 *
 * @author Hank Liu <kccc418@gmail.com>
 */
public class Main {

    private static String mnemonic;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws org.bitcoinj.wallet.UnreadableWalletException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, UnreadableWalletException {
        // TODO code application logic here
        File file = new File("");
        String projectPath = file.getAbsolutePath();
        Properties properties = new Properties();
        String configFile = projectPath + "/config/config.properties";
        properties.load(new FileInputStream(configFile));
        mnemonic = properties.getProperty("mnemonic");

        ChainList chain = ChainList.ETH;

        String apiKey = properties.getProperty(chain.toString() + "_API_KEY", "");
        System.out.println("apiKey: " + apiKey);
        Ethereum eth = new Ethereum(chain, apiKey);

        int index = 2;

        String to = "0xc30141b657f4216252dc59af2e7cdb9d8792e1b0";

        double value = 0.01;

        String data = "";

        String transaction = signTransaction(eth, index, to, value, data);
        System.out.println("Signed transaction: " + transaction);

        eth.sendTransaction("0x" + transaction, chain.getEthscanUrl());
    }

    private static String signTransaction(Ethereum chain, int index, String to, double value, String data) throws UnreadableWalletException {
        DeterministicSeed seed = new DeterministicSeed(mnemonic, null, "", new Date().getTime());
        DeterministicKeyChain keyChain = DeterministicKeyChain.builder().seed(seed).build();

        DeterministicKey indexKey = keyChain.getKeyByPath(ImmutableList.of(
                new ChildNumber(44, true),
                new ChildNumber(60, true),
                ChildNumber.ZERO_HARDENED,
                ChildNumber.ZERO,
                new ChildNumber(index, false)),
                true);

//        Transaction trans = new LegacyTransaction(chain, indexKey.getPublicKeyAsHex(), to, value, data);
        Transaction trans = new EIP1559Transaction(chain, indexKey.getPublicKeyAsHex(), to, value, data);

        String signData = trans.encodeRlp();
        System.out.println("Unsigned transaction: " + signData);
        SignatureData sign = Sign.signMessage(Hex.decode(signData), new ECKeyPair(indexKey.getPrivKey(), Sign.publicKeyFromPrivate(indexKey.getPrivKey())));
        return trans.toSignTransaction(sign);
    }
}
