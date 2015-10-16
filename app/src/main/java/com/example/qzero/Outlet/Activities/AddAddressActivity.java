package com.example.qzero.Outlet.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.AppCompatEditText;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class AddAddressActivity extends AppCompatActivity {

    @InjectView(R.id.edtTxtFirstName)

    AppCompatEditText edtTxtFirstName;

    @InjectView(R.id.edtTxtLastName)
    EditText edtTxtLastName;

    @InjectView(R.id.edtTxtAddress)
    EditText edtTxtAddress;

    @InjectView(R.id.edtTxtZipCode)
    EditText edtTxtZipCode;

    @InjectView(R.id.spnr_country)
    Spinner spnr_country;

    @InjectView(R.id.spnr_state)
    Spinner spnr_state;

    @InjectView(R.id.edtTxtTownCity)
    EditText edtTxtTownCity;

    @InjectView(R.id.edtTxtEmail)
    EditText edtTxtEmail;

    @InjectView(R.id.edtTxtContact)
    EditText edtTxtContact;


    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    int BILLING_ADD_ID;
    int SHIPPING_ADD_ID;

    String fname;
    String lname;
    String address;
    String zipcode;
    String countryId;
    String stateId;
    //String country;
    String[] state = {"Select State"};
    String city;
    String email;
    String contact;
    ArrayAdapter countryAdapter;
    ArrayAdapter stateAdapter;

    UserSession userSession;

    String[] country = {"Select country"};

    int addressType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        ButterKnife.inject(this);

        txtViewHeading.setText("Add Address");

        addressType = getIntent().getIntExtra("ADDRESSTYPE", 0);

        userSession = new UserSession(AddAddressActivity.this);

        fillDataCountrySpinner(country);

        fillDataStateSpinner(state);


        //AddAddress  screen will also open for edit Address,fill data according to addressType

        //ShippingAddressActivity
        if (addressType == 1) {
            //call Api to fill data of Shipping Address
            SHIPPING_ADD_ID = 1;


        }
        if (addressType == 4) {
            //call Api to Edit data of Shipping Address
            SHIPPING_ADD_ID = 0;
        }
        //BillingAddressActivity
        if (addressType == 2) {
            //call Api to fill data of Billing Address
            SHIPPING_ADD_ID = 1;
        }
        if (addressType == 3) {
            //call Api to Edit data of Billing Address
            SHIPPING_ADD_ID = 0;
        }
    }

    private void fillDataStateSpinner(String[] state) {
        stateAdapter = new ArrayAdapter(AddAddressActivity.this, R.layout.layout_spinner, state);
        stateAdapter.setDropDownViewResource(R.layout.layout_spinner_drop_down);
        spnr_state.setAdapter(stateAdapter);

    }

    private void fillDataCountrySpinner(String[] country) {

        countryAdapter = new ArrayAdapter(AddAddressActivity.this, R.layout.layout_spinner, country);
        countryAdapter.setDropDownViewResource(R.layout.layout_spinner_drop_down);
        spnr_country.setAdapter(countryAdapter);
    }


    @OnClick(R.id.btn_submit)
    void addAddress() {
        Toast.makeText(this, "Coming Soon.", Toast.LENGTH_SHORT).show();
       /* getValueFromEditText();

        //ShippingAddressActivity
        if (addressType == 1) {
            //add Billing address
            callAddEditAddressAPI(Const.TAG_ADD_SHIPPING_ADDRESS_ID);
        }
        //BillingAddressActivity
        else if (addressType == 2) {
            //add Shipping address
            callAddEditAddressAPI(Const.TAG_ADD_BILLING_ADDRESS_ID);

        } else if (addressType == 3) {
            //save edited address of Shipping Address
            callAddEditAddressAPI(Const.TAG_ADD_SHIPPING_ADDRESS_ID);

        } else if (addressType == 4) {
            //save edited address of Billing Address
            callAddEditAddressAPI(Const.TAG_ADD_BILLING_ADDRESS_ID);
        } else {
            //do nothing

        }*/
    }

    public void callAddEditAddressAPI(String addTypeID) {
        if (CheckInternetHelper.checkInternetConnection(AddAddressActivity.this)) {
            new AddOrEditAddress().execute(addTypeID);
        } else {
            AlertDialogHelper.showAlertDialog(AddAddressActivity.this, getString(R.string.internet_connection_message), "Alert");
        }

    }

    private void getValueFromEditText() {


        fname = edtTxtFirstName.getText().toString().trim();
        lname = edtTxtLastName.getText().toString().trim();
        address = edtTxtAddress.getText().toString().trim();
        zipcode = edtTxtZipCode.getText().toString().trim();

        city = edtTxtTownCity.getText().toString().trim();
        email = edtTxtEmail.getText().toString().trim();
        contact = edtTxtContact.getText().toString().trim();

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


//    private class AddOrEditShippingAddress extends AsyncTask<String, String, String> {
//
//        JsonParser jsonParser;
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            ProgresBar.start(AddAddressActivity.this);
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            jsonParser = new JsonParser();
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            ProgresBar.stop();
//        }
//    }

    private class AddOrEditAddress extends AsyncTask<String, String, String> {

        JsonParser jsonParser;
        JSONObject jsonObject;
        int status = -1;
        String msg;
        String urlParameters;
        String url;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(AddAddressActivity.this);
        }

        @Override
        protected String doInBackground(String... strings) {
            jsonParser = new JsonParser();
            url = Const.BASE_URL + Const.ADD_EDIT_BILL_ADDRESS;


            try {
                urlParameters = Const.TAG_ADD_FIRSTNAME + URLEncoder.encode(fname, "UTF-8") + Const.TAG_ADD_LASTNAME + URLEncoder.encode(lname, "UTF-8") +
                        Const.TAG_ADD_ADDRESS + URLEncoder.encode(address, "UTF-8") + Const.TAG_ADD_COUNTRYID + URLEncoder.encode(countryId, "UTF-8") +
                        Const.TAG_ADD_STATEID + URLEncoder.encode(stateId, "UTF-8") + Const.TAG_ADD_CITY + URLEncoder.encode(city, "UTF-8")
                        + Const.TAG_ADD_PHONE + URLEncoder.encode(contact, "UTF-8") + Const.TAG_ADD_ZIPCODE + URLEncoder.encode(zipcode, "UTF-8") +
                        Const.TAG_ADD_EMAIL + URLEncoder.encode(email, "UTF-8") + strings[0] + URLEncoder.encode(String.valueOf(SHIPPING_ADD_ID), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String jsonString = jsonParser.executePost(url, urlParameters, userSession.getUserID(), Const.TIME_OUT);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
                    status = jsonObject.getInt(Const.TAG_STATUS);
                    msg = jsonObject.getString(Const.TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ProgresBar.stop();

            if (status == 1) {
                if(addressType==1||addressType==2) {
                    AlertDialogHelper.showAlertDialog(AddAddressActivity.this, getString(R.string.address_saved), "Alert");
                }else if(addressType==3 ||addressType==2){
                    AlertDialogHelper.showAlertDialog(AddAddressActivity.this, getString(R.string.address_edit), "Alert");
                }
            }else if(status==0){
                if(addressType==1||addressType==2) {
                    AlertDialogHelper.showAlertDialog(AddAddressActivity.this, msg, "Alert");
                }else if(addressType==3 ||addressType==2){
                    AlertDialogHelper.showAlertDialog(AddAddressActivity.this, msg, "Alert");
                }
            }else{
                AlertDialogHelper.showAlertDialog(AddAddressActivity.this, getString(R.string.server_message), "Alert");
            }
        }
    }

    @OnItemSelected(R.id.spnr_country)
    public void getCountryId(int pos) {

    }

    @OnItemSelected(R.id.spnr_state)
    public void getStateId(int pos) {

    }
}
