package com.example.qzero.Outlet.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.ActionBarPolicy;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Common.ConstVarIntent;
import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.GetCheckOutDetails;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.ShippingAddSession;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Activities.AddAddressActivity;
import com.example.qzero.Outlet.Activities.BillingAddressActivity;
import com.example.qzero.Outlet.Activities.FinalChkoutActivity;
import com.example.qzero.Outlet.Activities.ShippingAddressActivity;
import com.example.qzero.Outlet.ObjectClasses.DbModifiers;
import com.example.qzero.Outlet.ObjectClasses.OrderItemStatusModel;
import com.example.qzero.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
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

    @InjectView(R.id.txt_user_bill_add)
    TextView txt_user_bill_add;

    @InjectView(R.id.txt_user_ship)
    TextView txt_user_ship;

    @InjectView(R.id.et_orderNote)
    EditText et_orderNote;

    @InjectView(R.id.btn_add_new_billing_address)
    Button btn_add_new_billing_address;

    @InjectView(R.id.btn_add_new_shipping_address)
    Button btn_add_new_shipping_address;

    @InjectView(R.id.rly_shippingAddress)
    RelativeLayout rly_shippingAddress;

    @InjectView(R.id.rly_billing_address)
    RelativeLayout rly_billing_address;

    @InjectView(R.id.rly_shipping_addressChoice)
    RelativeLayout rly_shipping_addressChoice;

    @InjectView(R.id.chk_shipmentChoice)
    CheckBox chk_shipmentChoice;

    @InjectView(R.id.txt_msg_billing)
    TextView txt_msg_billing;

    @InjectView(R.id.txt_msg_shipping)
    TextView txt_msg_shipping;

    Context context;

    int outletId;
    Double totalAmount;
    Double discountAmount;
    Double afterDiscountAmount;
    String taxApplicable;
    String tax;
    String tableNoId;
    String tableNO;
    String orderNotes;
    String billingAddressId;
    String deliveryTypeId;
    String deliveryType;
    int itemId;

    int orderId;

    String quantity;
    int isModifier;

    String mod_name;
    String mod_qty;

    String modifierId;
    String modifierPrice;


    //Session
    ShippingAddSession shippingAddSession;

    HashMap<String, String> hmBillAddressDetail;
    String billing_id;
    String bill_add_name;
    String bill_add_address;
    String bill_add_contact;

    HashMap<String, String> hmShipAddressDetail;
    String shipping_id;
    String ship_add_name;
    String ship_add_address;
    String ship_add_contact;

    DatabaseHelper databaseHelper;

    UserSession userSession;

    Bundle bundle;

    ArrayList<OrderItemStatusModel> orderStatusArrayList;
    ArrayList<OrderItemStatusModel> orderItemStatusArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipment, container, false);

        ButterKnife.inject(this, view);

        context = view.getContext();

        setFont();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        shippingAddSession = new ShippingAddSession(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        userSession = new UserSession(getActivity());

        hmBillAddressDetail = new HashMap<>();
        hmShipAddressDetail = new HashMap<>();

    }

    private void setFont() {
        FontHelper.applyFont(context, txt_logo_billing_add, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(context, txt_logo_shipping_add, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(context, txt_logo_order_note, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(context, et_orderNote, FontHelper.FontType.FONT);

        FontHelper.applyFont(context, txt_user_bill_add, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, txt_billing_address, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, txt_billingContact, FontHelper.FontType.FONT);

        FontHelper.applyFont(context, txt_user_ship, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, txt_shipping_address, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, txt_shipping_contact, FontHelper.FontType.FONT);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onresume", "onresume");

        hmBillAddressDetail.clear();
        hmShipAddressDetail.clear();

        GetCheckOutDetails getCheckOutDetails = new GetCheckOutDetails(getActivity(), "addedit");
        getCheckOutDetails.managingChkoutDetailAPI();

        new GetAddressAtFirstPosition().execute();
    }

    private void setAddressDetail() {

        if (shippingAddSession.getBillingAddress() == null) {

            if (hmBillAddressDetail.isEmpty()) {
                btn_add_new_billing_address.setVisibility(View.VISIBLE);
                rly_billing_address.setVisibility(View.GONE);
                txt_msg_billing.setVisibility(View.VISIBLE);

            } else {
                btn_add_new_billing_address.setVisibility(View.INVISIBLE);
                rly_billing_address.setVisibility(View.VISIBLE);
                txt_msg_billing.setVisibility(View.GONE);

                //Updating Billing Address
                txt_user_bill_add.setText(bill_add_name);
                txt_billing_address.setText(bill_add_address);
                txt_billingContact.setText(bill_add_contact);
            }
        } else {

            btn_add_new_billing_address.setVisibility(View.INVISIBLE);
            rly_billing_address.setVisibility(View.VISIBLE);
            txt_msg_billing.setVisibility(View.GONE);

            //Updating Billing Address
            txt_user_bill_add.setText(shippingAddSession.getBillingName());
            txt_billing_address.setText(shippingAddSession.getBillingAddress());
            txt_billingContact.setText(shippingAddSession.getBillingContact());

            shippingAddSession.clearBillingSharPref();
        }


        if (shippingAddSession.getShippingAddress() == null) {


            if (chk_shipmentChoice.isChecked()) {
                btn_add_new_shipping_address.setVisibility(View.INVISIBLE);
                rly_shippingAddress.setVisibility(View.GONE);
                txt_msg_shipping.setVisibility(View.GONE);

            } else if (hmShipAddressDetail.isEmpty()) {
                if (!chk_shipmentChoice.isChecked()) {
                    btn_add_new_shipping_address.setVisibility(View.VISIBLE);
                    rly_shippingAddress.setVisibility(View.GONE);
                    txt_msg_shipping.setVisibility(View.VISIBLE);
                }


            } else {

                rly_shippingAddress.setVisibility(View.VISIBLE);

                txt_msg_shipping.setVisibility(View.GONE);

                btn_add_new_shipping_address.setVisibility(View.GONE);

                //Updating Shipping Address
                txt_user_ship.setText(ship_add_name);
                txt_shipping_address.setText(ship_add_address);
                txt_shipping_contact.setText(ship_add_contact);
            }

        } else {
            rly_shippingAddress.setVisibility(View.VISIBLE);

            txt_msg_shipping.setVisibility(View.GONE);
            //Updating Shipping Address
            txt_user_ship.setText(shippingAddSession.getShippingName());
            txt_shipping_address.setText(shippingAddSession.getShippingAddress());
            txt_shipping_contact.setText(shippingAddSession.getShippingContact());

            shippingAddSession.clearShippingSharPref();
        }

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

    @OnClick(R.id.btn_add_new_billing_address)
    void addBillingAddress() {
        Intent intent = new Intent(getActivity(), AddAddressActivity.class);
        createBundle("0", "1");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.btn_add_new_shipping_address)
    void addShippingAddress() {
        Intent intent = new Intent(getActivity(), AddAddressActivity.class);

        createBundle("0", "0");

        intent.putExtras(bundle);

        startActivity(intent);
    }

    @OnClick(R.id.btn_Place_Order)
    public void place_Order() {

        createPostCheckout();
    }

    private void createBundle(String type, String addressType) {
        bundle = new Bundle();
        bundle.putString(ConstVarIntent.TAG_TYPE, type);
        bundle.putString(ConstVarIntent.TAG_TYPE_ADDRESS, addressType);
    }

    private void createPostCheckout() {
        Cursor outletCursor = databaseHelper.getCheckoutItems();

        if (outletCursor != null) {
            if (outletCursor.moveToFirst()) {

                itemId = Integer.parseInt(outletCursor.getString(1));
                outletId = Integer.parseInt(outletCursor.getString(2));
                discountAmount = outletCursor.getDouble(3);
                afterDiscountAmount = Double.parseDouble(outletCursor.getString(4));

            }
        }
        totalAmount = Double.parseDouble(userSession.getFinalPaybleAmount());

        JSONObject jsonObjDetails = new JSONObject();
        try {
            jsonObjDetails.put("outletId", outletId);
            jsonObjDetails.put("discountAmount", discountAmount);
            jsonObjDetails.put("afterDiscountAmount", afterDiscountAmount);
            jsonObjDetails.put("totalAmount", totalAmount);
            jsonObjDetails.put("billingAddressId", billingAddressId);
            jsonObjDetails.put("deliveryType","Shipment");
            jsonObjDetails.put("deliveryTypeId",2);

            JSONArray jsonArrayOrder = new JSONArray();
            JSONArray jsonArrayMod = new JSONArray();

            getOrderStatusData();

            for (int j = 0; j < orderItemStatusArrayList.size(); j++) {
                JSONObject orderStatusObj = new JSONObject();

                String modName = orderItemStatusArrayList.get(j).getMod_name();

                if (modName.equals("null")) {
                    isModifier = 0;
                } else
                    isModifier = 1;


                quantity = orderItemStatusArrayList.get(j).getQuantity();
                String status_id=orderItemStatusArrayList.get(j).getItemId();

                orderStatusObj.put("statusId",status_id);
                orderStatusObj.put("itemId", itemId);
                orderStatusObj.put("isModifier", isModifier);
                orderStatusObj.put("quantity", quantity);

                jsonArrayOrder.put(orderStatusObj);
            }

            for (int i = 0; i < orderStatusArrayList.size(); i++) {

                JSONObject modStatusObj = new JSONObject();

                String modName = orderStatusArrayList.get(i).getMod_name();
                String statusId=orderItemStatusArrayList.get(i).getItemId();

                modifierId = orderStatusArrayList.get(i).getMod_id();
                modifierPrice = orderStatusArrayList.get(i).getMod_price();

                if (modName.equals("null")) {
                    //do nothing
                } else {

                    modStatusObj.put("statusId",statusId);
                    modStatusObj.put("itemId", itemId);
                    modStatusObj.put("modifierId", modifierId);
                    modStatusObj.put("modifierPrice", modifierPrice);

                    jsonArrayMod.put(modStatusObj);
                }
            }


            jsonObjDetails.putOpt("orderItemStatus", jsonArrayOrder);
            jsonObjDetails.putOpt("orderItemModifiers", jsonArrayMod);

            Log.e("json", jsonObjDetails.toString());

            postToCheckOut(jsonObjDetails.toString());

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void postToCheckOut(String jsonDetails) {
        ((FinalChkoutActivity) getActivity()).callPostCheckout(jsonDetails);
    }


    private void getOrderStatusData() {

        orderStatusArrayList = new ArrayList<>();
        orderItemStatusArrayList = new ArrayList<>();
        Cursor distinctItemCursor = databaseHelper.getDistinctItems();

        if (distinctItemCursor != null) {
            while (distinctItemCursor.moveToNext()) {

                int index = distinctItemCursor.getColumnIndex(databaseHelper.NAME_COLUMN);

                String itemName = distinctItemCursor.getString(index);

                Cursor itemIdCursor = databaseHelper.selectItems(itemName);

                if (itemIdCursor != null) {
                    if (itemIdCursor.moveToFirst()) {
                        do {
                            ArrayList<DbModifiers> arrayListDbMod = new ArrayList<>();

                            int indexItemId = itemIdCursor.getColumnIndex(databaseHelper.ID_COLUMN);

                            String item_id = itemIdCursor.getString(indexItemId);

                            Cursor modCursor = databaseHelper.getModifiers(item_id);

                            if (modCursor != null) {

                                while (modCursor.moveToNext()) {
                                    int indexname = modCursor.getColumnIndex(databaseHelper.MOD_COLUMN);
                                    int indexprice = modCursor.getColumnIndex(databaseHelper.MOD_PRICE);
                                    int indexqty = modCursor.getColumnIndex(databaseHelper.QUANTITY);
                                    int indexModActualId = modCursor.getColumnIndex(databaseHelper.MOD_ACTUAL_ID);

                                    mod_name = modCursor.getString(indexname);
                                    String mod_price = modCursor.getString(indexprice);
                                    quantity = modCursor.getString(indexqty);
                                    String mod_id = modCursor.getString(indexModActualId);

                                    OrderItemStatusModel orderItemStatusModel = new OrderItemStatusModel(item_id,mod_name, quantity, true, mod_price, mod_id);
                                    orderStatusArrayList.add(orderItemStatusModel);
                                }
                            }

                            OrderItemStatusModel orderItemStatusModel = new OrderItemStatusModel(item_id,mod_name, quantity, true, " ", " ");
                            orderItemStatusArrayList.add(orderItemStatusModel);


                        } while (itemIdCursor.moveToNext());
                    }

                }
            }
        }
    }

    @OnCheckedChanged(R.id.chk_shipmentChoice)
    public void chk_shipmentChoice(boolean checked) {
        if (checked == true) {
            rly_shippingAddress.setVisibility(View.GONE);
            btn_add_new_shipping_address.setVisibility(View.INVISIBLE);
            txt_msg_shipping.setVisibility(View.GONE);
            btn_add_new_shipping_address.setVisibility(View.GONE);
        } else {
            if (hmShipAddressDetail.isEmpty()) {
                rly_shippingAddress.setVisibility(View.GONE);
                btn_add_new_shipping_address.setVisibility(View.VISIBLE);
                txt_msg_shipping.setVisibility(View.VISIBLE);
            } else {
                btn_add_new_shipping_address.setVisibility(View.INVISIBLE);
                rly_shippingAddress.setVisibility(View.VISIBLE);
                txt_msg_shipping.setVisibility(View.GONE);

                //Updating Billing Address
                txt_user_ship.setText(ship_add_name);
                txt_shipping_address.setText(ship_add_address);
                txt_shipping_contact.setText(ship_add_contact);
            }
        }

    }


    private class GetAddressAtFirstPosition extends AsyncTask<String, String, String> {

        JSONArray jsonArrayBillingAddressDetail;
        JSONArray jsonArrayShippingAddressDetail;

        int billingStatus = -1;
        int shippingStatus = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... strings) {

            if (shippingAddSession.getChkOutDetail() == null) {

                shippingStatus = 0;
                billingStatus = 0;
            } else {
                try {
                    JSONObject jsonObjResult = new JSONObject(shippingAddSession.getChkOutDetail());
                    Log.e("jsonObjResult", shippingAddSession.getChkOutDetail());
                    try {


                        jsonArrayShippingAddressDetail = jsonObjResult.getJSONArray(Const.TAG_CHKOUT_SHIPPING_ADDRESS);

                        if (jsonArrayShippingAddressDetail != null) {

                            shippingStatus = 1;

                            for (int i = 0; i < 1; i++) {

                                JSONObject jsonShippingAddress = jsonArrayShippingAddressDetail.getJSONObject(i);

                                hmShipAddressDetail.put(Const.TAG_CUST_ID, jsonShippingAddress.getString(Const.TAG_SHIPPING_ID));
                                hmShipAddressDetail.put(Const.TAG_CUST_ID, jsonShippingAddress.getString(Const.TAG_CUST_ID));
                                hmShipAddressDetail.put(Const.TAG_FNAME, jsonShippingAddress.getString(Const.TAG_FNAME) + " " + jsonShippingAddress.getString(Const.TAG_LNAME));
                                hmShipAddressDetail.put(Const.TAG_ADDRESS1, jsonShippingAddress.getString(Const.TAG_ADDRESS1));
                                hmShipAddressDetail.put(Const.TAG_ADDRESS2, jsonShippingAddress.getString(Const.TAG_ADDRESS2));
                                hmShipAddressDetail.put(Const.TAG_CITY, jsonShippingAddress.getString(Const.TAG_CITY));
                                hmShipAddressDetail.put(Const.TAG_STATE, jsonShippingAddress.getString(Const.TAG_STATE_NAME));
                                hmShipAddressDetail.put(Const.TAG_COUNTRY, jsonShippingAddress.getString(Const.TAG_COUNTRY_NAME));
                                hmShipAddressDetail.put(Const.TAG_ZIPCODE, jsonShippingAddress.getString(Const.TAG_ZIPCODE));
                                hmShipAddressDetail.put(Const.TAG_PHONE_NO, jsonShippingAddress.getString(Const.TAG_PHONE_NO));
                                hmShipAddressDetail.put(Const.TAG_EMAIL_ADD, jsonShippingAddress.getString(Const.TAG_EMAIL_ADD));

                            }
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    try {

                        jsonArrayBillingAddressDetail = jsonObjResult.getJSONArray(Const.TAG_CHKOUT_BILLING_ADDRESS);

                        if (jsonArrayBillingAddressDetail != null) {

                            for (int i = 0; i < 1; i++) {
                                billingStatus = 1;

                                JSONObject jsonBillingAddress = jsonArrayBillingAddressDetail.getJSONObject(i);

                                hmBillAddressDetail.put(Const.TAG_CUST_ID, jsonBillingAddress.getString(Const.TAG_BILLING_ID));
                                hmBillAddressDetail.put(Const.TAG_CUST_ID, jsonBillingAddress.getString(Const.TAG_CUST_ID));
                                hmBillAddressDetail.put(Const.TAG_CUST_ID, jsonBillingAddress.getString(Const.TAG_CUST_ID));
                                hmBillAddressDetail.put(Const.TAG_FNAME, jsonBillingAddress.getString(Const.TAG_FNAME) + " " + jsonBillingAddress.getString(Const.TAG_LNAME));
                                hmBillAddressDetail.put(Const.TAG_ADDRESS1, jsonBillingAddress.getString(Const.TAG_ADDRESS1));
                                hmBillAddressDetail.put(Const.TAG_ADDRESS2, jsonBillingAddress.getString(Const.TAG_ADDRESS2));
                                hmBillAddressDetail.put(Const.TAG_CITY, jsonBillingAddress.getString(Const.TAG_CITY));
                                hmBillAddressDetail.put(Const.TAG_STATE, jsonBillingAddress.getString(Const.TAG_STATE_NAME));
                                hmBillAddressDetail.put(Const.TAG_COUNTRY, jsonBillingAddress.getString(Const.TAG_COUNTRY_NAME));
                                hmBillAddressDetail.put(Const.TAG_ZIPCODE, jsonBillingAddress.getString(Const.TAG_ZIPCODE));
                                hmBillAddressDetail.put(Const.TAG_PHONE_NO, jsonBillingAddress.getString(Const.TAG_PHONE_NO));
                                hmBillAddressDetail.put(Const.TAG_EMAIL_ADD, jsonBillingAddress.getString(Const.TAG_EMAIL_ADD));
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ProgresBar.stop();
            if (billingStatus == 0) {

            } else {

                billing_id = hmBillAddressDetail.get(Const.TAG_BILLING_ADDRESS);
                bill_add_name = hmBillAddressDetail.get(Const.TAG_FNAME);
                bill_add_address = hmBillAddressDetail.get(Const.TAG_ADDRESS1) + ", " + hmBillAddressDetail.get(Const.TAG_CITY) + ", " + hmBillAddressDetail.get(Const.TAG_STATE) + ", " +
                        hmBillAddressDetail.get(Const.TAG_COUNTRY) + ", " + hmBillAddressDetail.get(Const.TAG_ZIPCODE);
                bill_add_contact = hmBillAddressDetail.get(Const.TAG_PHONE_NO);
            }
            if (shippingStatus == 0) {
            } else {
                shipping_id = hmShipAddressDetail.get(Const.TAG_SHIPPING_ADDRESS);
                ship_add_name = hmShipAddressDetail.get(Const.TAG_FNAME);
                ship_add_address = hmShipAddressDetail.get(Const.TAG_ADDRESS1) + ", " + hmShipAddressDetail.get(Const.TAG_CITY) + ", " + hmShipAddressDetail.get(Const.TAG_STATE) + ", " +
                        hmShipAddressDetail.get(Const.TAG_COUNTRY) + ", " + hmShipAddressDetail.get(Const.TAG_ZIPCODE);
                ship_add_contact = hmShipAddressDetail.get(Const.TAG_PHONE_NO);

            }
            setAddressDetail();
        }
    }
}
