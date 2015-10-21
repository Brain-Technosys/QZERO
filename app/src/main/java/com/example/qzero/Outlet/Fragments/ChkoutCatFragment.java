package com.example.qzero.Outlet.Fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.ShippingAddSession;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.ObjectClasses.DeliveryType;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ChkoutCatFragment extends Fragment {


    FragmentTabHost mTabHost;
    Context context;

    View tabIndicatorShipment;
    View tabIndicatorPickup;
    View tabIndicatorInHouse;

    TextView titleShipment;
    TextView titlePickUp;
    TextView titleInHouse;

    LayoutInflater layoutInflater;

    UserSession userSession;
    DatabaseHelper databaseHelper;

    ArrayList<String> arrayListDeliveryName;
    ArrayList<String> arrayListDeliveryId;

    String outletId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_chkout_cat, container, false);

        ButterKnife.inject(this, view);
        context = view.getContext();

        userSession = new UserSession(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());

        mTabHost = (FragmentTabHost) view
                .findViewById(android.R.id.tabhost);

        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.realtabcontent);
        mTabHost.setup(context, getChildFragmentManager(),
                frameLayout.getId());

        inflateLayouts();

        getDeliveryType();

        createShipmentTag();
        createPickUpTag();
        createInHouseTag();

        addTabs();

        setFont();

        return view;
    }

     private void getDeliveryType() {

        Cursor cursorOutletId = databaseHelper.selectOutletId();

        if (cursorOutletId != null) {
            if (cursorOutletId.moveToFirst()) {
                outletId = cursorOutletId.getString(0);
            }
        }

        if (CheckInternetHelper.checkInternetConnection(getActivity())) {
            new GetDeliveryType().execute();
        } else {
            AlertDialogHelper.showAlertDialog(getActivity(), getString(R.string.internet_connection_message), "Alert");
        }
    }

    private class GetDeliveryType extends AsyncTask<String, String, String> {


        String message;
        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {

            JsonParser jsonParser = new JsonParser();

            arrayListDeliveryName = new ArrayList<>();
            arrayListDeliveryId = new ArrayList<>();


            String url = Const.BASE_URL + Const.GET_DELIVERY_TYPE+"outletId="+outletId;

            String userId = userSession.getUserID();


            Log.e("userId", userId);

            String jsonString = jsonParser.getJSONFromUrl(url,Const.TIME_OUT,userId);

            Log.e("json", jsonString);

            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
                    status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");
                    if (status == 1) {
                        JSONObject jsonObj = jsonObject.getJSONObject(Const.TAG_JsonObj);

                        String deliveryTypeId = jsonObject.getString(Const.TAG_DELIVERY_ID);
                        String deliveryTypeName = jsonObject.getString(Const.TAG_DELIVERY_NAME);

                        arrayListDeliveryId.add(deliveryTypeId);
                        arrayListDeliveryName.add(deliveryTypeName);
                    }

                    //  orderId=jsonObject.getInt(Const.TAG_ORDER_ID);
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

                checkTabsPresent();

            } else if (status == 0) {
                AlertDialogHelper.showAlertDialog(getActivity(),
                        message, "Alert");
            } else {
                AlertDialogHelper.showAlertDialog(getActivity(),
                        getString(R.string.server_message), "Alert");
            }
        }
    }

    private void checkTabsPresent() {

        if(arrayListDeliveryName.contains("In-House"))
        {

        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }

    public void inflateLayouts() {
        tabIndicatorShipment = LayoutInflater.from(getActivity()).inflate(
                R.layout.tabtabtheme_tab_indicator_holo, mTabHost.getTabWidget(),
                false);

        tabIndicatorPickup = LayoutInflater.from(getActivity()).inflate(
                R.layout.tabtabtheme_tab_indicator_holo, mTabHost.getTabWidget(),
                false);

        tabIndicatorInHouse = LayoutInflater.from(getActivity()).inflate(
                R.layout.tabtabtheme_tab_indicator_holo, mTabHost.getTabWidget(),
                false);

    }

    // Creating Tab search_Value
    public void createShipmentTag() {

        titleShipment = (TextView) tabIndicatorShipment
                .findViewById(android.R.id.title);

        titleShipment.setText("Shipment");

        FontHelper.applyFont(getActivity(), titleShipment, FontHelper.FontType.FONT);


    }

    // Creating Tab search_Outlet
    public void createPickUpTag() {

        titlePickUp = (TextView) tabIndicatorPickup
                .findViewById(android.R.id.title);

        titlePickUp.setText("Pick up");

        FontHelper.applyFont(getActivity(), titlePickUp, FontHelper.FontType.FONT);


    }

    // Creating Tab search_Item
    public void createInHouseTag() {

        titleInHouse = (TextView) tabIndicatorInHouse
                .findViewById(android.R.id.title);

        titleInHouse.setText("In House");

        FontHelper.applyFont(getActivity(), titleInHouse, FontHelper.FontType.FONT);


    }

    public void addTabs() {

       // if(arrayListDeliveryName.contains("Shipment")) {
            mTabHost.addTab(
                    mTabHost.newTabSpec("Shipment").setIndicator(tabIndicatorShipment),
                    ShipmentTabFragment.class, null);
       // }

       // if(arrayListDeliveryName.contains("Pick Up")) {
            mTabHost.addTab(
                    mTabHost.newTabSpec("Pick Up").setIndicator(tabIndicatorPickup),
                    PickupTabFragment.class, null);
      //  }

      /*  if(arrayListDeliveryName.contains("In-House")) {*/

            mTabHost.addTab(
                    mTabHost.newTabSpec("In House").setIndicator(tabIndicatorInHouse),
                    InHouseTabFragment.class, null);
       // }
    }

    private void setFont() {

        FontHelper.applyFont(getActivity(), titleShipment, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), titlePickUp, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), titleInHouse, FontHelper.FontType.FONT);
    }


}
