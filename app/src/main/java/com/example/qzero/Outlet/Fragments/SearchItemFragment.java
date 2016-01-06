package com.example.qzero.Outlet.Fragments;

import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Activities.SearchItemActivity;
import com.example.qzero.Outlet.Activities.SearchVenueActivity;
import com.example.qzero.Outlet.ObjectClasses.Items;
import com.example.qzero.Outlet.ObjectClasses.Venue;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SearchItemFragment extends Fragment {

    @InjectView(R.id.edtTextItem)
    EditText edtTextItem;

    String itemName;

    String outlet;
    String city;
    String zip;
    String location;

    String message;
    String urlParameters;

    int status;

    JsonParser jsonParser;
    JSONObject jsonObject;
    JSONArray jsonArray;

    ArrayList<Items> arrayListItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_item, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FontHelper.applyFont(getActivity(), edtTextItem, FontType.FONT);
    }


    @OnClick(R.id.imgViewSubmit)
    public void submit() {
        validatingItem();
    }

    private void validatingItem() {
        String item = edtTextItem.getText().toString();

        itemName = item.replace(" ", "%20");

        if (CheckInternetHelper.checkInternetConnection(getActivity())) {
            fetchResponse();
        } else {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.internet_connection_message),
                    "Alert");
        }


    }

    public void fetchResponse() {

        new GetItem().execute();
    }

    public class GetItem extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {

            String url = null;

            status = -1;

            jsonParser = new JsonParser();

            UserSession userSession = new UserSession(getActivity());

            url = Const.BASE_URL + Const.SEARCH_ITEM + "?"+"itemName=" + itemName + "&serviceType=" + userSession.getDeliveryType();

            Log.e("urlitem", url);
            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            try {
                Log.e("json", jsonString);
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {

                    arrayListItems = new ArrayList<Items>(jsonObject.length());

                    status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");

                    Log.d("status", "" + status);
                    if (status == 1) {

                        jsonArray = new JSONArray();
                        jsonArray = jsonObject.getJSONArray(Const.TAG_JsonObj);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            String venue_id = jsonObj.getString(Const.TAG_VENUE_ID);
                            String outlet_id = jsonObj.getString(Const.TAG_OUTLET_ID);
                            String item_id = jsonObj.getString(Const.TAG_ITEM_ID);
                            String item_name = jsonObj.getString(Const.TAG_ITEM_NAME);
                            String venue_name = jsonObj.getString(Const.TAG_VENUE_NAME);
                            String outlet_name = jsonObj.getString(Const.TAG_OUTLET_NAME);
                            String venue_address = jsonObj.getString(Const.TAG_ADDRESS);
                            String venue_city = jsonObj.getString(Const.TAG_CITY);
                            String venue_phone = jsonObj.getString(Const.TAG_PHONE_NO);
                            String venue_mobile = jsonObj.getString(Const.TAG_MOB_N0);
                            String outletName = jsonObj.getString(Const.TAG_OUTLET_NAME);

                            Items items = new Items(item_id, venue_id, outlet_id, item_name, venue_name, outlet_name, venue_address, venue_city, venue_phone, venue_mobile);
                            arrayListItems.add(items);
                        }
                    }

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            ProgresBar.stop();

            Log.e("inside", "postexecute");

            if (status == 1) {

                passIntent();

            } else if (status == 0) {

                AlertDialogHelper.showAlertDialog(getActivity(), message, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(getActivity(),
                        getString(R.string.server_message), "Alert");
            }
        }
    }

    private void passIntent() {
        Intent intent = new Intent(getActivity(), SearchItemActivity.class);

        //pass values
        Bundle bundle = new Bundle();
        bundle.putSerializable("arrayListItems", arrayListItems);
        intent.putExtras(bundle);

        startActivity(intent);
    }

}