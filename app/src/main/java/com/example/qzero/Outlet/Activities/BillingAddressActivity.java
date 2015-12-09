package com.example.qzero.Outlet.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ConstVarIntent;
import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.GetCheckOutDetails;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.ShippingAddSession;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Adapters.CustomAdapterBillingAddress;
import com.example.qzero.Outlet.ObjectClasses.Advertisement;
import com.example.qzero.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BillingAddressActivity extends AppCompatActivity implements View.OnClickListener {


    @InjectView(R.id.listBillingAddress)
    ListView listBillingAddress;

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;


    @InjectView(R.id.imgViewAdAdmin)
    ImageView imgViewAdAdmin;

    @InjectView(R.id.imgViewAdVenue)
    ImageView imgViewAdVenue;


    ArrayList<HashMap<String, String>> listAddress;
    CustomAdapterBillingAddress adapter;

    Button btnAddAddress;
    Button btnPlaceOrder;
    int type = 2;

    Bundle bundle;

    ShippingAddSession shippingAddSession;

    GetCheckOutDetails getCheckOutDetails;

    View footerView;

    String venueId;

    int categoryId=0;
    int item_id=0;

    ArrayList<Advertisement> arrayListAdminAdvertisement;
    ArrayList<Advertisement> arrayListAdvertisement;

    public int currentImageIndexAdmin = 0;
    public int currentImagePosAdmin = 0;

    public int currentImageIndex = 0;
    public int currentImagePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_address);

        ButterKnife.inject(this);

        txtViewHeading.setText("Billing Address");

        shippingAddSession = new ShippingAddSession(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        getCheckOutDetails = new GetCheckOutDetails(this, "addedit");
        getCheckOutDetails.managingChkoutDetailAPI(arrayListAdvertisement);

        if (CheckInternetHelper.checkInternetConnection(BillingAddressActivity.this))
            new GetBillingAddressDetail().execute();
        else
            AlertDialogHelper.showAlertDialog(this, String.valueOf(R.string.internet_connection_message), "ALERT");

    }

    private void inflateAddressList() {

        if (listBillingAddress.getFooterViewsCount() == 0) {

            footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_billing_address, null, false);
            listBillingAddress.addFooterView(footerView);

            btnAddAddress = (Button) footerView.findViewById(R.id.btn_addNew);

            btnAddAddress.setOnClickListener(this);
        }

        adapter = new CustomAdapterBillingAddress(BillingAddressActivity.this, listAddress, type);
        listBillingAddress.setAdapter(adapter);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.btn_addNew:
                Intent intent = new Intent(this, AddAddressActivity.class);

                createBundle("0", "1");

                intent.putExtras(bundle);

                startActivity(intent);
                break;
        }

    }

    private void createBundle(String type, String addressType) {
        bundle = new Bundle();
        bundle.putString(ConstVarIntent.TAG_TYPE, type);
        bundle.putString(ConstVarIntent.TAG_TYPE_ADDRESS, addressType);
    }

    @Override
    public void onBackPressed() {


        finish();


    }

    @OnClick(R.id.imgViewBack)
    void imgViewBack() {


        finish();

    }


    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
        listBillingAddress.setAdapter(adapter);
    }

    public void clearList() {
        footerView.setVisibility(View.INVISIBLE);

        shippingAddSession.clear();
    }

    private class GetBillingAddressDetail extends AsyncTask<String, String, String> {

        JSONArray jsonArrayBillingAddressDetail;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(BillingAddressActivity.this);
        }

        @Override
        protected String doInBackground(String... strings) {

            if (shippingAddSession.getChkOutDetail() == null) {

            } else {


                try {
                    JSONObject jsonObjResult = new JSONObject(shippingAddSession.getChkOutDetail());

                    jsonArrayBillingAddressDetail = jsonObjResult.getJSONArray(Const.TAG_CHKOUT_BILLING_ADDRESS);

                    listAddress = new ArrayList<>();

                    listAddress.clear();

                    for (int i = 0; i < jsonArrayBillingAddressDetail.length(); i++) {

                        JSONObject jsonBillingAddress = jsonArrayBillingAddressDetail.getJSONObject(i);

                        HashMap<String, String> hmAddressDetail = new HashMap<>();

                      //  Log.e("i", "" + i);
                      //  Log.e("act", jsonBillingAddress.getString(Const.TAG_BILLING_ID));

                        hmAddressDetail.put(Const.TAG_CUST_ID, jsonBillingAddress.getString(Const.TAG_CUST_ID));
                        hmAddressDetail.put(Const.TAG_BILLING_ID, jsonBillingAddress.getString(Const.TAG_BILLING_ID));
                        hmAddressDetail.put(Const.TAG_FNAME, jsonBillingAddress.getString(Const.TAG_FNAME));
                        hmAddressDetail.put(Const.TAG_LAST_NAME, jsonBillingAddress.getString(Const.TAG_LNAME));
                        hmAddressDetail.put(Const.TAG_ADDRESS1, jsonBillingAddress.getString(Const.TAG_ADDRESS1));
                        hmAddressDetail.put(Const.TAG_ADDRESS2, jsonBillingAddress.getString(Const.TAG_ADDRESS2));
                        hmAddressDetail.put(Const.TAG_CITY, jsonBillingAddress.getString(Const.TAG_CITY));
                        hmAddressDetail.put(Const.TAG_STATE, jsonBillingAddress.getString(Const.TAG_STATE_NAME));
                        hmAddressDetail.put(Const.TAG_COUNTRY, jsonBillingAddress.getString(Const.TAG_COUNTRY_NAME));
                        hmAddressDetail.put(Const.TAG_ZIPCODE, jsonBillingAddress.getString(Const.TAG_ZIPCODE));
                        hmAddressDetail.put(Const.TAG_PHONE_NO, jsonBillingAddress.getString(Const.TAG_PHONE_NO));
                        hmAddressDetail.put(Const.TAG_EMAIL_ADD, jsonBillingAddress.getString(Const.TAG_EMAIL_ADD));
                        hmAddressDetail.put(Const.TAG_COUNTRY_ID, jsonBillingAddress.getString(Const.TAG_COUNTRYID));
                        hmAddressDetail.put(Const.TAG_STATE_ID, jsonBillingAddress.getString(Const.TAG_STATE_ID));

                        listAddress.add(hmAddressDetail);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (shippingAddSession.getChkOutDetail() == null) {

            } else {
                inflateAddressList();
            }
            ProgresBar.stop();

            getAdvertisement();
        }
    }

    public void getAdvertisement() {

        UserSession userSession=new UserSession(this);
        venueId=userSession.getVenueId();

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

            ProgresBar.start(BillingAddressActivity.this);
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

                Picasso.with(BillingAddressActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdVenue);
                Picasso.with(BillingAddressActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdAdmin);

            } else {
                AlertDialogHelper.showAlertDialog(BillingAddressActivity.this,
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
            Picasso.with(BillingAddressActivity.this).load(arrayListAdminAdvertisement.get(0).getImageAd()).error(R.drawable.noimage).into(imgViewAdAdmin);
        } else {

            autoSlideImagesAdmin();
        }

        //adding image to admin if image is from admin
        if (arrayListAdvertisement.size() == 0) {
            imgViewAdVenue.setVisibility(View.GONE);
            //Picasso.with(OutletActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdVenue);
        } else if (arrayListAdvertisement.size() == 1) {
            Picasso.with(BillingAddressActivity.this).load(arrayListAdvertisement.get(0).getImageAd()).error(R.drawable.noimage).into(imgViewAdVenue);
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
