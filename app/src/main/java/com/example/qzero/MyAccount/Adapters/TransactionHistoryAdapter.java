package com.example.qzero.MyAccount.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.qzero.R;

import java.util.ArrayList;

/**
 * Created by braintech on 16-Jun-15.
 */
public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {
    private ArrayList<String> dataset;

    public TransactionHistoryAdapter(ArrayList<String> data) {
        dataset = data;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView transactionTypeTextView;
        public TextView amountTextView;
        public TextView dateTextView;
        public TextView byUserTextView;
        public TextView promoCodeTextView;
        public TextView commentTextView;

        public TableRow firstRow;
        public TableRow secondRow;
        public TableRow thirdRow;



        public ViewHolder(View v) {
            super(v);
             transactionTypeTextView = (TextView) v.findViewById(R.id.tv_transaction_type);
            amountTextView = (TextView) v.findViewById(R.id.tv_amount);
            dateTextView = (TextView) v.findViewById(R.id.tv_date);
            byUserTextView = (TextView) v.findViewById(R.id.tv_by);
            promoCodeTextView = (TextView) v.findViewById(R.id.tv_promo_code);
            commentTextView = (TextView) v.findViewById(R.id.tv_comment);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transaction_history, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
// - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = dataset.get(position);
        holder.transactionTypeTextView.setText(dataset.get(position));

    }

    public void add(int position, String item) {
        dataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = dataset.indexOf(item);
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
