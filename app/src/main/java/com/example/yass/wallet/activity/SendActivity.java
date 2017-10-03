package com.example.yass.wallet.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yass.wallet.R;
import com.example.yass.wallet.bip32.ValidationException;
import com.example.yass.wallet.data.SharedHelper;
import com.example.yass.wallet.model.Utxo;
import com.example.yass.wallet.service.UpdateService;
import com.example.yass.wallet.utilities.Bip32;
import com.example.yass.wallet.utilities.Network;
import com.google.common.io.BaseEncoding;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.core.UTXOProvider;
import org.bitcoinj.core.UTXOProviderException;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yass on 10/2/17.
 */

public class SendActivity extends AppCompatActivity {

    private static final String TAG = "SendActivity ";
    //edit_address edit_amount
    private EditText addresEdit;
    private EditText amountEdit;
    private EditText feeEdit;
    private Button sendButton;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        new GetUTXO().execute("");

        addresEdit = (EditText) findViewById(R.id.edit_address);
        amountEdit = (EditText) findViewById(R.id.edit_amount);
        feeEdit = (EditText) findViewById(R.id.edit_fee);
        sendButton = (Button) findViewById(R.id.buttonSend);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendTx().execute(addresEdit.getText().toString() + "," +
                        amountEdit.getText().toString() + "," +
                        feeEdit.getText().toString());
            }
        });

        addresEdit.setText(getIntent().getStringExtra("addr"));
    }

    private class SendTx extends AsyncTask<String, Void, String> {
        Wallet walletAsync = null;
        List<Utxo> utxos = new ArrayList<>();
        JSONObject result = null;
        String resultStr = null;
        SendRequest sendRequest = null;
        Bip32 bip32 = new Bip32(SharedHelper.getInstance().getMnemonicsWords());

        @Override
        protected String doInBackground(String... strings) {
            long creationtime = 1409478661L;
            utxos = SharedHelper.getInstance().getUTXOBitcoin();
            byte[] seed = MnemonicCode.toSeed(Arrays.asList(
                    SharedHelper.getInstance().getMnemonicsWords().split(" ")), "");
            DeterministicSeed deterministicSeed = new DeterministicSeed(seed, new ArrayList<String>(), creationtime);
            KeyChainGroup keyChainGroupA = new KeyChainGroup(MainNetParams.get(), deterministicSeed);
            walletAsync = new Wallet(MainNetParams.get(), keyChainGroupA);
            walletAsync.setUTXOProvider(new UTXOProvider() {
                @Override
                public List<UTXO> getOpenTransactionOutputs(List<Address> addresses) throws UTXOProviderException {
                    List<UTXO> utxoList = new ArrayList<UTXO>();
                    // List<Utxo> utxos = SharedPrefHelper.getInstance(getApplication()).getUTXOBitcoin();

                    for (Utxo utxo : utxos) {
                        //     Log.i("utxo script ", utxo.getScriptPubKey());
                        //     Log.i("utxo script ", String.valueOf(utxo.getScriptPubKey().getBytes().length));
                        Script script = new Script(BaseEncoding.base16().decode(utxo.getScriptPubKey().toUpperCase()));
                        utxoList.add(new UTXO(Sha256Hash.wrap(utxo.getTxid()),
                                utxo.getVout(),
                                Coin.valueOf(utxo.getSatoshis()),
                                getChainHeadHeight(),
                                false,
                                script,
                                utxo.getAddress()));
                    }
                    return utxoList;
                }

                @Override
                public int getChainHeadHeight() throws UTXOProviderException {
                    // TODO: 10/3/17 добавить запрос
                    String count;
                    JSONObject object = null;

                    try {
                        count = Network.getResponseFromHttpUrlGet(Network.buildUrlGetBlockCount());
                        object = new JSONObject(count);
                        return object.getInt("height");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }

                @Override
                public NetworkParameters getParams() {
                    return MainNetParams.get();
                }
            });
            String addr = strings[0].split(",")[0];
            long amount = getCoin(strings[0].split(",")[1]);
            long fee = Math.round(Double.parseDouble(strings[0].split(",")[2]));

            Log.i(TAG, "add " + addr + "a " + amount + "f " + fee);
            try {
                sendRequest = SendRequest.to(Address.fromBase58(MainNetParams.get(), addr
                ), Coin.valueOf(amount));
                sendRequest.feePerKb = Coin.valueOf(fee);

                sendRequest.changeAddress = Address.fromBase58(MainNetParams.get(), bip32.getExternalAdresses(
                        SharedHelper.getInstance().getLastInUsedAddressIndexBIT(), SharedHelper.getInstance().getLastInUsedAddressIndexBIT() + 1
                ));
            } catch (ValidationException e) {
                e.printStackTrace();
            }


            try {
                walletAsync.completeTx(sendRequest);
            } catch (InsufficientMoneyException e) {
                e.printStackTrace();
            }

            walletAsync.signTransaction(sendRequest);

            Log.i(TAG, "Размер транзакцииbbbb " + String.valueOf(sendRequest.tx.getMessageSize()));

            byte[] b = sendRequest.tx.unsafeBitcoinSerialize();

            Log.i("HexTX our", BaseEncoding.base16().encode(b));

            Log.i("BALANCE", walletAsync.getBalance().toString());
            resultStr = Network.sendTx(BaseEncoding.base16().encode(b));
            // TODO: 10/3/17 добавить запрос отправки
            //(BaseEncoding.base16().encode(b)
            if (resultStr != null) {
                //  try {
                //  if (!result.getString("error").equals("")) {

                if (!resultStr.contains("error")) {
                    try {
                        SharedHelper.getInstance().saveCurrentReceiveAddressBIT(bip32.getExternalAdresses(
                                SharedHelper.getInstance().getLastInUsedAddressIndexBIT(), SharedHelper.getInstance().getLastInUsedAddressIndexBIT() + 1
                        ));
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                    ;

                    SharedHelper.getInstance().saveLastInUsedAddressIndexBIT(SharedHelper.getInstance().getLastInUsedAddressIndexBIT() + 1);

                    Intent intentService = UpdateService.newIntent(getApplication());
                    getApplication().startService(intentService);
                    return "ok";
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("ok")) {
                handler.post(new Runnable() {
                    public void run() {
                        walletAsync = null;
                        Toast.makeText(getBaseContext(), "Your money sent'", Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(SendActivity.this, MainActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }
    }

    private class GetUTXO extends AsyncTask<String, Void, String> {
        private static final String TAG = "GetUTXO";

        List<Utxo> utxos = new ArrayList<>();

        @Override
        protected String doInBackground(String[] urls) {
            String addrs = "";

            addrs = SharedHelper.getInstance().getCurrentReceiveAddressBIT();

            JSONArray s = null;

            s = Network.getUTXO(addrs);

            if (s.toString().equals("[]") || s.toString().equals("")) {
                Log.i(TAG, "empty wallet bitcoin");
            } else {

                try {

                    Log.i(TAG, s.toString());

                    JSONArray jsonArray = new JSONArray(s.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Utxo utxo = Utxo.fromJson(jsonArray.get(i).toString());
                        utxos.add(utxo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedHelper.getInstance().saveUTXOBitcoin(utxos);
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TAG, "Кол-во utxo " + SharedHelper.getInstance().getUTXOBitcoin());
        }
    }

    private long getCoin(String amount) {
        long answer = 0;

        if (amount.contains(".")) {
            answer = Long.parseLong(String.valueOf((long) (Double.parseDouble(amount) * Double.parseDouble("100000000"))));
            Log.i("AMOUNT", String.valueOf(answer));
        } else {
            answer = Long.parseLong(amount);
            Log.i("AMOUNT", String.valueOf(answer));
        }
        if (Double.parseDouble(String.valueOf(answer)) < 0.00040000) {
            return 0;
        }
        Log.i("AMOUNT", String.valueOf(answer));
        return answer;
    }
}
