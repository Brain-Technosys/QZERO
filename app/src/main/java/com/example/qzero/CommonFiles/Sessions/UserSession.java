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
    public static final String KEY_NAME= "name";

    public UserSession(Context _context) {
        this.context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createUserSession(String userID,String name) {
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
