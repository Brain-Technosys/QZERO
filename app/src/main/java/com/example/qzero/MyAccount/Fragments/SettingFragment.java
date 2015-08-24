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
import android.widget.EditText;

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
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Braintech on 7/31/2015.
 */
public class SettingFragment extends Fragment {

    public final Pattern PASSWORD_PATTERN = Pattern
            .compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!^&*~)(@#$%]).{6,20}");

    @InjectView(R.id.et_old_password)
    EditText oldPasswordEditText;

    @InjectView(R.id.et_new_password)
    EditText newPasswordEditText;

    @InjectView(R.id.et_cnf_password)
    EditText cnfPasswordEditText;

    @InjectView(R.id.btn_save)
    Button saveButton;

    CheckInternetHelper internetHelper;
    UserSession userSession;

    String oldPassword;
    String newPassword;
    String cnfPassowrd;

    boolean isSaveNotClicked = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.inject(this, view);

        getActivity().setTitle(getString(R.string.setting_title));
        setFonts();

        return view;
    }

    // Click event of save password button
    @OnClick(R.id.btn_save)
    public void savePassword() {
        oldPassword = oldPasswordEditText.getText().toString();
        newPassword = newPasswordEditText.getText().toString();
        cnfPassowrd = cnfPasswordEditText.getText().toString();

        // Checking Internet connectivity
        if (internetHelper.checkInternetConnection(getActivity())) {
            userSession = new UserSession(getActivity().getApplicationContext());
            // Getting values from Edit Text and validating
            if (isValid(getValues())) {
                if (isSaveNotClicked) {
                    new ChangePassword().execute();

                }

            } else {
                //AlertDialogHelper.showAlertDialog(getActivity(), "Not valid", "Alert");
            }


        } else {
            AlertDialogHelper.showAlertDialog(getActivity(), getString(R.string.internet_connection_message), "Alert");
        }
    }

    // Asynchronous class to change password
    private class ChangePassword extends AsyncTask {
        String message;
        int status = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());

        }

        @Override
        protected Object doInBackground(Object[] params) {
            JsonParser jsonParser = new JsonParser();

            String url = Const.BASE_URL + Const.CHANGE_PASSWORD_URL;
            String json = jsonParser.executePost(url, getURLParams(oldPassword, newPassword, cnfPassowrd), userSession.getUserID(), Const.TIME_OUT);
            if (json != null) {
                Log.v("SettingF", "API URL : " + url);
                Log.v("SettingF", "JSON: " + json);
                Log.v("SettingF", "OLD PWD : " + oldPassword);
                Log.v("SettingF", "New PWD " + newPassword);
                Log.v("SettingF", "CNF PWD " + cnfPassowrd);
                try {

                    JSONObject jsonObject = new JSONObject(json);
                    status = jsonObject.getInt(Const.TAG_STATUS);
                    if (status == 0) {
                        message = jsonObject.getString(Const.TAG_MESSAGE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    status = -1;
                }

            } else {
                status = -1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ProgresBar.stop();


            switch (status) {
                case 1:
                    AlertDialogHelper.showAlertDialog(getActivity(), getString(R.string.password_change_success), "Alert");
                    // clearing edit text
                    oldPasswordEditText.setText("");
                    newPasswordEditText.setText("");
                    cnfPasswordEditText.setText("");
                    // Enabling
                   // isSaveNotClicked = !isSaveNotClicked;
                    break;
                case 0:
                    AlertDialogHelper.showAlertDialog(getActivity(), message, "Alert");
                    break;
                case -1:
                    AlertDialogHelper.showAlertDialog(getActivity(), getString(R.string.response_failure), "Alert");
                    break;
                default:
                    break;
            }

        }
    }

    // Method to get parameters
    private String getURLParams(String oldPassword, String newPassword, String cnfPassword) {
        JSONObject params = new JSONObject();
        try {
            params.put("oldPassword", oldPassword);
            params.put("newPassword", newPassword);
            params.put("confirmPassword", cnfPassword);
        } catch (JSONException e) {
        }
        Log.v("PARAMS: ", params.toString());
        return params.toString();
    }

    // Method to set fonts
    private void setFonts() {
        FontHelper.applyFont(getActivity(), oldPasswordEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), newPasswordEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), cnfPasswordEditText, FontHelper.FontType.FONT);
    }

    private HashMap<String, String> getValues() {
        HashMap<String, String> values = new HashMap<String, String>();
        values.put(Const.TAG_OLD_PASSWORD, oldPasswordEditText.getText().toString().trim());
        values.put(Const.TAG_NEW_PASSWORD, newPasswordEditText.getText().toString().trim());
        values.put(Const.TAG_CNF_PASSWORD, cnfPasswordEditText.getText().toString().trim());
        return values;
    }

    // Method to validate password
    private boolean isValid(HashMap<String, String> data) {
        if (data.get(Const.TAG_OLD_PASSWORD).length() == 0) {
            oldPasswordEditText.setError("Please enter Old Password.");
            isSaveNotClicked = false;
            return false;
        } else if (data.get(Const.TAG_NEW_PASSWORD).length() == 0) {
            newPasswordEditText.setError("Please enter New Password.");
            isSaveNotClicked = false;
            return false;
        } else if (!PASSWORD_PATTERN.matcher(data.get(Const.TAG_NEW_PASSWORD)).matches()) {
            newPasswordEditText.setError(getString(R.string.password_pattern_error));
            isSaveNotClicked = false;
            return false;

        } else if (data.get(Const.TAG_CNF_PASSWORD).length() == 0) {
            cnfPasswordEditText.setError("Please enter Confirm Password.");
            isSaveNotClicked = false;
            return false;
        }
        return true;
    }
}
