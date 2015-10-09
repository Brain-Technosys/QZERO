package com.example.qzero.Outlet.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.ObjectClasses.DbItems;
import com.example.qzero.Outlet.ObjectClasses.DbModifiers;
import com.example.qzero.R;
import com.squareup.picasso.Picasso;

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
    TextView tvQty;
    TextView tvTotal;

    View[] viewItems;

    TextView modifierName;
    TextView modifierQty;
    TextView modifierTotal;

    TableLayout tableModifier;
    LayoutInflater inflater;

    HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers;

    HashMap<Integer, ArrayList<DbItems>> hashMapListItems;

    int itemsLength;

    int position = 0;

    public CustomAdapterCartItem(Context context, HashMap<Integer, ArrayList<DbItems>> hashMapListItems,HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers) {
        this.context = context;
        this.hashMapModifiers = hashMapModifiers;

        this.hashMapListItems = hashMapListItems;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return hashMapListItems.size();
    }

    @Override
    public Object getItem(int i) {
        return hashMapListItems.get(i);
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


            //    handleOperations(holder, i);
            manageTables(holder, i);

        }

        return view;
    }

    private void manageTables(ViewHolder holder, int pos) {


        ArrayList<DbItems> dbListItem = hashMapListItems.get(pos);


        Picasso.with(context).load(dbListItem.get(0).getItem_image()).placeholder(R.drawable.ic_placeholder).into(holder.imgItem);
        holder.txt_item_name.setText(dbListItem.get(0).getItem_name());
        holder.txt_item_Price.setText("$ " + Utility.formatDecimalByString(String.valueOf(dbListItem.get(0).getItem_price())));

        int countLength = dbListItem.get(0).getCount();

        viewItems = new View[countLength];


        for (int i = 0; i < countLength; i++) {
            viewItems[i] = inflater.inflate(R.layout.show_cart_item_table, null);

            ArrayList<DbModifiers> dbListModifiers = hashMapModifiers.get(position);
            setIdofTableItems(i);
            if (dbListModifiers.size() != 0) {
                holder.layoutAddModifier.addView(viewItems[i]);

                tvName.setText(dbListItem.get(0).getItem_name());
                tvQty.setText(dbListModifiers.get(0).getQuantity());
                tvTotal.setText(String.valueOf(Double.parseDouble(dbListItem.get(0).getItem_price()) * Double.parseDouble(dbListModifiers.get(0).getQuantity())));


                if (dbListModifiers.get(0).getModifier_name().equals("null")) {
                    //do not add modifier screen
                } else {
                    for (int modlist = 0; modlist < dbListModifiers.size(); modlist++) {

                        View modifier = inflater.inflate(R.layout.cart_modifier_items, null);

                        setIdOfTableModifier(modifier);

                        tableModifier.addView(modifier);


                        modifierName.setText(dbListModifiers.get(modlist).getModifier_name());
                        modifierQty.setText(dbListModifiers.get(modlist).getQuantity());
                        modifierTotal.setText(String.valueOf(Double.parseDouble(dbListModifiers.get(modlist).getModifier_price()) * Double.parseDouble(dbListModifiers.get(modlist).getQuantity())));
                    }
                }
            }

            position++;

                /*if (i == 0) {
                    tvName.setText(dbListItem.get(pos).getItem_name());
                    tvQty.setText(dbListModifiers.get(i).getQuantity());
                    tvTotal.setText(String.valueOf(Double.parseDouble(dbListItem.get(pos).getItem_price()) * Double.parseDouble(dbListModifiers.get(i).getQuantity())));
                }*/
        }

//        tvQty.setText(dbListItem.get(pos).get + "= ");
//        tvTotal.setText(String.valueOf(Double.parseDouble(listItem.get(j).get("PRICE")) * Double.parseDouble(listItem.get(j).get("QTY"))));

    }


    private void manageLayout(View view, ViewHolder holder) {
        holder.layoutAddModifier = (LinearLayout) view.findViewById(R.id.detail);
        holder.totalAmountWithModifier = (TextView) view.findViewById(R.id.totalAmount);
        holder.btn_edit = (ImageView) view.findViewById(R.id.btn_edit);

        holder.imgItem = (ImageView) view.findViewById(R.id.item_image);
        holder.txt_item_name = (TextView) view.findViewById(R.id.txt_item_name);
        holder.txt_item_Price = (TextView) view.findViewById(R.id.totalAmount);
    }


    private void setFont(ViewHolder holder) {
        FontHelper.applyFont(context, holder.totalAmountWithModifier, FontHelper.FontType.FONT);

    }


    private void setIdofTableItems(int j) {

        tableModifier = (TableLayout) viewItems[j].findViewById(R.id.table_modifier);
        tableItem = (TableLayout) viewItems[j].findViewById(R.id.tableItem);

        tvName = (TextView) viewItems[j].findViewById(R.id.itemName);

        tvQty = (TextView) viewItems[j].findViewById(R.id.item_qty);
        tvTotal = (TextView) viewItems[j].findViewById(R.id.item_totalPrice);

    }


    private void setIdOfTableModifier(View modifier) {
        modifierName = (TextView) modifier.findViewById(R.id.ModifierName);

        modifierQty = (TextView) modifier.findViewById(R.id.modifier_qty);
        modifierTotal = (TextView) modifier.findViewById(R.id.modifier_totalPrice);
    }


//    private void setDataToModifierTable(int i) {
//        modifierName.setText(listModifier.get(i).get("NAME"));
//        modifierQty.setText(listModifier.get(i).get("QTY") + "= ");
//
//        modifierTotal.setText(String.valueOf(Double.parseDouble(listModifier.get(i).get("PRICE")) * Double.parseDouble(listModifier.get(i).get("QTY"))));
//    }


    private class ViewHolder {
        LinearLayout layoutAddModifier;
        TextView totalAmountWithModifier;
        ImageView btn_edit;

        ImageView imgItem;
        TextView txt_item_name;
        TextView txt_item_Price;

    }


}
