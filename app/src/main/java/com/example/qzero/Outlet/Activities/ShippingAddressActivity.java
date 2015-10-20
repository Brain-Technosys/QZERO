package com.example.qzero.Outlet.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ConstVarIntent;
import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.GetCheckOutDetails;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.Sessions.ShippingAddSession;
import com.example.qzero.Outlet.Adapters.CustomAdapterBillingAddress;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShippingAddressActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.listShippingAddress)
    ListView listShippingAddress;

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    ArrayList<HashMap<String, String>> listAddress;

    Button btnAddAddress;
    Button btnPlaceOrder;
    int type = 1;

    Bundle bundle;

    CustomAdapterBillingAddress adapter;

    ShippingAddSession shippingAddSession;

    GetCheckOutDetails getCheckOutDetails;

    View footerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        ButterKnife.inject(this);

        txtViewHeading.setText("Shipping Address");


        shippingAddSession = new ShippingAddSession(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getCheckOutDetails = new GetCheckOutDetails(this, "addedit");
        getCheckOutDetails.managingChkoutDetailAPI();

        if (CheckInternetHelper.checkInternetConnection(ShippingAddressActivity.this))
            new GetShipingAddress().execute();
        else
            AlertDialogHelper.showAlertDialog(this, String.valueOf(R.string.internet_connection_message), "ALERT");
    }

    private void inflateAddressList() {

        if (listShippingAddress.getFooterViewsCount() == 0) {


           footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_billing_address, null, false);

            btnAddAddress = (Button) footerView.findViewById(R.id.btn_addNew);

            btnAddAddress.setOnClickListener(this);

            listShippingAddress.addFooterView(footerView);
        }

        adapter = new CustomAdapterBillingAddress(ShippingAddressActivity.this, listAddress, type);
        listShippingAddress.setAdapter(adapter);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.btn_addNew:
                Intent intent = new Intent(this, AddAddressActivity.class);

                createBundle("0", "0");

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
        listShippingAddress.setAdapter(adapter);
    }

    public void clearList()
    {
        footerView.setVisibility(View.INVISIBLE);

        shippingAddSession.clear();
    }

    private class GetShipingAddress extends AsyncTask<String, String, String> {

        JSONArray jsonArrayShippingAddressDetail;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(ShippingAddressActivity.this);
        }

        @Override
        protected String doInBackground(String... strings) {


            if (shippingAddSession.getChkOutDetail() == null) {
            } else {
                try {
                    JSONObject jsonObjResult = new JSONObject(shippingAddSession.getChkOutDetail());
                    Log.e("jsonObjResult", shippingAddSession.getChkOutDetail());
                    jsonArrayShippingAddressDetail = jsonObjResult.getJSONArray(Const.TAG_CHKOUT_SHIPPING_ADDRESS);

                    listAddress = new ArrayList<>();

                    listAddress.clear();

                    for (int i = 0; i < jsonArrayShippingAddressDetail.length(); i++) {

                        JSONObject jsonShippingAddress = jsonArrayShippingAddressDetail.getJSONObject(i);

                        HashMap<String, String> hmAddressDetail = new HashMap<>();

                        hmAddressDetail.put(Const.TAG_CUST_ID, jsonShippingAddress.getString(Const.TAG_CUST_ID));
                        hmAddressDetail.put(Const.TAG_BILLING_ID, jsonShippingAddress.getString(Const.TAG_SHIPPING_ID));
                        hmAddressDetail.put(Const.TAG_FNAME, jsonShippingAddress.getString(Const.TAG_FNAME) + " " + jsonShippingAddress.getString(Const.TAG_LNAME));
                        hmAddressDetail.put(Const.TAG_ADDRESS1, jsonShippingAddress.getString(Const.TAG_ADDRESS1));
                        hmAddressDetail.put(Const.TAG_ADDRESS2, jsonShippingAddress.getString(Const.TAG_ADDRESS2));
                        hmAddressDetail.put(Const.TAG_CITY, jsonShippingAddress.getString(Const.TAG_CITY));
                        hmAddressDetail.put(Const.TAG_STATE, jsonShippingAddress.getString(Const.TAG_STATE_NAME));
                        hmAddressDetail.put(Const.TAG_COUNTRY, jsonShippingAddress.getString(Const.TAG_COUNTRY_NAME));
                        hmAddressDetail.put(Const.TAG_ZIPCODE, jsonShippingAddress.getString(Const.TAG_ZIPCODE));
                        hmAddressDetail.put(Const.TAG_PHONE_NO, jsonShippingAddress.getString(Const.TAG_PHONE_NO));
                        hmAddressDetail.put(Const.TAG_EMAIL_ADD, jsonShippingAddress.getString(Const.TAG_EMAIL_ADD));
                        hmAddressDetail.put(Const.TAG_COUNTRY_ID, jsonShippingAddress.getString(Const.TAG_COUNTRY_ID));
                        hmAddressDetail.put(Const.TAG_STATE_ID, jsonShippingAddress.getString(Const.TAG_STATE_ID));

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
        }
    }
}
