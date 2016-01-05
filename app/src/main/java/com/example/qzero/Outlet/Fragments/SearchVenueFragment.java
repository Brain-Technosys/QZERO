package com.example.qzero.Outlet.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;

import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Activities.SearchVenueActivity;
import com.example.qzero.Outlet.ObjectClasses.Venue;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SearchVenueFragment extends Fragment {

    @InjectView(R.id.edtTextVenue)
    EditText edtTextVenue;

    @InjectView(R.id.edtTextCity)
    EditText edtTextCity;

    @InjectView(R.id.edtTextZip)
    EditText edtTextZip;

    String venue;
    String city;
    String zip;
    String location;

    String message;
    String urlParameters;

    String delivery_type;
    int status;

    JsonParser jsonParser;
    JSONObject jsonObject;
    JSONArray jsonArray;

    ArrayList<Venue> arrayListVenue;

    UserSession userSession;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_venue, null);

        ButterKnife.inject(this, view);



        userSession =new UserSession(getActivity().getApplicationContext());



        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        setCityName();

        setFonts();
    }


    public void setCityName(){
        if(userSession.getUserLocation()!=null){

            edtTextCity.setText(userSession.getUserLocation());
            edtTextZip.setText(userSession.getZipCode());

        }
    }


    public void setFonts() {

        FontHelper.applyFont(getActivity(), edtTextVenue, FontType.FONT);
        FontHelper.applyFont(getActivity(), edtTextCity, FontType.FONT);
        FontHelper.applyFont(getActivity(), edtTextZip, FontType.FONT);
    }

    @OnClick(R.id.imgViewSubmit)
    public void submit() {

        getVenueData();
        validatingVenue();

    }

    public void getVenueData() {
        venue = edtTextVenue.getText().toString();
        city = edtTextCity.getText().toString();
        zip = edtTextZip.getText().toString();
    }

    public void validatingVenue() {

        if (CheckInternetHelper.checkInternetConnection(getActivity())) {
            fetchResponse();
        } else {
            AlertDialogHelper.showAlertDialog(getActivity(),
                    getString(R.string.internet_connection_message),
                    "Alert");
        }
    }


    public void fetchResponse() {

        new GetVenue().execute();
    }

    public class GetVenue extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("inside", "do in");
            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.SEARCH_VENUE + "?restaurantName=" + venue.replace(" ","%20") + "&city=" + city.replace(" ","%20")+"&serviceType="+userSession.getDeliveryType() + "&zipCode=" + zip;


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {

                    arrayListVenue = new ArrayList<Venue>(jsonObject.length());

                    status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");

                    Log.d("status", "" + status);
                    if (status == 1) {

                        jsonArray = new JSONArray();
                        jsonArray = jsonObject.getJSONArray(Const.TAG_JsonObj);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            String venue_id = jsonObj.getString(Const.TAG_VENUE_ID);
                            String venue_name = jsonObj.getString(Const.TAG_VENUE_NAME);
                            String venue_address = jsonObj.getString(Const.TAG_ADDRESS);
                            String venue_city = jsonObj.getString(Const.TAG_CITY);
                            String venue_zip = jsonObj.getString(Const.TAG_ZIP);
                            String venue_phone = jsonObj.getString(Const.TAG_PHONE_NO);
                            String venue_mobile = jsonObj.getString(Const.TAG_MOB_N0);
                            String outlet_id="";
                            Venue venue = new Venue(venue_id,outlet_id,venue_name, venue_address, venue_city, venue_zip, venue_phone, venue_mobile);
                            arrayListVenue.add(venue);
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

                AlertDialogHelper.showAlertDialog(getActivity(),message, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(getActivity(),
                        getString(R.string.server_message), "Alert");
            }
        }
    }

    private void passIntent() {
        Intent intent = new Intent(getActivity(), SearchVenueActivity.class);

        //pass values
        Bundle bundle = new Bundle();
        bundle.putString("classname","Venue Details");
        bundle.putSerializable("arrayListVenue", arrayListVenue);
        intent.putExtras(bundle);

        startActivity(intent);
    }

}
