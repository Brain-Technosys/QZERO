package com.example.qzero.Outlet.Activities;

import android.app.Activity;

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


import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
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

    TableLayout[] tableItems;
    TableLayout tableModifiers;

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    @InjectView(R.id.layout_your_order)
    LinearLayout layoutAddModifier;

    String[] srNum = {"1", "2"};
    String[] productName = {"Roasted Stuffed Mushroom", "Honey Chilli Potato"};
    String[] price = {"10.00", "10.00"};
    String[] qty = {"5", "4"};

    View child[];
    HashMap<Integer, ArrayList<DbItems>> hashMapItems;
    HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers;
    HashMap<Integer,ArrayList<DbItems>> hashMapListItems;
    View[] viewItems;

    TableLayout tableModifier;
    TableLayout tableItem;
    TextView tvName;
    TextView tvQty;
    TextView tvTotal;

    TextView modifierName;
    TextView modifierQty;
    TextView modifierTotal;


    //Reference of DatabaseHelper class to access its components
    private DatabaseHelper databaseHelper = null;

    int pos;

    ArrayList<HashMap<String, String>> listItem;
    ArrayList<HashMap<String, String>> listModifier;

    HashMap<String, String> itemHashMap;
    HashMap<String, String> modifierHashMap;

    int itemsLength;


    Cursor itemCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_chkout);
        ButterKnife.inject(this);

        txtViewHeading.setText("Your Order");

        databaseHelper = new DatabaseHelper(this);
        hashMapItems = new HashMap<>();
        hashMapModifiers = new HashMap<>();
        hashMapListItems=new HashMap<>();

        getDataFromDatabase();

        if (hashMapItems.size() == 0) {

        } else {
           // sendDataToAdapterClass();
        }


        createTableItems();

        AddChkOutCatfrag();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void getDataFromDatabase() {

        Cursor distinctItemCursor = databaseHelper.getDistinctItems();
        pos = 0;
        if (distinctItemCursor != null) {
            while (distinctItemCursor.moveToNext()) {

                int index = distinctItemCursor.getColumnIndex(databaseHelper.NAME_COLUMN);

                String item_name = distinctItemCursor.getString(index);

                Log.e("item_name", item_name);

                Cursor itemIdCursor = databaseHelper.selectItems(item_name);

                if (itemIdCursor != null) {
                    if (itemIdCursor.moveToFirst()) {
                        int indexItemId = itemIdCursor.getColumnIndex(databaseHelper.ID_COLUMN);

                        String item_id = itemIdCursor.getString(indexItemId);

                        Log.e("item_id", item_id);
                        itemCursor = databaseHelper.getItems(item_id);

                        itemsLength=itemIdCursor.getCount();

                        storeData();

                    }
                }
            }

            getListData();
        }

    }

    private void storeData() {

        Log.e("pos", "" + pos);

        if (itemCursor != null) {
            if (itemCursor.moveToFirst()) {

                ArrayList<DbItems> arrayListDbIetms = new ArrayList<>();
                ArrayList<DbModifiers> arrayListDbMod = new ArrayList<>();

                String item_id = itemCursor.getString(0);
                String item_name = itemCursor.getString(1);
                String item_price = itemCursor.getString(2);
                String item_image = itemCursor.getString(3);
                String item_discount = itemCursor.getString(4);

                Log.e("item_idy", item_id);
                Log.e("item_namey", item_name);

                DbItems dbItems = new DbItems(item_name, item_price, item_discount, item_image,itemsLength);
                arrayListDbIetms.add(dbItems);

                hashMapListItems.put(pos, arrayListDbIetms);

                pos++;

            }
        }
    }

    private void getListData()
    {
        Cursor itemCursor = databaseHelper.getModItems();

        int position = 0;

        if (itemCursor != null) {
            while (itemCursor.moveToNext()) {

                ArrayList<DbItems> arrayListDbIetms = new ArrayList<>();
                ArrayList<DbModifiers> arrayListDbMod = new ArrayList<>();

                String item_id = itemCursor.getString(0);
                String item_name = itemCursor.getString(1);
                String item_price = itemCursor.getString(2);
                String item_image = itemCursor.getString(3);
                String item_discount = itemCursor.getString(4);


                Cursor modCursor = databaseHelper.getModifiers(item_id);

                if (modCursor != null) {
                    while (modCursor.moveToNext()) {

                        String mod_name = modCursor.getString(2);
                        String mod_price = modCursor.getString(4);
                        String quantity = modCursor.getString(5);

                        DbModifiers dbModifiers = new DbModifiers(item_name, quantity, mod_name, mod_price);
                        arrayListDbMod.add(dbModifiers);
                    }
                }

                DbItems dbItems = new DbItems(item_name, item_price, item_discount, item_image,0);
                arrayListDbIetms.add(dbItems);

                hashMapItems.put(position, arrayListDbIetms);
                hashMapModifiers.put(position, arrayListDbMod);


                position++;
            }
        }
    }

    private void createTableItems() {

        for(int pos=0;pos<hashMapListItems.size();pos++){

            ArrayList<DbItems> dbListItem = hashMapListItems.get(pos);
            int countLength=dbListItem.get(0).getCount();
            LayoutInflater inflater = LayoutInflater.from(FinalChkoutActivity.this);

            viewItems = new View[countLength];

            for (int i = 0; i < countLength; i++) {
                viewItems[i] = inflater.inflate(R.layout.order_summary_tables, null);
                ArrayList<DbModifiers> dbListModifiers = hashMapModifiers.get(i);
                setIdofTableItems(i);

                if(dbListModifiers.size()!=0) {
                    layoutAddModifier.addView(viewItems[i]);

                    tvName.setText(dbListItem.get(0).getItem_name());
                    tvQty.setText("$"+dbListItem.get(0).getItem_price()+"*"+dbListModifiers.get(0).getQuantity());
                    tvTotal.setText(String.valueOf(Double.parseDouble(dbListItem.get(0).getItem_price()) * Double.parseDouble(dbListModifiers.get(0).getQuantity())));

                    for (int modlist = 0; modlist < dbListModifiers.size(); modlist++) {

                        View modifier = inflater.inflate(R.layout.order_summary_table_row, null);

                        setIdOfTableModifier(modifier);

                        tableModifier.addView(modifier);

                        modifierName.setText(dbListModifiers.get(modlist).getModifier_name());
                        modifierQty.setText("$"+dbListModifiers.get(modlist).getModifier_price()+"*"+dbListModifiers.get(modlist).getQuantity());
                        modifierTotal.setText(String.valueOf(Double.parseDouble(dbListModifiers.get(modlist).getModifier_price()) * Double.parseDouble(dbListModifiers.get(modlist).getQuantity())));
                    }
                }
            }
        }
    }



    private void setIdofTableItems(int j) {

        tableModifier = (TableLayout) viewItems[j].findViewById(R.id.table_modifier);
        tableItem = (TableLayout) viewItems[j].findViewById(R.id.tableItem);
        tvName = (TextView) viewItems[j].findViewById(R.id.itemName);
        tvQty = (TextView) viewItems[j].findViewById(R.id.item_qty);
        tvTotal = (TextView) viewItems[j].findViewById(R.id.item_totalPrice);
        ImageView delete=(ImageView)viewItems[j].findViewById(R.id.img_delete);
        delete.setVisibility(View.GONE);

    }


    private void setIdOfTableModifier(View modifier) {
        modifierName = (TextView) modifier.findViewById(R.id.ModifierName);
        modifierQty = (TextView) modifier.findViewById(R.id.modifier_qty);
        modifierTotal = (TextView) modifier.findViewById(R.id.modifier_totalPrice);

        ImageView img_delete=(ImageView)modifier.findViewById(R.id.img_delete);
        img_delete.setVisibility(View.GONE);
    }

    private void AddChkOutCatfrag() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        ChkoutCatFragment chkoutCatFragment = new ChkoutCatFragment();
        fragmentTransaction.add(R.id.chkout_detail_frag, chkoutCatFragment, "login");
        fragmentTransaction.commit();
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
