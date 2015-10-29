package com.example.qzero.CommonFiles.Helpers;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Braintech on 10/30/2015.
 */
public class GCMHelper {

    Context context;

    UserSession userSession;

    String deviceId;

    public GCMHelper(Context context) {
        this.context = context;
        userSession = new UserSession(context);
    }

    public void checkRegisterDevice() {

        deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("device", deviceId);

        checkIfDeviceRegistered();

    }


    private void checkIfDeviceRegistered() {
        if (CheckInternetHelper.checkInternetConnection(context)) {
            new GetRegisteredDevice().execute();
        }
    }

    private void registerDevice() {
        if (CheckInternetHelper.checkInternetConnection(context)) {
            new RegisterDevice().execute();
        }
    }

    private void changeLoginBit() {
        if (CheckInternetHelper.checkInternetConnection(context)) {
            new DeviceLoginLogout().execute();
        }
    }

    private class GetRegisteredDevice extends AsyncTask<String, String, String> {

        int status = -1;
        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(context);
        }

        @Override
        protected String doInBackground(String... params) {

            JsonParser jsonParser = new JsonParser();

            String url = Const.BASE_URL + Const.GET_REGISTER_DEVICE;

            JSONObject jsonObjParams = new JSONObject();
            try {

                jsonObjParams.put("customerId", userSession.getUserID());
                jsonObjParams.put("token", userSession.getGcmToken());
                jsonObjParams.put("deviceKey", deviceId);
                jsonObjParams.put("deviceType", context.getString(R.string.device_type));
                jsonObjParams.put("isLogin", true);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            Log.e("jsonparams", jsonObjParams.toString());

            String jsonString = jsonParser.executePost(url, jsonObjParams.toString(),"",Const.TIME_OUT);

            Log.e("checkregister", jsonString);


            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
                    status = jsonObject.getInt("status");
                    msg = jsonObject.getString("message");
                    if (status == 1) {

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

                changeLoginBit();

            } else if (status == 0) {

                registerDevice();

            } else {

            }
        }
    }

    private class DeviceLoginLogout extends AsyncTask<String, String, String> {

        int status = -1;
        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(context);
        }

        @Override
        protected String doInBackground(String... params) {

            JsonParser jsonParser = new JsonParser();

            String url = Const.BASE_URL + Const.POST_DEVICE_LOGIN_LOGOUT;

            JSONObject jsonObjParams = new JSONObject();
            try {

                jsonObjParams.put("customerId", userSession.getUserID());
                jsonObjParams.put("token", userSession.getGcmToken());
                jsonObjParams.put("deviceKey", deviceId);
                jsonObjParams.put("deviceType", context.getString(R.string.device_type));
                jsonObjParams.put("isLogin", true);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            String jsonString = jsonParser.executePost(url, jsonObjParams.toString()," ",Const.TIME_OUT);

            Log.e("jsondevicelogin", jsonString);


            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
                    status = jsonObject.getInt("status");
                    msg = jsonObject.getString("message");
                    if (status == 1) {

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


            } else if (status == 0) {


            } else {

            }
        }
    }


    private class RegisterDevice extends AsyncTask<String, String, String> {
        int status = -1;

        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(context);
        }

        @Override
        protected String doInBackground(String... params) {

            JsonParser jsonParser = new JsonParser();

            String url = Const.BASE_URL + Const.POST_REGISTER_DEVICE;

            JSONObject jsonObjParams = new JSONObject();
            try {

                jsonObjParams.put("customerId", userSession.getUserID());
                jsonObjParams.put("token", userSession.getGcmToken());
                jsonObjParams.put("deviceKey", deviceId);
                jsonObjParams.put("deviceType", context.getString(R.string.device_type));
                jsonObjParams.put("isLogin", true);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            String jsonString = jsonParser.executePost(url, jsonObjParams.toString()," ",Const.TIME_OUT);
            Log.e("registerdevice", jsonString);

            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
                    status = jsonObject.getInt("status");
                    msg = jsonObject.getString("message");
                    if (status == 1) {

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

            } else if (status == 0) {



            } else {

            }
        }
    }
}
