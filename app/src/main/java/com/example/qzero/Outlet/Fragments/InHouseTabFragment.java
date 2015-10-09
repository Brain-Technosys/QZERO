package com.example.qzero.Outlet.Fragments;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class InHouseTabFragment extends Fragment {

    Context context;

    @InjectView(R.id.spnr_selectTable)
    Spinner spnr_selectTable;

    String[] tableTitle = {"Select Table no."};
    ArrayAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_house_tab, container, false);

        ButterKnife.inject(this, view);

        context = view.getContext();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, tableTitle);
        spnr_selectTable.setAdapter(adapter);
    }


    @OnClick(R.id.btn_Place_Order)public void place_order(){

    }
}
