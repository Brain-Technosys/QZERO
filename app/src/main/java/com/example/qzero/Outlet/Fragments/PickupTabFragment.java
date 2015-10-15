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

    DatabaseHelper databaseHelper;

    UserSession userSession;

    ArrayList<OrderItemStatusModel> orderStatusArrayList;

    //set the environment for production/sandbox/no netowrk
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    private static final String CONFIG_CLIENT_ID = "ASEg2Vc9lKh1QephLG7NYj4kzcL6MuMzo4GIGeMM5zqaDjxYwtliRgJkxnZx6utGsSfb81Kok3atIvR4";

    private static final int REQUEST_PAYPAL_PAYMENT = 1;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("QZERO");


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

        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);
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
                discountAmount =outletCursor.getDouble(3);
                afterDiscountAmount = Double.parseDouble(outletCursor.getString(4));

            }
        }
        totalAmount = Double.parseDouble(userSession.getFinalPaybleAmount());

        JSONObject jsonObjDetails = new JSONObject();
        try {
            jsonObjDetails.put("itemId", itemId);
            jsonObjDetails.put("outletId", outletId);
            jsonObjDetails.put("discountAmount",discountAmount);
            jsonObjDetails.put("afterDiscountAmount", afterDiscountAmount);
            jsonObjDetails.put("totalAmount", totalAmount);
           /* jsonObjDetails.put("taxApplicable",taxApplicable);
            jsonObjDetails.put("tax",tax);
            jsonObjDetails.put("tableNoId",tableNoId);
            jsonObjDetails.put("tableNO",tableNO );
            jsonObjDetails.put("orderNotes",orderNotes );
            jsonObjDetails.put("billingAddressId",billingAddressId );
            jsonObjDetails.put("deliveryTypeId",deliveryTypeId);*/
            jsonObjDetails.put("deliveryType",3);

            JSONArray jsonArrayOrder = new JSONArray();
            JSONArray jsonArrayMod = new JSONArray();

            getOrderStatusData();
            for (int i = 0; i < orderStatusArrayList.size(); i++) {

                JSONObject orderStatusObj = new JSONObject();

                JSONObject modStatusObj=new JSONObject();

                String modName = orderStatusArrayList.get(i).getMod_name();
                if (modName.equals("null")) {
                    isModifier = 0;
                } else {
                    isModifier = 1;

                    modifierId=orderStatusArrayList.get(i).getMod_id();
                    modifierPrice=orderStatusArrayList.get(i).getMod_price();

                    modStatusObj.put("itemId", itemId);
                    modStatusObj.put("modifierId",modifierId);
                    modStatusObj.put("modifierPrice",modifierPrice);

                    jsonArrayMod.put(modStatusObj);
                }

                quantity = orderStatusArrayList.get(i).getQuantity();

                orderStatusObj.put("itemId",itemId);
                orderStatusObj.put("isModifier",isModifier);
                orderStatusObj.put("quantity",quantity);

                jsonArrayOrder.put(orderStatusObj);


            }

            jsonObjDetails.putOpt("orderItemStatus",jsonArrayOrder);
            jsonObjDetails.putOpt("orderItemModifiers",jsonArrayMod);

            Log.e("json",jsonObjDetails.toString());

            postToCheckOut(jsonObjDetails.toString());

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void postToCheckOut(String jsonDetails)
    {
       if(CheckInternetHelper.checkInternetConnection(getActivity())) {
           new PostCheckOut().execute(jsonDetails);
       }
        else
       {
           AlertDialogHelper.showAlertDialog(getActivity(),getString(R.string.server_message),"Alert");
       }
    }

    private class PostCheckOut extends AsyncTask<String, String, String> {

        String message;
        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {

           JsonParser jsonParser = new JsonParser();

            String url = Const.BASE_URL + Const.POST_CHECKOUT;

            String parameter=params[0];
            String userId=userSession.getUserID();

            Log.e("parameter",parameter);
            Log.e("userId",userId);

            String jsonString = jsonParser.executePost(url,parameter,userId,Const.TIME_OUT);

            Log.e("json",jsonString);


            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
                   status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");
                    if (status == 1) {

                    }

                  //  orderId=jsonObject.getInt(Const.TAG_ORDER_ID);
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

            if(status==1) {

                PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(((FinalChkoutActivity) getActivity()).getFinalPrice()), "USD", "QZERO",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(getActivity(), PaymentActivity.class);

                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

                startActivityForResult(intent, REQUEST_PAYPAL_PAYMENT);
            }
            else
                if(status==0)
                {
                    AlertDialogHelper.showAlertDialog(getActivity(),
                            message, "Alert");
                }
            else
                {
                    AlertDialogHelper.showAlertDialog(getActivity(),
                            getString(R.string.server_message), "Alert");
                }
        }
    }

    private void getOrderStatusData() {

        orderStatusArrayList=new ArrayList<>();
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
                                    int indexModActualId=modCursor.getColumnIndex(databaseHelper.MOD_ACTUAL_ID);

                                    String mod_name = modCursor.getString(indexname);
                                    String mod_price = modCursor.getString(indexprice);
                                    String quantity = modCursor.getString(indexqty);
                                    String mod_id=modCursor.getString(indexModActualId);

                                    OrderItemStatusModel orderItemStatusModel = new OrderItemStatusModel(mod_name, quantity, true,mod_price,mod_id);
                                    orderStatusArrayList.add(orderItemStatusModel);
                                }
                            }

                        } while (itemIdCursor.moveToNext());
                    }

                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println("Responseeee" + confirm);
                        Log.i("paymentExample", confirm.toJSONObject().toString());


                        JSONObject jsonObj = new JSONObject(confirm.toJSONObject().toString());

                        String paymentId = jsonObj.getJSONObject("response").getString("id");
                        System.out.println("payment id:-==" + paymentId);
                        Toast.makeText(getActivity(), paymentId, Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment was submitted. Please see the docs.");
            }
        }


    }


}
