package com.example.yass.wallet.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.yass.wallet.App;

/**
 * Created by yass on 10/2/17.
 */

public class SharedHelper {

    public static final String APP_PREFERENCES = "application";
    private static final String MNEMONIC = "mnemonic";
    private static final String MNEMONIC_DEFAULT = "";
    private static final String CURRENT_ADDRESS_B = "addressBit";
    private static final String DEFAULT_CURRENT_ADDRESS = "ooo";
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
}
