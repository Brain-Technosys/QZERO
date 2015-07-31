package com.example.qzero.MyAccount.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by braintech on 13-Jul-15.
 */
public class ProfileInfoFragment extends Fragment {
    UserSession userSession;
    String userIdString;
    String addressString;
    String city;
    String state;
    String country;
    String pincode;
    String phoneNo;
    String mobileNo;
    String firstName;
    String lastName;
    String email;
    String createdOn;
    String updatedOn;
    boolean isActive;

    @InjectView(R.id.tv_firstName)
    TextView firstNameTextView;

    @InjectView(R.id.tv_lastName)
    TextView lastNameTextView;

    @InjectView(R.id.tv_userName)
    TextView userNameTextView;

    @InjectView(R.id.tv_createdOn)
    TextView createOnTextView;

    @InjectView(R.id.tv_email)
    TextView emailTextView;

    @InjectView(R.id.tv_phone_no)
    TextView phoneNoTextView;

    @InjectView(R.id.tv_mobile_no)
    TextView mobileNoTextView;

    @InjectView(R.id.tv_address)
    TextView addressTextView;

    @InjectView(R.id.tv_state)
    TextView stateTextView;

    @InjectView(R.id.tv_city)
    TextView cityTextView;

    @InjectView(R.id.tv_country)
    TextView countryTextView;

    @InjectView(R.id.tv_zipcode)
    TextView pincodeTextView;

    @InjectView(R.id.tv_updated_on)
    TextView updatedOnTextView;

    @InjectView(R.id.btn_edit)
    Button editButton;

    CheckInternetHelper internetHelper;

    Bundle profileBundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, null);
        ButterKnife.inject(this, view);

        internetHelper = new CheckInternetHelper();
        userSession = new UserSession(getActivity().getApplicationContext());

        if (userSession.isUserLoggedIn()) {
            userIdString = userSession.getUserID();
        }

        if (internetHelper.checkInternetConnection(getActivity())) {
            new GetProfileInfo().execute();
        } else {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.internet_connection_message), "Alert");
        }
        return view;
    }

    class GetProfileInfo extends AsyncTask<String, String, String> {
        JsonParser jsonParser;
        int status = 0;
        String message;

        @Override
        protected void onPreExecute() {
            ProgresBar.start(getActivity());
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.PROFILE_INFO_URL;
            String json = jsonParser.getJSONFromUrl(url, Const.TIME_OUT, userIdString);
            if (json != null) {
                try {
                    Log.v("Profile", "URL: " + url);
                    Log.v("Profile", "JSON: " + json);

                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt(Const.TAG_STATUS) == 1) {
                        status = 1;
                        JSONObject result = jsonObject.getJSONObject(Const.TAG_RESULT);

                        addressString = result.getString(Const.TAG_ADDRESS);
                        city = result.getString(Const.TAG_CITY);
                        state = result.getString(Const.TAG_STATE);
                        country = result.getString(Const.TAG_COUNTRY);
                        pincode = result.getString(Const.TAG_PIN_CODE);
                        phoneNo = result.getString(Const.TAG_PHONE);
                        mobileNo = result.getString(Const.TAG_MOBILE);
                        firstName = result.getString(Const.TAG_FIRST_NAME);
                        lastName = result.getString(Const.TAG_LAST_NAME);
                        email = result.getString(Const.TAG_EMAIL);
                        createdOn = result.getString(Const.TAG_CREATED_ON);
                        updatedOn = result.getString(Const.TAG_UPDATED_ON);
                        isActive = result.getBoolean(Const.TAG_IS_ACTIVE);


                    } else {
                        status = 0;
                        message = jsonObject.getString(Const.TAG_MESSAGE);
                    }
                } catch (JSONException e) {
                    status = -1;
                }

            } else {
                status = -1;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgresBar.stop();
            if (status == 1) {
                setTextValues();
            } else if (status == -1) {
                AlertDialogHelper.showAlertDialog(getActivity(),
                        getActivity().getString(R.string.response_failure), "Alert");
            } else {
                AlertDialogHelper.showAlertDialog(getActivity(),
                        message, "Alert");
            }

        }
    }

    // Click event ot Edit button
    @OnClick(R.id.btn_edit)
    public void edit() {



        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setArguments(profileBundle);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.flContent, fragment, "Edit Profile")
                .addToBackStack(null)
                .commit();
    }

    // Method to set values on TextView
    private void setTextValues() {
        firstNameTextView.setText(firstName);
        lastNameTextView.setText(lastName);
        userNameTextView.setText(userSession.getUserName());
        createOnTextView.setText(createdOn);
        emailTextView.setText(email);
        phoneNoTextView.setText(phoneNo);
        mobileNoTextView.setText(mobileNo);
        addressTextView.setText(addressString);
        cityTextView.setText(city);
        stateTextView.setText(state);
        countryTextView.setText(country);
        pincodeTextView.setText(pincode);
        updatedOnTextView.setText(updatedOn);

        profileBundle = new Bundle();
        profileBundle.putString(Const.TAG_FIRST_NAME,firstName);
        profileBundle.putString(Const.TAG_LAST_NAME,lastName);
        profileBundle.putString(Const.TAG_USER_NAME,userSession.getUserName());

        profileBundle.putString(Const.TAG_EMAIL,email);
        profileBundle.putString(Const.TAG_PHONE,phoneNo);
        profileBundle.putString(Const.TAG_MOBILE,mobileNo);
        profileBundle.putString(Const.TAG_ADDRESS,addressString);
        profileBundle.putString(Const.TAG_CITY,city);
        profileBundle.putString(Const.TAG_STATE,state);
        profileBundle.putString(Const.TAG_COUNTRY,country);
        profileBundle.putString(Const.TAG_PIN_CODE,pincode);

    }


}
