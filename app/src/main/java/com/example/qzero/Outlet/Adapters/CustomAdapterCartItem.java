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
import java.util.HashSet;

/**
 * Created by Braintech on 05-Oct-15.
 */
public class CustomAdapterCartItem extends BaseAdapter {

    Context context;

    ArrayList<HashMap<String, String>> listItem;
    ArrayList<HashMap<String, String>> listModifier;

    TableLayout tableItem;
    TextView tvName;
    TextView tvPrice;
    TextView tvQty;
    TextView tvTotal;
    TextView itemNum;
    View[] v;

    TextView modifierName;
    TextView modifierPrice;
    TextView modifierQty;
    TextView modifierTotal;

    TableLayout tableModifier;
    LayoutInflater inflater;

    public CustomAdapterCartItem(Context context, ArrayList<HashMap<String, String>> listItem, ArrayList<HashMap<String, String>> listModifier) {
        this.context = context;
        this.listModifier = listModifier;
        this.listItem = listItem;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int i) {
        return listItem.get(i);
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

//            manageLayout(view, holder);
//            setFont(holder);
//
//            v = new View[listModifier.size()];
//            setValue(holder, i);
//
//            handleOperations(holder, i);
//
//            handleClickEvent(holder, i);


        }

        return view;
    }

    private void setValue(ViewHolder holder, int pos) {
        holder.showModifier.setText("Show Detail");

    }


    private void manageLayout(View view, ViewHolder holder) {
        holder.layoutAddModifier = (LinearLayout) view.findViewById(R.id.detail);
        holder.showModifier = (TextView) view.findViewById(R.id.show_detail);
        holder.totalAmountWithModifier = (TextView) view.findViewById(R.id.totalAmount);

        holder.btn_edit = (ImageView) view.findViewById(R.id.btn_edit);
    }


    private void setFont(ViewHolder holder) {
        FontHelper.applyFont(context, holder.totalAmountWithModifier, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.showModifier, FontHelper.FontType.FONT);


    }

    private void handleOperations(ViewHolder holder, int pos) {

        int p = 0;
        for (int j = 0; j < listModifier.size(); j++) {

            if (listModifier.get(j).get("ITEM").equals(listItem.get(pos).get("NAME"))) {
                v[p] = inflater.inflate(R.layout.show_cart_item_table, null);

                tableModifier = (TableLayout) v[p].findViewById(R.id.table_modifier);
                tableItem = (TableLayout) v[p].findViewById(R.id.tableItem);

                setIdofTableItems(p);
                setValueInTableItems(p);

                for (int i = 0; i < listModifier.size(); i++) {

                    //Inflating row items for table Modifier
                    View modifier = inflater.inflate(R.layout.cart_modifier_items, null);

                    setIdOfTableModifier(modifier);
                    setDataToModifierTable(i);

                    //adding view of row items to tableModifier
                    tableModifier.addView(modifier);
                }

                //Adding tables to layoutAddModifier of view_cart_item
                holder.layoutAddModifier.addView(v[p]);
                p++;
            }

        }
    }


    private void setIdofTableItems(int j) {
        tvName = (TextView) v[j].findViewById(R.id.itemName);
        tvPrice = (TextView) v[j].findViewById(R.id.itemPrice);
        tvQty = (TextView) v[j].findViewById(R.id.item_qty);
        tvTotal = (TextView) v[j].findViewById(R.id.item_totalPrice);
        itemNum = (TextView) v[j].findViewById(R.id.txt_item_num);
    }

    private void setValueInTableItems(int j) {
        itemNum.setText(String.valueOf(j + 1));
        tvName.setText(listItem.get(j).get("NAME"));
        tvQty.setText(listItem.get(j).get("QTY") + "= ");
        tvPrice.setText("$" + listItem.get(j).get("PRICE") + "* ");
        tvTotal.setText(String.valueOf(Double.parseDouble(listItem.get(j).get("PRICE")) * Double.parseDouble(listItem.get(j).get("QTY"))));

    }

    private void setIdOfTableModifier(View modifier) {
        modifierName = (TextView) modifier.findViewById(R.id.ModifierName);
        modifierPrice = (TextView) modifier.findViewById(R.id.ModifierPrice);
        modifierQty = (TextView) modifier.findViewById(R.id.modifier_qty);
        modifierTotal = (TextView) modifier.findViewById(R.id.modifier_totalPrice);
    }


    private void setDataToModifierTable(int i) {
        modifierName.setText(listModifier.get(i).get("NAME"));
        modifierQty.setText(listModifier.get(i).get("QTY") + "= ");
        modifierPrice.setText("$" + listModifier.get(i).get("PRICE") + "* ");
        modifierTotal.setText(String.valueOf(Double.parseDouble(listModifier.get(i).get("PRICE")) * Double.parseDouble(listModifier.get(i).get("QTY"))));
    }

    private void handleClickEvent(final ViewHolder holder, final int position) {

        //Hide and show modifier on clicking show detail
        holder.showModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("ShowModifierClick", "Clicked");
                if (holder.layoutAddModifier.getVisibility() == View.VISIBLE) {
                    holder.layoutAddModifier.setVisibility(View.GONE);
                    holder.showModifier.setText("Show Detail");
                } else {
                    holder.layoutAddModifier.setVisibility(View.VISIBLE);
                    holder.showModifier.setText("Hide Detail");
                }

            }
        });

        //perform delete operation


        //perform edit quantity operation
//        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


    }


    private class ViewHolder {
        LinearLayout layoutAddModifier;
        TextView showModifier;
        TextView totalAmountWithModifier;
        ImageView btn_edit;

    }


}
