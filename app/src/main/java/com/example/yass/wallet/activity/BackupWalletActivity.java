package com.example.yass.wallet.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.yass.wallet.R;
import com.example.yass.wallet.data.SharedHelper;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.Wallet;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by yass on 10/2/17.
 */

public class BackupWalletActivity extends AppCompatActivity {

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
            SharedHelper.getInstance().saveCurrentReceiveAddressBIT(walletAsync.currentReceiveAddress().toString());
            return NewWalletActivity.toWords(walletAsync.getKeyChainSeed().toString().split(" "));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            if (!s.equals("")) {
                SharedHelper.getInstance().saveMnemonic(s);
                startActivity(new Intent(BackupWalletActivity.this, MainActivity.class).
                        addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)));
            }
        }
    }
}
