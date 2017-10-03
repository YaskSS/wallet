package com.example.yass.wallet.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;
import java.util.List;

/**
 * Created by yass on 9/4/17.
 */

public class Utxo {

    private String address;
    private String txid;
    private Integer vout;
    private String scriptPubKey;
    private Float amount;
    private Integer satoshis;
    private Integer height;
    private Integer confirmations;

    private static transient Gson gson = new Gson();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public Integer getVout() {
        return vout;
    }

    public void setVout(Integer vout) {
        this.vout = vout;
    }

    public String getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(String scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Integer getSatoshis() {
        return satoshis;
    }

    public void setSatoshis(Integer satoshis) {
        this.satoshis = satoshis;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }

    @Override
    public String toString() {
        return address + " " + txid +" " + vout +" " +scriptPubKey +" " +amount+" " +satoshis+" " +height+" " +confirmations;
    }

    public static Utxo fromJson(String addr){
        return gson.fromJson(addr, new TypeToken<Utxo>() {
        }.getType());
    }

    public static List<Utxo> fromJsonCollection(String treeCollection){
        return gson.fromJson(treeCollection, new TypeToken<Collection<Utxo>>() {
        }.getType());
    }

    public static String toJsonArray(Collection<Utxo> utxo){
        return gson.toJson(utxo);
    }

}
