package com.example.qzero.Outlet.Fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class PickupTabFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pickup_tab, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.btn_PlaceOrder)void placeOrder(){

    }


}
