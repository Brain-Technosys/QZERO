package com.example.qzero.Outlet.Fragments;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.Outlet.Activities.FinalChkoutActivity;
import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class InHouseTabFragment extends Fragment {

    Context context;

    @InjectView(R.id.spnr_selectTable)
    Spinner spnr_selectTable;

    String[] tableTitle = {"Select Table no.","Tb1","Tb2","Tb3"};
    ArrayAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_house_tab, container, false);

        ButterKnife.inject(this, view);

        context = view.getContext();

       // ((FinalChkoutActivity) getActivity()).getFinalPrice()
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

     //   ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_item);

        adapter = new ArrayAdapter(context,R.layout.layout_spinner, tableTitle);
        adapter.setDropDownViewResource(R.layout.layout_spinner_drop_down);
        spnr_selectTable.setAdapter(adapter);
    }


    @OnClick(R.id.btn_Place_Order)public void place_order(){

    }
}
