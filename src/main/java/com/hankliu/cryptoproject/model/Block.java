/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hankliu.cryptoproject.model;

/**
 *
 * @author Hank Liu <kccc418@gmail.com>
 */
public class Block {

    private String jsonrpc;
    private int id;
    private Result result;

    public Result getResult() {
        return result;
    }

    public static class Result {
        private String baseFeePerGas;
        private String difficulty;
        private String gasUsed;
        private String number;

        public String getBaseFeePerGas() {
            return baseFeePerGas;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public String getGasUsed() {
            return gasUsed;
        }
        
        public String getNumber() {
            return number;
        }
        
    }
}
