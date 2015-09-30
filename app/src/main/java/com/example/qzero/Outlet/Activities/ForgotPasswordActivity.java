package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ForgotPasswordActivity extends Activity {

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;


    @InjectView(R.id.editTextEmail)
    EditText editTextEmail;

    @InjectView(R.id.imgViewSubmit)
    ImageView emailButton;

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

    JsonParser jsonParser;
    JSONObject jsonObject;

    int status;

    String message;
    String urlParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.inject(this);

        txtViewHeading.setText(getString(R.string.title_forgot));

        setFont();
    }

    public void setFont() {
        FontHelper.applyFont(this, txtViewHeading, FontHelper.FontType.FONT);

        FontHelper.applyFont(this, editTextEmail, FontHelper.FontType.FONT);
    }

    @OnClick(R.id.imgViewSubmit)
    void emailLink() {
        validateEmail();
    }

    private void validateEmail() {
        String emailid = editTextEmail.getText().toString();

        //Check if emial id is valid or not
        if (isEmailValid(emailid)) {
            sendEmailLink(emailid);
        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.forget_pwd_email), "Alert");
        }
    }

    private boolean isEmailValid(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private void sendEmailLink(String emailId) {
        if (CheckInternetHelper.checkInternetConnection(this)) {
            new PostEmailId().execute(emailId);
        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.internet_connection_message), "Alert");
        }
    }

    public class PostEmailId extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(ForgotPasswordActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.FORGOT_PASSWORD;

            try {
                urlParameters = "email="
                        + URLEncoder.encode(params[0], "UTF-8");
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

            String jsonString = jsonParser.executePost(url, urlParameters,
                    Const.TIME_OUT);

            Log.e("json", jsonString);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {

                    status = jsonObject.getInt(Const.TAG_STATUS);

                    Log.d("status", "" + status);
                    if (status == 1) {
                        message = jsonObject.getString(Const.TAG_MESSAGE);

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
                AlertDialogHelper.showAlertDialog(ForgotPasswordActivity.this, getString(R.string.success_message), "Alert");

            } else if (status == 0) {

                AlertDialogHelper.showAlertDialog(ForgotPasswordActivity.this, message, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(ForgotPasswordActivity.this,
                        getString(R.string.server_message), "Alert");
            }
        }
    }


    @OnClick(R.id.imgViewBack)
    void finishAct() {
        finish();
    }

}
