package com.example.qzero.MyAccount.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Activities.OrderedItemActivity;
import com.example.qzero.MyAccount.Adapters.OrdersAdapter;
import com.example.qzero.Outlet.ObjectClasses.Order;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;


/**
 * Created by braintech on 13-Jul-15.
 */
public class OrderFragment extends Fragment {
    UserSession session;
    String userID;
    ArrayList<Order> orderArrayList;

    @InjectView(R.id.orderListView)
    ListView orderListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.inject(this, view);

        getActivity().setTitle(getString(R.string.order_title));

        session = new UserSession(getActivity().getApplicationContext());
        if (session.isUserLoggedIn())
            userID = session.getUserID();


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        new GetOrders().execute();
    }

    // Item click event of order list view
    @OnItemClick(R.id.orderListView)
    void onItemClick(int pos) {

        Order order=orderArrayList.get(pos);

        String orderId=String.valueOf(order.getOrderId());

        Log.e("order",""+orderId);

        Intent intent=new Intent(getActivity(), OrderedItemActivity.class);
        intent.putExtra("order_id",orderId);
        startActivity(intent);
    }

    // Async Task to fetch orders of user
    class GetOrders extends AsyncTask<String, String, String> {
        JsonParser jsonParser;
        JSONArray resultJsonArray;
        String json;
        int status = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            String url = Const.BASE_URL + Const.GET_ORDER_URL;
            jsonParser = new JsonParser();
            json = jsonParser.executePost(url, "", userID, Const.TIME_OUT);
            if (json != null && json.length() != 0) {
                Log.v("Order Fragment: ", json);

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt(Const.TAG_STATUS) == 1) {
                        status = 1;
                        orderArrayList = new ArrayList<Order>();
                        resultJsonArray = jsonObject.getJSONArray(Const.TAG_RESULT);
                        for (int i = 0; i < resultJsonArray.length(); i++) {


                            JSONObject orderJson = resultJsonArray.getJSONObject(i);

                            int orderId = orderJson.getInt(Const.TAG_ORDER_ID);
                            String purchaseDate = orderJson.getString(Const.TAG_PURCHASE_DATE);
                            boolean isShipped = orderJson.getBoolean(Const.TAG_IS_SHIPPED);
                            String orderStatus = orderJson.getString(Const.TAG_ORDER_STATUS);
                            int itemCount = orderJson.getInt(Const.TAG_ITEM_COUNT);
                            String customer = orderJson.getString(Const.TAG_CUSTOMER);
                            String shippingAddress = orderJson.getString(Const.TAG_SHIPPING_ADDRESS);
                            String billingAddress = orderJson.getString(Const.TAG_BILLING_ADDRESS);
                            String discount = orderJson.getString(Const.TAG_DISCOUNT);
                            String amount  = orderJson.getString(Const.TAG_AMOUNT);
                            Order order = new Order(orderId, purchaseDate, isShipped, orderStatus, itemCount, customer, shippingAddress, billingAddress,discount,amount);
                            orderArrayList.add(order);

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
                OrdersAdapter adapter = new OrdersAdapter(getActivity(), orderArrayList);
                orderListView.setAdapter(adapter);
            }
        }
    }
}
