package com.example.qzero.CommonFiles.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.ShippingAddSession;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Activities.FinalChkoutActivity;
import com.example.qzero.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Braintech on 10/16/2015.
 */
public class GetCheckOutDetails {

    ShippingAddSession shippingAddSession;

    UserSession userSession;

    Activity context;

    DatabaseHelper databaseHelper;

    String outletId;
    String className;

    public GetCheckOutDetails(Activity context,String className) {

        this.context = context;
        this.className=className;
    }

    public void managingChkoutDetailAPI() {
        shippingAddSession = new ShippingAddSession(context);

        userSession = new UserSession(context);

        databaseHelper = new DatabaseHelper(context);

        Cursor cursorOutletId = databaseHelper.selectOutletId();

        if (cursorOutletId != null) {
            if (cursorOutletId.moveToFirst()) {
                outletId = cursorOutletId.getString(0);
            }
        }


        if (CheckInternetHelper.checkInternetConnection(context))
            new GetChkOutDetail().execute(outletId);
        else
            AlertDialogHelper.showAlertDialog(context, String.valueOf(R.string.internet_connection_message), "Alert");

    }

    private class GetChkOutDetail extends AsyncTask<String, String, String> {

        JsonParser jsonParser;
        JSONObject jsonObject;

        String url;
        int status = -1;
        String msg;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            jsonParser = new JsonParser();

            try {
                url = Const.BASE_URL + Const.GET_CHKOUT_DETAIL + URLEncoder.encode(strings[0], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT, userSession.getUserID());

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
                    status = jsonObject.getInt(Const.TAG_STATUS);
                    msg = jsonObject.getString(Const.TAG_MESSAGE);
                    if (status == 1) {
                        JSONObject jsonObjResult = jsonObject.getJSONObject(Const.TAG_RESULT);
                        shippingAddSession.saveChkOutDetail(jsonObjResult.toString());

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ProgresBar.stop();

            if (status == 1) {

                if(className.equals("addedit")) {
                //do nothing
                }
                else {
                    Intent intent = new Intent(context, FinalChkoutActivity.class);
                    if (className.equals("login")) {
                        context.finish();
                    }
                    context.startActivity(intent);
                }
            } else if (status == 0) {
                AlertDialogHelper.showAlertDialog(context, msg, "Alert");
            } else if (status == -1) {
                AlertDialogHelper.showAlertDialog(context, context.getString(R.string.server_message), "Alert");

            }

        }
    }
}
