package com.example.yass.wallet.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.yass.wallet.R;
import com.example.yass.wallet.model.BdTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yass on 10/2/17.
 */

public class TxAdapter extends RecyclerView.Adapter<TxAdapter.TxViewHolder> {

    private static final String TAG = "TxAdapter ";

    private Context context;
    private Cursor cursor;
    List<BdTransaction> transations = new ArrayList<>();

    public TxAdapter(Context context, List<BdTransaction> transations) {
        this.context = context;
        this.transations = transations;

        BdTransaction bdTransaction = new BdTransaction();
        bdTransaction.setAmount("0.00041");
        bdTransaction.setTime("2017/02/10 19.55");

        transations.add(bdTransaction);
        transations.add(bdTransaction);
        transations.add(bdTransaction);
        transations.add(bdTransaction);
        transations.add(bdTransaction);
    }

    @Override
    public TxAdapter.TxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_transaction, parent, false);
        return new TxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TxAdapter.TxViewHolder holder, int position) {
        holder.timeTextView.setText(transations.get(position).getTime());
        holder.amountTextView.setText(transations.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return transations.size();
    }

    public class TxViewHolder extends RecyclerView.ViewHolder {

        private TextView timeTextView;
        private TextView amountTextView;
        private LinearLayout linearLayout;

        public TxViewHolder(View itemView) {
            super(itemView);
            timeTextView = (TextView) itemView.findViewById(R.id.dateTX);
            amountTextView = (TextView) itemView.findViewById(R.id.amountTX);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_trax);
        }
    }
}
