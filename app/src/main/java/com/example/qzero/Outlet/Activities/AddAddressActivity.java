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

import com.example.qzero.CommonFiles.Common.ConstVarIntent;
import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.ObjectClasses.Country;
import com.example.qzero.Outlet.ObjectClasses.State;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

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

    String countryId;
    String stateId;

    String fname;
    String lname;
    String address;
    String zipcode;

    int country_id;
    int state_id;


    String city;
    String email;
    String contact;
    String countryName;
    String stateName;

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

    String addressType;
    String type;


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
        }

        userSession = new UserSession(AddAddressActivity.this);

        arrayListCountry = new ArrayList<>();

        ArrayList<State> arrayListState = new ArrayList<>();


        fillDataCountrySpinner(country);

        fillDataStateSpinner(state);

        getCountryAndState();

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

    public void getCountryAndState() {
        if (CheckInternetHelper.checkInternetConnection(this)) {
            new GetCountry().execute();
        }
    }

    @OnItemSelected(R.id.spnr_country)
    void getCountryId(int position) {

        if (position == 0) {
            //do nothing
        } else {
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
                    }

                    fillDataStateSpinner(state);
                }
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
        if (fname.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter first name.", "Alert");
        } else if (lname.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter last name.", "Alert");
        } else if (address.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter address.", "Alert");
        } else if (zipcode.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter address.", "Alert");
        } else if (countryName == null) {
            AlertDialogHelper.showAlertDialog(this, "Please select country.", "Alert");
        } else if (stateName == null) {
            AlertDialogHelper.showAlertDialog(this, "Please select state.", "Alert");
        } else if (city.length() == 0) {
            AlertDialogHelper.showAlertDialog(this, "Please enter city.", "Alert");
        } else if (!checkEmail(email)) {
            AlertDialogHelper.showAlertDialog(this, "Please enter valid email address.", "Alert");
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

                Log.e("json", jsonString);

                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
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


            if (status == 1) {

                country = new String[arrayListCountry.size() + 1];

                country[0] = "Select Country";

                for (int i = 0; i < arrayListCountry.size(); i++) {
                    country[i + 1] = arrayListCountry.get(i).getCountryName();
                }

                fillDataCountrySpinner(country);


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

                Log.e("type", type);
                jsonObj.put("firstName", fname);
                jsonObj.put("lastName", lname);
                jsonObj.put("address1", address);
                jsonObj.put("city", city);
                jsonObj.put("countryId",country_id);
                jsonObj.put("zipCode", zipcode);
                jsonObj.put("emailAddress", email);
                jsonObj.put("phoneNo", contact);
                jsonObj.put("countryName", countryName);
                jsonObj.put("stateName", stateName);
                jsonObj.put("shippingAddressId", type);
                jsonObj.put("stateId", state_id);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            String jsonString = jsonParser.executePost(url, jsonObj.toString(), userSession.getUserID(), Const.TIME_OUT);

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

                AlertDialogHelper.showAlertDialog(AddAddressActivity.this, msg, "Alert");

            } else if (status == 0) {
                AlertDialogHelper.showAlertDialog(AddAddressActivity.this, msg, "Alert");
            } else {
                AlertDialogHelper.showAlertDialog(AddAddressActivity.this, getString(R.string.server_message), "Alert");
            }
        }
    }


}
