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
public class Gas {

    private int fast;
    private int fastest;
    private int safeLow;

    public int getFast() {
        return fast;
    }

    public void setFast(int fast) {
        this.fast = fast;
    }

    public int getFastest() {
        return fastest;
    }

    public void setFastest(int fastest) {
        this.fastest = fastest;
    }

    public int getSafeLow() {
        return safeLow;
    }

    public void setSafeLow(int safeLow) {
        this.safeLow = safeLow;
    }
}
