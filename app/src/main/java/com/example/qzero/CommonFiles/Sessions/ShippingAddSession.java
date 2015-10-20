package com.example.qzero.CommonFiles.Sessions;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Braintech on 14-Oct-15.
 */
public class ShippingAddSession {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "shipping_add_pref";

    private static final String TAG_BILLING_ADD = "tag_billing_add";
    private static final String TAG_BILLING_ADD_POS = "tag_billing_pos";
    private static final String TAG_BILLING_NAME = "tag_billing_name";
    private static final String TAG_BILLING_CONTACT = "tag_billing_contact";

    private static final String TAG_SHIPPING_ADD = "tag_shipping_add";
    private static final String TAG_SHIPPING_ADD_POS = "tag_shipping_pos";
    private static final String TAG_SHIPPING_NAME = "tag_shipping_name";
    private static final String TAG_SHIPPING_CONTACT = "tag_shipping_contact";

    private static final String TAG_CHKOUT_DETAIL = "chkout_detail";

    public ShippingAddSession(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    //Save Address of different type
    public void saveBillingAddress(String address) {
        editor.putString(TAG_BILLING_ADD, address);
        editor.commit();
    }


    public void saveBillingAddressPos(int pos) {
        editor.putInt(TAG_BILLING_ADD_POS, pos);
        editor.commit();
    }

    public void saveBillingName(String name) {
        editor.putString(TAG_BILLING_NAME, name);
        editor.commit();
    }

    public void saveBillingContact(String contact) {
        editor.putString(TAG_BILLING_CONTACT, contact);
        editor.commit();
    }

    public void saveShippingAddressDetail(String address) {
        editor.putString(TAG_SHIPPING_ADD, address);
        editor.commit();
    }

    public void saveShippingAddressPos(int pos) {
        editor.putInt(TAG_SHIPPING_ADD_POS, pos);
        editor.commit();
    }

    public void saveShippingName(String name) {
        editor.putString(TAG_SHIPPING_NAME, name);
        editor.commit();
    }

    public void saveShippingContact(String contact) {
        editor.putString(TAG_SHIPPING_CONTACT, contact);
        editor.commit();
    }

    //storing chkout_detail jsonObject detail as string

    public void saveChkOutDetail(String chkOutDetail) {
        editor.putString(TAG_CHKOUT_DETAIL, chkOutDetail);
        editor.commit();
    }

    //get Address of different type
    public String getBillingAddress() {
        return pref.getString(TAG_BILLING_ADD, null);
    }

    public String getBillingName() {
        return pref.getString(TAG_BILLING_NAME, null);
    }

    public String getBillingContact() {
        return pref.getString(TAG_BILLING_CONTACT, null);
    }

    public int getBillingAddressPos() {
        return pref.getInt(TAG_BILLING_ADD_POS, 0);
    }

    public String getShippingAddress() {
        return pref.getString(TAG_SHIPPING_ADD, null);
    }

    public String getShippingName() {
        return pref.getString(TAG_SHIPPING_NAME, null);
    }

    public String getShippingContact() {
        return pref.getString(TAG_SHIPPING_CONTACT, null);
    }

    public int getShippingAddressPos() {
        return pref.getInt(TAG_SHIPPING_ADD_POS, 0);
    }

    //fetching chkOutDetail json as string
    public String getChkOutDetail() {
        return pref.getString(TAG_CHKOUT_DETAIL, null);
    }


    public void clearShippingSharPref() {
        editor.remove(TAG_SHIPPING_ADD);
        editor.remove(TAG_SHIPPING_ADD_POS);
        editor.remove(TAG_SHIPPING_CONTACT);
        editor.commit();
    }

    public void clearBillingSharPref() {
        editor.remove(TAG_BILLING_ADD);
        editor.remove(TAG_BILLING_ADD_POS);
        editor.remove(TAG_BILLING_CONTACT);
        editor.remove(TAG_BILLING_NAME);
        editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

}
