package com.example.qzero.Outlet.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.example.qzero.CommonFiles.Common.Utility;

import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.Activities.ViewCartActivity;
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

    View[] viewItems;

    TableLayout tableItem;
    TextView tvName;
    TextView tvPrice;
    TextView tvQty;
    TextView tvTotal;
    TextView itemNum;



    TextView modifierName;
    TextView modifierPrice;
    TextView modifierQty;
    TextView modifierTotal;


    View[] v;

    TableLayout tableModifier;

    EditText editTextItemQty;

    LayoutInflater inflater;


    HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers;

    HashMap<Integer, ArrayList<DbItems>> hashMapListItems;

    int itemsLength;

    int position = 0;
    Double totalAmountCart;

    Double totalItemPriceUpdated;
    Double updatedTotal;


    //Reference of DatabaseHelper class to access its components
    private DatabaseHelper databaseHelper = null;

    public CustomAdapterCartItem(Context context, HashMap<Integer, ArrayList<DbItems>> hashMapListItems, HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers) {

        this.context = context;

        this.hashMapModifiers = hashMapModifiers;

        this.hashMapListItems = hashMapListItems;

        databaseHelper = new DatabaseHelper(context);

        totalAmountCart = 0.0;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        Log.e("inside","inside");

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


            manageLayout(view, holder, i);

            setFont(holder);

            manageTables(holder, i);

        }

        return view;
    }


    private void manageTables(ViewHolder holder, int pos) {


        ArrayList<DbItems> dbListItem = hashMapListItems.get(pos);


        Picasso.with(context).load(dbListItem.get(0).getItem_image()).placeholder(R.drawable.ic_placeholder).into(holder.imgItem);
        holder.txt_item_name.setText(dbListItem.get(0).getItem_name());

        Double totalPriceView = 0.0;


        int countLength = dbListItem.get(0).getCount();
        viewItems = new View[countLength];

        for (int i = 0; i < countLength; i++) {
            viewItems[i] = inflater.inflate(R.layout.show_cart_item_table, null);

            ArrayList<DbModifiers> dbListModifiers = hashMapModifiers.get(position);

            String item_id = dbListItem.get(0).getItem_id();

            //set item_id as tag of view

            viewItems[i].setTag(item_id);

            setIdofTableItems(i, item_id);

            if (dbListModifiers.size() != 0) {
                holder.layoutAddModifier.addView(viewItems[i]);

                tvName.setText(dbListItem.get(0).getItem_name());
                tvQty.setText(dbListModifiers.get(0).getQuantity());

                //Checking discount price is 0 or not
                Double amount;
                if (Double.parseDouble(dbListItem.get(0).getDiscount_price()) == 0.0) {
                    amount = Double.parseDouble(Utility.formatDecimalByString(String.valueOf(Double.parseDouble(dbListItem.get(0).getItem_price()) * Double.parseDouble(dbListModifiers.get(0).getQuantity()))));
                } else {
                    amount = Double.parseDouble(Utility.formatDecimalByString(String.valueOf(Double.parseDouble(dbListItem.get(0).getDiscount_price()) * Double.parseDouble(dbListModifiers.get(0).getQuantity()))));
                }

                totalPriceView = totalPriceView + amount;

                tvTotal.setTag(dbListItem.get(0).getItem_price());

                tvTotal.setText("$" + Utility.formatDecimalByString(String.valueOf(amount)));


                if (dbListModifiers.get(0).getModifier_name().equals("null")) {
                    //do not add modifier screen
                } else {
                    for (int modlist = 0; modlist < dbListModifiers.size(); modlist++) {

                        View modifier = inflater.inflate(R.layout.cart_modifier_items, null);

                        setIdOfTableModifier(modifier);

                        tableModifier.addView(modifier);
                        modifierTotal.setTag(dbListModifiers.get(modlist).getModifier_price());

                        modifierName.setText(dbListModifiers.get(modlist).getModifier_name());
                        modifierQty.setText(dbListModifiers.get(modlist).getQuantity());
                        Double mod_amount = Double.parseDouble(Utility.formatDecimalByString(String.valueOf(Double.parseDouble(dbListModifiers.get(modlist).getModifier_price()) * Double.parseDouble(dbListModifiers.get(modlist).getQuantity()))));
                        totalPriceView = totalPriceView + mod_amount;
                        modifierTotal.setText("$" + Utility.formatDecimalByString(String.valueOf(mod_amount)));
                    }
                }
            }

            position++;


        }

        holder.txt_item_Price.setText("Total Price: $" + Utility.formatDecimalByString(String.valueOf(totalPriceView)));

        totalAmountCart = totalAmountCart + Double.parseDouble(Utility.formatDecimalByString(String.valueOf(totalPriceView)));
        if (context instanceof ViewCartActivity) {
            ((ViewCartActivity) context).setfinalAmountCart(totalAmountCart);
        }

    }


    private void manageLayout(View view, final ViewHolder holder, int pos) {

        holder.layoutAddModifier = (LinearLayout) view.findViewById(R.id.detail);

        holder.imgViewEdit = (ImageView) view.findViewById(R.id.imgViewEdit);
        holder.imgViewDone = (ImageView) view.findViewById(R.id.imgViewDone);
        holder.imgViewCancel = (ImageView) view.findViewById(R.id.imgViewCancel);

        holder.imgItem = (ImageView) view.findViewById(R.id.item_image);
        holder.txt_item_name = (TextView) view.findViewById(R.id.txt_item_name);
        holder.txt_item_Price = (TextView) view.findViewById(R.id.totalAmount);

        holder.txtViewModName=(TextView) view.findViewById(R.id.txtViewModName);
        holder.txtViewModQty=(TextView) view.findViewById(R.id.txtViewModQty);
        holder.txtViewModTotal=(TextView) view.findViewById(R.id.txtViewModTotal);



        holder.imgViewEdit.setTag(pos);

        holder.layoutAddModifier.setTag(pos);

        holder.imgViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int tag = Integer.parseInt(v.getTag().toString());

                holder.imgViewEdit.setVisibility(View.GONE);

                holder.imgViewDone.setVisibility(View.VISIBLE);
                holder.imgViewCancel.setVisibility(View.VISIBLE);

                Log.e("tag", "" + tag);

                Log.e("count", "" + holder.layoutAddModifier.getChildCount());

                for (int i = 0; i < holder.layoutAddModifier.getChildCount(); i++) {

                    View view = holder.layoutAddModifier.getChildAt(i);

                    tvQty = (TextView) view.findViewById(R.id.item_qty);

                    String qty = tvQty.getText().toString();

                    tvQty.setVisibility(View.GONE);

                    editTextItemQty = (EditText) view.findViewById(R.id.editTextItemQty);

                    editTextItemQty.setVisibility(View.VISIBLE);

                    editTextItemQty.setText(qty);
                }

            }
        });

        holder.imgViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemId = null;
                String qty = null;

                totalItemPriceUpdated = 0.0;

                updatedTotal = 0.0;

                Log.e("count", "" + holder.layoutAddModifier.getChildCount());

                holder.imgViewCancel.setVisibility(View.GONE);

                holder.imgViewDone.setVisibility(View.GONE);

                holder.imgViewEdit.setVisibility(View.VISIBLE);

                for (int i = 0; i < holder.layoutAddModifier.getChildCount(); i++) {

                    View view = holder.layoutAddModifier.getChildAt(i);

                    itemId = view.getTag().toString();

                    Log.e("item_id", itemId);

                    editTextItemQty = (EditText) view.findViewById(R.id.editTextItemQty);

                    tvTotal = (TextView) view.findViewById(R.id.item_totalPrice);

                    qty = editTextItemQty.getText().toString();

                    editTextItemQty.setVisibility(View.GONE);

                    tvQty = (TextView) view.findViewById(R.id.item_qty);

                    tvQty.setVisibility(View.VISIBLE);

                    tvQty.setText(qty);

                    Double tot_amount = Double.parseDouble(Utility.formatDecimalByString(String.valueOf(Double.parseDouble(tvTotal.getTag().toString()) * Double.parseDouble(qty))));

                    tvTotal.setText("$" + Utility.formatDecimalByString(String.valueOf(tot_amount)));

                    tableModifier = (TableLayout) view.findViewById(R.id.table_modifier);

                    for (int j = 0; j < tableModifier.getChildCount(); j++) {
                        View tableView = tableModifier.getChildAt(j);

                        modifierQty = (TextView) tableView.findViewById(R.id.modifier_qty);

                        modifierTotal = (TextView) tableView.findViewById(R.id.modifier_totalPrice);

                        modifierQty.setText(qty);

                        Double mod_amount = Double.parseDouble(Utility.formatDecimalByString(String.valueOf(Double.parseDouble(modifierTotal.getTag().toString()) * Double.parseDouble(qty))));

                        modifierTotal.setText("$" + Utility.formatDecimalByString(String.valueOf(mod_amount)));

                        totalItemPriceUpdated = totalItemPriceUpdated + tot_amount + mod_amount;

                        databaseHelper.updateModifiers(itemId, qty);
                    }

                    holder.txt_item_Price.setText("Total Price: $" + Utility.formatDecimalByString(String.valueOf(totalItemPriceUpdated)));
                }

                ((ViewCartActivity) context).refreshDatabase();

            }
        });

        holder.imgViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.imgViewCancel.setVisibility(View.GONE);

                holder.imgViewDone.setVisibility(View.GONE);

                holder.imgViewEdit.setVisibility(View.VISIBLE);

                for (int i = 0; i < holder.layoutAddModifier.getChildCount(); i++) {

                    View view = holder.layoutAddModifier.getChildAt(i);

                    editTextItemQty = (EditText) view.findViewById(R.id.editTextItemQty);

                    editTextItemQty.setVisibility(View.GONE);

                    tvQty = (TextView) view.findViewById(R.id.item_qty);

                    tvQty.setVisibility(View.VISIBLE);

                }

            }
        });

    }

    private void setFont(ViewHolder holder) {
        FontHelper.applyFont(context, holder.totalAmountWithModifier, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.txt_item_name, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.txtViewModName, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(context, holder.txtViewModQty, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(context, holder.txtViewModTotal, FontHelper.FontType.FONTSANSBOLD);
    }


    private void setIdofTableItems(int j, String item_id) {

        tableModifier = (TableLayout) viewItems[j].findViewById(R.id.table_modifier);
        tableItem = (TableLayout) viewItems[j].findViewById(R.id.tableItem);

        tvName = (TextView) viewItems[j].findViewById(R.id.itemName);

        tvQty = (TextView) viewItems[j].findViewById(R.id.item_qty);

        tvTotal = (TextView) viewItems[j].findViewById(R.id.item_totalPrice);

        editTextItemQty = (EditText) viewItems[j].findViewById(R.id.editTextItemQty);

        //applying bold font
        FontHelper.applyFont(context, tvName, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(context, tvQty, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, tvTotal, FontHelper.FontType.FONT);

        ImageView img_delete = (ImageView) viewItems[j].findViewById(R.id.img_delete);

        img_delete.setTag(R.string.key_pos, j);
        img_delete.setTag(R.string.key_id, item_id);


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("view",""+viewItems.length);
                int tag = Integer.parseInt(v.getTag(R.string.key_pos).toString());
                String item_id = v.getTag(R.string.key_id).toString();
               /* Log.e("tag",""+tag);
                viewItems[tag].setVisibility(View.INVISIBLE);*/

                Log.e("item_id", item_id);


                databaseHelper.deleteValuesItem(item_id);

                ((ViewCartActivity) context).refreshDatabase();

            }
        });

    }

    private void setIdOfTableModifier(View modifier) {
        modifierName = (TextView) modifier.findViewById(R.id.ModifierName);

        modifierQty = (TextView) modifier.findViewById(R.id.modifier_qty);
        modifierTotal = (TextView) modifier.findViewById(R.id.modifier_totalPrice);
    }

    private class ViewHolder {
        LinearLayout layoutAddModifier;
        TextView totalAmountWithModifier;

        ImageView imgViewEdit;
        ImageView imgViewDone;
        ImageView imgViewCancel;
        ImageView imgItem;

        TextView txt_item_name;
        TextView txt_item_Price;
        TextView txtViewModName;
        TextView txtViewModQty;
        TextView txtViewModTotal;

    }
}
