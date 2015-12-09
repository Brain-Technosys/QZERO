package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ConstVarIntent;
import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.GetCheckOutDetails;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.ShippingAddSession;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Adapters.CustomAdapterCartItem;
import com.example.qzero.Outlet.ObjectClasses.Advertisement;
import com.example.qzero.Outlet.ObjectClasses.DbItems;
import com.example.qzero.Outlet.ObjectClasses.DbModifiers;
import com.example.qzero.Outlet.ObjectClasses.OrderItemStatusModel;
import com.example.qzero.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ViewCartActivity extends Activity {

    @InjectView(R.id.listCartItem)
    ListView listCartItem;

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    @InjectView(R.id.imgViewAdAdmin)
    ImageView imgViewAdAdmin;

    @InjectView(R.id.imgViewAdVenue)
    ImageView imgViewAdVenue;


    //Code changed by himanshu
    @InjectView(R.id.rly_emptyCart_layout)
    RelativeLayout rly_emptyCart_layout;

    @InjectView(R.id.txt_empty_cart_msg)
    TextView txt_empty_cart_msg;

    Button continue_shopping;
    Button placeOrder;

    TextView txt_CartFinalAmount;

    ArrayList<HashMap<String, String>> mainCartItem;

    ArrayList<Advertisement> arrayListAdminAdvertisement;
    ArrayList<Advertisement> arrayListAdvertisement;

    HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers;
    HashMap<Integer, ArrayList<DbItems>> hashMapListItems;

    HashMap<String, String> hashMapPaymentDetails;

    //Reference of DatabaseHelper class to access its components
    private DatabaseHelper databaseHelper = null;

    private GetCheckOutDetails getCheckOutDetails;

    int pos;
    int itemsLength;
    int position = 0;

    String itemName;
    String venueId;

    int categoryId = 0;
    int item_id = 0;

    Cursor itemCursor;
    Cursor itemIdCursorMod;


    Double totalPaybleAmount = 0.0;

    View footerView;

    UserSession userSession;
    ShippingAddSession shippingAddSession;

    public static String client_id;

    public int currentImageIndexAdmin = 0;
    public int currentImagePosAdmin = 0;

    public int currentImageIndex = 0;
    public int currentImagePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        ButterKnife.inject(this);


        userSession = new UserSession(this);

        databaseHelper = new DatabaseHelper(this);

        getCheckOutDetails = new GetCheckOutDetails(this, "checkout");

        hashMapModifiers = new HashMap<>();
        hashMapListItems = new HashMap<>();

        txtViewHeading.setText("Shopping Cart");
        mainCartItem = new ArrayList<>();

        setFont();

        getIntentData();

        getDataFromDatabase();

        //Code changed by himanshu
        if (hashMapListItems.size() == 0) {
            rly_emptyCart_layout.setVisibility(View.VISIBLE);
            listCartItem.setVisibility(View.GONE);

        } else {
            sendFirstDataToAdapterClass();
            rly_emptyCart_layout.setVisibility(View.GONE);
            listCartItem.setVisibility(View.VISIBLE);
        }

        getAdvertisement();
    }

    private void setFont() {
        FontHelper.setFontFace(txtViewHeading, FontHelper.FontType.FONT, this);
        FontHelper.setFontFace(txt_empty_cart_msg, FontHelper.FontType.FONT, this);
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            venueId = bundle.getString(ConstVarIntent.TAG_VENUE_ID);
        }
    }

    public void getDataFromDatabase() {
        pos = 0;
        String item_id = null;
        Cursor distinctItemCursor = databaseHelper.getDistinctItems();

        if (distinctItemCursor != null) {
            while (distinctItemCursor.moveToNext()) {

                int index = distinctItemCursor.getColumnIndex(databaseHelper.NAME_COLUMN);

                itemName = distinctItemCursor.getString(index);

                //Log.e("item_name", itemName);

                Cursor itemIdCursor = databaseHelper.selectItems(itemName);

                itemIdCursorMod = itemIdCursor;

                if (itemIdCursor != null) {
                    if (itemIdCursor.moveToFirst()) {
                        int indexItemId = itemIdCursor.getColumnIndex(databaseHelper.ID_COLUMN);

                        item_id = itemIdCursor.getString(indexItemId);

                        // Log.e("item_id", item_id);
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
        // Log.e("ietm", itemd);
        if (itemIdCursorMod != null) {

            // Log.e("itemId", "" + itemIdCursorMod.getCount());
            if (itemIdCursorMod.moveToFirst()) {
                do {
                    ArrayList<DbModifiers> arrayListDbMod = new ArrayList<>();

                    int indexItemId = itemIdCursorMod.getColumnIndex(databaseHelper.ID_COLUMN);

                    String item_id = itemIdCursorMod.getString(indexItemId);

                    //  Log.e("modItemId", item_id);

                    Cursor modCursor = databaseHelper.getModifiers(item_id);

                    if (modCursor != null) {
                        while (modCursor.moveToNext()) {
                            int indexname = modCursor.getColumnIndex(databaseHelper.MOD_COLUMN);
                            int indexprice = modCursor.getColumnIndex(databaseHelper.MOD_PRICE);
                            int indexqty = modCursor.getColumnIndex(databaseHelper.QUANTITY);

                            String mod_name = modCursor.getString(indexname);
                            String mod_price = modCursor.getString(indexprice);
                            String quantity = modCursor.getString(indexqty);

                            //Log.e("mod_name", mod_name);
                            // Log.e("position", "" + position);

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

        listCartItem.setAdapter(adapterCartItem);

        // Adding footer
        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_final_chkout, null, false);
        continue_shopping = (Button) footerView.findViewById(R.id.continue_shopping);
        placeOrder = (Button) footerView.findViewById(R.id.placeOrder);
        txt_CartFinalAmount = (TextView) footerView.findViewById(R.id.txt_CartFinalAmount);

        //code changed by himanshu
        FontHelper.applyFont(ViewCartActivity.this, txt_CartFinalAmount, FontHelper.FontType.FONT);

        clickEventOfContinueShopping();
        clickEventOfPlaceOrder();

        listCartItem.addFooterView(footerView);
        listCartItem.setAdapter(adapterCartItem);
    }

    private void sendDataToAdapterClass() {

        CustomAdapterCartItem adapterCartItem = new CustomAdapterCartItem(this, hashMapListItems, hashMapModifiers);

        listCartItem.setAdapter(adapterCartItem);
        adapterCartItem.notifyDataSetChanged();
    }

    public void setfinalAmountCart(Double amount) {
        userSession.saveFinalPaybleAmount(String.valueOf(amount));
        txt_CartFinalAmount.setText("Total: $" + Utility.formatDecimalByString(String.valueOf(amount)));
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

                callPaymentDetailsApi();

            }
        });
    }


    @Override
    public void onBackPressed() {

        finish();
    }

    @OnClick(R.id.imgViewBack)
    void imgViewBack() {
        finish();
    }

    //code changed by himanshu
    @OnClick(R.id.btn_countinue_shop)
    public void gotoOutlet() {
        finish();
    }

    public void refreshDatabase() {
        pos = 0;
        position = 0;
        hashMapModifiers.clear();
        hashMapListItems.clear();

        String item_id = null;
        Cursor distinctItemCursor = databaseHelper.getDistinctItems();

        if (distinctItemCursor != null) {
            while (distinctItemCursor.moveToNext()) {

                int index = distinctItemCursor.getColumnIndex(databaseHelper.NAME_COLUMN);

                itemName = distinctItemCursor.getString(index);

                // Log.e("item_name", itemName);

                Cursor itemIdCursor = databaseHelper.selectItems(itemName);

                itemIdCursorMod = itemIdCursor;

                if (itemIdCursor != null) {
                    if (itemIdCursor.moveToFirst()) {
                        int indexItemId = itemIdCursor.getColumnIndex(databaseHelper.ID_COLUMN);

                        item_id = itemIdCursor.getString(indexItemId);

                        //   Log.e("item_id", item_id);
                        itemCursor = databaseHelper.getItems(item_id);

                        itemsLength = itemIdCursor.getCount();

                        storeData();

                    }
                }

                getListData(item_id);
            }
        }

        if (hashMapListItems.size() == 0) {
            listCartItem.removeFooterView(footerView);

            rly_emptyCart_layout.setVisibility(View.VISIBLE);
            listCartItem.setVisibility(View.GONE);
        } else {
            sendDataToAdapterClass();

            rly_emptyCart_layout.setVisibility(View.GONE);
            listCartItem.setVisibility(View.VISIBLE);
        }
    }

    private void callPaymentDetailsApi() {
        if (CheckInternetHelper.checkInternetConnection(this)) {
            new GetPaymentGatewayDetails().execute();
        }
    }

    private class GetPaymentGatewayDetails extends AsyncTask<String, String, String> {

        String message;
        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(ViewCartActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            JsonParser jsonParser = new JsonParser();

            hashMapPaymentDetails = new HashMap<String, String>();

            String url = Const.BASE_URL + Const.GET_PAYMENTGATEWAY_DETAILS + venueId;


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT, " ");

            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    //Log.e("json", jsonString);
                    status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");
                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Const.TAG_JsonObj);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            client_id = jsonObj.getString(Const.TAG_KEY);
                            String gatewayName = jsonObj.getString(Const.TAG_GATEWAY_NAME);

                            //  hashMapPaymentDetails.put(Const.TAG_KEY,key);
                            hashMapPaymentDetails.put(Const.TAG_GATEWAY_NAME, gatewayName);

                        }
                    }
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                status = -1;
            } catch (JSONException e) {

                e.printStackTrace();
                status = -1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            ProgresBar.stop();

            if (status == 1) {

                UserSession userSession = new UserSession(ViewCartActivity.this);
                if (userSession.isUserLoggedIn()) {

                    getCheckOutDetails.managingChkoutDetailAPI(arrayListAdvertisement);

                } else {
                    Intent intent = new Intent(ViewCartActivity.this, LoginActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("LOGINTYPE", "CHECKOUT");
                    intent.putExtras(bundle);

                    startActivity(intent);
                }

            } else if (status == 0) {
                AlertDialogHelper.showAlertDialog(ViewCartActivity.this,
                        message, "Alert");
            } else {
                AlertDialogHelper.showAlertDialog(ViewCartActivity.this,
                        getString(R.string.server_message), "Alert");
            }
        }
    }

    public void getAdvertisement() {

        userSession.saveVenueId(venueId);
        if (CheckInternetHelper.checkInternetConnection(this)) {
            new GetAdvertisement().execute();
        } else {
            AlertDialogHelper.showAlertDialog(this,
                    getString(R.string.internet_connection_message),
                    "Alert");
        }
    }

    public class GetAdvertisement extends AsyncTask<String, String, String> {

        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(ViewCartActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            status = -1;
            JsonParser jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_VENUE_ADVERTISEMENT + venueId + "?" + Const.TAG_CAT_ID + "=" + categoryId + "&" + Const.TAG_ITEM_ID + "=" + item_id;
            //Log.e("url", url);

            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                //Log.e("jsonobject", jsonObject.toString());

                arrayListAdvertisement = new ArrayList<Advertisement>();
                arrayListAdminAdvertisement = new ArrayList<Advertisement>();

                if (jsonObject != null) {


                    status = jsonObject.getInt(Const.TAG_STATUS);

                    if (status == 1) {

                        JSONObject jsonObj = jsonObject.getJSONObject(Const.TAG_RESULT);

                        JSONArray jsonArrayAd = jsonObj.getJSONArray(Const.TAG_JsonAd);

                        if (jsonArrayAd.length() != 0) {


                            for (int i = 0; i < jsonArrayAd.length(); i++) {

                                JSONObject advertisementObj = jsonArrayAd.getJSONObject(i);

                                String advertisementId = advertisementObj.getString(Const.TAG_ADD_ID);
                                String image = Const.BASE_URL + Const.AD_IMAGE_URL + advertisementId;

                                String linkUrl = advertisementObj.getString(Const.TAG_ADD_URL);
                                Boolean isAdminAdd = advertisementObj.getBoolean(Const.TAG_ADD_ISADMIN);

                                Advertisement advertisement = new Advertisement(image, advertisementId, linkUrl);

                                if (isAdminAdd) {
                                    arrayListAdminAdvertisement.add(advertisement);
                                } else {
                                    arrayListAdvertisement.add(advertisement);
                                }


                            }
                        } else {
                            status = 0;
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

            if (status == 1) {

                setAdvertisement();

            } else if (status == 0) {

                Picasso.with(ViewCartActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdVenue);
                Picasso.with(ViewCartActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdAdmin);

            } else {
                AlertDialogHelper.showAlertDialog(ViewCartActivity.this,
                        getString(R.string.server_message), "Alert");
            }
        }
    }

    private void setAdvertisement() {
        //adding image to admin if image is not from admin
        if (arrayListAdminAdvertisement.size() == 0) {
            imgViewAdAdmin.setVisibility(View.GONE);
            //Picasso.with(OutletActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdAdmin);
        } else if (arrayListAdminAdvertisement.size() == 1) {
            Picasso.with(ViewCartActivity.this).load(arrayListAdminAdvertisement.get(0).getImageAd()).error(R.drawable.noimage).into(imgViewAdAdmin);
        } else {

            autoSlideImagesAdmin();
        }

        //adding image to admin if image is from admin
        if (arrayListAdvertisement.size() == 0) {
            imgViewAdVenue.setVisibility(View.GONE);
            //Picasso.with(OutletActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdVenue);
        } else if (arrayListAdvertisement.size() == 1) {
            Picasso.with(ViewCartActivity.this).load(arrayListAdvertisement.get(0).getImageAd()).error(R.drawable.noimage).into(imgViewAdVenue);
        } else {

            autoSlideImages();
        }
    }

    private void autoSlideImages() {
        final Handler mHandler = new Handler();

        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            public void run() {

                AnimateandSlideShow();

            }
        };

        int delay = 1000; // delay for 1 sec.

        int period = 4000; // repeat every 4 sec.

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                mHandler.post(mUpdateResults);

            }

        }, delay, period);


    }


    private void autoSlideImagesAdmin() {
        final Handler mHandler = new Handler();

        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            public void run() {

                AnimateandSlideShowAdmin();

            }
        };

        int delay = 1000; // delay for 1 sec.

        int period = 4000; // repeat every 4 sec.

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                mHandler.post(mUpdateResults);

            }

        }, delay, period);


    }

    private void AnimateandSlideShow() {

        Picasso.with(this).load(arrayListAdvertisement.get(currentImageIndex % arrayListAdvertisement.size()).getImageAd()).into(imgViewAdVenue);
        currentImagePos = currentImageIndex % arrayListAdvertisement.size();
        currentImageIndex++;
        //Log.e("cu1", "" + currentImagePos);
    }

    private void AnimateandSlideShowAdmin() {

        Picasso.with(this).load(arrayListAdminAdvertisement.get(currentImageIndexAdmin % arrayListAdminAdvertisement.size()).getImageAd()).into(imgViewAdAdmin);
        currentImagePosAdmin = currentImageIndexAdmin % arrayListAdminAdvertisement.size();
        currentImageIndexAdmin++;

    }


    @OnClick(R.id.imgViewAdVenue)
    void openBrowser() {
        if (arrayListAdvertisement.size() != 0) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayListAdvertisement.get(currentImagePos).getImgUrl()));
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    @OnClick(R.id.imgViewAdAdmin)
    void openBrowserAdmin() {
        if (arrayListAdminAdvertisement.size() != 0) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayListAdminAdvertisement.get(currentImagePosAdmin).getImgUrl()));
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }


}
