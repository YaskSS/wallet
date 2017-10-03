package com.example.yass.wallet.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.yass.wallet.App;
import com.example.yass.wallet.R;
import com.example.yass.wallet.activity.MainActivity;
import com.example.yass.wallet.bip32.ValidationException;
import com.example.yass.wallet.data.SharedHelper;
import com.example.yass.wallet.model.BdTransaction;
import com.example.yass.wallet.model.TransationInfo;
import com.example.yass.wallet.utilities.BackUpHelper;
import com.example.yass.wallet.utilities.Bip32;
import com.example.yass.wallet.utilities.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 03.10.2017.
 */

public class UpdateService extends IntentService {

    private Bip32 bip32 = new Bip32(SharedHelper.getInstance().getMnemonicsWords());
    String address = null;
    JSONArray transationInfoArr = null;
    JSONArray addrInfoArr = null;
    List<BdTransaction> bdTransactions = new ArrayList<>();
    List<BdTransaction> bdTransactions1 = new ArrayList<>();

    JSONObject jsonObject = null;

    public UpdateService() {
        super("UpdateService");
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, UpdateService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        bdTransactions1 = SharedHelper.getInstance().getBdTransaction();


        address = SharedHelper.getInstance().getCurrentReceiveAddressBIT();
        Log.i(TAG, "address " + address);

        jsonObject = Network.buildUrlAddressesInfoBTC(address);
        if (jsonObject == null) return;
        Log.i("UpdateService ", jsonObject.toString());

        try {
            transationInfoArr = jsonObject.getJSONArray("transactions");
            addrInfoArr = jsonObject.getJSONArray("addrs_info");
            //get addresses info
            //  addressesInfo = jsonObject.getString("unconfirmedBalance");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (transationInfoArr == null) return;

        for (int t = 0; t < transationInfoArr.length(); t++) {
            TransationInfo transationInfo = null;
            try {
                transationInfo = TransationInfo.fromJson(transationInfoArr.getString(t));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("UpdateService ", transationInfo.toString());
            BdTransaction transation = new BdTransaction();

            transation.setTime(BackUpHelper.convertDate(transationInfo.getBlocktime()));
            transation.setAmount(transationInfo.getVout().get(0).getValue());
            Log.i("UpdateService ", transation.toString());
            bdTransactions.add(transation);

        }

        for (BdTransaction trans : bdTransactions) {
            Log.i("UpdateService1  ", trans.getAmount());
            for (int i = 0; i < bdTransactions1.size(); i++) {
                Log.i("UpdateService2 ", bdTransactions1.get(i).getAmount());
                if (bdTransactions1.get(i).getAmount().equals(trans.getAmount())) {
                    //   bdTransactions.remove(trans);
                    Log.i("UpdateService2 ", bdTransactions1.get(i).getAmount());
                    bdTransactions.remove(trans);
                }
            }
        }
        bdTransactions1.addAll(bdTransactions);
        SharedHelper.getInstance().saveBdTransaction(bdTransactions1);
        Intent intents = new Intent(MainActivity.BROADCAST_ACTION);
        App.getContext().sendBroadcast(intents);
    }
}
