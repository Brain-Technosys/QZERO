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
import android.widget.ListView;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
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

    ArrayList<HashMap<String, String>> mainCartItem;

    HashMap<Integer,ArrayList<DbItems>> hashMapItems;
    HashMap<Integer,ArrayList<DbModifiers>> hashMapModifiers;

    //Reference of DatabaseHelper class to access its components
    private DatabaseHelper databaseHelper = null;

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        ButterKnife.inject(this);

        databaseHelper=new DatabaseHelper(this);

        hashMapItems=new HashMap<>();
        hashMapModifiers=new HashMap<>();

        txtViewHeading.setText("Shopping Cart");

        mainCartItem = new ArrayList<>();

        setFont();

        getDataFromDatabase();
    }

    private void setFont() {
        FontHelper.setFontFace(txtViewHeading, FontHelper.FontType.FONT, this);
    }

    private void getDataFromDatabase()
    {

        Cursor itemCursor=databaseHelper.getItems();

        pos=0;

        if (itemCursor!=null) {
            while(itemCursor.moveToNext()){

                ArrayList<DbItems> arrayListDbIetms=new ArrayList<>();
                ArrayList<DbModifiers> arrayListDbMod=new ArrayList<>();

                String item_id = itemCursor.getString(0);
                String item_name = itemCursor.getString(1);
                String item_price=itemCursor.getString(2);
                String item_image=itemCursor.getString(3);
                String item_discount=itemCursor.getString(4);


                Cursor modCursor=databaseHelper.getModifiers(item_id);

                if(modCursor!=null)
                {
                    while(modCursor.moveToNext()) {

                        String mod_name = modCursor.getString(2);
                        String mod_price=modCursor.getString(4);
                        String quantity = modCursor.getString(5);

                        DbModifiers dbModifiers=new DbModifiers(item_name,quantity,mod_name,mod_price);
                        arrayListDbMod.add(dbModifiers);
                    }
                }

                DbItems dbItems=new DbItems(item_name,item_price,item_discount,item_image);
                arrayListDbIetms.add(dbItems);

                hashMapItems.put(pos,arrayListDbIetms);
                hashMapModifiers.put(pos, arrayListDbMod);

                //checkDuplicateData(arrayListDbIetms, arrayListDbMod, pos);

                pos++;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> mySampleHashmap = new HashMap<>();
            mySampleHashmap.put("number", String.valueOf(i));
            mainCartItem.add(mySampleHashmap);
        }

        CustomAdapterCartItem adapterCartItem = new CustomAdapterCartItem(this, mainCartItem);


        // Adding footer
        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_final_chkout, null, false);
        continue_shopping = (Button) footerView.findViewById(R.id.continue_shopping);
        placeOrder = (Button) footerView.findViewById(R.id.placeOrder);

        clickEventOfContinueShopping();
        clickEventOfPlaceOrder();

        listCartItem.addFooterView(footerView);
        listCartItem.setAdapter(adapterCartItem);

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
                Intent intent = new Intent(ViewCartActivity.this, FinalChkoutActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }

    @OnClick(R.id.imgViewBack)
    void imgViewBack(){
        finish();
    }
}
