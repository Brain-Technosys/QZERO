package com.example.qzero.Outlet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Common.ConstVarIntent;
import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.ShippingAddSession;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Activities.AddAddressActivity;
import com.example.qzero.Outlet.Activities.BillingAddressActivity;
import com.example.qzero.Outlet.Activities.ShippingAddressActivity;
import com.example.qzero.Outlet.Activities.ViewCartActivity;
import com.example.qzero.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Braintech on 08-Oct-15.
 */
public class CustomAdapterBillingAddress extends BaseAdapter {

    Activity context;

    ArrayList<HashMap<String, String>> addressDetail;

    int type = 0;
    int delPos;
    int deleteId;

    ShippingAddSession shippingAddSession;

    UserSession userSession;

    Bundle bundle;

    public CustomAdapterBillingAddress(Activity context, ArrayList<HashMap<String, String>> addressDetail, int type) {
        this.context = context;
        this.addressDetail = addressDetail;
        this.type = type;
        shippingAddSession = new ShippingAddSession(context);

        userSession=new UserSession(context);
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
        Holder holder;
        if (view == null) {

            holder = new Holder();


            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.list_address, null);

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

            FontHelper.applyFont(context, holder.addressName, FontHelper.FontType.FONT);
            FontHelper.applyFont(context, holder.addressLine1, FontHelper.FontType.FONT);
            FontHelper.applyFont(context, holder.addressCity, FontHelper.FontType.FONT);
            FontHelper.applyFont(context, holder.addressState, FontHelper.FontType.FONT);
            FontHelper.applyFont(context, holder.addressCountry, FontHelper.FontType.FONT);
            FontHelper.applyFont(context, holder.addressPostcode, FontHelper.FontType.FONT);
            FontHelper.applyFont(context, holder.addressContact, FontHelper.FontType.FONT);

            //Handling all chkbox event
            handlingRadioButtonEvent(holder, view, i);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }


        holder.addressName.setText(addressDetail.get(i).get(Const.TAG_FNAME));
        holder.addressLine1.setText(addressDetail.get(i).get(Const.TAG_ADDRESS1));
        holder.addressCity.setText(addressDetail.get(i).get(Const.TAG_CITY));
        holder.addressState.setText(addressDetail.get(i).get(Const.TAG_STATE));
        holder.addressCountry.setText(addressDetail.get(i).get(Const.TAG_COUNTRY));
        holder.addressPostcode.setText(addressDetail.get(i).get(Const.TAG_ZIPCODE));
        holder.addressContact.setText(addressDetail.get(i).get(Const.TAG_PHONE_NO));

        holder.imgEdit.setTag(R.string.key_pos, i);
        holder.imgEdit.setTag(R.string.key_id, addressDetail.get(i).get(Const.TAG_BILLING_ID));

        Log.e("settag", addressDetail.get(i).get(Const.TAG_BILLING_ID));
        Log.e("Itag", "" + i);
        holder.imgDelete.setTag(R.string.key_pos, i);
        holder.imgDelete.setTag(R.string.key_id, addressDetail.get(i).get(Const.TAG_BILLING_ID));

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tag = view.getTag(R.string.key_id).toString();
                String pos = view.getTag(R.string.key_pos).toString();

                Log.e("tag", tag);

                if (context instanceof BillingAddressActivity) {
                    Intent intent = new Intent(context, AddAddressActivity.class);
                    createBundle(tag, "1", pos);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else if (context instanceof ShippingAddressActivity) {
                    Intent intent = new Intent(context, AddAddressActivity.class);
                    createBundle(tag, "0", pos);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteId = Integer.parseInt(view.getTag(R.string.key_id).toString());

                delPos = Integer.parseInt(view.getTag(R.string.key_pos).toString());

                Log.e("position", "" + delPos);

                deleteAddress();


            }
        });

        return view;
    }

    private void createBundle(String type, String addressType, String pos) {
        bundle = new Bundle();
        bundle.putString(ConstVarIntent.TAG_TYPE, type);
        bundle.putString(ConstVarIntent.TAG_TYPE_ADDRESS, addressType);
        bundle.putSerializable(ConstVarIntent.TAG_LIST_ADDRESS, addressDetail);
        bundle.putString(ConstVarIntent.TAG_POS, pos);
    }


    private void handlingRadioButtonEvent(final Holder holder, final View view, int pos) {

        holder.rb_selected_address.setTag(pos);

        //Handling chkBox selection
        if (type == 1) {
            if (pos == shippingAddSession.getShippingAddressPos()) {
                holder.rb_selected_address.setChecked(true);
                // storeAddressInSession(String.valueOf(pos));
            } else
                holder.rb_selected_address.setChecked(false);

        } else if (type == 2) {
            if (pos == shippingAddSession.getBillingAddressPos()) {
                holder.rb_selected_address.setChecked(true);
                //  storeAddressInSession(String.valueOf(pos));
            } else
                holder.rb_selected_address.setChecked(false);
        }

        //Applying Click event on chkAddress
        holder.rb_selected_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // addressDetail.get()
                String position = String.valueOf(holder.rb_selected_address.getTag());
                //radioButtonSelection(holder);
                // notifyDataSetChanged();

                storeAddressInSession(position);

            }
        });
    }


    public void storeAddressInSession(String position) {

        HashMap<String, String> hmAddressDetail = addressDetail.get(Integer.parseInt(position));
        String name = hmAddressDetail.get(Const.TAG_FNAME);
        String address = hmAddressDetail.get(Const.TAG_ADDRESS1) + ", " + hmAddressDetail.get(Const.TAG_CITY) + ", " + hmAddressDetail.get(Const.TAG_STATE) + ", " +
                hmAddressDetail.get(Const.TAG_COUNTRY) + ", " + hmAddressDetail.get(Const.TAG_ZIPCODE);
        String contact = hmAddressDetail.get(Const.TAG_PHONE_NO);

        //For Shipping Address
        if (type == 1) {
            shippingAddSession.saveShippingName(name);
            shippingAddSession.saveShippingAddressDetail(address);
            shippingAddSession.saveShippingContact(contact);
            shippingAddSession.saveShippingAddressPos(Integer.parseInt(position));
            ((ShippingAddressActivity) context).notifyAdapter();

            // for Billing Address
        } else if (type == 2) {
            shippingAddSession.saveBillingName(name);
            shippingAddSession.saveBillingAddress(address);
            shippingAddSession.saveBillingContact(contact);
            shippingAddSession.saveBillingAddressPos(Integer.parseInt(position));
            ((BillingAddressActivity) context).notifyAdapter();

        }
    }

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

    private void deleteAddress() {
        String addressType;
        if (context instanceof BillingAddressActivity)
            addressType = "billing";
        else
            addressType = "shipping";

        if (CheckInternetHelper.checkInternetConnection(context)) {
            new DeleteAddress().execute(addressType);
        } else {
            AlertDialogHelper.showAlertDialog(context,
                    context.getString(R.string.internet_connection_message), "Alert");
        }
    }

    private class DeleteAddress extends AsyncTask<String, String, String> {

        int status = -1;

        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(context);
        }

        @Override
        protected String doInBackground(String... params) {

            JsonParser jsonParser = new JsonParser();


            String url = Const.BASE_URL + Const.DELETE_ADDRESS + deleteId+"?type="+params[0];

            Log.e("urel",url);


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT,userSession.getUserID());

            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
                    status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");
                    if (status == 1) {

                    }

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                status = -1;
            } catch (JSONException e) {

                e.printStackTrace();
                status = -1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            ProgresBar.stop();

            if (status == 1) {

                addressDetail.remove(delPos);

                notifyDataSetChanged();

            } else if (status == 0) {

                AlertDialogHelper.showAlertDialog(context,
                        message, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(context,
                        context.getString(R.string.server_message), "Alert");
            }
        }
    }
}
