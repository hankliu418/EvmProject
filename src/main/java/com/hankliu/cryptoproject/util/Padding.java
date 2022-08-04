/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hankliu.cryptoproject.util;

/**
 *
 * @author Hank Liu <kccc418@gmail.com>
 */
public class Padding {

    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuilder sb = new StringBuilder();
                sb.append("0").append(str);// 左補0
                // sb.append(str).append("0");//右補0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

}
