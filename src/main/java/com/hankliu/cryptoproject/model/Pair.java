/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hankliu.cryptoproject.model;

/**
 *
 * @author hank.liu
 * @param <T>
 * @param <U>
 */
public class Pair<T, U> {
    public T first;
    public U second;
    
    public Pair(T t, U u) {
        this.first = t;
        this.second = u;
    }
}
