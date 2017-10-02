package com.example.yass.wallet.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yass.wallet.R;
import com.example.yass.wallet.data.SharedHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Created by yass on 10/2/17.
 */

public class WordsActivity extends AppCompatActivity {
    private TextView wordsTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_words);

        wordsTextView = (TextView) findViewById(R.id.textView_show_words);


        wordsTextView.setText(SharedHelper.getInstance().getMnemonicsWords());
    }
}
