package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.Outlet.Adapters.CustomAdapterVenue;
import com.example.qzero.Outlet.ObjectClasses.Venue;
import com.example.qzero.R;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class SearchVenueActivity extends Activity {

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    @InjectView(R.id.imgViewBack)
    ImageView imgViewBack;

    @InjectView(R.id.venueListView)
    ListView venueListView;

    String venue;
    String city;
    String zipCode;
    String location;
    String title;
    String venue_id;

    String message;
    String urlParameters;

    int status;

    JsonParser jsonParser;
    JSONObject jsonObject;

    ArrayList<Venue> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_venue);
        ButterKnife.inject(this);

        rowItems = new ArrayList<Venue>();


        setFont();

        getVenueData();

        addItemToListView();


    }

    private void setText() {

        txtViewHeading.setText(title);
    }

    private void setFont() {
        FontHelper.setFontFace(txtViewHeading, FontType.FONT, this);
    }

    public void getVenueData() {
        if (getIntent().hasExtra("arrayListVenue")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                title = bundle.getString("classname");
                rowItems = (ArrayList<Venue>) bundle.getSerializable("arrayListVenue");
                setText();
            } else
                Log.e("null", "null");
        }
    }

    public void addItemToListView() {
        CustomAdapterVenue adapter = new CustomAdapterVenue(this, rowItems);

        venueListView.setAdapter(adapter);


    }

    @OnClick(R.id.imgViewBack)
    void finishAct()
    {
        finish();
    }

    @OnItemClick(R.id.venueListView)
    void  onItemClick(int pos){

        Venue venue = rowItems.get(pos);
        venue_id=venue.getVenue_id();

        passIntent();

    }

    public void passIntent()
    {
        Intent intent=new Intent(SearchVenueActivity.this,OutletActivity.class);
        intent.putExtra("venue_id",venue_id);
        startActivity(intent);
    }


}

