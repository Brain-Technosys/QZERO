package com.example.qzero.MyAccount.Activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Adapters.OrderItemsAdapter;
import com.example.qzero.MyAccount.Adapters.OrdersAdapter;
import com.example.qzero.Outlet.ObjectClasses.Order;
import com.example.qzero.Outlet.ObjectClasses.OrderItems;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class OrderedItemActivity extends Activity {

    @InjectView(R.id.imgViewBack)
    ImageView imgViewBack;

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    @InjectView(R.id.lbl_billing_address)
    TextView lblBillingAddress;

    @InjectView(R.id.lbl_shipping_address)
    TextView lblShippingAddress;

    @InjectView(R.id.txt_billing_address)
    TextView txtBillingAddress;

    @InjectView(R.id.txt_shipping_address)
    TextView txtShippingAddress;


    @InjectView(R.id.orderListView)
    ListView orderListView;

    String userID;
    String orderId;
    String billingAddress;
    String shippingAddress;

    ArrayList<OrderItems> orderItemArrayList;

    UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ordered_items);

        ButterKnife.inject(this);

        session = new UserSession(this);
        if (session.isUserLoggedIn())
            userID = session.getUserID();

        // getting data from intent
        getIntentData();

        // Setting Fonts and title
        setFont();


    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            orderId = bundle.getString("order_id");
            billingAddress = bundle.getString(Const.TAG_BILLING_ADDRESS);
            shippingAddress = bundle.getString(Const.TAG_SHIPPING_ADDRESS);
            // Log.e("orderact", orderId);
            getItemData();
        }
    }

    // Method to set the required font and activity title.
    private void setFont() {
        txtViewHeading.setText("Order Details");
        FontHelper.applyFont(this, txtViewHeading, FontHelper.FontType.FONT);
        FontHelper.applyFont(OrderedItemActivity.this, lblBillingAddress, FontHelper.FontType.FONTROBOLD);
        FontHelper.applyFont(OrderedItemActivity.this, lblShippingAddress, FontHelper.FontType.FONTROBOLD);
        FontHelper.applyFont(OrderedItemActivity.this, txtBillingAddress, FontHelper.FontType.FONT);
        FontHelper.applyFont(OrderedItemActivity.this, txtShippingAddress, FontHelper.FontType.FONT);
    }

    private void getItemData() {
        if (CheckInternetHelper.checkInternetConnection(this)) {

            new GetOrderedItems().execute();


        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.internet_connection_message), "Alert");
        }
    }


    // Async Task to fetch orders of user
    class GetOrderedItems extends AsyncTask<String, String, String> {
        JsonParser jsonParser;
        JSONArray resultJsonArray;
        String json;
        int status = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(OrderedItemActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = Const.BASE_URL + Const.GET_ORDER_ITEM + "/" + orderId;
            jsonParser = new JsonParser();
            json = jsonParser.executePost(url, "", userID, Const.TIME_OUT);
            if (json != null && json.length() != 0) {
                Log.e("order detail url: ", url);
                Log.e("Order Fragment: ", json);

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt(Const.TAG_STATUS) == 1) {
                        status = 1;
                        orderItemArrayList = new ArrayList<OrderItems>();
                        resultJsonArray = jsonObject.getJSONArray(Const.TAG_RESULT);
                        for (int i = 0; i < resultJsonArray.length(); i++) {

                            JSONObject orderJson = resultJsonArray.getJSONObject(i);

                            int orderId = orderJson.getInt(Const.TAG_ORDER_ID);
                            int itemId = orderJson.getInt(Const.TAG_ITEM_ID);

                            String itemName = orderJson.getString(Const.TAG_ITEM_NAME);
                            String timing = orderJson.getString(Const.TAG_TIMINGS);
                            String itemStatus = orderJson.getString(Const.TAG_ITEM_STATUS);
                            String itemPrice = orderJson.getString(Const.TAG_ITEM_PRICE);
                            String remarks = orderJson.getString(Const.TAG_REMARKS);
                            String itemCode = orderJson.getString(Const.TAG_ITEM_CODE);
                            String qty = orderJson.getString("items");


//                            OrderItems orderItems = new OrderItems(orderId, itemId, itemCode, itemName, timing, itemStatus, itemPrice, remarks, qty,dis);
//
//                            orderItemArrayList.add(orderItems);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    status = -1;
                }
            } else {
                status = -1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgresBar.stop();
            if (status == 1) {
                OrderItemsAdapter adapter = new OrderItemsAdapter(OrderedItemActivity.this, orderItemArrayList);
                orderListView.setAdapter(adapter);

                if (billingAddress != null && billingAddress.length() > 0) {
                    txtBillingAddress.setText(billingAddress);
                } else {
                    lblBillingAddress.setVisibility(View.GONE);
                    txtBillingAddress.setVisibility(View.GONE);
                }

                if (shippingAddress != null && shippingAddress.length() > 0) {
                    txtShippingAddress.setText(shippingAddress);
                } else {
                    lblShippingAddress.setVisibility(View.GONE);
                    txtShippingAddress.setVisibility(View.GONE);
                }

            }
        }
    }

    @OnClick(R.id.imgViewBack)
    void finishAct() {
        finish();
    }


}
