package com.example.qzero.Outlet.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Fragments.LoginTabFragment;
import com.example.qzero.Outlet.Fragments.SearchTabFragment;
import com.example.qzero.Outlet.Fragments.SearchVenueFragment;
import com.example.qzero.Outlet.SlidingUpPanel.SlidingUpPanelLayout;
import com.example.qzero.Outlet.SlidingUpPanel.SlidingUpPanelLayout.PanelState;
import com.example.qzero.R;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HomeActivity extends FragmentActivity {

    @InjectView(R.id.txtViewSearch)
    TextView txtViewSearch;

    @InjectView(R.id.txtViewLogin)
    TextView txtViewLogin;

    UserSession userSession;
    Geocoder geocoder;
    List<Address> addresses;
    Double latitude, longitude;

    String LOGINTYPE = "SIMPLELOGIN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);

        setFonts();

        userSession = new UserSession(HomeActivity.this);

        //getting intent from Login Screen, LOGINTYPE will differentiate login between Simple Login, Login From Cart Screen, And Login From Outlet
        Intent intent = getIntent();

        if (getIntent().hasExtra("LOGINTYPE")) {
            LOGINTYPE = intent.getStringExtra("LOGINTYPE");
        }

        openPopUpWindowToGetPermissionAbtLoc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userSession.isUserLoggedIn()) {
            txtViewLogin.setText(getString(R.string.txt_my_profile));
        } else {
            txtViewLogin.setText(getString(R.string.txt_login));
        }
    }

    private void openPopUpWindowToGetPermissionAbtLoc() {

        if (!userSession.getAppLaunchStatus()) {
            AlertDialogHelper.showAlertBoxLocationPermission(this, getString(R.string.msg_perm_get_location),
                    getString(R.string.title_perm_get_location));
        } else {
            getUserLocation();
        }
    }

    public void getUserLocation() {
        if (userSession.getUserPermissionLoc()) {
            setUserLocation();
        }
    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewSearch, FontType.FONT, this);
        FontHelper.setFontFace(txtViewLogin, FontType.FONT, this);

    }


    @OnClick(R.id.relLaySearch)
    void openSearchFragment() {

        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        startActivity(intent);

    }


    @OnClick(R.id.relLayLogin)
    void openLoginFragment() {


        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.putExtra("LOGINTYPE", LOGINTYPE);

        if (getIntent().hasExtra("LOGINTYPE")) {
            if (LOGINTYPE.equals("OUTLET")||LOGINTYPE.equals("CHECKOUT")) {
                finish();
            }
        }

        startActivity(intent);
    }

    private void setUserLocation() {

        if (CheckInternetHelper.checkInternetConnection(this)) {

            LocationManager location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener listner = new MyLocationListener();
            location_manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 2000, 2000, listner);
            location_manager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } else {

            Toast.makeText(this, getString(R.string.wifi_mobile_network_error), Toast.LENGTH_LONG).show();
        }

    }

    public class MyLocationListener implements LocationListener {

        StringBuilder str;


        @Override
        public void onLocationChanged(Location arg0) {

            latitude = arg0.getLatitude();
            longitude = arg0.getLongitude();

            try {

                geocoder = new Geocoder(HomeActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (geocoder.isPresent()) {

                    Address returnAddress = addresses.get(0);

                    String city = returnAddress.getLocality();
                    String zipCode=returnAddress.getPostalCode();

                    userSession.saveUserLocation(city,zipCode);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProviderDisabled(String arg0) {
        }

        @Override
        public void onProviderEnabled(String arg0) {
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        }
    }
}
