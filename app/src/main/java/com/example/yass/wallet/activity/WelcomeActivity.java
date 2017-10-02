package com.example.yass.wallet.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.yass.wallet.R;

/**
 * Created by yass on 10/2/17.
 */

public class WelcomeActivity extends AppCompatActivity {

    private TextView newWallet;
    private TextView restoreWallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    private void initView(){
        newWallet = (TextView) findViewById(R.id.newWallet);
        restoreWallet = (TextView) findViewById(R.id.restoreWallet);

        newWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, NewWalletActivity.class));
            }
        });

        restoreWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, BackupWalletActivity.class));
            }
        });
    }

}
