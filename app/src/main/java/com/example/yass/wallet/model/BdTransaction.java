package com.example.yass.wallet.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;
import java.util.List;

/**
 * Created by yass on 10/2/17.
 */

public class BdTransaction {
    String time;
    String amount;

    private static transient Gson gson = new Gson();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static List<BdTransaction> fromJsonCollection(String treeCollection){
        return gson.fromJson(treeCollection, new TypeToken<Collection<BdTransaction>>() {
        }.getType());
    }

    public static String toJsonArray(Collection<BdTransaction> utxo){
        return gson.toJson(utxo);
    }
}
