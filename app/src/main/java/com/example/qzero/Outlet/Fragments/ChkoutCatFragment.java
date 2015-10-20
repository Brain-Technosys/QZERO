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
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater=inflater;
        View view = inflater.inflate(R.layout.fragment_chkout_cat, container, false);

        ButterKnife.inject(this, view);
        context = view.getContext();

        mTabHost = (FragmentTabHost)view
                .findViewById(android.R.id.tabhost);

        FrameLayout frameLayout=(FrameLayout)view.findViewById( R.id.realtabcontent);
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

    private void getDeliveryType()
    {
        if(CheckInternetHelper.checkInternetConnection(getActivity()))
        {
            new GetDeliveryType().execute();
        }
        else
        {
            AlertDialogHelper.showAlertDialog(getActivity(),getString(R.string.internet_connection_message),"Alert");
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

        mTabHost.addTab(
                mTabHost.newTabSpec("Shipment").setIndicator(tabIndicatorShipment),
                ShipmentTabFragment.class, null);

        mTabHost.addTab(
                mTabHost.newTabSpec("Pick Up").setIndicator(tabIndicatorPickup),
                PickupTabFragment.class, null);

        mTabHost.addTab(
                mTabHost.newTabSpec("In House").setIndicator(tabIndicatorInHouse),
                InHouseTabFragment.class, null);
    }

    private void setFont() {

        FontHelper.applyFont(getActivity(), titleShipment, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), titlePickUp, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), titleInHouse, FontHelper.FontType.FONT);
    }



}
