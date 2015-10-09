package com.example.qzero.Outlet.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.AppCompatEditText;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddAddressActivity extends AppCompatActivity {

    @InjectView(R.id.edtTxtFirstName)

    AppCompatEditText edtTxtFirstName;

    @InjectView(R.id.edtTxtLastName)
    EditText edtTxtLastName;

    @InjectView(R.id.edtTxtAddress)
    EditText edtTxtAddress;

    @InjectView(R.id.edtTxtZipCode)
    EditText edtTxtZipCode;

    @InjectView(R.id.edtTxtCountry)
    EditText edtTxtCountry;

    @InjectView(R.id.edtTxtState)
    EditText edtTxtState;

    @InjectView(R.id.edtTxtTownCity)
    EditText edtTxtTownCity;

    @InjectView(R.id.edtTxtEmail)
    EditText edtTxtEmail;

    @InjectView(R.id.edtTxtContact)
    EditText edtTxtContact;


    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    String fname;
    String lname;
    String address;
    String zipcode;
    String country;
    String state;
    String city;
    String email;
    String contact;

    int addressType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        ButterKnife.inject(this);

        txtViewHeading.setText("Add Address");

       addressType=getIntent().getIntExtra("ADDRESSTYPE",0);



        //AddAddress  screen will also open for edit Address,fill data according to addressType

        //ShippingAddressActivity
        if(addressType==3){
            //call Api to fill data of Shipping Address
        }
        //BillingAddressActivity
        else if(addressType==4){
            //call Api to fill data of Billing Address
        }else{
            //do nothing
        }
    }





    @OnClick(R.id.btn_submit) void addAddress(){

        getValueFromEditText();

        //ShippingAddressActivity
        if(addressType==1){
            //do somthing
        }
        //BillingAddressActivity
        else if(addressType==2){
            //do somthing

        }else if(addressType==3){
            //save edited address of Shipping Address
        }else if(addressType==4){
            //save edited address of Billing Address
        }else {
            //do nothing

        }
    }

    private void getValueFromEditText() {
        fname=edtTxtFirstName.getText().toString().trim();
        lname=edtTxtLastName.getText().toString().trim();
        address=edtTxtAddress.getText().toString().trim();
        zipcode=edtTxtZipCode.getText().toString().trim();
        country=edtTxtCountry.getText().toString().trim();
        state=edtTxtState.getText().toString().trim();
        city=edtTxtTownCity.getText().toString().trim();
        email=edtTxtEmail.getText().toString().trim();
        contact=edtTxtContact.getText().toString().trim();

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }

    @OnClick(R.id.imgViewBack)void imgViewBack(){
        finish();
    }


}
