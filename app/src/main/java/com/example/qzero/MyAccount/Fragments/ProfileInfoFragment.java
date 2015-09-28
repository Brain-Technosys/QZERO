package com.example.qzero.MyAccount.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
    String userProfileId;
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
    String userName;
    boolean isActive;
    boolean isEditNotClicked;

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
        getActivity().setTitle(getString(R.string.profile_title));
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

    @Override
    public void onResume() {
        super.onResume();
        isEditNotClicked = true;
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

                        userName = result.getString(Const.TAG_USER_NAME);
                        userProfileId = result.getString(Const.TAG_USER_PROFILE_ID);
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

//            this.getFragmentManager().beginTransaction()
//                    .replace(R.id.flContent, fragment, "Edit Profile")
//                    .addToBackStack(null)
//                    .commit();

        this.getFragmentManager().beginTransaction()
                .hide(getFragmentManager().findFragmentByTag(this.getTag()))
                .add(R.id.flContent, fragment, fragment.getClass().getName())
                .addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();


    }

    // Method to set values on TextView
    private void setTextValues() {

        //Check the strings for null before setting the text
        if((firstName.equals("null")||firstName==null))
        {
            firstNameTextView.setText(getString(R.string.null_error));
        }
        else
        {
            firstNameTextView.setText(firstName);
        }

        if((lastName.equals("null")||lastName==null))
        {
            lastNameTextView.setText(getString(R.string.null_error));
        }
        else
        {
            lastNameTextView.setText(lastName);
        }

        if((createdOn.equals("null")||createdOn==null))
        {
            createOnTextView.setText(getString(R.string.null_error));
        }
        else
        {
            createOnTextView.setText(createdOn);
        }

        if((email.equals("null")||email==null)) {
            emailTextView.setText(getString(R.string.null_error));
        }
        else
        {
            emailTextView.setText(email);
        }


        if((phoneNo.equals("null")||phoneNo==null))
        {
            phoneNoTextView.setText(getString(R.string.null_error));
        }
        else
        {
            phoneNoTextView.setText(phoneNo);
        }

        if((mobileNo.equals("null")||mobileNo==null))
        {
            mobileNoTextView.setText(getString(R.string.null_error));
        }
        else
        {
            mobileNoTextView.setText(mobileNo);
        }

        if((addressString.equals("null")||addressString==null))
        {
            addressTextView.setText(getString(R.string.null_error));
        }
        else
        {
            addressTextView.setText(addressString);
        }

        if((city.equals("null")||city==null))
        {
            cityTextView.setText(getString(R.string.null_error));
        }
        else
        {
            cityTextView.setText(city);
        }

        if((state.equals("null")||state==null))
        {
            stateTextView.setText(getString(R.string.null_error));
        }
        else
        {
            stateTextView.setText(state);
        }

        if((country.equals("null")||country==null))
        {
            countryTextView.setText(getString(R.string.null_error));
        }
        else
        {
            countryTextView.setText(country);
        }

        if((pincode.equals("null")||pincode==null))
        {
            pincodeTextView.setText(getString(R.string.null_error));
        }
        else
        {
            pincodeTextView.setText(pincode);
        }

        if((updatedOn.equals("null")||updatedOn==null))
        {
            updatedOnTextView.setText(getString(R.string.null_error));
        }
        else
        {
            updatedOnTextView.setText(updatedOn);
        }


        userNameTextView.setText(userName);

        profileBundle = new Bundle();
        profileBundle.putString(Const.TAG_FIRST_NAME, firstName);
        profileBundle.putString(Const.TAG_LAST_NAME, lastName);
        profileBundle.putString(Const.TAG_USER_NAME, userName);
        profileBundle.putString(Const.TAG_USER_PROFILE_ID,userProfileId);
        profileBundle.putString(Const.TAG_EMAIL, email);
        profileBundle.putString(Const.TAG_PHONE, phoneNo);
        profileBundle.putString(Const.TAG_MOBILE, mobileNo);
        profileBundle.putString(Const.TAG_ADDRESS, addressString);
        profileBundle.putString(Const.TAG_CITY, city);
        profileBundle.putString(Const.TAG_STATE, state);
        profileBundle.putString(Const.TAG_COUNTRY, country);
        profileBundle.putString(Const.TAG_PIN_CODE, pincode);

    }


}
