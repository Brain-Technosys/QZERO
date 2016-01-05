package com.example.qzero.Outlet.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;


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

        //
        if (LOGINTYPE.equals("OUTLETCAT")) {
            AlertDialogHelper.alertBoxDeliveryType(this);
        }

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

            AlertDialogHelper.alertBoxDeliveryType(this);
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
            if (LOGINTYPE.equals("OUTLET") || LOGINTYPE.equals("CHECKOUT")) {
                finish();
            }
        }

        startActivity(intent);
    }

    @OnClick(R.id.relLayDeliveryTyp)
    public void setDeliveryType() {
        AlertDialogHelper.alertBoxDeliveryType(this);
    }

    private void setUserLocation() {

        if (CheckInternetHelper.checkInternetConnection(this)) {

            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= Build.VERSION_CODES.M) {
                if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), this)) {
                    Log.e("check permission", "granted");

                    getLocation();
                } else {
                    requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), this);
                }
            } else {
                Log.e("else", "else");
                getLocation();
            }


        } else {

            Toast.makeText(this, getString(R.string.wifi_mobile_network_error), Toast.LENGTH_LONG).show();
        }

    }

    private void getLocation() {
        LocationManager location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean networkLocationEnabled = location_manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (networkLocationEnabled) {

            LocationListener listner = new MyLocationListener();
            location_manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 2000, 2000, listner);
            location_manager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < Build.VERSION_CODES.M) {
            AlertDialogHelper.showAlertDialogSettings(this, "GPS permission allows us to access location data. Please enable location for additional functionality.");
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
                    String zipCode = returnAddress.getPostalCode();

                    userSession.saveUserLocation(city, zipCode);

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

    public static boolean checkPermission(String strPermission, Context _c, Activity _a) {
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    public void requestPermission(String strPermission, int perCode, Context context, Activity _a) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {

            Log.e("inisde", "permi");

            AlertDialogHelper.showAlertDialogSettings(this, "GPS permission allows us to access location data. Please go to Permissions in App Info for additional functionality.");
            //Toast.makeText(context, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            Log.e("permission", "granted");
            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e("permisson", "true");
                    getLocation();
                } else {
                    Toast.makeText(this, "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

}
