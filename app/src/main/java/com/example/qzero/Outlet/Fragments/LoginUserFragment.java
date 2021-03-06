package com.example.qzero.Outlet.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.CommonFiles.Helpers.GCMHelper;
import com.example.qzero.CommonFiles.Helpers.GetCheckOutDetails;

import com.example.qzero.CommonFiles.Push.QuickstartPreferences;
import com.example.qzero.CommonFiles.Push.RegistrationIntentService;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Activities.DashBoardActivity;
import com.example.qzero.Outlet.Activities.FinalChkoutActivity;
import com.example.qzero.Outlet.Activities.ForgotPasswordActivity;
import com.example.qzero.Outlet.Activities.LoginActivity;
import com.example.qzero.Outlet.ObjectClasses.Advertisement;
import com.example.qzero.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginUserFragment extends Fragment {

    @InjectView(R.id.edtTextUserName)
    EditText edtTextUserName;

    @InjectView(R.id.edtTxtPassword)
    EditText edtTxtPassword;

    @InjectView(R.id.txtViewForgotPass)
    TextView txtViewForgotPass;

    JsonParser jsonParser;
    JSONObject jsonObject;

    UserSession userSession;

    String urlParameters;

    String user_id;
    String name;
    String userName;
    String password;

    String deviceId;

    int status;
    String LOGINTYPE = "SIMPLELOGIN";

    GetCheckOutDetails getCheckOutDetails;

    ArrayList<Advertisement> arrayListAdvertisement;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceView) {
        View v = inflater.inflate(R.layout.fragment_login_user, null);
        ButterKnife.inject(this, v);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setFont();

        getCheckOutDetails = new GetCheckOutDetails(getActivity(), "login");

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LOGINTYPE = ((LoginActivity) getActivity()).getloginType();
    }

    public void setFont() {
        FontHelper.applyFont(getActivity(), edtTextUserName, FontType.FONT);
        FontHelper.applyFont(getActivity(), edtTxtPassword, FontType.FONT);


    }

    @OnClick(R.id.imgViewSubmit)
    public void submit() {
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        authenticateLogin();

    }

    public void authenticateLogin() {
        userName = edtTextUserName.getText().toString();
        password = edtTxtPassword.getText().toString();
        if (userName.length() == 0 && password.length() == 0) {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.fields_error), "Alert");
        } else if (userName.length() == 0) {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.email_error), "Alert");
        } else if (password.length() == 0) {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.password_empty), "Alert");
        } else {
            if (CheckInternetHelper.checkInternetConnection(getActivity())) {
                requestLogin();
            } else {
                AlertDialogHelper.showAlertDialog(getActivity(),
                        getString(R.string.internet_connection_message),
                        "Alert");
            }
        }
    }

    public void requestLogin() {
        new Login().execute();
    }

    private class Login extends AsyncTask<String, String, String> {
        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {

            jsonParser = new JsonParser();

            String url = Const.BASE_URL + Const.LOGIN_URL;

            try {
                urlParameters = "username="
                        + URLEncoder.encode(userName, "UTF-8") + "&password="
                        + URLEncoder.encode(password, "UTF-8");
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

            String jsonString = jsonParser.executePost(url, urlParameters,
                    Const.TIME_OUT);


            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
                    status = jsonObject.getInt("status");
                    msg = jsonObject.getString("message");
                    if (status == 1) {
                        user_id = jsonObject.getString("userId");

                        Log.e("user", user_id);
                        name = jsonObject.getString("name");
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
            ProgresBar.stop();

            if (status == 1) {

                // Creating User session
                userSession = new UserSession(getActivity().getApplicationContext());
                userSession.createUserSession(user_id, name);

                userSession.saveLogin(true);

                clearFields();

                if (LOGINTYPE.equals("CHECKOUT")) {

                    getCheckOutDetails.managingChkoutDetailAPI(arrayListAdvertisement);
                } else if (LOGINTYPE.equals("SIMPLELOGIN")) {
                    Intent intent = new Intent(getActivity(),
                            DashBoardActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
                else if (LOGINTYPE.equals("OUTLET")) {

                    getActivity().finish();
               }

            } else if (status == 0) {

                AlertDialogHelper.showAlertDialog(getActivity(),
                        msg, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(getActivity(),
                        getString(R.string.server_message), "Alert");
            }
        }
    }

    public void editTextActionDone() {


        edtTxtPassword.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    authenticateLogin();
                }
                return false;
            }
        });

    }

    public void clearFields() {
        edtTextUserName.setText("");
        edtTxtPassword.setText("");
    }

    @OnClick(R.id.txtViewForgotPass)
    void forgotPassword() {
        Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
        startActivity(intent);
    }






}
