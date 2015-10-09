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


<<<<<<< HEAD
import com.example.qzero.CommonFiles.Common.Utility;
=======
>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.ObjectClasses.DbItems;
import com.example.qzero.Outlet.ObjectClasses.DbModifiers;
import com.example.qzero.R;
<<<<<<< HEAD
import com.squareup.picasso.Picasso;
=======
>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c

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
<<<<<<< HEAD
    TableLayout tableItem;
    TextView tvName;
    TextView tvQty;
    TextView tvTotal;

    View[] viewItems;

    TextView modifierName;
=======

    TableLayout tableItem;
    TextView tvName;
    TextView tvPrice;
    TextView tvQty;
    TextView tvTotal;
    TextView itemNum;
    View[] v;

    TextView modifierName;
    TextView modifierPrice;
>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c
    TextView modifierQty;
    TextView modifierTotal;

    TableLayout tableModifier;
    LayoutInflater inflater;

<<<<<<< HEAD
    HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers;

    HashMap<Integer, ArrayList<DbItems>> hashMapListItems;

    int itemsLength;

    int position = 0;

    public CustomAdapterCartItem(Context context, HashMap<Integer, ArrayList<DbItems>> hashMapListItems,HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers) {
        this.context = context;
        this.hashMapModifiers = hashMapModifiers;

        this.hashMapListItems = hashMapListItems;
=======
    public CustomAdapterCartItem(Context context, ArrayList<HashMap<String, String>> listItem, ArrayList<HashMap<String, String>> listModifier) {
        this.context = context;
        this.listModifier = listModifier;
        this.listItem = listItem;
>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
<<<<<<< HEAD
        return hashMapListItems.size();
=======
        return listItem.size();
>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c
    }

    @Override
    public Object getItem(int i) {
<<<<<<< HEAD
        return hashMapListItems.get(i);
=======
        return listItem.get(i);
>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c
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

<<<<<<< HEAD
            manageLayout(view, holder);
            setFont(holder);

=======
//            manageLayout(view, holder);
//            setFont(holder);
//
//            v = new View[listModifier.size()];
//            setValue(holder, i);
//
//            handleOperations(holder, i);
//
//            handleClickEvent(holder, i);
>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c

            //    handleOperations(holder, i);
            manageTables(holder, i);

        }

        return view;
    }

<<<<<<< HEAD
    private void manageTables(ViewHolder holder, int pos) {
=======
    private void setValue(ViewHolder holder, int pos) {
        holder.showModifier.setText("Show Detail");
>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c


        ArrayList<DbItems> dbListItem = hashMapListItems.get(pos);


        Picasso.with(context).load(dbListItem.get(0).getItem_image()).placeholder(R.drawable.ic_placeholder).into(holder.imgItem);
        holder.txt_item_name.setText(dbListItem.get(0).getItem_name());
        holder.txt_item_Price.setText("$ " + Utility.formatDecimalByString(String.valueOf(dbListItem.get(0).getItem_price())));

        int countLength = dbListItem.get(0).getCount();

        viewItems = new View[countLength];


<<<<<<< HEAD
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

=======
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
>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c

    private void setFont(ViewHolder holder) {
        FontHelper.applyFont(context, holder.totalAmountWithModifier, FontHelper.FontType.FONT);

<<<<<<< HEAD
    }
=======
                Log.e("ShowModifierClick", "Clicked");
                if (holder.layoutAddModifier.getVisibility() == View.VISIBLE) {
                    holder.layoutAddModifier.setVisibility(View.GONE);
                    holder.showModifier.setText("Show Detail");
                } else {
                    holder.layoutAddModifier.setVisibility(View.VISIBLE);
                    holder.showModifier.setText("Hide Detail");
                }
>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c


    private void setIdofTableItems(int j) {

        tableModifier = (TableLayout) viewItems[j].findViewById(R.id.table_modifier);
        tableItem = (TableLayout) viewItems[j].findViewById(R.id.tableItem);

<<<<<<< HEAD
        tvName = (TextView) viewItems[j].findViewById(R.id.itemName);

        tvQty = (TextView) viewItems[j].findViewById(R.id.item_qty);
        tvTotal = (TextView) viewItems[j].findViewById(R.id.item_totalPrice);

    }
=======
        //perform edit quantity operation
//        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c


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

<<<<<<< HEAD
    }


=======

>>>>>>> 8eb851b4549b0ab4c71551291810f9eb429fde2c
}
