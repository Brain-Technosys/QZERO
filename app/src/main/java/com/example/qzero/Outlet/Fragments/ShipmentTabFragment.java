package com.example.qzero.Outlet.Fragments;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qzero.Outlet.Activities.BillingAddressActivity;
import com.example.qzero.Outlet.Activities.ShippingAddressActivity;
import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class ShipmentTabFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipment, container, false);
        ButterKnife.inject(this, view);

        
        return view;
    }

    @OnClick(R.id.btn_Shipping_Address)
    public void gotoShippingAddressFragment() {

        Intent intent = new Intent(getActivity(), ShippingAddressActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_Billing_Address)
    public void gotoBillingAddressFragment() {

        Intent intent = new Intent(getActivity(), BillingAddressActivity.class);
        startActivity(intent);
    }


}
