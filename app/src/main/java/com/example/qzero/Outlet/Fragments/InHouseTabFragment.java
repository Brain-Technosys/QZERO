package com.example.qzero.Outlet.Fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.Sessions.ShippingAddSession;
import com.example.qzero.Outlet.Activities.FinalChkoutActivity;
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

    String[] tableTitle = {"Select Table no.", "Tb1", "Tb2", "Tb3"};
    ArrayAdapter adapter;

    ShippingAddSession shippingAddSession;

    ArrayList<HashMap<String, String>> arrayListTableDetail;

    String seatNo;
    String tableNo_ID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_house_tab, container, false);

        ButterKnife.inject(this, view);

        context = view.getContext();

        shippingAddSession = new ShippingAddSession(context);


        // ((FinalChkoutActivity) getActivity()).getFinalPrice()
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

    }

    private class GetTableNoDetail extends AsyncTask<String, String, String> {


        JSONArray jsonArrayTableNo;
        String[] strAssignSeatNumber;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            if (shippingAddSession.getChkOutDetail() == null) {

            } else {
                try {
                    JSONObject jsonObjectResult = new JSONObject(shippingAddSession.getChkOutDetail());
                    Log.e("jsonObjResult", shippingAddSession.getChkOutDetail());
                    jsonArrayTableNo = jsonObjectResult.getJSONArray(Const.TAG_TABLE_NO);

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
            tableNo_ID=arrayListTableDetail.get(pos).get(Const.TAG_TABLE_NO_ID);
        }
    }
}
