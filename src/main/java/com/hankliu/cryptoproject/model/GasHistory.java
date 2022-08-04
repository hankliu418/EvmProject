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
public class GasHistory {

    private String jsonrpc;
    private int id;
    private Result result;

    public Result getResult() {
        return result;
    }

    public class Result {

        private String[] baseFeePerGas;
        private double[] gasUsedRatio;
        private String oldestBlock;
        private String[][] reward;

        public String[] getBaseFeePerGas() {
            return baseFeePerGas;
        }

        public double[] getGasUsedRatio() {
            return gasUsedRatio;
        }

        public String getOldestBlock() {
            return oldestBlock;
        }

        public String[][] getReward() {
            return reward;
        }
    }
}
