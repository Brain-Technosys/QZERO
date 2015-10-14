package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.qzero.CommonFiles.Common.ConstVarIntent;
import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.Outlet.Adapters.CustomAdapterItem;
import com.example.qzero.Outlet.Adapters.CustomAdapterVenue;
import com.example.qzero.Outlet.ObjectClasses.Category;
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.Outlet.ObjectClasses.Items;
import com.example.qzero.Outlet.ObjectClasses.Outlet;
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
import butterknife.OnItemClick;


public class SearchItemActivity extends Activity {

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    @InjectView(R.id.imgViewBack)
    ImageView imgViewBack;

    @InjectView(R.id.itemListView)
    ListView itemListView;

    ArrayList<Items> rowItems;

    int status;
    String message;

    ArrayList<Outlet> arrayListOutlet;
    ArrayList<ItemOutlet> arrayListItem;
    ArrayList<Category> arrayListCat;

    HashMap<Integer,ArrayList<SubCategory>> hashMapSubCat;

    JsonParser jsonParser;
    JSONObject jsonObject;
    JSONArray jsonArray;

    String venue_id;
    String outletId;
    String itemId;
    String subCatId;

    String oldOutletId="null";

    String outletTitle;

    Category category;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        ButterKnife.inject(this);

        rowItems = new ArrayList<Items>();


        setFont();
        setText();

        getItemData();

        databaseHelper=new DatabaseHelper(this);

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

    @OnItemClick(R.id.itemListView)
    void  onItemClick(int pos){

        Items items = rowItems.get(pos);

        venue_id=rowItems.get(pos).getVenue_id();
        outletId=rowItems.get(pos).getOutlet_id();
        itemId=rowItems.get(pos).getItem_id();
        outletTitle=rowItems.get(pos).getOutlet_name();

        Cursor outletCursor=databaseHelper.selectOutletId();

        if(outletCursor!=null)
        {
            if(outletCursor.moveToFirst())
            {
                oldOutletId=outletCursor.getString(0);
            }
        }

        if (oldOutletId.equals("null")) {
            getOutletItems();
        } else if (oldOutletId.equals(outletId)) {
            getOutletItems();
        } else {
            openDialog();
        }

    }

    private void openDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_outlet);

        TextView txtViewTitle = (TextView) dialog.findViewById(R.id.txtViewTitle);
        TextView txtViewCancel = (TextView) dialog.findViewById(R.id.txtViewCancel);
        TextView txtViewChange = (TextView) dialog.findViewById(R.id.txtViewChange);

        FontHelper.setFontFace(txtViewTitle, FontHelper.FontType.FONT,this);
        FontHelper.setFontFace(txtViewCancel, FontHelper.FontType.FONT,this);
        FontHelper.setFontFace(txtViewChange, FontHelper.FontType.FONT,this);

        txtViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtViewChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper databaseHelper=new DatabaseHelper(SearchItemActivity.this);

                databaseHelper.deleteModifierTable();
                databaseHelper.deleteItemTable();;
                databaseHelper.deleteCheckOutTable();

                dialog.dismiss();

                getOutletItems();
            }
        });

        dialog.show();
    }


    @OnClick(R.id.imgViewBack)
    void finishAct() {
        finish();
    }

    public void getOutletItems()
    {
        if (CheckInternetHelper.checkInternetConnection(this)) {
            new GetOutletItems().execute();
        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.internet_connection_message), "Alert");
        }
    }

    //Get outlet items on outlet click
    public class GetOutletItems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(SearchItemActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("inside", "do in");
            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_ITEMS + venue_id + "/?outletId=" + outletId + "&itemId=" +itemId
                    + "&subCatId=" +subCatId;


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("inside", "json");

                    arrayListItem = new ArrayList<ItemOutlet>(jsonObject.length());

                    arrayListCat = new ArrayList<Category>(jsonObject.length());

                    hashMapSubCat=new HashMap<Integer,ArrayList<SubCategory>>();

                    status = jsonObject.getInt(Const.TAG_STATUS);
                    message = jsonObject.getString(Const.TAG_MESSAGE);

                    Log.d("status", "" + status);
                    if (status == 1) {

                        JSONObject jsonObj = jsonObject.getJSONObject(Const.TAG_JsonObj);

                        //Get json Array for items
                        jsonArray = new JSONArray();
                        jsonArray = jsonObj.getJSONArray(Const.TAG_JsonItemObj);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjItem = jsonArray.getJSONObject(i);

                            String item_id = jsonObjItem.getString(Const.TAG_ITEM_ID);
                            String item_name = jsonObjItem.getString(Const.TAG_CAT_ITEM_NAME);
                            String item_price = jsonObjItem.getString(Const.TAG_PRICE);
                            String item_desc = jsonObjItem.getString(Const.TAG_DESC);
                            String sub_item_id = jsonObjItem.getString(Const.TAG_SUB_ID);
                            String item_image = Const.BASE_URL + Const.IMAGE_URL + item_id;
                            ItemOutlet ItemOutlet = new ItemOutlet(item_id, item_name, item_image, item_price, item_desc, sub_item_id);
                            arrayListItem.add(ItemOutlet);
                        }

                        //Get json array for categories
                        JSONArray jsonArrayCategory = new JSONArray();

                        jsonArrayCategory = jsonObj.getJSONArray(Const.TAG_JsonCatObj);

                        for (int i = 0; i < jsonArrayCategory.length(); i++) {
                            JSONObject jsonObjCat = jsonArrayCategory.getJSONObject(i);

                            String category_id = jsonObjCat.getString(Const.TAG_CAT_ID);
                            String category_name = jsonObjCat.getString(Const.TAG_CAT_NAME);

                            category = new Category(category_id, category_name);

                            arrayListCat.add(category);

                            JSONArray jsonArraySubCat = jsonObjCat.getJSONArray(Const.TAG_JsonSubCatObj);





                            ArrayList<SubCategory> subCatArrayList=new ArrayList<SubCategory>();

                            for (int j = 0; j < jsonArraySubCat.length(); j++) {
                                JSONObject jsonObjSubCat = jsonArraySubCat.getJSONObject(j);
                                String sub_cat_id = jsonObjSubCat.getString(Const.TAG_SUB_CAT_ID);
                                String sub_cat_name = jsonObjSubCat.getString(Const.TAG_SUB_CAT_NAME);

                                SubCategory subCategory=new SubCategory(sub_cat_id,sub_cat_name);
                                subCatArrayList.add(subCategory);


                            }

                            hashMapSubCat.put(i,subCatArrayList);

                        }
                    }

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            ProgresBar.stop();

            Log.e("inside", "postexecute");

            if (status == 1) {

                passIntentOutlet();

            } else if (status == 0) {

                AlertDialogHelper.showAlertDialog(SearchItemActivity.this, message, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(SearchItemActivity.this, getString(R.string.server_message), "Alert");
            }
        }
    }

    public void passIntentOutlet() {

        Intent intent = new Intent(SearchItemActivity.this, OutletCategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("arraylistitem", arrayListItem);
        bundle.putSerializable("arrayListCat", arrayListCat);
        bundle.putSerializable("hashMapSubCat",hashMapSubCat);
        bundle.putString(Const.TAG_OUTLET_NAME, outletTitle);
        bundle.putString(ConstVarIntent.TAG_CLASSNAME,"items");
        bundle.putString("venue_id", venue_id);
        bundle.putString("outlet_id",outletId);
        bundle.putString(Const.TAG_ITEM_ID,itemId);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

