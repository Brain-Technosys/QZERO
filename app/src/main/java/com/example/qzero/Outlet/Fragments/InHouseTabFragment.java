package com.example.qzero.Outlet.Fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.ShippingAddSession;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Activities.FinalChkoutActivity;
import com.example.qzero.Outlet.ObjectClasses.DbModifiers;
import com.example.qzero.Outlet.ObjectClasses.OrderItemStatusModel;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;


public class InHouseTabFragment extends Fragment {

    Context context;

    @InjectView(R.id.spnr_selectTable)
    Spinner spnr_selectTable;

    ArrayAdapter adapter;

    ShippingAddSession shippingAddSession;

    ArrayList<HashMap<String, String>> arrayListTableDetail;

    String seatNo;
    String tableNo_ID;

    DatabaseHelper databaseHelper;

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

    UserSession userSession;

    ArrayList<OrderItemStatusModel> orderStatusArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_house_tab, container, false);

        ButterKnife.inject(this, view);

        context = view.getContext();

        shippingAddSession = new ShippingAddSession(context);

        userSession = new UserSession(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (CheckInternetHelper.checkInternetConnection(context))
            new GetTableNoDetail().execute();
        else
            AlertDialogHelper.showAlertDialog(getActivity(), String.valueOf(R.string.internet_connection_message), "Alert");
    }


    @OnClick(R.id.btn_Place_Order)
    public void place_order() {

        if (seatNo==null)
        {
            AlertDialogHelper.showAlertDialog(getActivity(),"Please select Table No.","Alert");
        }
        else {
            createPostCheckout();
        }
    }

    private class GetTableNoDetail extends AsyncTask<String, String, String> {


        JSONArray jsonArrayTableNo;
        String[] strAssignSeatNumber;

        int status=-1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            if (shippingAddSession.getChkOutDetail() == null) {

                status=0;

            } else {
                try {
                    JSONObject jsonObjectResult = new JSONObject(shippingAddSession.getChkOutDetail());
                    Log.e("jsonObjResult", shippingAddSession.getChkOutDetail());
                    jsonArrayTableNo = jsonObjectResult.getJSONArray(Const.TAG_TABLE_NO);

                    if(jsonArrayTableNo.length()==0)
                    {
                        status=0;
                    }
                    else {
                        status=1;
                        arrayListTableDetail = new ArrayList<>();
                        strAssignSeatNumber = new String[jsonArrayTableNo.length() + 1];

                        strAssignSeatNumber[0] = "Select";
                        for (int i = 0; i < jsonArrayTableNo.length(); i++) {

                            JSONObject jsonObjectTableNo = jsonArrayTableNo.getJSONObject(i);

                            HashMap<String, String> hmTableDetail = new HashMap<>();

                            hmTableDetail.put(Const.TAG_VENUE_ID, jsonObjectTableNo.getString(Const.TAG_VENUE_ID));
                            hmTableDetail.put(Const.TAG_OUTLET_ID, jsonObjectTableNo.getString(Const.TAG_OUTLET_ID));
                            hmTableDetail.put(Const.TAG_PREFIX, jsonObjectTableNo.getString(Const.TAG_PREFIX));
                            hmTableDetail.put(Const.TAG_SEAT_NUMBER, jsonObjectTableNo.getString(Const.TAG_SEAT_NUMBER));
                            hmTableDetail.put(Const.TAG_ASSIGN_NUMBER, jsonObjectTableNo.getString(Const.TAG_ASSIGN_NUMBER));
                            hmTableDetail.put(Const.TAG_TABLE_NO_ID, jsonObjectTableNo.getString(Const.TAG_TABLE_NO_ID));

                            arrayListTableDetail.add(hmTableDetail);

                            strAssignSeatNumber[i + 1] = jsonObjectTableNo.getString(Const.TAG_ASSIGN_NUMBER);

                        }
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
            if (shippingAddSession.getChkOutDetail() == null) {


            } else {

                adapter = new ArrayAdapter(context, R.layout.layout_spinner, strAssignSeatNumber);
                adapter.setDropDownViewResource(R.layout.layout_spinner_drop_down);
                spnr_selectTable.setAdapter(adapter);
            }
            ProgresBar.stop();
        }
    }

    @OnItemSelected(R.id.spnr_selectTable)
    public void spnr_selectTable(int pos) {

        if (pos == 0) {

        } else {
            seatNo = arrayListTableDetail.get(pos).get(Const.TAG_SEAT_NUMBER);
            tableNo_ID = arrayListTableDetail.get(pos).get(Const.TAG_TABLE_NO_ID);
        }
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
            jsonObjDetails.put("tableNoId",tableNoId);
            jsonObjDetails.put("tableNO",tableNO );
            jsonObjDetails.put("deliveryType",1);

            JSONArray jsonArrayOrder = new JSONArray();
            JSONArray jsonArrayMod = new JSONArray();

            getOrderStatusData();
            for (int i = 0; i < orderStatusArrayList.size(); i++) {

                JSONObject orderStatusObj = new JSONObject();

                JSONObject modStatusObj = new JSONObject();

                String modName = orderStatusArrayList.get(i).getMod_name();
                if (modName.equals("null")) {
                    isModifier = 0;
                } else {
                    isModifier = 1;

                    modifierId = orderStatusArrayList.get(i).getMod_id();
                    modifierPrice = orderStatusArrayList.get(i).getMod_price();

                    modStatusObj.put("itemId", itemId);
                    modStatusObj.put("modifierId", modifierId);
                    modStatusObj.put("modifierPrice", modifierPrice);

                    jsonArrayMod.put(modStatusObj);
                }

                quantity = orderStatusArrayList.get(i).getQuantity();

                orderStatusObj.put("itemId", itemId);
                orderStatusObj.put("isModifier", isModifier);
                orderStatusObj.put("quantity", quantity);

                jsonArrayOrder.put(orderStatusObj);


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
        ((FinalChkoutActivity)getActivity()).callPostCheckout(jsonDetails);
    }

    private void getOrderStatusData() {

        orderStatusArrayList = new ArrayList<>();
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

                                    String mod_name = modCursor.getString(indexname);
                                    String mod_price = modCursor.getString(indexprice);
                                    String quantity = modCursor.getString(indexqty);
                                    String mod_id = modCursor.getString(indexModActualId);

                                    OrderItemStatusModel orderItemStatusModel = new OrderItemStatusModel(mod_name, quantity, true, mod_price, mod_id);
                                    orderStatusArrayList.add(orderItemStatusModel);
                                }
                            }

                        } while (itemIdCursor.moveToNext());
                    }

                }
            }
        }
    }
}
