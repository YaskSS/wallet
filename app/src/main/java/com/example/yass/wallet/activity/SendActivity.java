package com.example.yass.wallet.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yass.wallet.R;
import com.example.yass.wallet.data.SharedHelper;
import com.example.yass.wallet.model.Utxo;
import com.google.common.io.BaseEncoding;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.core.UTXOProvider;
import org.bitcoinj.core.UTXOProviderException;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

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

        @Override
        protected String doInBackground(String... strings) {
            long creationtime = 1409478661L;
            byte[] seed = MnemonicCode.toSeed(Arrays.asList(
                    SharedHelper.getInstance().getMnemonicsWords().split(" ")), "");
            DeterministicSeed deterministicSeed = new DeterministicSeed(seed, new ArrayList<String>(), creationtime);
            KeyChainGroup keyChainGroupA = new KeyChainGroup(MainNetParams.get(), deterministicSeed);
            walletAsync = new Wallet(MainNetParams.get(), keyChainGroupA);
            walletAsync.setUTXOProvider(new UTXOProvider() {
                @Override
                public List<UTXO> getOpenTransactionOutputs(List<Address> addresses) throws UTXOProviderException {
                    // TODO: 10/3/17 добавить запрос
                    return null;
                }

                @Override
                public int getChainHeadHeight() throws UTXOProviderException {
                    // TODO: 10/3/17 добавить запрос
                    return 0;
                }

                @Override
                public NetworkParameters getParams() {
                    // TODO: 10/3/17 добавить запрос
                    return null;
                }
            });

            sendRequest = SendRequest.to(Address.fromBase58(MainNetParams.get(), strings[0].split(",")[0]
            ), Coin.valueOf(getCoin(strings[0].split(",")[1])));
            sendRequest.feePerKb = Coin.valueOf(Math.round(Double.parseDouble(strings[0].split(",")[2]) * 100000000d));
            sendRequest.changeAddress = Address.fromBase58(MainNetParams.get(), walletAsync.freshReceiveAddress().toString());


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

            // TODO: 10/3/17 добавить запрос отправки
            //(BaseEncoding.base16().encode(b)
            return null;
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
