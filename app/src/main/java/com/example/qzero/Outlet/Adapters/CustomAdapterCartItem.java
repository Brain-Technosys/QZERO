package com.example.qzero.Outlet.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Braintech on 05-Oct-15.
 */
public class CustomAdapterCartItem extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> mainCartItem;

    LayoutInflater inflater;

    public CustomAdapterCartItem(Context context, ArrayList<HashMap<String, String>> mainCartItem) {
        this.context = context;
        this.mainCartItem = mainCartItem;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mainCartItem.size();
    }

    @Override
    public Object getItem(int i) {
        return mainCartItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder = new ViewHolder();
        if (view == null) {
            view = inflater.inflate(R.layout.view_cart_item, null);

            manageLayout(view, holder);

            setFont(holder);

           // handleOperations(holder);

            handleClickEvent(holder);

            holder.showModifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("ShowModifierClick","Clicked");
                    if (holder.layoutAddModifier.getVisibility() == View.VISIBLE) {
                        holder.layoutAddModifier.setVisibility(View.GONE);
                    } else {
                        holder.layoutAddModifier.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        return view;
    }


    private void manageLayout(View view, ViewHolder holder) {
        holder.layoutAddModifier = (LinearLayout) view.findViewById(R.id.detail);
        holder.showModifier = (TextView) view.findViewById(R.id.show_detail);
        holder.totalAmount = (TextView) view.findViewById(R.id.totalAmount);
    }


    private void setFont(ViewHolder holder) {
        FontHelper.applyFont(context, holder.totalAmount, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.showModifier, FontHelper.FontType.FONT);
    }

    private void handleOperations(ViewHolder holder) {
        holder.tableItem = new TableLayout(context);
        holder.tableItem.setLayoutParams(holder.layoutAddModifier.getLayoutParams());


        holder.rowItem = new TableRow(context);
        holder.rowItem.setLayoutParams(holder.tableItem.getLayoutParams());


        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        TextView txtNameItem = new TextView(context);
        createTxtView("1. Roasted Stuffed Mushroom", txtNameItem);
        holder.rowItem.addView(txtNameItem, param);

        TextView txtPriceQuantity = new TextView(context);
        createTxtView("$10.00  x 3" + " " + "=", txtPriceQuantity);
        holder.rowItem.addView(txtPriceQuantity, param);

        TextView txtTotal = new TextView(context);
        createTxtView("$30.00", txtTotal);
        holder.rowItem.addView(txtTotal, param);

        ImageView iv = new ImageView(context);
        iv.setImageResource(R.drawable.ic_delete);
        holder.rowItem.addView(iv, param);

        holder.tableItem.addView(holder.rowItem);

        holder.layoutAddModifier.addView(holder.tableItem);
        holder.layoutAddModifier.setVisibility(View.GONE );

    }

    private void handleClickEvent(final ViewHolder holder) {

        //Hide and show modifier on clicking show detail

    }

    private class ViewHolder {
        LinearLayout layoutAddModifier;
        TextView showModifier;
        TextView totalAmount;

        TableLayout tableItem;

        TableRow rowItem;
        TableLayout tableModifier;
        TableRow rowModifier;
    }

    public void createTxtView(String txt, TextView tv) {

        tv.setText(txt);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setTextSize(15);

    }
}
