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
    int itemId;

    Double totalAmount;
    Double itemPrice;
    Double discountAmount;
    Double afterDiscountAmount;

    String quantity;
    int isModifier;

    String modifierId;
    String modifierPrice;

    String mod_name;
    String mod_qty;

    UserSession userSession;

    ArrayList<OrderItemStatusModel> orderStatusArrayList;
    ArrayList<OrderItemStatusModel> orderItemStatusArrayList;


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


                outletId = Integer.parseInt(outletCursor.getString(2));

            }
        }
        totalAmount = Double.parseDouble(userSession.getFinalPaybleAmount());

        JSONObject jsonObjDetails = new JSONObject();
        try {

            jsonObjDetails.put("outletId", outletId);
            jsonObjDetails.put("totalAmount", totalAmount);
            jsonObjDetails.put("tableNoId",tableNo_ID);
            jsonObjDetails.put("tableNO",tableNo_ID);
            jsonObjDetails.put("deliveryType",1);
            jsonObjDetails.put("deliveryTypeId",1);

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

                int itemId=Integer.parseInt(orderItemStatusArrayList.get(j).getItemCode());

                itemPrice=orderItemStatusArrayList.get(j).getItemPrice();
                discountAmount=orderItemStatusArrayList.get(j).getDiscountAmt();

                if(discountAmount==0.0)
                {
                    afterDiscountAmount=0.0;
                }
                else {
                    afterDiscountAmount = itemPrice - discountAmount;
                }

                orderStatusObj.put("statusId",status_id);
                orderStatusObj.put("itemId", itemId);
                orderStatusObj.put("isModifier", isModifier);
                orderStatusObj.put("quantity", quantity);
                orderStatusObj.put("itemPrice", itemPrice);
                orderStatusObj.put("discountAmount", afterDiscountAmount);
                if(discountAmount==0.0)
                    orderStatusObj.put("afterDiscountAmount", itemPrice);
                else
                    orderStatusObj.put("afterDiscountAmount", discountAmount);


                jsonArrayOrder.put(orderStatusObj);
            }

            for (int i = 0; i < orderStatusArrayList.size(); i++) {

                JSONObject modStatusObj = new JSONObject();

                String modName = orderStatusArrayList.get(i).getMod_name();
                String statusId=orderStatusArrayList.get(i).getItemId();

                int itemId = Integer.parseInt(orderStatusArrayList.get(i).getItemCode());

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
        ((FinalChkoutActivity)getActivity()).callPostCheckout(jsonDetails);
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
                            int indexItemCode = itemIdCursor.getColumnIndex(databaseHelper.ITEM_CODE);
                            int indexItemPrice=itemIdCursor.getColumnIndex(databaseHelper.ITEM_PRICE);
                            int indexItemDiscount=itemIdCursor.getColumnIndex(databaseHelper.ITEM_DISCOUNT);

                            String item_id = itemIdCursor.getString(indexItemId);
                            String itemCode = itemIdCursor.getString(indexItemCode);

                            Double item_price = Double.parseDouble(itemIdCursor.getString(indexItemPrice));
                            Double discount_amount = Double.parseDouble(itemIdCursor.getString(indexItemDiscount));


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

                                    OrderItemStatusModel orderItemStatusModel = new OrderItemStatusModel(itemCode,item_id,mod_name, quantity, true, mod_price, mod_id,0.0,0.0);
                                    orderStatusArrayList.add(orderItemStatusModel);
                                }
                            }

                            OrderItemStatusModel orderItemStatusModel = new OrderItemStatusModel(itemCode,item_id,mod_name, quantity, true, " ", " ",item_price,discount_amount);
                            orderItemStatusArrayList.add(orderItemStatusModel);


                        } while (itemIdCursor.moveToNext());
                    }

                }
            }
        }
    }
}
