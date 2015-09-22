package com.example.qzero.Outlet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.R;


public class SearchTabFragment extends Fragment {

    FragmentTabHost mTabHost;

    View tabIndicatorVenue;
    View tabIndicatorOutlet;
    View tabIndicatorItem;

    TextView titleVenue;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_search,
                container, false);

        mTabHost = (FragmentTabHost) rootView
                .findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(),
                R.id.realtabcontent);

        inflateLayouts();

        createValueTag();
        createOutletTag();
        createItemTag();

        addTabs();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }

    public void inflateLayouts() {
        tabIndicatorVenue = LayoutInflater.from(getActivity()).inflate(
                R.layout.tabtheme_tab_indicator_holo, mTabHost.getTabWidget(),
                false);

        tabIndicatorOutlet = LayoutInflater.from(getActivity()).inflate(
                R.layout.tabtheme_tab_indicator_holo, mTabHost.getTabWidget(),
                false);

        tabIndicatorItem = LayoutInflater.from(getActivity()).inflate(
                R.layout.tabtheme_tab_indicator_holo, mTabHost.getTabWidget(),
                false);

    }

    // Creating Tab search_Value
    public void createValueTag() {

        TextView titleVenue = (TextView) tabIndicatorVenue
                .findViewById(android.R.id.title);

        titleVenue.setText("Venue");

        FontHelper.applyFont(getActivity(), titleVenue, FontType.FONT);


    }

    // Creating Tab search_Outlet
    public void createOutletTag() {

        TextView titleOutlet = (TextView) tabIndicatorOutlet
                .findViewById(android.R.id.title);

        titleOutlet.setText("Outlet");

        FontHelper.applyFont(getActivity(), titleOutlet, FontType.FONT);


    }

    // Creating Tab search_Item
    public void createItemTag() {

        TextView titleItem = (TextView) tabIndicatorItem
                .findViewById(android.R.id.title);

        titleItem.setText("Item");

        FontHelper.applyFont(getActivity(), titleItem, FontType.FONT);


    }

    public void addTabs() {

        mTabHost.addTab(
                mTabHost.newTabSpec("venue").setIndicator(tabIndicatorVenue),
                SearchVenueFragment.class, null);

        mTabHost.addTab(
                mTabHost.newTabSpec("outlet").setIndicator(tabIndicatorOutlet),
                SearchOutletFragment.class, null);

        mTabHost.addTab(
                mTabHost.newTabSpec("item").setIndicator(tabIndicatorItem),
                SearchItemFragment.class, null);
    }

}
