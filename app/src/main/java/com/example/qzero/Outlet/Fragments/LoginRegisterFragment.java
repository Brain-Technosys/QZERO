package com.example.qzero.Outlet.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.CommonFiles.Helpers.GCMHelper;
import com.example.qzero.CommonFiles.Push.QuickstartPreferences;
import com.example.qzero.CommonFiles.Push.RegistrationIntentService;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Activities.DashBoardActivity;
import com.example.qzero.Outlet.Activities.FinalChkoutActivity;
import com.example.qzero.Outlet.Activities.LoginActivity;
import com.example.qzero.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginRegisterFragment extends Fragment {

    public final Pattern PASSWORD_PATTERN = Pattern
            .compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!^&*~)(@#$%]).{6,20}");
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    @InjectView(R.id.edtTxtFirstName)
    EditText edtFname;

    @InjectView(R.id.edtTxtLastName)
    EditText edtLname;

    @InjectView(R.id.edtTextEmail)
    EditText edtEmail;

    @InjectView(R.id.edtTxtUname)
    EditText edtUname;

    @InjectView(R.id.edtTextPass)
    EditText edtPass;

    @InjectView(R.id.edtTxtConfPassword)
    EditText edtConfPass;

    @InjectView(R.id.txtAgreement1)
    TextView txtAgreement1;

    @InjectView(R.id.txtAgreement2)
    TextView txtAgreement2;

    @InjectView(R.id.chkBoxAgreement)
    CheckBox chkAgreement;

    String fname;
    String lname;
    String email;
    String password;
    String confPassword;
    String username;

    String user_id;
    String name;

    Boolean agreement;

    JsonParser jsonParser;
    JSONObject jsonObject;

    int status;

    String message;
    String urlParameters;

    String LOGINTYPE;

    UserSession userSession;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "Register";

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login_register, container,
                false);
        ButterKnife.inject(this, v);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        //Adjust layout when keyboard becomes visible
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setFont();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LOGINTYPE=((LoginActivity)getActivity()).getloginType();
    }


    public void setFont() {

        FontHelper.applyFont(getActivity(), edtFname, FontType.FONT);
        FontHelper.applyFont(getActivity(), edtLname, FontType.FONT);
        FontHelper.applyFont(getActivity(), edtEmail, FontType.FONT);
        FontHelper.applyFont(getActivity(), edtUname, FontType.FONT);
        FontHelper.applyFont(getActivity(), edtPass, FontType.FONT);
        FontHelper.applyFont(getActivity(), edtConfPass, FontType.FONT);
        FontHelper.applyFont(getActivity(), txtAgreement1, FontType.FONT);
        FontHelper.applyFont(getActivity(), txtAgreement2, FontType.FONT);


    }

    @OnClick(R.id.ivRegisterSubmit)
    public void submit() {
        // Hiding soft keyboard
        Utility.hideSoftKeyboard(getActivity());
        authenticateRegister();
    }

    public void requestRegister() {
        new Register().execute();

    }


    public void authenticateRegister() {
        fname = edtFname.getText().toString().trim();
        lname = edtLname.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        password = edtPass.getText().toString();
        confPassword = edtConfPass.getText().toString();
        username = edtUname.getText().toString().trim();

        if (chkAgreement.isChecked()) {
            agreement = true;
        } else
            agreement = false;

        if (fname.length() == 0 && lname.length() == 0 && email.length() == 0
                && username.length() == 0 && password.length() == 0
                && confPassword.length() == 0) {

            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.fields_error), "Alert");
        } else if (fname.length() == 0) {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.reg_fname), "Alert");
        } else if (lname.length() == 0) {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.reg_lname), "Alert");
        } else if (email.length() == 0) {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.reg_email), "Alert");
        } else if (!checkEmail(email)) {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.reg_valid_email), "Alert");

        } else if (username.length() == 0) {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.reg_username), "Alert");
        } else if (password.length() == 0) {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.reg_pwd), "Alert");
        } else if (confPassword.length() == 0) {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.reg_cnf_pwd), "Alert");
        } else if (!checkPassword(password)) {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.password_pattern_error), "Alert");

        } else if (!password.equals(confPassword)) {

            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.password_error), "Alert");

        } else if (!chkAgreement.isChecked()) {

            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.chkbox_error), "Alert");

        } else {
            if (CheckInternetHelper.checkInternetConnection(getActivity())) {
                requestRegister();
            } else {
                AlertDialogHelper.showAlertDialog(getActivity(),
                        getString(R.string.internet_connection_message), "Alert");
            }
        }
    }

    private boolean checkPassword(String Password) {
        return PASSWORD_PATTERN.matcher(Password).matches();
    }

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public class Register extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.REGISTER_URL;

            try {
                urlParameters = "userName="
                        + URLEncoder.encode(username, "UTF-8") + "&firstName="
                        + URLEncoder.encode(fname, "UTF-8") + "&lastName="
                        + URLEncoder.encode(lname, "UTF-8") + "&email="
                        + URLEncoder.encode(email, "UTF-8") + "&password="
                        + URLEncoder.encode(password, "UTF-8")
                        + "&confirmPassword="
                        + URLEncoder.encode(confPassword, "UTF-8")
                        + "&accept_terms="
                        + URLEncoder.encode(agreement.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

            String jsonString = jsonParser.executePost(url, urlParameters,
                    Const.TIME_OUT);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {

                    status = jsonObject.getInt("status");
                    Log.d("URL ", "" + url);
                    Log.d("JSON ", "" + jsonString);
                    Log.d("status", "" + status);
                    message = jsonObject.getString("message");

                    if (status == 1) {
                        user_id = jsonObject.getString("userId");

                        Log.e("user", user_id);
                        name = jsonObject.getString("name");
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

                // Creating User session
                userSession = new UserSession(getActivity().getApplicationContext());
                userSession.createUserSession(user_id, name);



                //Check if the device has not been registered to GCM
                if (userSession.getGcmToken().equals("null")) {
                    Log.e("insde","registerToGCM");
                    registerToGCM();
                }
                else
                {
                    GCMHelper gcmHelper=new GCMHelper(getActivity());
                    gcmHelper.checkRegisterDevice();
                }
                if (LOGINTYPE.equals("CHECKOUT")) {
                    Intent intent = new Intent(getActivity(),
                            FinalChkoutActivity.class);
                    startActivity(intent);
                } else if(LOGINTYPE.equals("SIMPLELOGIN")) {
                    Intent intent = new Intent(getActivity(),
                            DashBoardActivity.class);
                    startActivity(intent);
                }


            } else if (status == 0) {
                Log.d("message ", "" + message);
                AlertDialogHelper.showAlertDialog(getActivity(), message, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(getActivity(),
                        getString(R.string.server_message), "Alert");
            }
        }
    }

    private void registerToGCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ProgresBar.start(getActivity());
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {

                    Log.e("gcm message", getString(R.string.gcm_send_message));
                } else {
                    Log.e("gcm message", getString(R.string.token_error_message));
                }
            }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(getActivity(), RegistrationIntentService.class);
            getActivity().startService(intent);
        }
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


}
