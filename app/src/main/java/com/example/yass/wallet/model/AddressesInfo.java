package com.example.yass.wallet.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by yass on 9/6/17.
 */

public class AddressesInfo {
    private Float balance;
    private String addrStr;
    private Float totalReceived;
    private Integer unconfirmedTxApperances;
    private Integer unconfirmedBalanceSat;
    private Integer totalSentSat;
    private Integer totalReceivedSat;
    private Float totalSent;
    private Integer balanceSat;
    private Double unconfirmedBalance;
    private Integer txApperances;

    private static transient Gson gson = new Gson();

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public String getAddrStr() {
        return addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    public Float getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(Float totalReceived) {
        this.totalReceived = totalReceived;
    }

    public Integer getUnconfirmedTxApperances() {
        return unconfirmedTxApperances;
    }

    public void setUnconfirmedTxApperances(Integer unconfirmedTxApperances) {
        this.unconfirmedTxApperances = unconfirmedTxApperances;
    }

    public Integer getUnconfirmedBalanceSat() {
        return unconfirmedBalanceSat;
    }

    public void setUnconfirmedBalanceSat(Integer unconfirmedBalanceSat) {
        this.unconfirmedBalanceSat = unconfirmedBalanceSat;
    }

    public Integer getTotalSentSat() {
        return totalSentSat;
    }

    public void setTotalSentSat(Integer totalSentSat) {
        this.totalSentSat = totalSentSat;
    }

    public Integer getTotalReceivedSat() {
        return totalReceivedSat;
    }

    public void setTotalReceivedSat(Integer totalReceivedSat) {
        this.totalReceivedSat = totalReceivedSat;
    }

    public Float getTotalSent() {
        return totalSent;
    }

    public void setTotalSent(Float totalSent) {
        this.totalSent = totalSent;
    }

    public Integer getBalanceSat() {
        return balanceSat;
    }

    public void setBalanceSat(Integer balanceSat) {
        this.balanceSat = balanceSat;
    }

    public Double getUnconfirmedBalance() {
        return unconfirmedBalance;
    }

    public void setUnconfirmedBalance(Double unconfirmedBalance) {
        this.unconfirmedBalance = unconfirmedBalance;
    }

    public Integer getTxApperances() {
        return txApperances;
    }

    public void setTxApperances(Integer txApperances) {
        this.txApperances = txApperances;
    }


    public static AddressesInfo fromJson(String addr){
        return gson.fromJson(addr, new TypeToken<AddressesInfo>() {
        }.getType());
    }

    public static String toJsonArray(AddressesInfo addressInfo){
        return gson.toJson(addressInfo);
    }
}
