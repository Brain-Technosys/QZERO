package com.example.qzero.Outlet.Adapters;

import android.content.Context;
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
    View[] v;

    TextView modifierName;
    TextView modifierPrice;

    TextView modifierQty;
    TextView modifierTotal;

    TableLayout tableModifier;

    EditText editTextItemQty;

    LayoutInflater inflater;


    HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers;

    HashMap<Integer, ArrayList<DbItems>> hashMapListItems;

    int itemsLength;

    int position = 0;


    //Reference of DatabaseHelper class to access its components
    private DatabaseHelper databaseHelper = null;

    public CustomAdapterCartItem(Context context, HashMap<Integer, ArrayList<DbItems>> hashMapListItems, HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers) {

        this.context = context;

        this.hashMapModifiers = hashMapModifiers;

        this.hashMapListItems = hashMapListItems;

        databaseHelper=new DatabaseHelper(context);

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


            manageLayout(view, holder,i);

            setFont(holder);

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

               String item_id=dbListModifiers.get(0).getItem_name();

                setIdofTableItems(i,item_id);

                if (dbListModifiers.size() != 0) {
                    holder.layoutAddModifier.addView(viewItems[i]);

                    tvName.setText(dbListItem.get(0).getItem_name());
                    tvQty.setText(dbListModifiers.get(0).getQuantity());
                    tvTotal.setText("$ "+ Utility.formatDecimalByString(String.valueOf(Double.parseDouble(dbListItem.get(0).getItem_price()) * Double.parseDouble(dbListModifiers.get(0).getQuantity()))));


                    if (dbListModifiers.get(0).getModifier_name().equals("null")) {
                        //do not add modifier screen
                    } else {
                        for (int modlist = 0; modlist < dbListModifiers.size(); modlist++) {

                            View modifier = inflater.inflate(R.layout.cart_modifier_items, null);

                            setIdOfTableModifier(modifier);

                            tableModifier.addView(modifier);


                            modifierName.setText(dbListModifiers.get(modlist).getModifier_name());
                            modifierQty.setText(dbListModifiers.get(modlist).getQuantity());
                            modifierTotal.setText("$" + Utility.formatDecimalByString(String.valueOf(Double.parseDouble(dbListModifiers.get(modlist).getModifier_price()) * Double.parseDouble(dbListModifiers.get(modlist).getQuantity()))));
                        }
                    }
                }

                position++;


            }
    }


    private void manageLayout(View view, final ViewHolder holder,int pos) {
        holder.layoutAddModifier = (LinearLayout) view.findViewById(R.id.detail);
        holder.totalAmountWithModifier = (TextView) view.findViewById(R.id.totalAmount);
        holder.imgViewEdit = (ImageView) view.findViewById(R.id.imgViewEdit);

        holder.imgItem = (ImageView) view.findViewById(R.id.item_image);
        holder.txt_item_name = (TextView) view.findViewById(R.id.txt_item_name);
        holder.txt_item_Price = (TextView) view.findViewById(R.id.totalAmount);

        holder.imgViewEdit.setTag(pos);

        holder.layoutAddModifier.setTag(pos);

        holder.imgViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int tag=Integer.parseInt(v.getTag().toString());

                Log.e("tag", "" + tag);

                Log.e("count", "" + holder.layoutAddModifier.getChildCount());

             for(int i=0;i<holder.layoutAddModifier.getChildCount();i++) {

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


    }

    private void setFont(ViewHolder holder) {
        FontHelper.applyFont(context, holder.totalAmountWithModifier, FontHelper.FontType.FONT);

    }

    private void setIdofTableItems(int j,String item_id) {

        tableModifier = (TableLayout) viewItems[j].findViewById(R.id.table_modifier);
        tableItem = (TableLayout) viewItems[j].findViewById(R.id.tableItem);

        tvName = (TextView) viewItems[j].findViewById(R.id.itemName);

        tvQty = (TextView) viewItems[j].findViewById(R.id.item_qty);

        tvTotal = (TextView) viewItems[j].findViewById(R.id.item_totalPrice);

        editTextItemQty=(EditText) viewItems[j].findViewById(R.id.editTextItemQty);

        ImageView img_delete=(ImageView) viewItems[j].findViewById(R.id.img_delete);

        img_delete.setTag(R.string.key_pos,j);
        img_delete.setTag(R.string.key_id,item_id);


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int tag = Integer.parseInt(v.getTag(R.string.key_pos).toString());
                String item_id=v.getTag(R.string.key_id).toString();
                viewItems[tag].setVisibility(View.GONE);

                Log.e("item_id",item_id);
                databaseHelper.deleteValuesItem(item_id);
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

        ImageView imgItem;
        TextView txt_item_name;
        TextView txt_item_Price;

    }


}
