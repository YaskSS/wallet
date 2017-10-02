package com.example.yass.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.yass.wallet.R;
import com.example.yass.wallet.data.SharedHelper;

/**
 * Created by yass on 10/3/17.
 */

public class SpleshActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);

        if (SharedHelper.getInstance().getMnemonicsWords().equals("")) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
