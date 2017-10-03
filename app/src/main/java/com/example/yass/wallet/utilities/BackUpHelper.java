package com.example.yass.wallet.utilities;

import android.content.ContentValues;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.yass.wallet.R;
import com.example.yass.wallet.bip32.ValidationException;
import com.example.yass.wallet.data.SharedHelper;
import com.example.yass.wallet.model.AddressesInfo;
import com.example.yass.wallet.model.BdTransaction;
import com.example.yass.wallet.model.TransationInfo;
import com.example.yass.wallet.model.Vin;
import com.example.yass.wallet.model.Vout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 03.10.2017.
 */

public class BackUpHelper {


    private static final String TAG = "BackUpHelper";

    public static String checkAddresses(JSONObject jsonObject, Bip32 bip32, int iteration) {
        List<BdTransaction> bdTransactions = new ArrayList<>();
        JSONArray transactionsJArr = null;
        JSONArray addrsInfoJArr = null;
        String totalBalance = "";
        String balance = "";
        ContentValues cv = new ContentValues();
        int lastIndex = 0;
        List<String> usedAddresses = new ArrayList<>();
        int nextAddresses = 0;

        try {
            transactionsJArr = jsonObject.getJSONArray("transactions");
            addrsInfoJArr = jsonObject.getJSONArray("addrs_info");
            Log.i(TAG, "transactionsJArr" + transactionsJArr.toString());
            Log.i(TAG, "addrsInfoJArr" + addrsInfoJArr.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            totalBalance = jsonObject.getString("total_balance_sat");
            Log.i(TAG, totalBalance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (transactionsJArr != null) {
            if (!(transactionsJArr.length() == 0) || !(transactionsJArr.toString().equals("[]"))) {
                for (int i = 0; i < addrsInfoJArr.length(); i++) {

                    AddressesInfo addJO = null;
                    try {
                        addJO = AddressesInfo.fromJson(addrsInfoJArr.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (addJO.getTxApperances() != 0 && addJO.getTotalReceivedSat() != 0 && addJO.getTotalSentSat() != 0) {
                        usedAddresses.add(addJO.getAddrStr());

                    }
                    if (addJO.getTotalReceivedSat() != 0 && addJO.getTotalSentSat() == 0) {
                        usedAddresses.add(addJO.getAddrStr());
                        lastIndex = i;
                    }
                    if (addJO.getTotalReceivedSat() != 0 && addJO.getBalanceSat() == 0) {
                        Log.i(TAG, "C I  " + i);
                        nextAddresses = i;
                    }
                    if (addJO.getUnconfirmedBalanceSat() != 0 && addJO.getUnconfirmedTxApperances() != 0) {
                        usedAddresses.add(addJO.getAddrStr());
                        lastIndex = i;
                    }
                }

                try {
                    AddressesInfo addressesInfo = AddressesInfo.fromJson(addrsInfoJArr.getString(lastIndex));

                    if (addressesInfo.getBalance() != 0) {
                        balance = String.valueOf(addressesInfo.getBalance());
                        Log.i(TAG, "BA:A" + balance);
                    } else {
                        balance = String.valueOf(addressesInfo.getUnconfirmedBalance());
                        Log.i(TAG, "BA:A" + balance);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lastIndex = iteration + lastIndex;
                Log.i(TAG, "lastIndex" + lastIndex);
            } else {
                try {

                    Log.i("BIT", balance);
                    SharedHelper.getInstance().saveCurrentReceiveAddressBIT(bip32.getExternalAdresses(0, 1));

                    return "save";
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }

            // if (!totalBalance.equals("0")) {

            Log.i(TAG, "save bit" + balance);
            SharedHelper.getInstance().saveTotalBalance(balance);

            // }
            for (int t = 0; t < transactionsJArr.length(); t++) {
                BdTransaction bdTransaction = new BdTransaction();
                TransationInfo transationInfo = null;
                List<Vout> vouts = null;
                List<Vin> vins = null;
                try {
                    transationInfo = TransationInfo.fromJson(transactionsJArr.getString(t));
                    vouts = transationInfo.getVout();
                    vins = transationInfo.getVin();
                    System.out.println("LINE VOUTS: " + vouts.size());
                    System.out.println("LINE VIN: " + vins.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (transationInfo.getDirection().equals("in")) {
                    for (int k = 0; k < usedAddresses.size(); k++) {
                        for (int vo = 0; vo < vouts.size(); vo++) {
                            if (usedAddresses.get(k).equals(vouts.get(vo).getAddresses().get(0))) {
                                if (transationInfo.getBlockheight() != -1) {
                                    Log.d(TAG, "getBlocktime: " + convertDate(transationInfo.getBlocktime()));
                                    bdTransaction.setTime(convertDate(transationInfo.getBlocktime()));
                                    //  cv.put(TransactionContract.TransactionEntry.TIME, convertDate(transationInfo.getBlocktime()));
                                    Log.i(TAG, "save to bd getBlocktime" + transationInfo.getBlocktime());
                                } else {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                                    String currentDateandTime = sdf.format(new Date());
                                    //   cv.put(TransactionContract.TransactionEntry.TIME, currentDateandTime);
                                    bdTransaction.setTime(currentDateandTime);
                                    Log.i(TAG, "save to bd currentDateandTime " + currentDateandTime);
                                }

                                // cv.put(TransactionContract.TransactionEntry.AMOUNT, "+ " + vouts.get(vo).getValue());
                                bdTransaction.setAmount(vouts.get(vo).getValue());
                                Log.d(TAG, "getValue: " + vouts.get(vo).getValue());

                                Log.i(TAG, " in" + cv.toString());

                            }
                        }
                    }
                } else if (transationInfo.getDirection().equals("out")) {
                    for (int k = 0; k < usedAddresses.size(); k++) {
                        for (int vi = 0; vi < vins.size(); vi++) {
                            if (usedAddresses.get(k).equals(vins.get(vi).getAddress())) {
                                Log.d(TAG, "getBlocktime: " + convertDate(transationInfo.getBlocktime()));
                                bdTransaction.setTime(convertDate(transationInfo.getBlocktime()));
                                //  cv.put(TransactionContract.TransactionEntry.TIME, convertDate(transationInfo.getBlocktime()));

                                if (usedAddresses.contains(vouts.get(0).getAddresses().get(0))) {
                                    Log.d(TAG, "getValue: " + vouts.get(1).getValue());
                                    bdTransaction.setAmount(vouts.get(1).getValue());
                                    //  cv.put(TransactionContract.TransactionEntry.AMOUNT, "- " + vouts.get(1).getValue());
                                } else if (usedAddresses.contains(vouts.get(1).getAddresses().get(0))) {
                                    bdTransaction.setAmount(vouts.get(0).getValue());
                                    Log.d(TAG, "getValue: " + vouts.get(0).getValue());
                                    //  cv.put(TransactionContract.TransactionEntry.AMOUNT, "- " + vouts.get(0).getValue());
                                } else {
                                    bdTransaction.setAmount(vouts.get(1).getValue());
                                    Log.d(TAG, "getValue: " + vouts.get(1).getValue());
                                    // cv.put(TransactionContract.TransactionEntry.AMOUNT, "- " + vouts.get(1).getValue());
                                }

                            }
                        }
                    }
                }
                bdTransactions.add(bdTransaction);
            }

            SharedHelper.getInstance().saveBdTransaction(bdTransactions);
            Log.i(TAG, "size" + String.valueOf(usedAddresses.size()));
            //  if (usedAddresses.size() == 20) return "again";

            try {
                SharedHelper.getInstance().saveLastInUsedAddressIndexBIT(lastIndex);
                SharedHelper.getInstance().saveCurrentReceiveAddressBIT(bip32.getExternalAdresses(lastIndex, ++lastIndex));
                SharedHelper.getInstance().saveLastInUsedAddressIndexBIT(lastIndex);
                if (SharedHelper.getInstance().getCurrentReceiveAddressBIT().toCharArray()[0] != '1') {

                    SharedHelper.getInstance().saveCurrentReceiveAddressBIT(
                            bip32.getExternalAdresses(SharedHelper.getInstance().getLastInUsedAddressIndexBIT(),
                                    SharedHelper.getInstance().getLastInUsedAddressIndexBIT() + 1));

                }


                Log.i(TAG, "nexA " + nextAddresses);
                if (nextAddresses == 19) {
                    return "again";
                } else {
                    return "save";
                }

                //  Log.i(TAG, "nexA " + nextAddresses);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            return "ok";
        }
        return "error";
    }

    public static String convertDate(long Time) {
        return DateFormat.format("dd/MM/yyyy hh:mm a", new Date(Time * 1000)).toString();
    }
}
