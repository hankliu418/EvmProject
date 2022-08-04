/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hankliu.cryptoproject;

import org.web3j.crypto.Sign;

/**
 *
 * @author hank.liu
 */
public interface Transaction {

    public String encodeRlp();
    
    public String toSignTransaction(Sign.SignatureData sign);
}
