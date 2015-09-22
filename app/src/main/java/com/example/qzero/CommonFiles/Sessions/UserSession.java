package com.example.qzero.CommonFiles.Sessions;

import android.content.Context;
import android.content.SharedPreferences;

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


    public void saveUserLocation(String city) {
        editor.putString(KEY_USER_LOCATION, city);
        editor.commit();
    }

    public boolean getUserPermissionLoc() {
        return pref.getBoolean(KEY_LOCATION_PERM, true);
    }

    public boolean getAppLaunchStatus() {
        return pref.getBoolean(KEY_APP_LAUNCH, false);
    }

    public String getUserLocation() {
        return pref.getString(KEY_USER_LOCATION, "");
    }

    /**
     * Clear session details
     */

    public void ClearUserName() { // Clearing all data from Shared
        editor.clear();
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
