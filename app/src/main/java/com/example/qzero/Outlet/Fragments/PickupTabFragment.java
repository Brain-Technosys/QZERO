package com.example.qzero.Outlet.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Activities.DashBoardActivity;
import com.example.qzero.Outlet.Activities.FinalChkoutActivity;
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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class PickupTabFragment extends Fragment {

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

    String modifierId;
    String modifierPrice;

    String mod_name;
    String mod_qty;

    DatabaseHelper databaseHelper;

    UserSession userSession;

    ArrayList<OrderItemStatusModel> orderStatusArrayList;

    ArrayList<OrderItemStatusModel> orderItemStatusArrayList;


    //set the environment for production/sandbox/no netowrk

    private static final int REQUEST_PAYPAL_PAYMENT = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pickup_tab, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        databaseHelper = new DatabaseHelper(getActivity());

        userSession = new UserSession(getActivity());
    }

    @OnClick(R.id.btn_PlaceOrder)
    void placeOrder() {

        createPostCheckout();
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
            jsonObjDetails.put("itemId", itemId);
            jsonObjDetails.put("outletId", outletId);
            jsonObjDetails.put("discountAmount", discountAmount);
            jsonObjDetails.put("afterDiscountAmount", afterDiscountAmount);
            jsonObjDetails.put("totalAmount", totalAmount);
            jsonObjDetails.put("deliveryType", 3);

            JSONArray jsonArrayOrder = new JSONArray();
            JSONArray jsonArrayMod = new JSONArray();

            getOrderStatusData();

            for (int j = 0; j < orderItemStatusArrayList.size(); j++) {
                JSONObject orderStatusObj = new JSONObject();

                String modName = orderItemStatusArrayList.get(j).getMod_name();

                if (modName.equals("null")) {
                    isModifier = 0;
                } else {
                    isModifier = 1;

                    quantity = orderItemStatusArrayList.get(j).getQuantity();

                    orderStatusObj.put("itemId", itemId);
                    orderStatusObj.put("isModifier", isModifier);
                    orderStatusObj.put("quantity", quantity);

                    jsonArrayOrder.put(orderStatusObj);
                }
                for (int i = 0; i < orderStatusArrayList.size(); i++) {

                    JSONObject modStatusObj = new JSONObject();


                    modifierId = orderStatusArrayList.get(i).getMod_id();
                    modifierPrice = orderStatusArrayList.get(i).getMod_price();

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

                                    OrderItemStatusModel orderItemStatusModel = new OrderItemStatusModel(mod_name, quantity, true, mod_price, mod_id);
                                    orderStatusArrayList.add(orderItemStatusModel);
                                }
                            }

                            OrderItemStatusModel orderItemStatusModel = new OrderItemStatusModel(mod_name, quantity, true, " ", " ");
                            orderItemStatusArrayList.add(orderItemStatusModel);


                        } while (itemIdCursor.moveToNext());
                    }

                }
            }
        }
    }

    private void postToCheckOut(String jsonDetails) {

        ((FinalChkoutActivity) getActivity()).callPostCheckout(jsonDetails);

    }


}
