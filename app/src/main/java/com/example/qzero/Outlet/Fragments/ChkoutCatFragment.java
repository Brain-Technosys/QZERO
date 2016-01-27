package com.example.qzero.Outlet.Fragments;


import android.content.Context;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ConstVarIntent;

import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;

import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.ObjectClasses.DeliveryType;
import com.example.qzero.R;


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

        getBundle();

        inflateLayouts();

        createShipmentTag();
        createPickUpTag();
        createInHouseTag();

        addTabs();

        setFont();

        return view;
    }

    private void getBundle() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();

            arrayListDeliveryName = (ArrayList<String>) bundle.getSerializable(ConstVarIntent.TAG_DELIVERY_TYPE);
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

        titleShipment.setText("Delivery");

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

        titleInHouse.setText("In-Venue");

        FontHelper.applyFont(getActivity(), titleInHouse, FontHelper.FontType.FONT);


    }

    public void addTabs() {

        Log.e("deliverytype", userSession.getDeliveryType());
        if (userSession.getDeliveryType().equals("2")) {
            if (arrayListDeliveryName.contains("Delivery")) {
                TabHost.TabSpec shipmentspec = mTabHost.newTabSpec("Delivery");
                // setting Title and Icon for the Tab
                shipmentspec.setIndicator(tabIndicatorShipment);

                mTabHost.addTab(shipmentspec, ShipmentTabFragment.class, null);
            }
        } else if (userSession.getDeliveryType().equals("3")) {


            if (arrayListDeliveryName.contains("Pick Up")) {
                mTabHost.addTab(
                        mTabHost.newTabSpec("Pick Up").setIndicator(tabIndicatorPickup),
                        PickupTabFragment.class, null);
            }
        } else {

            if (arrayListDeliveryName.contains("In-Venue")) {

                mTabHost.addTab(
                        mTabHost.newTabSpec("In-Venue").setIndicator(tabIndicatorInHouse),
                        InHouseTabFragment.class, null);
            }
        }
    }

    private void setFont() {

        FontHelper.applyFont(getActivity(), titleShipment, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), titlePickUp, FontHelper.FontType.FONT);
        FontHelper.applyFont(getActivity(), titleInHouse, FontHelper.FontType.FONT);
    }


}
