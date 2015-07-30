package com.example.qzero.MyAccount.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
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
 * Created by braintech on 13-Jul-15.
 */
public class DashboardFragment extends Fragment {
    @InjectView(R.id.tv_order_count)
    TextView orderCountTextView;

    @InjectView(R.id.tv_profile)
    TextView profileLableTextView;

    @InjectView(R.id.tv_order)
    TextView orderLabelTextView;

    @InjectView(R.id.layout_profile)
    LinearLayout profileLayout;

    @InjectView(R.id.ll_order)
    LinearLayout orderLayout;

    UserSession userSession;

    String userIDString;
    String walletAmount;
    String clubsount;
    String orderCount;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.inject(this, view);
        userSession = new UserSession(getActivity().getApplicationContext());

        // Setting fonts
        setFont();

        if (userSession.isUserLoggedIn()) {
            userIDString = userSession.getUserID();
        }

        new GetCounts().execute();

        return view;
    }

    @OnClick(R.id.layout_profile)
    void openProfile()
    {
        ProfileInfoFragment nextFrag= new ProfileInfoFragment();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.flContent, nextFrag,"Profile")
                .addToBackStack(null)
                .commit();
    }

    public void setFont() {
        FontHelper.applyFont(getActivity(), orderCountTextView, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), profileLableTextView, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), orderLabelTextView, FontHelper.FontType.FONT);

    }
    private class GetCounts extends AsyncTask<String, String, String> {
        JsonParser jsonParser;
        int status = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());


        }

        @Override
        protected String doInBackground(String... params) {
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.DASHBOARD_URL;

            Log.v("Dashboard:", "UserID: " + userIDString);
            Log.v("Dashboard:", "URL: " + url);
            String json = jsonParser.getJSONFromUrl(url, Const.TIME_OUT, userIDString);
            Log.v("Dashboard:", "JSON: " + json);

            try {

                if (json != null) {
                    JSONObject jsonObject = new JSONObject(json);

                    if (jsonObject.getInt(Const.TAG_STATUS) == 1) {
                        status = 1;
                        JSONObject result = jsonObject.getJSONObject(Const.TAG_RESULT);
                        orderCount = result.getString(Const.TAG_ORDER_COUNT);
                    } else {
                        status = 0;
                    }

                } else {
                    status = -1;
                }
            } catch (JSONException e) {
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgresBar.stop();
            switch (status) {
                case 1:
                    break;

                case 0:
                    AlertDialogHelper.showAlertDialog(getActivity(),
                            "Server is not responding!", "Alert");
                    break;
                case -1:
                    AlertDialogHelper.showAlertDialog(getActivity(),
                            "Server is not responding!", "Alert");
                    break;
                default:
                    break;

            }

            orderCountTextView.setText(orderCount);

        }
    }
}
