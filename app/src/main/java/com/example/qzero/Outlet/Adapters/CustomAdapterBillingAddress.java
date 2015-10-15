package com.example.qzero.Outlet.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Sessions.ShippingAddSession;
import com.example.qzero.Outlet.Activities.AddAddressActivity;
import com.example.qzero.Outlet.Activities.BillingAddressActivity;
import com.example.qzero.Outlet.Activities.ShippingAddressActivity;
import com.example.qzero.Outlet.Activities.ViewCartActivity;
import com.example.qzero.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Braintech on 08-Oct-15.
 */
public class CustomAdapterBillingAddress extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> addressDetail;
    LayoutInflater inflater;
    int type = 0;
    ShippingAddSession shippingAddSession;

    public CustomAdapterBillingAddress(Context context, ArrayList<HashMap<String, String>> addressDetail, int type) {
        this.context = context;
        this.addressDetail = addressDetail;
        this.type = type;
        shippingAddSession = new ShippingAddSession(context);
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
            view.setTag(i);

            //Adding address Detail in List
            addAddressDetail(holder, i, view);

            //Handling all click event
            applyingClickEvent(holder, view);

            //Handling all chkbox event
            handlingRadioButtonEvent(holder, view, i);
        }

        return view;
    }


    private void setID(Holder holder, View view) {

        holder.rb_selected_address = (RadioButton) view.findViewById(R.id.rb_selected_address);
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
                    Intent intent = new Intent(context, AddAddressActivity.class);
                    intent.putExtra("ADDRESSTYPE", 4);
                    context.startActivity(intent);
                } else if (context instanceof ShippingAddressActivity) {
                    Intent intent = new Intent(context, AddAddressActivity.class);
                    intent.putExtra("ADDRESSTYPE", 3);
                    context.startActivity(intent);
                }
            }
        });
    }

    private void handlingRadioButtonEvent(final Holder holder, final View view, int pos) {

        //Handling chkBox selection
        if (type == 1) {
            if (pos == shippingAddSession.getShippingAddressPos())
                holder.rb_selected_address.setChecked(true);
            else
                holder.rb_selected_address.setChecked(false);

        } else if (type == 2) {
            if (pos == shippingAddSession.getBillingAddressPos())
                holder.rb_selected_address.setChecked(true);
            else
                holder.rb_selected_address.setChecked(false);
        }

        //Applying Click event on chkAddress
        holder.rb_selected_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // addressDetail.get()
                String pos = String.valueOf(view.getTag());
                //radioButtonSelection(holder);
                notifyDataSetChanged();

                HashMap<String, String> hmAddressDetail = addressDetail.get(Integer.parseInt(pos));
                String name = hmAddressDetail.get("NAME");
                String address = hmAddressDetail.get("ADDRESSLINE1") + ", " + hmAddressDetail.get("CITY") + ", " + hmAddressDetail.get("STATE") + ", " +
                        hmAddressDetail.get("COUNTRY") + ", " + hmAddressDetail.get("POSTCODE");
                String contact = hmAddressDetail.get("CONTACT");

                //For Shipping Address
                if (type == 1) {
                    shippingAddSession.saveShippingName(name);
                    shippingAddSession.saveShippingAddressDetail(address);
                    shippingAddSession.saveShippingContact(contact);
                    shippingAddSession.saveShippingAddressPos(Integer.parseInt(pos));
                    ((ShippingAddressActivity)context).notifyAdapter();

                    // for Billing Address
                } else if (type == 2) {
                    shippingAddSession.saveBillingName(name);
                    shippingAddSession.saveBillingAddress(address);
                    shippingAddSession.saveBillingContact(contact);
                    shippingAddSession.saveBillingAddressPos(Integer.parseInt(pos));
                    ((BillingAddressActivity)context).notifyAdapter();

                }
            }
        });
    }

//    private void radioButtonSelection(Holder holder) {
//        for (int pos = 0; pos < addressDetail.size(); pos++) {
//            if (type == 1) {
//                if (pos == shippingAddSession.getShippingAddressPos())
//                    holder.rb_selected_address.setChecked(true);
//                else
//                    holder.rb_selected_address.setChecked(false);
//
//            } else if (type == 2) {
//                if (pos == shippingAddSession.getBillingAddressPos())
//                    holder.rb_selected_address.setChecked(true);
//                else
//                    holder.rb_selected_address.setChecked(false);
//            }
//        }
//    }

    private class Holder {
        TextView addressName;
        TextView addressLine1;
        TextView addressCity;
        TextView addressState;
        TextView addressCountry;
        TextView addressPostcode;
        TextView addressContact;
        RadioButton rb_selected_address;
        ImageView imgDelete;
        ImageView imgEdit;
    }
}
