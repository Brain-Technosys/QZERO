package com.example.qzero.Outlet.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.Activities.AddAddressActivity;
import com.example.qzero.Outlet.Activities.BillingAddressActivity;
import com.example.qzero.Outlet.Activities.ShippingAddressActivity;
import com.example.qzero.Outlet.Activities.ViewCartActivity;
import com.example.qzero.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Braintech on 08-Oct-15.
 */
public class CustomAdapterBillingAddress extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> addressDetail;
    LayoutInflater inflater;

    public CustomAdapterBillingAddress(Context context, ArrayList<HashMap<String, String>> addressDetail) {
        this.context = context;
        this.addressDetail = addressDetail;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return addressDetail.size();
    }

    @Override
    public Object getItem(int i) {
        return addressDetail.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        if (view == null) {
            view = inflater.inflate(R.layout.list_address, null);

            setID(holder, view);

            setFont(holder);

            addAddressDetail(holder, i, view);

            applyingClickEvent(holder, view);
        }

        return view;
    }


    private void setID(Holder holder, View view) {

        holder.chkAddress = (CheckBox) view.findViewById(R.id.chk_selected_address);
        holder.addressName = (TextView) view.findViewById(R.id.txtName);
        holder.addressLine1 = (TextView) view.findViewById(R.id.addressLine1);
        holder.addressCity = (TextView) view.findViewById(R.id.addressCity);
        holder.addressState = (TextView) view.findViewById(R.id.addressState);
        holder.addressCountry = (TextView) view.findViewById(R.id.addressCountry);
        holder.addressPostcode = (TextView) view.findViewById(R.id.addressPinCode);
        holder.addressContact = (TextView) view.findViewById(R.id.addressContact);
        holder.imgDelete = (ImageView) view.findViewById(R.id.deleteAddress);
        holder.imgEdit = (ImageView) view.findViewById(R.id.edit_address);

    }


    private void setFont(Holder holder) {
        FontHelper.applyFont(context, holder.addressName, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.addressLine1, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.addressCity, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.addressState, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.addressCountry, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.addressPostcode, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.addressContact, FontHelper.FontType.FONT);
    }

    private void addAddressDetail(Holder holder, int i, View view) {

        holder.addressName.setText(addressDetail.get(i).get("NAME"));
        holder.addressLine1.setText(addressDetail.get(i).get("ADDRESSLINE1"));
        holder.addressCity.setText(addressDetail.get(i).get("CITY"));
        holder.addressState.setText(addressDetail.get(i).get("STATE"));
        holder.addressCountry.setText(addressDetail.get(i).get("COUNTRY"));
        holder.addressPostcode.setText(addressDetail.get(i).get("POSTCODE"));
        holder.addressContact.setText(addressDetail.get(i).get("CONTACT"));

    }

    public void applyingClickEvent(Holder holder, View view) {
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (context instanceof BillingAddressActivity) {
                    Intent i = new Intent(context, AddAddressActivity.class);
                    i.putExtra("ADDRESSTYPE", 4);
                    context.startActivity(i);
                } else if (context instanceof ShippingAddressActivity) {
                    Intent i = new Intent(context, AddAddressActivity.class);
                    i.putExtra("ADDRESSTYPE", 3);
                    context.startActivity(i);
                }
            }
        });
    }


    private class Holder {
        TextView addressName;
        TextView addressLine1;
        TextView addressCity;
        TextView addressState;
        TextView addressCountry;
        TextView addressPostcode;
        TextView addressContact;
        CheckBox chkAddress;
        ImageView imgDelete;
        ImageView imgEdit;
    }
}
