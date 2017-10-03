package com.example.yass.wallet.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yass.wallet.R;
import com.example.yass.wallet.bip32.ValidationException;
import com.example.yass.wallet.data.SharedHelper;
import com.example.yass.wallet.utilities.BackUpHelper;
import com.example.yass.wallet.utilities.Bip32;
import com.example.yass.wallet.utilities.Network;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.Wallet;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by yass on 10/2/17.
 */

public class BackupWalletActivity extends AppCompatActivity {

    private static final String TAG = "BackupWalletActivity";
    private EditText wordsEditText;
    private ProgressBar progressBar;
    private Button doneButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);


        wordsEditText = (EditText) findViewById(R.id.edit_words);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_backup);
        doneButton = (Button) findViewById(R.id.button_done_backup);
        doneButton.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String words = wordsEditText.getText().toString();
            new RestoreWallet().execute(words);
        }
    };

    private class RestoreWallet extends AsyncTask<String, Void, String> {
        Bip32 bip32;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            wordsEditText.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            long creationtime = 1409478661L;

            byte[] seed = MnemonicCode.toSeed(Arrays.asList(strings[0].split(" ")), "");
            DeterministicSeed deterministicSeed = new DeterministicSeed(seed, new ArrayList<String>(), creationtime);
            KeyChainGroup keyChainGroupB = new KeyChainGroup(MainNetParams.get(), deterministicSeed);
            Wallet walletAsync = new Wallet(MainNetParams.get(), keyChainGroupB);
            //  SharedHelper.getInstance().saveCurrentReceiveAddressBIT(walletAsync.currentReceiveAddress().toString());
            bip32 = new Bip32(strings[0]);
            String resBit = "";

            int indexStart = 0;
            int index = 0;
            while (resBit.equals("")) {
                index += 20;
                JSONObject result = null;
                try {
                    result = Network.buildUrlAddressesInfoBTC(bip32.getExternalAdresses(indexStart, index));
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
                if (result == null) {
                    strings[0] = "serverError";
                } else {
                    //resBit = TxParser.getLastUsedAddress(getApplication(), result, dataBase, bip32, "BIT");
                    resBit = BackUpHelper.checkAddresses(result, bip32, indexStart);

                       /* if (resBit.equals("")) {
                            indexStart = index;
                        } else if (resBit.equals("save")) {
                            SharedPrefHelper.getInstance(getApplication()).saveCurrentReceiveAddressBIT(bip32.getExternalAdresses(0, 1));
                        } else if (!resBit.equals("save")) {
                            SharedPrefHelper.getInstance(getApplication()).saveCurrentReceiveAddressBIT(resBit);
                        }*/
                    if (resBit.equals("again")) {
                        resBit = "";
                        indexStart = index;
                    }
                    if (resBit.equals("error")) {
                        //   Toast.makeText(getApplication(), "Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
            Log.i(TAG, walletAsync.getKeyChainSeed().toString());
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            Log.i(TAG, "onPostExecute " + s);
            if (!s.equals("")) {
                SharedHelper.getInstance().saveMnemonic(s);
                startActivity(new Intent(BackupWalletActivity.this, MainActivity.class).
                        addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)));
            }
        }
    }
}
