package com.example.qzero.MyAccount.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by braintech on 13-Jul-15.
 */
public class ProfileInfoFragment extends Fragment {
    UserSession userSession;
    String userIdString;
    String addressString;
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

    boolean isActive;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, null);
        ButterKnife.inject(this, view);

        userSession = new UserSession(getActivity().getApplicationContext());
        if(userSession.isUserLoggedIn())
        {
            userIdString = userSession.getUserID();
        }
        return view;
    }

    class GetProfileInfo extends AsyncTask<String, String, String> {
        JsonParser jsonParser;
        int status = 0;

        @Override
        protected void onPreExecute() {
            ProgresBar.start(getActivity());
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.PROFILE_INFO_URL;
            String json = jsonParser.getJSONFromUrl(url,Const.TIME_OUT,userIdString);
            if(json!=null)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt(Const.TAG_STATUS) == 1) {
                        status = 1;
                        JSONObject result = jsonObject.getJSONObject(Const.TAG_RESULT);

                    } else {
                        status = 0;
                    }
                }
                catch (JSONException e){}

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgresBar.stop();
        }
    }
}
