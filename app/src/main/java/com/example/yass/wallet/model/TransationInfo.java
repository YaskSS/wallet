package com.example.yass.wallet.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by yass on 9/6/17.
 */

public class TransationInfo {
    private Integer confirmations;
    private Integer blockheight;
    private String direction;
    private Integer blocktime;
    private String txid;
    private List<Vin> vin = null;
    private List<Vout> vout = null;

    private static transient Gson gson = new Gson();

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }

    public Integer getBlockheight() {
        return blockheight;
    }

    public void setBlockheight(Integer blockheight) {
        this.blockheight = blockheight;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getBlocktime() {
        return blocktime;
    }

    public void setBlocktime(Integer blocktime) {
        this.blocktime = blocktime;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public List<Vin> getVin() {
        return vin;
    }

    public void setVin(List<Vin> vin) {
        this.vin = vin;
    }

    public List<Vout> getVout() {
        return vout;
    }

    public void setVout(List<Vout> vout) {
        this.vout = vout;
    }


    public static TransationInfo fromJson(String trans){
        return gson.fromJson(trans, new TypeToken<TransationInfo>() {
        }.getType());
    }

    public static String toJsonArray(TransationInfo transationInfo){
        return gson.toJson(transationInfo);
    }
}
