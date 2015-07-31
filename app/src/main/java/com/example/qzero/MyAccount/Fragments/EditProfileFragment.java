package com.example.qzero.MyAccount.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.R;

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

    Bundle profileBundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.inject(this, view);

        internetHelper = new CheckInternetHelper();
        setValues();

        if (internetHelper.checkInternetConnection(getActivity())) {
            userSession = new UserSession(getActivity().getApplicationContext());
            userID = userSession.getUserID();

        } else {
            AlertDialogHelper.showAlertDialog(getActivity(), getString(R.string.internet_connection_message), "Alert");
        }


        return view;
    }

    // Asynchronous class to fetch user info
    private class FetchUserInfo extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ProgresBar.stop();
        }
    }

    @OnClick(R.id.btn_update)
    public void updateInfo() {

    }

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
}
