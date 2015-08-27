package com.example.qzero.MyAccount.Fragments;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Braintech on 7/31/2015.
 */
public class EditProfileFragment extends Fragment {
    @InjectView(R.id.et_user_name)
    EditText userNameEditText;

    @InjectView(R.id.et_email)
    EditText emailEditText;

    @InjectView(R.id.et_first_name)
    EditText firstNameEditText;

    @InjectView(R.id.et_last_name)
    EditText lastNameEditText;

    @InjectView(R.id.et_address)
    EditText addressEditText;

    @InjectView(R.id.et_city)
    EditText cityEditText;

    @InjectView(R.id.et_state)
    EditText stateEditText;

    @InjectView(R.id.et_country)
    EditText countryEditText;

    @InjectView(R.id.et_zip_code)
    EditText zipcodeEditText;

    @InjectView(R.id.et_phone)
    EditText phoneEditText;

    @InjectView(R.id.et_mobile)
    EditText mobileEditText;

    @InjectView(R.id.btn_update)
    Button updateButton;

    CheckInternetHelper internetHelper;
    UserSession userSession;

    String userID;
    String message;

    int userProfileId;

    Bundle profileBundle;
    boolean isUpdateNotClicked = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.inject(this, view);

        // Setting title of activity
        getActivity().setTitle(getString(R.string.edit_profile_title));
        userSession = new UserSession(getActivity().getApplicationContext());
        userID = userSession.getUserID();


        setValues();
        setFonts();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isUpdateNotClicked = true;
    }

    // Click event of update button
    @OnClick(R.id.btn_update)
    public void updateInfo() {
        internetHelper = new CheckInternetHelper();
        if (internetHelper.checkInternetConnection(getActivity())) {
            userSession = new UserSession(getActivity().getApplicationContext());
            userID = userSession.getUserID();

            if (isUpdateNotClicked) {
                // Getting values from Edit Text and validating
                if (isValid(getUpdatedValues())) {
                    // Call API
                    updateProfile();
                } else {
                    //
                }

                isUpdateNotClicked = !isUpdateNotClicked;
            }

        } else {
            AlertDialogHelper.showAlertDialog(getActivity(), getString(R.string.internet_connection_message), "Alert");
        }
    }

    // Method to set values of EditText, values are coming from profile fragment
    private void setValues() {
        profileBundle = getArguments();

        userNameEditText.setText(profileBundle.getString(Const.TAG_USER_NAME));
        emailEditText.setText(profileBundle.getString(Const.TAG_EMAIL));
        firstNameEditText.setText(profileBundle.getString(Const.TAG_FIRST_NAME));
        lastNameEditText.setText(profileBundle.getString(Const.TAG_LAST_NAME));
        addressEditText.setText(profileBundle.getString(Const.TAG_ADDRESS));
        cityEditText.setText(profileBundle.getString(Const.TAG_CITY));
        stateEditText.setText(profileBundle.getString(Const.TAG_STATE));
        countryEditText.setText(profileBundle.getString(Const.TAG_COUNTRY));
        zipcodeEditText.setText(profileBundle.getString(Const.TAG_PIN_CODE));
        phoneEditText.setText(profileBundle.getString(Const.TAG_PHONE));
        mobileEditText.setText(profileBundle.getString(Const.TAG_MOBILE));
    }

    // Method to set the fonts
    private void setFonts() {
        FontHelper.applyFont(getActivity(), userNameEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), emailEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), firstNameEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), addressEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), lastNameEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), cityEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), stateEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), countryEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), zipcodeEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), phoneEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), mobileEditText, FontHelper.FontType.FONT);

    }

    private HashMap<String, String> getUpdatedValues() {
        HashMap<String, String> updatedValues = new HashMap<String, String>();

        updatedValues.put(Const.TAG_FIRST_NAME, firstNameEditText.getText().toString().trim());
        updatedValues.put(Const.TAG_ADDRESS, addressEditText.getText().toString().trim());
        updatedValues.put(Const.TAG_LAST_NAME, lastNameEditText.getText().toString().trim());
        updatedValues.put(Const.TAG_CITY, cityEditText.getText().toString().trim());
        updatedValues.put(Const.TAG_STATE, stateEditText.getText().toString().trim());
        updatedValues.put(Const.TAG_COUNTRY, countryEditText.getText().toString().trim());
        updatedValues.put(Const.TAG_ZIP, zipcodeEditText.getText().toString().trim());
        updatedValues.put(Const.TAG_PHONE, phoneEditText.getText().toString().trim());
        updatedValues.put(Const.TAG_MOBILE, mobileEditText.getText().toString().trim());
        return updatedValues;
    }

    // Method to validate data
    private boolean isValid(HashMap<String, String> data) {
        if (data.get(Const.TAG_FIRST_NAME).length() == 0) {
            firstNameEditText.setError("Please enter First Name.");
            isUpdateNotClicked = false;
            return false;
        } else if (data.get(Const.TAG_ADDRESS).length() == 0) {
            addressEditText.setError("Please enter Address.");
            isUpdateNotClicked = false;
            return false;
        } else if (data.get(Const.TAG_LAST_NAME).length() == 0) {
            lastNameEditText.setError("Please enter Last Name.");
            isUpdateNotClicked = false;
            return false;
        } else if (data.get(Const.TAG_CITY).length() == 0) {
            cityEditText.setError("Please enter City.");
            isUpdateNotClicked = false;
            return false;
        } else if (data.get(Const.TAG_STATE).length() == 0) {
            stateEditText.setError("Please enter State.");
            isUpdateNotClicked = false;
            return false;
        } else if (data.get(Const.TAG_COUNTRY).length() == 0) {
            countryEditText.setError("Please enter Country.");
            isUpdateNotClicked = false;
            return false;
        } else if (data.get(Const.TAG_ZIP).length() == 0) {
            zipcodeEditText.setError("Please enter ZIP Code.");
            isUpdateNotClicked = false;
            return false;
        } else if (data.get(Const.TAG_PHONE).length() == 0) {
            phoneEditText.setError("Please enter Phone No.");
            isUpdateNotClicked = false;
            return false;
        } else if (data.get(Const.TAG_MOBILE).length() == 0) {
            mobileEditText.setError("Please enter Mobile No.");
            isUpdateNotClicked = false;
            return false;
        } else if (data.get(Const.TAG_MOBILE).length() > 11 || data.get(Const.TAG_MOBILE).length() < 10) {
            mobileEditText.setError("Mobile No. is not valid.");
            isUpdateNotClicked = false;
            return false;
        }
        return true;
    }

    private void updateProfile() {
        if (CheckInternetHelper.checkInternetConnection(getActivity())) {
            new UpdateUserInfo().execute();
        }
    }

    // Asynchronous class to fetch user info
    private class UpdateUserInfo extends AsyncTask {

        int status=-1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());
        }

        @Override
        protected Object doInBackground(Object[] params) {
            JsonParser jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.PROFILE_INFO_URL;

            HashMap<String, String> data = getUpdatedValues();

            JSONObject jsonObj = new JSONObject();
            try {

                userProfileId = Integer.parseInt(profileBundle.getString(Const.TAG_USER_PROFILE_ID));
                jsonObj.put(Const.TAG_USER_PROFILE_ID, userProfileId);
                jsonObj.put(Const.TAG_ADDRESS, data.get(Const.TAG_ADDRESS));
                jsonObj.put(Const.TAG_CITY, data.get(Const.TAG_CITY));
                jsonObj.put(Const.TAG_STATE, data.get(Const.TAG_STATE));
                jsonObj.put(Const.TAG_PIN_CODE, data.get(Const.TAG_ZIP));
                jsonObj.put(Const.TAG_PHONE, data.get(Const.TAG_PHONE));
                jsonObj.put(Const.TAG_MOBILE, data.get(Const.TAG_MOBILE));
                jsonObj.put(Const.TAG_COUNTRY, data.get(Const.TAG_COUNTRY));
                jsonObj.put(Const.TAG_USER_NAME, data.get(Const.TAG_USER_NAME));
                jsonObj.put(Const.TAG_FIRST_NAME, data.get(Const.TAG_FIRST_NAME));
                jsonObj.put(Const.TAG_LAST_NAME, data.get(Const.TAG_LAST_NAME));
                jsonObj.put(Const.TAG_EMAIL, data.get(Const.TAG_EMAIL));

                String json = jsonParser.executePost(url, jsonObj.toString(), userID, Const.TIME_OUT);

                JSONObject jsonObject=new JSONObject(json);

                status=jsonObject.getInt(Const.TAG_STATUS);
                message=jsonObject.getString(Const.TAG_MESSAGE);

                Log.e("json", json);
            }
            catch(NullPointerException ex)
            {
                ex.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ProgresBar.stop();

            if(status==0||status==1) {
                AlertDialogHelper.showAlertDialog(getActivity(),message,"Alert");
            }
            else
            {
                AlertDialogHelper.showAlertDialog(getActivity(),getString(R.string.internet_connection_message),"Alert");
            }
        }
    }
}
