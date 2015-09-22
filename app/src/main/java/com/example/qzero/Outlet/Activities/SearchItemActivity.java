package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.Outlet.Adapters.CustomAdapterItem;
import com.example.qzero.Outlet.Adapters.CustomAdapterVenue;
import com.example.qzero.Outlet.ObjectClasses.Category;
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.Outlet.ObjectClasses.Items;
import com.example.qzero.Outlet.ObjectClasses.SubCategory;
import com.example.qzero.Outlet.ObjectClasses.Venue;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SearchItemActivity extends Activity {

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    @InjectView(R.id.imgViewBack)
    ImageView imgViewBack;

    @InjectView(R.id.itemListView)
    ListView itemListView;

    ArrayList<Items> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        ButterKnife.inject(this);

        rowItems = new ArrayList<Items>();


        setFont();
        setText();

        getItemData();

    }

    private void setText() {

        txtViewHeading.setText("Item Details");
    }

    private void setFont() {
        FontHelper.setFontFace(txtViewHeading, FontType.FONT, this);
    }

    public void getItemData() {
        if (getIntent().hasExtra("arrayListItems")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                Log.e("bundle","bundle");
                rowItems = (ArrayList<Items>) bundle.getSerializable("arrayListItems");
                addItemToListView();

            } else
                Log.e("null", "null");
        }
    }

    public void addItemToListView() {
        CustomAdapterItem adapter = new CustomAdapterItem(this, rowItems);

        itemListView.setAdapter(adapter);
    }

    @OnClick(R.id.imgViewBack)
    void finishAct() {
        finish();
    }
}

