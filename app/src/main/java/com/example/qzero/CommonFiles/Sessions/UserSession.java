package com.example.qzero.CommonFiles.Sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Braintech on 7/22/2015.
 */
public class UserSession {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "user_pref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "is_user_logged_in";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_NAME = "name";

    //Search Tab fragment get users location permission
    public static final String KEY_LOCATION_PERM = "location_permission";
    public static final String KEY_APP_LAUNCH = "launch";

    public static final String KEY_USER_LOCATION = "user_loc";
    public static final String KEY_ZIP_CODE = "zip_code";

    //Session for OrderSummary and CartViewActivity
    public static final String KEY_FINALPAYBLE_AMOUNT = "final_amount";

    //GCM Token
    public static final String GCM_TOKEN = "gcm_token";

    public static final String GCM_LOGIN = "gcm_login";

    public static final String VENUE_ID="venue_id";



    public UserSession(Context _context) {
        this.context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createUserSession(String userID, String name) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_USER_ID, userID);
        editor.putString(KEY_NAME, name);

        // commit changes
        editor.commit();
    }

    /* Get User ID */
    public String getUserID() {
        return pref.getString(KEY_USER_ID, null);
    }

    /* Get User Name */
    public String getUserName() {
        return pref.getString(KEY_NAME, null);
    }

    // save user permission
    public void saveUserLocationPermission(boolean status) {
        editor.putBoolean(KEY_LOCATION_PERM, status);
        editor.commit();
    }

    //Check that , app launch first time or not
    public void saveAppLaunchStatus(boolean status) {
        editor.putBoolean(KEY_APP_LAUNCH, status);
        editor.commit();
    }


    public void saveUserLocation(String city,String zip_code) {
        editor.putString(KEY_USER_LOCATION, city);
        editor.putString(KEY_ZIP_CODE, zip_code);

        editor.commit();
    }

    //save finalPaybleAmount
    public void saveFinalPaybleAmount(String amount) {
        editor.putString(KEY_FINALPAYBLE_AMOUNT, amount);
        editor.commit();
    }

    //save GCM Token
    public void saveGCMToken(String token)
    {
       Log.e("session", token);
        editor.putString(GCM_TOKEN,token);
        editor.commit();
    }

    public void saveLogin(Boolean value)
    {
        editor.putBoolean(GCM_LOGIN, value);
        editor.commit();
    }

    //save GCM Token
    public void saveVenueId(String venue_id)
    {
        editor.putString(VENUE_ID, venue_id);
        editor.commit();
    }


    public boolean getUserPermissionLoc() {
        return pref.getBoolean(KEY_LOCATION_PERM, true);
    }

    public boolean getAppLaunchStatus() {
        return pref.getBoolean(KEY_APP_LAUNCH, false);
    }

    public String getUserLocation() {
        return pref.getString(KEY_USER_LOCATION,null);
    }

    public String getZipCode() {
        return pref.getString(KEY_ZIP_CODE,null);
    }



    //get finalPaybleAmount

    public String getFinalPaybleAmount(){
        return pref.getString(KEY_FINALPAYBLE_AMOUNT, "0.0");
    }

    /**
     * Clear session details
     */

    public String getGcmToken()
    {
        return pref.getString(GCM_TOKEN, "null");
    }

    public boolean getLogin() {
        return pref.getBoolean(GCM_LOGIN, false);
    }

    public String getVenueId()
    {
        return pref.getString(VENUE_ID, "null");
    }

    public void clearCartSession() { // Clearing all data from Shared
        editor.remove(KEY_FINALPAYBLE_AMOUNT);
        editor.commit();
    }

    public void logout() { // Clearing all data from Shared

        editor.remove(IS_LOGIN);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_NAME);
        editor.remove(GCM_LOGIN);
        editor.commit();
    }

    /**
     * Quick check for login
     * *
     */
    // Get Login State
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
