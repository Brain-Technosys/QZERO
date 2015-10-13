package com.example.qzero.Outlet.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.Activities.BillingAddressActivity;
import com.example.qzero.Outlet.Activities.FinalChkoutActivity;
import com.example.qzero.Outlet.Activities.ShippingAddressActivity;
import com.example.qzero.R;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ShipmentTabFragment extends Fragment {
    @InjectView(R.id.txt_logo_billing_add)
    TextView txt_logo_billing_add;

    @InjectView(R.id.txt_logo_shipping_add)
    TextView txt_logo_shipping_add;

    @InjectView(R.id.txt_logo_order_note)
    TextView txt_logo_order_note;

    @InjectView(R.id.txt_billing_address)
    TextView txt_billing_address;

    @InjectView(R.id.txt_billingContact)
    TextView txt_billingContact;

    @InjectView(R.id.txt_shipping_address)
    TextView txt_shipping_address;

    @InjectView(R.id.txt_shipping_contact)
    TextView txt_shipping_contact;

    @InjectView(R.id.et_orderNote)
    EditText et_orderNote;

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipment, container, false);
        ButterKnife.inject(this, view);
        context = view.getContext();

        setFont();

        //((FinalChkoutActivity) getActivity()).getFinalPrice()
        return view;
    }

    private void setFont() {
        FontHelper.applyFont(context, txt_logo_billing_add, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(context, txt_logo_shipping_add, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(context, txt_logo_order_note, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(context, txt_billing_address, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, txt_billingContact, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, txt_shipping_address, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, txt_shipping_contact, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, et_orderNote, FontHelper.FontType.FONT);

    }

    @OnClick(R.id.iv_edit_shipping_add)
    public void gotoShippingAddressFragment() {

        Intent intent = new Intent(getActivity(), ShippingAddressActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_edit_billing_add)
    public void gotoBillingAddressFragment() {

        Intent intent = new Intent(getActivity(), BillingAddressActivity.class);
        startActivity(intent);
    }


}
