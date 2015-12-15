package com.example.qzero.Outlet.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.AppCompatEditText;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Common.ConstVarIntent;
import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.ObjectClasses.Advertisement;
import com.example.qzero.Outlet.ObjectClasses.Country;
import com.example.qzero.Outlet.ObjectClasses.State;
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
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTouch;

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

    @InjectView(R.id.imgViewAdAdmin)
    ImageView imgViewAdAdmin;

    @InjectView(R.id.imgViewAdVenue)
    ImageView imgViewAdVenue;

    @InjectView(R.id.linLayAdvertisement)
    LinearLayout linLayAdvertisement;


    String countryId;

    String fname;
    String lname;
    String address;
    String zipcode;

    int country_id;
    int state_id;

    int stateId = 0;

    String city;
    String email;
    String contact;
    String countryName;
    String stateName;

    String outletId;

    String venueId;

    int categoryId=0;
    int item_id=0;

    ArrayList<Advertisement> arrayListAdminAdvertisement;
    ArrayList<Advertisement> arrayListAdvertisement;

    public int currentImageIndexAdmin = 0;
    public int currentImagePosAdmin = 0;

    public int currentImageIndex = 0;
    public int currentImagePos = 0;

    //Check email address
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

    ArrayAdapter countryAdapter;
    ArrayAdapter stateAdapter;

    UserSession userSession;

    String[] country = {"Select country"};

    String[] state = {"Select State"};

    ArrayList<Country> arrayListCountry;

    ArrayList<State> stateArrayList;

    HashMap<Integer, ArrayList<State>> hashMapState;

    ArrayList<HashMap<String, String>> addressDetail;

    String addressType;
    String type;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        ButterKnife.inject(this);

        txtViewHeading.setText("Add Address");

        if (getIntent().hasExtra(ConstVarIntent.TAG_TYPE)) {

            Bundle bundle = getIntent().getExtras();

            addressType = bundle.getString(ConstVarIntent.TAG_TYPE_ADDRESS);
            type = bundle.getString(ConstVarIntent.TAG_TYPE);

            if (!type.equals("0")) {

                addressDetail = (ArrayList<HashMap<String, String>>) bundle.getSerializable(ConstVarIntent.TAG_LIST_ADDRESS);

                position = Integer.parseInt(bundle.getString(ConstVarIntent.TAG_POS));
            }

        }

        userSession = new UserSession(AddAddressActivity.this);

        arrayListCountry = new ArrayList<>();

        ArrayList<State> arrayListState = new ArrayList<>();

        fillDataCountrySpinner(country);

        fillDataStateSpinner(state, 0);

        getCountryAndState();

        getOutletId();


    }


    private void fillDataStateSpinner(String[] state, int pos) {
        stateAdapter = new ArrayAdapter(AddAddressActivity.this, R.layout.layout_spinner, state);
        stateAdapter.setDropDownViewResource(R.layout.layout_spinner_drop_down);
        spnr_state.setAdapter(stateAdapter);
        try {


            spnr_state.setSelection(pos, true);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

    }

    private void fillDataCountrySpinner(String[] country) {

        countryAdapter = new ArrayAdapter(AddAddressActivity.this, R.layout.layout_spinner, country);
        countryAdapter.setDropDownViewResource(R.layout.layout_spinner_drop_down);
        spnr_country.setAdapter(countryAdapter);
    }

    private void setPrefiiledData() {

        edtTxtFirstName.setText(addressDetail.get(position).get(Const.TAG_FNAME));
        edtTxtLastName.setText(addressDetail.get(position).get(Const.TAG_LAST_NAME));
        edtTxtAddress.setText(addressDetail.get(position).get(Const.TAG_ADDRESS1));
        edtTxtTownCity.setText(addressDetail.get(position).get(Const.TAG_CITY));
        edtTxtEmail.setText(addressDetail.get(position).get(Const.TAG_EMAIL_ADD));
        edtTxtZipCode.setText(addressDetail.get(position).get(Const.TAG_ZIPCODE));
        edtTxtContact.setText(addressDetail.get(position).get(Const.TAG_PHONE_NO));

        int country_id = Integer.parseInt(addressDetail.get(position).get(Const.TAG_COUNTRY_ID));
        stateId = Integer.parseInt(addressDetail.get(position).get(Const.TAG_STATE_ID));
        try {


            spnr_country.setSelection(country_id);


            if (hashMapState.containsKey(country_id)) {
                stateArrayList = new ArrayList<State>();

                stateArrayList = hashMapState.get(country_id);
                state = new String[stateArrayList.size() + 1];
                if (stateArrayList.size() != 0) {

                    state[0] = "Select State";
                    for (int i = 0; i < stateArrayList.size(); i++) {

                        if (stateArrayList.get(i).getStateId() == stateId) {
                            stateId = i + 1;


                        }
                        state[i + 1] = stateArrayList.get(i).getStateName();
                    }

                    fillDataStateSpinner(state, stateId);

                } else {
                    state[0] = "Select State";

                    fillDataStateSpinner(state, 0);
                }

            }

        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public void getCountryAndState() {
        if (CheckInternetHelper.checkInternetConnection(this)) {
            new GetCountry().execute();
        }
    }

    private void getOutletId() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor outletCursor = databaseHelper.getCheckoutItems();

        if (outletCursor != null) {
            if (outletCursor.moveToFirst()) {

                outletId = outletCursor.getString(2);
               // Log.e("outletId", outletId);
            }
        }
    }

    @OnItemSelected(R.id.spnr_country)
    void getCountryId(int position) {

        if (position == 0) {
            //do nothing
        } else {

            try {

                if (arrayListCountry.size() != 0) {

                    country_id = arrayListCountry.get(position - 1).getCountryId();

                    countryName = arrayListCountry.get(position - 1).getCountryName();

                    if (hashMapState.containsKey(country_id)) {
                        stateArrayList = new ArrayList<State>();

                        stateArrayList = hashMapState.get(country_id);
                        state = new String[stateArrayList.size() + 1];
                        if (stateArrayList.size() != 0) {

                            state[0] = "Select State";
                            for (int i = 0; i < stateArrayList.size(); i++) {
                                state[i + 1] = stateArrayList.get(i).getStateName();
                            }


                        } else {
                            state[0] = "Select State";
                            stateId = 0;
                        }

                        fillDataStateSpinner(state, stateId);
                    }
                }
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }

        }

    }


    @OnItemSelected(R.id.spnr_state)
    public void getStateId(int pos) {

        if (pos == 0) {
            //do nothing
        } else {

            stateName = state[pos];

            state_id = stateArrayList.get(pos - 1).getStateId();
        }

    }

    @OnTouch(R.id.spnr_country)
    boolean onTouchSpinner() {
        stateId = 0;
        return false;
    }

    @OnClick(R.id.btn_submit)
    void addAddress() {
        //   Toast.makeText(this, "Coming Soon.", Toast.LENGTH_SHORT).show();
        getValueFromEditText();

        validateValues();

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

    private void validateValues() {

        if (fname.length() == 0 && lname.length() == 0 && address.length() == 0 && zipcode.length() == 0 && countryName == null && stateName == null && city.length() == 0 && email.length() == 0 && contact.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter all fields.", "Alert");
        } else if (fname.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter first name.", "Alert");
        } else if (lname.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter last name.", "Alert");
        } else if (address.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter address.", "Alert");
        } else if (zipcode.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter zipcode.", "Alert");
        } else if (countryName == null) {
            AlertDialogHelper.showAlertDialog(this, "Please select country.", "Alert");
        } else if (stateName == null) {
            AlertDialogHelper.showAlertDialog(this, "Please select state.", "Alert");
        } else if (city.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter city.", "Alert");
        } else if (email.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter email address.", "Alert");
        } else if (!checkEmail(email)) {
            AlertDialogHelper.showAlertDialog(this, "Please enter a valid email address.", "Alert");
        } else if (contact.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter phone number.", "Alert");
        } else {

            if (addressType.equals("0")) {

                manipulateShippingAddress();
            }
            if (addressType.equals("1")) {
                manipulateBillingAddress();
            }

        }


    }

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private void manipulateShippingAddress() {


        if (CheckInternetHelper.checkInternetConnection(this)) {
            new AddEditShippingAddress().execute();
        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.server_message), "Alert");
        }
    }

    private void manipulateBillingAddress() {
        if (CheckInternetHelper.checkInternetConnection(this)) {
            new AddEditBillingAddress().execute();
        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.server_message), "Alert");
        }
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

    private class GetCountry extends AsyncTask<String, String, String> {

        String message;
        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(AddAddressActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            JsonParser jsonParser = new JsonParser();

            String url = Const.BASE_URL + Const.GET_COUNTRY;

            hashMapState = new HashMap<>();

            arrayListCountry = new ArrayList<Country>();

            try {

                String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

                // Log.e("json", jsonString);

                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    // Log.e("json", jsonString);
                    status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");
                    if (status == 1) {
                        JSONArray jsonArrayResult = jsonObject.getJSONArray(Const.TAG_JsonObj);

                        for (int i = 0; i < jsonArrayResult.length(); i++) {

                            ArrayList<State> arrayListState = new ArrayList<>();

                            JSONObject jsonObj = jsonArrayResult.getJSONObject(i);

                            int countryId = jsonObj.getInt(Const.TAG_COUNTRYID);
                            String countryName = jsonObj.getString(Const.TAG_COUNTRY_NAME);
                            Country country = new Country(countryId, countryName);
                            arrayListCountry.add(country);

                            JSONArray jsonArrayState = jsonObj.getJSONArray(Const.TAG_JsonState);

                            for (int j = 0; j < jsonArrayState.length(); j++) {

                                JSONObject jsonObjState = jsonArrayState.getJSONObject(j);

                                int stateId = jsonObjState.getInt(Const.TAG_STATE_ID);

                                String stateName = jsonObjState.getString(Const.TAG_STATE_NAME);

                                State state = new State(stateId, stateName);
                                arrayListState.add(state);
                            }

                            hashMapState.put(countryId, arrayListState);
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

            getAdvertisement();

            if (status == 1) {

                country = new String[arrayListCountry.size() + 1];

                country[0] = "Select Country";

                for (int i = 0; i < arrayListCountry.size(); i++) {
                    country[i + 1] = arrayListCountry.get(i).getCountryName();
                }

                fillDataCountrySpinner(country);

                if (!type.equals("0")) {

                    setPrefiiledData();
                }

            } else if (status == 0) {
                AlertDialogHelper.showAlertDialog(AddAddressActivity.this,
                        message, "Alert");
            } else {
                AlertDialogHelper.showAlertDialog(AddAddressActivity.this,
                        getString(R.string.server_message), "Alert");
            }

            ProgresBar.stop();
        }


    }


    private class AddEditShippingAddress extends AsyncTask<String, String, String> {

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
            url = Const.BASE_URL + Const.POST_SHIPPING_ADD;

            JSONObject jsonObj = new JSONObject();
            try {

                // Log.e("type", type);
                jsonObj.put("firstName", fname);
                jsonObj.put("lastName", lname);
                jsonObj.put("address1", address);
                jsonObj.put("city", city);
                jsonObj.put("countryId", country_id);
                jsonObj.put("zipCode", zipcode);
                jsonObj.put("emailAddress", email);
                jsonObj.put("phoneNo", contact);
                jsonObj.put("countryName", countryName);
                jsonObj.put("stateName", stateName);
                jsonObj.put("shippingAddressId", type);
                jsonObj.put("stateId", state_id);
                jsonObj.put("OutletId", outletId);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            Log.e("json", jsonObj.toString());
            String jsonString = jsonParser.executePost(url, jsonObj.toString(), userSession.getUserID(), Const.TIME_OUT);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    //  Log.e("json", jsonString);
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

                AlertDialogHelper.showAlertDialog(AddAddressActivity.this, msg, "Alert");

            } else if (status == 0) {
                AlertDialogHelper.showAlertDialog(AddAddressActivity.this, msg, "Alert");
            } else {
                AlertDialogHelper.showAlertDialog(AddAddressActivity.this, getString(R.string.server_message), "Alert");
            }
        }
    }

    private class AddEditBillingAddress extends AsyncTask<String, String, String> {

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
            url = Const.BASE_URL + Const.POST_BILLING_ADD;

            JSONObject jsonObj = new JSONObject();
            try {

                // Log.e("type", type);
                jsonObj.put("firstName", fname);
                jsonObj.put("lastName", lname);
                jsonObj.put("address1", address);
                jsonObj.put("city", city);
                jsonObj.put("countryId", country_id);
                jsonObj.put("zipCode", zipcode);
                jsonObj.put("emailAddress", email);
                jsonObj.put("phoneNo", contact);
                jsonObj.put("countryName", countryName);
                jsonObj.put("stateName", stateName);
                jsonObj.put("billingAddressId", type);
                jsonObj.put("stateId", state_id);
                jsonObj.put("OutletId", outletId);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            //Log.e("jsonbill", jsonObj.toString());

            String jsonString = jsonParser.executePost(url, jsonObj.toString(), userSession.getUserID(), Const.TIME_OUT);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    // Log.e("json", jsonString);
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

                AlertDialogHelper.showAlertDialog(AddAddressActivity.this, msg, "Alert");

            } else if (status == 0) {
                AlertDialogHelper.showAlertDialog(AddAddressActivity.this, msg, "Alert");
            } else {
                AlertDialogHelper.showAlertDialog(AddAddressActivity.this, getString(R.string.server_message), "Alert");
            }
        }
    }

    public void getAdvertisement() {

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

            ProgresBar.start(AddAddressActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            status = -1;
            JsonParser jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_VENUE_ADVERTISEMENT + venueId + "?" + Const.TAG_CAT_ID + "=" + categoryId + "&" + Const.TAG_ITEM_ID + "=" + item_id;
           // Log.e("url", url);

            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
               // Log.e("jsonobject", jsonObject.toString());

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

//                Picasso.with(AddAddressActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdVenue);
//                Picasso.with(AddAddressActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdAdmin);

                linLayAdvertisement.setVisibility(View.GONE);
            } else {
                AlertDialogHelper.showAlertDialog(AddAddressActivity.this,
                        getString(R.string.server_message), "Alert");
            }
        }
    }

    private void setAdvertisement() {
        if (arrayListAdminAdvertisement.size() == 0 && arrayListAdvertisement.size() == 0) {

//            Picasso.with(OutletCategoryActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdAdmin);
//            Picasso.with(OutletCategoryActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdVenue);

            linLayAdvertisement.setVisibility(View.GONE);
        } else {

            //adding image to admin if image is not from admin
            if (arrayListAdminAdvertisement.size() == 0) {
                imgViewAdAdmin.setVisibility(View.GONE);
                //Picasso.with(OutletActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdAdmin);
            } else if (arrayListAdminAdvertisement.size() == 1) {
                Picasso.with(AddAddressActivity.this).load(arrayListAdminAdvertisement.get(0).getImageAd()).error(R.drawable.noimage).into(imgViewAdAdmin);
            } else {

                autoSlideImagesAdmin();
            }

            //adding image to admin if image is from admin
            if (arrayListAdvertisement.size() == 0) {
                imgViewAdVenue.setVisibility(View.GONE);
                //Picasso.with(OutletActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdVenue);
            } else if (arrayListAdvertisement.size() == 1) {
                Picasso.with(AddAddressActivity.this).load(arrayListAdvertisement.get(0).getImageAd()).error(R.drawable.noimage).into(imgViewAdVenue);
            } else {

                autoSlideImages();
            }
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
