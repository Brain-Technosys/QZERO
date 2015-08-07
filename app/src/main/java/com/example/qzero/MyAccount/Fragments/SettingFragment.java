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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Braintech on 7/31/2015.
 */
public class SettingFragment extends Fragment {

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
            if (isValid()) {
                if(isSaveNotClicked){
                    new ChangePassword().execute();
                    isSaveNotClicked = !isSaveNotClicked;
                }

            } else {
                AlertDialogHelper.showAlertDialog(getActivity(), "Not valid", "Alert");
            }


        } else {
            AlertDialogHelper.showAlertDialog(getActivity(), getString(R.string.internet_connection_message), "Alert");
        }
    }

    // Asynchronous class to change password
    private class ChangePassword extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());

        }

        @Override
        protected Object doInBackground(Object[] params) {
            JsonParser jsonParser = new JsonParser();
            int status;
            String url = Const.BASE_URL + Const.CHANGE_PASSWORD_URL;
            String json = jsonParser.executePost(url, getURLParams(oldPassword, newPassword, cnfPassowrd), userSession.getUserID(), Const.TIME_OUT);
            if (json != null) {
                Log.v("SettingF", "JSON: " + json);
            } else {
                status = -1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ProgresBar.stop();
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

        return params.toString();
    }

    // Method to set fonts
    private void setFonts() {
        FontHelper.applyFont(getActivity(), oldPasswordEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), newPasswordEditText, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), cnfPasswordEditText, FontHelper.FontType.FONT);
    }

    // Method to validate password
    private boolean isValid() {
        return true;
    }
}
