package com.example.yass.wallet.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yass.wallet.R;
import com.example.yass.wallet.data.SharedHelper;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.Wallet;

/**
 * Created by yass on 10/2/17.
 */

public class NewWalletActivity extends AppCompatActivity {

    private Button showButton;
    private Button doneButton;
    private ProgressBar progressBar;
    private TextView phraseTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wallet);

        showButton = (Button) findViewById(R.id.button_show_new_wallet);
        showButton.setOnClickListener(onClickListener);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_show_new_wallet);
        phraseTextView = (TextView) findViewById(R.id.textView_phrase);
        doneButton = (Button) findViewById(R.id.button_done_new_wallet);


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   Intent intent = new Intent(NewWalletActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new CreateMultiWallet().execute();
        }
    };

    class CreateMultiWallet extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            showButton.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            Wallet wallet = new Wallet(MainNetParams.get());

            SharedHelper.getInstance().saveMnemonic(toWords(wallet.getKeyChainSeed().toString().split(" ")));
            SharedHelper.getInstance().saveCurrentReceiveAddressBIT(wallet.currentReceiveAddress().toString());
            return toWords(wallet.getKeyChainSeed().toString().split(" "));
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            phraseTextView.setText(aVoid);
            doneButton.setVisibility(View.VISIBLE);
        }
    }

    public static String toWords(String[] wordsArr) {
        StringBuilder resultWords = new StringBuilder();
        for (int i = 2; i < wordsArr.length; i++) {
            //we need save this wordsBit
            resultWords.append(wordsArr[i] + " ");
        }
        return resultWords.toString();
    }
}
