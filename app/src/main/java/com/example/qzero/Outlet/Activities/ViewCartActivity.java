package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Adapters.CustomAdapterCartItem;
import com.example.qzero.Outlet.ObjectClasses.DbItems;
import com.example.qzero.Outlet.ObjectClasses.DbModifiers;
import com.example.qzero.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ViewCartActivity extends Activity {

    @InjectView(R.id.listCartItem)
    ListView listCartItem;

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    Button continue_shopping;
    Button placeOrder;
    TextView txt_CartFinalAmount;
    ArrayList<HashMap<String, String>> mainCartItem;
    HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers;
    HashMap<Integer, ArrayList<DbItems>> hashMapListItems;

    //Reference of DatabaseHelper class to access its components
    private DatabaseHelper databaseHelper = null;
    int pos;
    String itemName;
    int itemsLength;
    Cursor itemCursor;
    Cursor itemIdCursorMod;
    int position = 0;

    Double totalItemPriceUpdated;
    Double updatedTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        ButterKnife.inject(this);

        databaseHelper = new DatabaseHelper(this);

        hashMapModifiers = new HashMap<>();
        hashMapListItems = new HashMap<>();

        txtViewHeading.setText("Shopping Cart");
        mainCartItem = new ArrayList<>();

        setFont();

        getDataFromDatabase();
        if (hashMapListItems.size() == 0) {

        } else {
           sendFirstDataToAdapterClass();
        }
    }


    private void setFont() {
        FontHelper.setFontFace(txtViewHeading, FontHelper.FontType.FONT, this);
    }

    public void getDataFromDatabase() {
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
        sendDataToAdapterClass();

    }

    public HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers()
    {
        getDataFromDatabase();
        return hashMapModifiers;
    }

    public HashMap<Integer, ArrayList<DbItems>> getHashMapListItems()
    {
        getDataFromDatabase();
        return hashMapListItems;
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

                DbItems dbItems = new DbItems(item_name, item_price, item_discount, item_image, itemsLength);
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

                    hashMapModifiers.put(position, arrayListDbMod);
                    position++;
                } while (itemIdCursorMod.moveToNext());
            }

        }
    }

    private void sendFirstDataToAdapterClass() {

        CustomAdapterCartItem adapterCartItem = new CustomAdapterCartItem(this, hashMapListItems, hashMapModifiers);

        adapterCartItem.notifyDataSetChanged();


        // Adding footer
        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_final_chkout, null, false);
        continue_shopping = (Button) footerView.findViewById(R.id.continue_shopping);
        placeOrder = (Button) footerView.findViewById(R.id.placeOrder);
        txt_CartFinalAmount = (TextView) footerView.findViewById(R.id.txt_CartFinalAmount);

        clickEventOfContinueShopping();
        clickEventOfPlaceOrder();

        listCartItem.addFooterView(footerView);
        listCartItem.setAdapter(adapterCartItem);
    }

    private void sendDataToAdapterClass() {

        CustomAdapterCartItem adapterCartItem = new CustomAdapterCartItem(this, hashMapListItems, hashMapModifiers);

        listCartItem.setAdapter(adapterCartItem);
    }

    public void setfinalAmountCart(Double amount) {
        txt_CartFinalAmount.setText("$" + Utility.formatDecimalByString(String.valueOf(amount)));
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    private void clickEventOfContinueShopping() {
        continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void clickEventOfPlaceOrder() {
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSession userSession = new UserSession(ViewCartActivity.this);
                if (userSession.isUserLoggedIn()) {
                    Intent intent = new Intent(ViewCartActivity.this, FinalChkoutActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ViewCartActivity.this, LoginActivity.class);
                    intent.putExtra("LOGINTYPE", "CHECKOUT");
                    startActivity(intent);
                }
            }
        });
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

    public void updatePrice(int pos) {
        totalItemPriceUpdated=0.0;

        String itemId = null;
        String qty = null;

        View viewList = listCartItem.getChildAt(pos);

        LinearLayout layoutAddModifier = (LinearLayout) viewList.findViewById(R.id.detail);
        TextView txt_item_Price=(TextView) viewList.findViewById(R.id.totalAmount);

        for (int i = 0; i < layoutAddModifier.getChildCount(); i++) {

            View view = layoutAddModifier.getChildAt(i);

            itemId = view.getTag().toString();

            Log.e("item_id", itemId);

            TextView tvTotal = (TextView) view.findViewById(R.id.item_totalPrice);

            TextView tvQty = (TextView) view.findViewById(R.id.item_qty);

            qty=tvQty.getText().toString();

            Double tot_amount = Double.parseDouble(Utility.formatDecimalByString(String.valueOf(Double.parseDouble(tvTotal.getTag().toString()) * Double.parseDouble(qty))));

            tvTotal.setText("$" + tot_amount);

            TableLayout tableModifier = (TableLayout) view.findViewById(R.id.table_modifier);

            for (int j = 0; j < tableModifier.getChildCount(); j++) {
                View tableView = tableModifier.getChildAt(j);

                TextView modifierTotal = (TextView) tableView.findViewById(R.id.modifier_totalPrice);

                Double mod_amount = Double.parseDouble(Utility.formatDecimalByString(String.valueOf(Double.parseDouble(modifierTotal.getTag().toString()) * Double.parseDouble(qty))));

                modifierTotal.setText("$" + mod_amount);

                totalItemPriceUpdated = totalItemPriceUpdated + tot_amount + mod_amount;

                databaseHelper.updateModifiers(itemId, qty);
            }


          txt_item_Price.setText("Total Price: $" + Utility.formatDecimalByString(String.valueOf(totalItemPriceUpdated)));

        }
    }

    public void findTotalPrice()
    {
        Double updatePrice=0.0;

        Log.e("count",""+listCartItem.getChildCount());
        for(int i=0;i<listCartItem.getChildCount();i++)
        {
            View viewList = listCartItem.getChildAt(i);

            TextView txt_item_Price=(TextView) viewList.findViewById(R.id.totalAmount);

            String totPrice=txt_item_Price.getText().toString();

            String price=totPrice.substring(14,totPrice.length());

            Double totalPrice=Double.parseDouble(price);

            updatePrice=updatePrice+totalPrice;
        }

        setfinalAmountCart(updatePrice);
    }
}
