package com.example.qzero.Outlet.Fragments;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.qzero.Outlet.Adapters.CustomAdapterCartItem;
import com.example.qzero.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ViewCartFragment extends Fragment {

@InjectView(R.id.listCartItem)ListView listCartItem;

    ArrayList<HashMap<String,String>> mainCartItem;

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_cart, container, false);
        ButterKnife.inject(this,view);
        context = view.getContext();

        mainCartItem=new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for(int i=0;i<5;i++){
            HashMap<String,String> mySampleHashmap=new HashMap<>();
            mySampleHashmap.put("number",String.valueOf(i));
           mainCartItem.add(mySampleHashmap);
        }

        CustomAdapterCartItem adapterCartItem=new CustomAdapterCartItem(context,mainCartItem);
        listCartItem.setAdapter(adapterCartItem);


    }

//    @OnClick(R.id.show_detail)
//    void show_item(){
//
//    }

//    @OnClick(R.id.continue_shopping)
//    void continue_shopping(){
//
//    }
//
//    @OnClick(R.id.placeOrder)
//    void placeOrder(){
//
//    }


}
