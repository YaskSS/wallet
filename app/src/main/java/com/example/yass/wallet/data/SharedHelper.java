package com.example.yass.wallet.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.yass.wallet.App;
import com.example.yass.wallet.model.BdTransaction;
import com.example.yass.wallet.model.Utxo;

import java.util.Collection;
import java.util.List;

/**
 * Created by yass on 10/2/17.
 */

public class SharedHelper {

    public static final String APP_PREFERENCES = "application";
    private static final String MNEMONIC = "mnemonic";
    private static final String MNEMONIC_DEFAULT = "";
    private static final String CURRENT_ADDRESS_B = "addressBit";
    private static final String DEFAULT_CURRENT_ADDRESS = "ooo";
    private static final String AMOUNT = "amount";
    private static final String AMOUNT_DEFAULT = "0.00";
    private static final String INDEX = "index";
    private static final int INDEX_DEFAULT = 0;
    private static final String UTXO = "utxo";
    private static final String UTXO_DEFAULT = "";
    private static final String TX = "tx";
    private static final String TX_DEFAULT = "";
    private SharedPreferences sharedPreferences;
    private static SharedHelper sharedPrefHelper;

    public SharedHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static SharedHelper getInstance() {
        Context context = App.getContext();
        if (sharedPrefHelper == null) {
            sharedPrefHelper = new SharedHelper(context);
        }
        return sharedPrefHelper;
    }

    public void saveMnemonic(String mnemonic) {
        sharedPreferences.edit().putString(MNEMONIC, mnemonic).apply();
    }

    public String getMnemonicsWords() {
        return sharedPreferences.getString(MNEMONIC, MNEMONIC_DEFAULT);
    }

    public void saveCurrentReceiveAddressBIT(String address) {
        sharedPreferences.edit().putString(CURRENT_ADDRESS_B, address).apply();
    }

    public String getCurrentReceiveAddressBIT() {
        return sharedPreferences.getString(CURRENT_ADDRESS_B, DEFAULT_CURRENT_ADDRESS);
    }

    public void saveTotalBalance(String amount) {
        sharedPreferences.edit().putString(AMOUNT, amount).apply();
    }

    public String getTotalBalance() {
        return sharedPreferences.getString(AMOUNT, AMOUNT_DEFAULT);
    }

    public void saveUTXOBitcoin(Collection<Utxo> utxo) {
        sharedPreferences.edit().putString(UTXO, Utxo.toJsonArray(utxo)).apply();
    }

    public List<Utxo> getUTXOBitcoin() {
        return Utxo.fromJsonCollection(sharedPreferences.getString(UTXO, UTXO_DEFAULT));
    }

    public void saveBdTransaction(Collection<BdTransaction> utxo) {
        sharedPreferences.edit().putString(TX, "").apply();
        sharedPreferences.edit().putString(TX, BdTransaction.toJsonArray(utxo)).apply();
    }

    public List<BdTransaction> getBdTransaction() {
        return BdTransaction.fromJsonCollection(sharedPreferences.getString(TX, TX_DEFAULT));
    }

    public void saveLastInUsedAddressIndexBIT(int index) {
        sharedPreferences.edit().putInt(INDEX, index).apply();
    }

    public int getLastInUsedAddressIndexBIT() {
        return sharedPreferences.getInt(INDEX, INDEX_DEFAULT);
    }
}
