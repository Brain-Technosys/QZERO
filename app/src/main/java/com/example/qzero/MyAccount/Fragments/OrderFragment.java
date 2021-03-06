package com.example.qzero.MyAccount.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
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
public class OrderFragment extends Fragment implements SearchView.OnQueryTextListener {
    UserSession session;
    String userID;
    ArrayList<Order> orderArrayList;

    @InjectView(R.id.orderListView)
    ListView orderListView;

    @InjectView(R.id.searchView)
    SearchView searchView;

    private Bundle savedState;
    private boolean saved;
    private static final String _FRAGMENT_STATE = "FRAGMENT_STATE";

    /*android.widget.Filter filter;*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.inject(this, view);

        // getActivity().setTitle(getString(R.string.order_title));
        searchView.setFocusable(false);
        orderListView.setTextFilterEnabled(false);
        searchView.setSubmitButtonEnabled(false);

        setupSearchView();
        session = new UserSession(getActivity().getApplicationContext());
        if (session.isUserLoggedIn())
            userID = session.getUserID();


        if (CheckInternetHelper.checkInternetConnection(getActivity())) {

           /* if (savedInstanceState != null && savedState == null) {
                savedState = savedInstanceState.getBundle(_FRAGMENT_STATE);

            }
            if (savedState != null) {
                //orderCountTextView.setText(savedState.getString("order_count"));
            } else {
                new GetOrders().execute();
            }*/

            new GetOrders().execute();
        } else {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.internet_connection_message),
                    "Alert");
        }


        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Order order = orderArrayList.get(position);

                String orderId = String.valueOf(order.getOrderId());

                Log.e("order", "" + orderId);
                
                Bundle bundle = new Bundle();
                bundle.putString(Const.TAG_ORDER_ID, ((TextView) view.findViewById(R.id.txtOrderID)).getText().toString());
                bundle.putString(Const.TAG_BILLING_ADDRESS, ((TextView) view.findViewById(R.id.txtViewBilling)).getText().toString());
                bundle.putString(Const.TAG_SHIPPING_ADDRESS,((TextView) view.findViewById(R.id.txtViewShipping)).getText().toString());

                bundle.putString(Const.TAG_PURCHASE_DATE, ((TextView) view.findViewById(R.id.txtDate)).getText().toString());
                bundle.putString(Const.TAG_ITEM_COUNT, ((TextView) view.findViewById(R.id.txtItemCount)).getText().toString());
                bundle.putString(Const.TAG_ORDER_STATUS, ((TextView) view.findViewById(R.id.txtStatus)).getText().toString());
                bundle.putString(Const.TAG_DISCOUNT, ((TextView) view.findViewById(R.id.txtDiscount)).getText().toString().replace("$", ""));
                bundle.putString(Const.TAG_AMOUNT, ((TextView) view.findViewById(R.id.txtAmount)).getText().toString().replace("$", ""));
                bundle.putString(Const.TAG_DELIVERY_TYPE,((TextView) view.findViewById(R.id.txtViewDeliveryType)).getText().toString());
                bundle.putString(Const.TAG_SEAT_NO,((TextView) view.findViewById(R.id.txtViewSeatNo)).getText().toString());
                bundle.putString(Const.TAG_AMOUNT, ((TextView) view.findViewById(R.id.txtAmount)).getText().toString().replace("$", ""));

                OrderDetailFragment fragment = new OrderDetailFragment();
                fragment.setArguments(bundle);
                // this.getFragmentManager().beginTransaction().replace(R.id.flContent, fragment, fragment.getClass().getName()).addToBackStack(null).commit();
//
                getFragmentManager().beginTransaction()
                        .hide(getFragmentManager().findFragmentByTag(getTag()))
                        .add(R.id.flContent, fragment, fragment.getClass().getName())
                        .addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(_FRAGMENT_STATE, (savedState != null) ? savedState : saveState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedState = saveState();
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        OrdersAdapter ca = (OrdersAdapter)orderListView.getAdapter();

        if (TextUtils.isEmpty(newText)) {
            System.out.println("isEmpty");
            ca.getFilter().filter(newText);
        } else {

            ca.getFilter().filter(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Order ID");
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    // Method to save data on bundle
    private Bundle saveState() { /* called either from onDestroyView() or onSaveInstanceState() */
        Bundle state = new Bundle();
        //state.putString("order_count", orderCount);
        saved = true;
        return state;
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
                Log.v("Order URL: ", url);
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
                            String purchaseDate = orderJson.getString(Const.TAG_FORMATTED_DATE);
                            boolean isShipped = orderJson.getBoolean(Const.TAG_IS_SHIPPED);
                            String orderStatus = orderJson.getString(Const.TAG_ORDER_STATUS);
                            int itemCount = orderJson.getInt(Const.TAG_ITEM_COUNT);

                            String customer = orderJson.getString(Const.TAG_CUSTOMER);
                            String shippingAddress = orderJson.getString(Const.TAG_SHIPPING_ADDRESS);
                            String billingAddress = orderJson.getString(Const.TAG_BILLING_ADDRESS);
                            String discount = orderJson.getString(Const.TAG_DISCOUNT);
                            String amount = orderJson.getString(Const.TAG_AMOUNT);
                            String seatNo=orderJson.getString(Const.TAG_SEAT_NO);
                            String deliveryType=orderJson.getString(Const.TAG_DELIVERY_TYPE);

                            Order order = new Order(orderId, purchaseDate, isShipped, orderStatus, itemCount, customer, shippingAddress, billingAddress, discount, amount,deliveryType,seatNo);
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

              //  filter = adapter.getFilter();
            }
        }
    }
}
