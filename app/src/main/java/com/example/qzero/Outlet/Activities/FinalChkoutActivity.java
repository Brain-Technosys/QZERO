package com.example.qzero.Outlet.Activities;

import android.app.Activity;

import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import android.widget.TextView;


import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Fragments.ChkoutCatFragment;
import com.example.qzero.Outlet.ObjectClasses.DbItems;
import com.example.qzero.Outlet.ObjectClasses.DbModifiers;
import com.example.qzero.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Braintech on 06-Oct-15.
 */
public class FinalChkoutActivity extends AppCompatActivity {

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    //code change by himanshu

    @InjectView(R.id.layout_your_order)
    LinearLayout layoutAddModifier;

    //code change by himanshu
    @InjectView(R.id.txt__item_Name)
    TextView ModifierName;

    //code change by himanshu
    @InjectView(R.id.txt__item_qty)
    TextView modifier_qty;

    @InjectView(R.id.txt_item_totalPrice)
    TextView modifier_totalPrice;

    @InjectView(R.id.txt_final_price)
    TextView txt_final_price;

    HashMap<Integer, ArrayList<DbItems>> hashMapItems;
    HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers;
    HashMap<Integer, ArrayList<DbItems>> hashMapListItems;
    View[] viewItems;

    TableLayout tableModifier;
    TableLayout tableItem;
    TextView tvName;
    TextView tvQty;
    TextView tvTotal;

    TextView modifierName;
    TextView modifierQty;
    TextView modifierTotal;

    //code changed by himanshu
    Double totalpaybleAmount = 0.0;

    //Reference of DatabaseHelper class to access its components
    private DatabaseHelper databaseHelper = null;

    int pos;
    int itemsLength;

    String itemName;
    Cursor itemCursor;
    Cursor itemIdCursorMod;
    int position = 0;
    int posMod = 0;

    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_chkout);
        ButterKnife.inject(this);

        userSession=new UserSession(this);

        txtViewHeading.setText("Your Order");

        //code changed by himanshu
        totalpaybleAmount =  Double.parseDouble(userSession.getFinalPaybleAmount());

        txt_final_price.setText("Total: $" + Utility.formatDecimalByString(String.valueOf(totalpaybleAmount)));

        databaseHelper = new DatabaseHelper(this);
        hashMapItems = new HashMap<>();
        hashMapModifiers = new HashMap<>();
        hashMapListItems = new HashMap<>();

        getDataFromDatabase();

        if (hashMapItems.size() == 0) {

        } else {
            // sendDataToAdapterClass();
        }


        createTableItems();

        AddChkOutCatfrag();

        setFont();
    }

    private void setFont() {
        FontHelper.applyFont(this, ModifierName, FontHelper.FontType.FONT);
        FontHelper.applyFont(this, modifier_qty, FontHelper.FontType.FONT);
        FontHelper.applyFont(this, modifier_totalPrice, FontHelper.FontType.FONT);
        FontHelper.applyFont(this, txt_final_price, FontHelper.FontType.FONTSANSBOLD);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void getDataFromDatabase() {
        String item_id = null;
        Cursor distinctItemCursor = databaseHelper.getDistinctItems();
        pos = 0;
        if (distinctItemCursor != null) {
            while (distinctItemCursor.moveToNext()) {

                int index = distinctItemCursor.getColumnIndex(databaseHelper.NAME_COLUMN);

                itemName = distinctItemCursor.getString(index);

                Log.e("item_name", itemName);

                Cursor itemIdCursor = databaseHelper.selectItems(itemName);

                itemIdCursorMod = itemIdCursor;

                if (itemIdCursor != null) {
                    if (itemIdCursor.moveToFirst()) {
                        int indexItemId = itemIdCursor.getColumnIndex(databaseHelper.ID_COLUMN);

                        item_id = itemIdCursor.getString(indexItemId);

                        Log.e("item_id", item_id);
                        itemCursor = databaseHelper.getItems(item_id);

                        itemsLength = itemIdCursor.getCount();

                        storeData();

                    }
                }

                getListData(item_id);
            }


        }

    }

    private void storeData() {

        if (itemCursor != null) {
            if (itemCursor.moveToFirst()) {

                ArrayList<DbItems> arrayListDbIetms = new ArrayList<>();
                ArrayList<DbModifiers> arrayListDbMod = new ArrayList<>();

                String item_id = itemCursor.getString(0);
                String item_name = itemCursor.getString(1);
                String item_price = itemCursor.getString(2);
                String item_image = itemCursor.getString(3);
                String item_discount = itemCursor.getString(4);


                DbItems dbItems = new DbItems(item_id, item_name, item_price, item_discount, item_image, itemsLength);
                arrayListDbIetms.add(dbItems);

                hashMapListItems.put(pos, arrayListDbIetms);

                pos++;

            }
        }
    }

    private void getListData(String itemd) {
        Log.e("ietm", itemd);
        if (itemIdCursorMod != null) {

            Log.e("itemId", "" + itemIdCursorMod.getCount());
            if (itemIdCursorMod.moveToFirst()) {
                do {
                    ArrayList<DbModifiers> arrayListDbMod = new ArrayList<>();

                    int indexItemId = itemIdCursorMod.getColumnIndex(databaseHelper.ID_COLUMN);

                    String item_id = itemIdCursorMod.getString(indexItemId);

                    Log.e("modItemId", item_id);

                    Cursor modCursor = databaseHelper.getModifiers(item_id);

                    if (modCursor != null) {
                        while (modCursor.moveToNext()) {

                            String mod_name = modCursor.getString(2);
                            String mod_price = modCursor.getString(4);
                            String quantity = modCursor.getString(5);

                            Log.e("mod_name", mod_name);


                            DbModifiers dbModifiers = new DbModifiers(item_id, quantity, mod_name, mod_price);
                            arrayListDbMod.add(dbModifiers);
                        }
                    }

                    hashMapModifiers.put(posMod, arrayListDbMod);
                    posMod++;
                } while (itemIdCursorMod.moveToNext());
            }

        }
    }

    private void createTableItems() {

        for (int pos = 0; pos < hashMapListItems.size(); pos++) {

            ArrayList<DbItems> dbListItem = hashMapListItems.get(pos);
            LayoutInflater inflater = LayoutInflater.from(FinalChkoutActivity.this);
            int countLength = dbListItem.get(0).getCount();

            viewItems = new View[countLength];

            for (int i = 0; i < countLength; i++) {
                viewItems[i] = inflater.inflate(R.layout.order_summary_tables, null);

                ArrayList<DbModifiers> dbListModifiers = new ArrayList<>();
                dbListModifiers = hashMapModifiers.get(position);

                setIdofTableItems(i);

                if (dbListModifiers.size() != 0) {
                    layoutAddModifier.addView(viewItems[i]);

                    tvName.setText(dbListItem.get(0).getItem_name());
                    tvQty.setText("$" + Utility.formatDecimalByString(dbListItem.get(0).getItem_price()) + " x " + dbListModifiers.get(0).getQuantity());

                    //Chking discount price is 0 or not
                    if (Double.parseDouble(dbListItem.get(0).getDiscount_price()) == 0.0) {
                        tvTotal.setText("$" + Utility.formatDecimalByString(String.valueOf(Double.parseDouble(dbListItem.get(0).getItem_price()) * Double.parseDouble(dbListModifiers.get(0).getQuantity()))));
                    } else {
                        tvTotal.setText("$" + Utility.formatDecimalByString(String.valueOf(Double.parseDouble(dbListItem.get(0).getDiscount_price()) * Double.parseDouble(dbListModifiers.get(0).getQuantity()))));
                    }


                    if (dbListModifiers.get(0).getModifier_name().equals("null")) {
                        //do not add modifier screen
                    } else {
                        for (int modlist = 0; modlist < dbListModifiers.size(); modlist++) {

                            View modifier = inflater.inflate(R.layout.order_summary_table_row, null);

                            setIdOfTableModifier(modifier);

                            tableModifier.addView(modifier);


                            modifierName.setText(dbListModifiers.get(modlist).getModifier_name());

                            modifierQty.setText("$" + Utility.formatDecimalByString(dbListModifiers.get(modlist).getModifier_price()) + " x " + dbListModifiers.get(modlist).getQuantity());
                            modifierTotal.setText("$" + Utility.formatDecimalByString(String.valueOf(Double.parseDouble(dbListModifiers.get(modlist).getModifier_price()) * Double.parseDouble(dbListModifiers.get(modlist).getQuantity()))));
                        }
                    }
                }

                position++;


            }
        }
    }


    private void setIdofTableItems(int j) {

        tableModifier = (TableLayout) viewItems[j].findViewById(R.id.table_modifier);
        tableItem = (TableLayout) viewItems[j].findViewById(R.id.tableItem);
        tvName = (TextView) viewItems[j].findViewById(R.id.itemName);
        tvQty = (TextView) viewItems[j].findViewById(R.id.item_qty);
        tvTotal = (TextView) viewItems[j].findViewById(R.id.item_totalPrice);

        FontHelper.applyFont(this, tvName, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(this, tvQty, FontHelper.FontType.FONT);
        FontHelper.applyFont(this, tvTotal, FontHelper.FontType.FONT);


    }


    private void setIdOfTableModifier(View modifier) {
        modifierName = (TextView) modifier.findViewById(R.id.ModifierName);
        modifierQty = (TextView) modifier.findViewById(R.id.modifier_qty);
        modifierTotal = (TextView) modifier.findViewById(R.id.modifier_totalPrice);


    }

    private void AddChkOutCatfrag() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        ChkoutCatFragment chkoutCatFragment = new ChkoutCatFragment();
        fragmentTransaction.add(R.id.chkout_detail_frag, chkoutCatFragment, "login");
        fragmentTransaction.commit();
    }

    //code changed by himanshu
    public Double getFinalPrice() {
        return totalpaybleAmount;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }

    @OnClick(R.id.imgViewBack)
    void imgViewBack() {
        finish();
    }


}
