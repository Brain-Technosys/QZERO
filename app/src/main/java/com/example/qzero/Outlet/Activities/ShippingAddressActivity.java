package com.example.qzero.Outlet.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qzero.Outlet.Adapters.CustomAdapterBillingAddress;
import com.example.qzero.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShippingAddressActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.listShippingAddress)
    ListView listShippingAddress;

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    ArrayList<HashMap<String, String>> listAddress;

    Button btnAddAddress;
    Button btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        ButterKnife.inject(this);
        listAddress = new ArrayList<>();

        txtViewHeading.setText("Shipping Address");
        inflateAddressList();

    }

    private void inflateAddressList() {

        HashMap<String, String> hmAddressDetail = new HashMap<>();
        hmAddressDetail.put("NAME", "Braintechnosys pvt. ltd");
        hmAddressDetail.put("ADDRESSLINE1", "B-84,D Block");
        hmAddressDetail.put("CITY", "Noida,sec 63");
        hmAddressDetail.put("STATE", "UP");
        hmAddressDetail.put("COUNTRY", "India");
        hmAddressDetail.put("POSTCODE", "110017");
        hmAddressDetail.put("CONTACT", "999999999");

        listAddress.add(hmAddressDetail);

        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_billing_address, null, false);

        btnAddAddress = (Button) footerView.findViewById(R.id.btn_addNew);
        btnPlaceOrder = (Button) footerView.findViewById(R.id.btn_PlaceOrder);

        btnPlaceOrder.setOnClickListener(this);
        btnAddAddress.setOnClickListener(this);

        listShippingAddress.addFooterView(footerView);
        CustomAdapterBillingAddress adapter = new CustomAdapterBillingAddress(ShippingAddressActivity.this, listAddress);
        listShippingAddress.setAdapter(adapter);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.btn_addNew:
                Intent i = new Intent(ShippingAddressActivity.this, AddAddressActivity.class);
                i.putExtra("ADDRESSTYPE", 1);
                startActivity(i);
                break;

            case R.id.btn_PlaceOrder:
                break;


        }

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }

    @OnClick(R.id.imgViewBack)
    void imgViewBack() {
        finish();
    }

    @OnClick(R.id.edit_address)
    void editAddress() {
        Intent i = new Intent(ShippingAddressActivity.this, AddAddressActivity.class);
        i.putExtra("ADDRESSTYPE", 3);
        startActivity(i);
    }
}
