package com.example.qzero.MyAccount.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private Bundle savedState;
    private boolean saved;
    private static final String _FRAGMENT_STATE = "FRAGMENT_STATE";

    String userIDString;
    String orderCount;

    CheckInternetHelper internetHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.inject(this, view);

        getActivity().setTitle(getString(R.string.dashboard_title));

        internetHelper = new CheckInternetHelper();
        userSession = new UserSession(getActivity().getApplicationContext());

        if (internetHelper.checkInternetConnection(getActivity())) {
            // Getting counts of Order, Wallet etc

            if (savedInstanceState != null && savedState == null) {
                savedState = savedInstanceState.getBundle("instance");

            }
            if (savedState != null) {
                orderCountTextView.setText(savedState.getString("order_count"));
            } else {
                new GetCounts().execute();
            }


            // Checking session and getting user ID from session
            if (userSession.isUserLoggedIn()) {
                userIDString = userSession.getUserID();
            }
        } else {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getActivity().getString(R.string.internet_connection_message), "Alert");
        }
        // Setting fonts
        setFont();

        savedState = null;

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        //Save the fragment's instance
        outState.putBundle("instance", (savedState != null) ? savedState : saveState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedState = saveState();
    }


    // Click event of Profile
    @OnClick(R.id.layout_profile)
    void openProfile() {

        ProfileInfoFragment nextFrag = new ProfileInfoFragment();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.flContent, nextFrag, "Profile")
                .addToBackStack(null)
                .commit();

//        this.getFragmentManager().beginTransaction()
//                .hide(getFragmentManager().findFragmentByTag(this.getTag()))
//                .add(R.id.flContent, nextFrag, nextFrag.getClass().getName())
//                .addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();


    }

    // Click event of Order
    @OnClick(R.id.ll_order)
    void openOrder() {

        OrderFragment fragment = new OrderFragment();

        this.getFragmentManager().beginTransaction().replace(R.id.flContent, fragment, fragment.getClass().getName()).addToBackStack(null).commit();

//        this.getFragmentManager().beginTransaction()
//                .hide(getFragmentManager().findFragmentByTag(this.getTag()))
//                .add(R.id.flContent, fragment, fragment.getClass().getName())
//                .addToBackStack(fragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();


    }

    private Bundle saveState() { /* called either from onDestroyView() or onSaveInstanceState() */
        Bundle state = new Bundle();
        state.putString("order_count", orderCount);
        return state;
    }

    // Method to set Fonts
    public void setFont() {
        FontHelper.applyFont(getActivity(), orderCountTextView, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), profileLableTextView, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), orderLabelTextView, FontHelper.FontType.FONT);

    }

    // Asynchronous class to get counts
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
