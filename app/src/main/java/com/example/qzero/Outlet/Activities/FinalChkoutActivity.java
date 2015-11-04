package com.example.qzero.Outlet.Activities;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import android.widget.TextView;
import android.widget.Toast;


import com.example.qzero.CommonFiles.Common.ConstVarIntent;
import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.GCMHelper;
import com.example.qzero.CommonFiles.Helpers.GetCheckOutDetails;
import com.example.qzero.CommonFiles.Push.QuickstartPreferences;
import com.example.qzero.CommonFiles.Push.RegistrationIntentService;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Activities.DashBoardActivity;
import com.example.qzero.Outlet.Fragments.ChkoutCatFragment;
import com.example.qzero.Outlet.ObjectClasses.DbItems;
import com.example.qzero.Outlet.ObjectClasses.DbModifiers;
import com.example.qzero.Outlet.ObjectClasses.OrderItemStatusModel;
import com.example.qzero.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Braintech on 06-Oct-15.
 */
public class FinalChkoutActivity extends AppCompatActivity {

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    //code change by himanshu

    @InjectView(R.id.layout_your_order)
    LinearLayout layoutAddModifier;

    //code change by himanshu
    @InjectView(R.id.txt__item_Name)
    TextView txt__item_Name;

    //code change by himanshu
    @InjectView(R.id.txt__item_qty)
    TextView txt__item_qty;

    @InjectView(R.id.txt_item_totalPrice)
    TextView txt_item_totalPrice;

    @InjectView(R.id.txt_final_price)
    TextView txt_final_price;

    HashMap<Integer, ArrayList<DbItems>> hashMapItems;
    HashMap<Integer, ArrayList<DbModifiers>> hashMapModifiers;
    HashMap<Integer, ArrayList<DbItems>> hashMapListItems;

    ArrayList<String> arrayListDeliveryName;

    View[] viewItems;

    TableLayout tableModifier;
    TableLayout tableItem;
    TextView tvName;
    TextView tvQty;
    TextView tvTotal;

    TextView modifierName;
    TextView modifierQty;
    TextView modifierTotal;

    //code changed by himanshu
    Double totalpaybleAmount = 0.0;

    //Reference of DatabaseHelper class to access its components
    private DatabaseHelper databaseHelper = null;

    int pos;
    int itemsLength;

    String itemName;
    String transactionId;

    String outletId;

    Cursor itemCursor;
    Cursor itemIdCursorMod;
    int position = 0;
    int posMod = 0;

    int orderId;

    UserSession userSession;

    ArrayList<OrderItemStatusModel> orderStatusArrayList;


    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    private static final String CONFIG_CLIENT_ID = ViewCartActivity.client_id;
    //"AZoJOGOvT2BOO7sVIRSVRG0Pwtc8XUX0Vl7RykN3KUASlTfkFhr7PQnwQQvo6dS37kf32irnxNwbH8kP";

    private static final int REQUEST_PAYPAL_PAYMENT = 1;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("QZERO")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "login";

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_chkout);
        ButterKnife.inject(this);

        userSession = new UserSession(this);

        txtViewHeading.setText("Your Order");

        //code changed by himanshu
        totalpaybleAmount = Double.parseDouble(userSession.getFinalPaybleAmount());

        txt_final_price.setText("Total: $" + Utility.formatDecimalByString(String.valueOf(totalpaybleAmount)));

        databaseHelper = new DatabaseHelper(this);
        hashMapItems = new HashMap<>();
        hashMapModifiers = new HashMap<>();
        hashMapListItems = new HashMap<>();

        getDataFromDatabase();

        if (hashMapItems.size() == 0) {

        } else {
            // sendDataToAdapterClass();
        }


        createTableItems();

        setFont();

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        getDeliveryType();

    }

    private void setFont() {
        FontHelper.applyFont(this, txt_final_price, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(this, txt__item_Name, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(this, txt__item_qty, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(this, txt_item_totalPrice, FontHelper.FontType.FONTSANSBOLD);


    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    private void getDataFromDatabase() {
        String item_id = null;
        Cursor distinctItemCursor = databaseHelper.getDistinctItems();
        pos = 0;
        if (distinctItemCursor != null) {
            while (distinctItemCursor.moveToNext()) {

                int index = distinctItemCursor.getColumnIndex(databaseHelper.NAME_COLUMN);

                itemName = distinctItemCursor.getString(index);

                Log.e("item_name", itemName);

                Cursor itemIdCursor = databaseHelper.selectItems(itemName);

                itemIdCursorMod = itemIdCursor;

                if (itemIdCursor != null) {
                    if (itemIdCursor.moveToFirst()) {
                        int indexItemId = itemIdCursor.getColumnIndex(databaseHelper.ID_COLUMN);

                        item_id = itemIdCursor.getString(indexItemId);

                        Log.e("item_id", item_id);
                        itemCursor = databaseHelper.getItems(item_id);

                        itemsLength = itemIdCursor.getCount();

                        storeData();

                    }
                }

                getListData(item_id);
            }


        }

    }

    private void storeData() {

        if (itemCursor != null) {
            if (itemCursor.moveToFirst()) {

                ArrayList<DbItems> arrayListDbIetms = new ArrayList<>();
                ArrayList<DbModifiers> arrayListDbMod = new ArrayList<>();

                String item_id = itemCursor.getString(0);
                String item_name = itemCursor.getString(1);
                String item_price = itemCursor.getString(2);
                String item_image = itemCursor.getString(3);
                String item_discount = itemCursor.getString(4);


                DbItems dbItems = new DbItems(item_id, item_name, item_price, item_discount, item_image, itemsLength);
                arrayListDbIetms.add(dbItems);

                hashMapListItems.put(pos, arrayListDbIetms);

                pos++;

            }
        }
    }

    private void getListData(String itemd) {
        Log.e("ietm", itemd);
        if (itemIdCursorMod != null) {

            Log.e("itemId", "" + itemIdCursorMod.getCount());
            if (itemIdCursorMod.moveToFirst()) {
                do {
                    ArrayList<DbModifiers> arrayListDbMod = new ArrayList<>();

                    int indexItemId = itemIdCursorMod.getColumnIndex(databaseHelper.ID_COLUMN);

                    String item_id = itemIdCursorMod.getString(indexItemId);

                    Log.e("modItemId", item_id);

                    Cursor modCursor = databaseHelper.getModifiers(item_id);

                    if (modCursor != null) {
                        while (modCursor.moveToNext()) {

                            String mod_name = modCursor.getString(2);
                            String mod_price = modCursor.getString(4);
                            String quantity = modCursor.getString(5);

                            Log.e("mod_name", mod_name);


                            DbModifiers dbModifiers = new DbModifiers(item_id, quantity, mod_name, mod_price);
                            arrayListDbMod.add(dbModifiers);
                        }
                    }

                    hashMapModifiers.put(posMod, arrayListDbMod);
                    posMod++;
                } while (itemIdCursorMod.moveToNext());
            }

        }
    }

    private void createTableItems() {

        for (int pos = 0; pos < hashMapListItems.size(); pos++) {

            ArrayList<DbItems> dbListItem = hashMapListItems.get(pos);
            LayoutInflater inflater = LayoutInflater.from(FinalChkoutActivity.this);
            int countLength = dbListItem.get(0).getCount();

            viewItems = new View[countLength];

            for (int i = 0; i < countLength; i++) {
                viewItems[i] = inflater.inflate(R.layout.order_summary_tables, null);

                ArrayList<DbModifiers> dbListModifiers = new ArrayList<>();
                dbListModifiers = hashMapModifiers.get(position);

                setIdofTableItems(i);

                if (dbListModifiers.size() != 0) {
                    layoutAddModifier.addView(viewItems[i]);

                    tvName.setText(dbListItem.get(0).getItem_name());
                    tvQty.setText("$" + Utility.formatDecimalByString(dbListItem.get(0).getItem_price()) + " x " + dbListModifiers.get(0).getQuantity());

                    //Chking discount price is 0 or not
                    if (Double.parseDouble(dbListItem.get(0).getDiscount_price()) == 0.0) {
                        tvTotal.setText("$" + Utility.formatDecimalByString(String.valueOf(Double.parseDouble(dbListItem.get(0).getItem_price()) * Double.parseDouble(dbListModifiers.get(0).getQuantity()))));
                    } else {
                        tvTotal.setText("$" + Utility.formatDecimalByString(String.valueOf(Double.parseDouble(dbListItem.get(0).getDiscount_price()) * Double.parseDouble(dbListModifiers.get(0).getQuantity()))));
                    }


                    if (dbListModifiers.get(0).getModifier_name().equals("null")) {
                        //do not add modifier screen
                    } else {
                        for (int modlist = 0; modlist < dbListModifiers.size(); modlist++) {

                            View modifier = inflater.inflate(R.layout.order_summary_table_row, null);

                            setIdOfTableModifier(modifier);

                            tableModifier.addView(modifier);


                            modifierName.setText(dbListModifiers.get(modlist).getModifier_name());

                            modifierQty.setText("$" + Utility.formatDecimalByString(dbListModifiers.get(modlist).getModifier_price()) + " x " + dbListModifiers.get(modlist).getQuantity());
                            modifierTotal.setText("$" + Utility.formatDecimalByString(String.valueOf(Double.parseDouble(dbListModifiers.get(modlist).getModifier_price()) * Double.parseDouble(dbListModifiers.get(modlist).getQuantity()))));
                        }
                    }
                }

                position++;


            }
        }
    }


    private void setIdofTableItems(int j) {

        tableModifier = (TableLayout) viewItems[j].findViewById(R.id.table_modifier);
        tableItem = (TableLayout) viewItems[j].findViewById(R.id.tableItem);
        tvName = (TextView) viewItems[j].findViewById(R.id.itemName);
        tvQty = (TextView) viewItems[j].findViewById(R.id.item_qty);
        tvTotal = (TextView) viewItems[j].findViewById(R.id.item_totalPrice);

        FontHelper.applyFont(this, tvName, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(this, tvQty, FontHelper.FontType.FONT);
        FontHelper.applyFont(this, tvTotal, FontHelper.FontType.FONT);
    }


    private void setIdOfTableModifier(View modifier) {
        modifierName = (TextView) modifier.findViewById(R.id.ModifierName);
        modifierQty = (TextView) modifier.findViewById(R.id.modifier_qty);
        modifierTotal = (TextView) modifier.findViewById(R.id.modifier_totalPrice);


    }

    private void AddChkOutCatfrag() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        ChkoutCatFragment chkoutCatFragment = new ChkoutCatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstVarIntent.TAG_DELIVERY_TYPE, arrayListDeliveryName);
        chkoutCatFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.chkout_detail_frag, chkoutCatFragment, "login");
        fragmentTransaction.commit();
    }

    //code changed by himanshu
    public Double getFinalPrice() {
        return totalpaybleAmount;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }

    @OnClick(R.id.imgViewBack)
    void imgViewBack() {
        finish();
    }

    public void callPostCheckout(String jsonDetails) {
        if (CheckInternetHelper.checkInternetConnection(this)) {

            Log.e("jsonDetailsFinal", jsonDetails);
            new PostCheckOut().execute(jsonDetails);
        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.server_message), "Alert");
        }
    }

    private class PostCheckOut extends AsyncTask<String, String, String> {

        String message;
        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(FinalChkoutActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            JsonParser jsonParser = new JsonParser();

            String url = Const.BASE_URL + Const.POST_CHECKOUT;

            String parameter = params[0];
            String userId = userSession.getUserID();

            Log.e("parameter", parameter);
            Log.e("userId", userId);

            String jsonString = jsonParser.executePost(url, parameter, userId, Const.TIME_OUT);

            Log.e("json", jsonString);


            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
                    status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");
                    if (status == 1) {
                        JSONObject jsonObj = jsonObject.getJSONObject(Const.TAG_JsonObj);

                        orderId = Integer.parseInt(jsonObj.getString(Const.TAG_ORDER_ID));
                    }

                    //  orderId=jsonObject.getInt(Const.TAG_ORDER_ID);
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                status = -1;
            } catch (JSONException e) {

                e.printStackTrace();
                status = -1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            ProgresBar.stop();

            if (status == 1) {

                callPayPal();
            } else if (status == 0) {
                AlertDialogHelper.showAlertDialog(FinalChkoutActivity.this,
                        message, "Alert");
            } else {
                AlertDialogHelper.showAlertDialog(FinalChkoutActivity.this,
                        getString(R.string.server_message), "Alert");
            }
        }
    }

    public void callPayPal() {
        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(totalpaybleAmount), "USD", "QZERO",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_PAYPAL_PAYMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println("Responseeee" + confirm);
                        Log.i("paymentExample", confirm.toJSONObject().toString());


                        JSONObject jsonObj = new JSONObject(confirm.toJSONObject().toString());

                        String paymentId = jsonObj.getJSONObject("response").getString("id");
                        System.out.println("payment id:-==" + paymentId);
                        Toast.makeText(this, paymentId, Toast.LENGTH_LONG).show();
                        transactionId = paymentId;
                        callTransactionApi();

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment was submitted. Please see the docs.");
            }
        }


    }

    public void callTransactionApi() {
        new FinalPaymentOrder().execute();
    }

    private class FinalPaymentOrder extends AsyncTask<String, String, String> {

        int status = -1;
        String message;
        String urlParameters;

        JSONObject jsonObjParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(FinalChkoutActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            JsonParser jsonParser = new JsonParser();

            String url = Const.BASE_URL + Const.POST_FINAL_PAYMENT + orderId + "?TransactionId=" + transactionId;

            // Log.e("urel", url);


            try {

                String jsonString = jsonParser.getJSONFromUrl(url,
                        Const.TIME_OUT, userSession.getUserID());

                //  Log.e("jsonfinalcheck", jsonString);
                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    // Log.e("json", jsonString);
                    status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");
                    if (status == 1) {

                    }

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                status = -1;
            } catch (JSONException e) {

                e.printStackTrace();
                status = -1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            ProgresBar.stop();

            if (status == 1) {

                clearCart();

                passIntent();

            } else if (status == 0) {

                AlertDialogHelper.showAlertDialog(FinalChkoutActivity.this,
                        message, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(FinalChkoutActivity.this,
                        getString(R.string.server_message), "Alert");
            }
        }
    }

    private void clearCart() {
        DatabaseHelper databaseHelper = new DatabaseHelper(FinalChkoutActivity.this);
        databaseHelper.deleteModifierTable();
        databaseHelper.deleteItemTable();
        ;
        databaseHelper.deleteCheckOutTable();
    }

    public void passIntent() {
        Intent intent = new Intent(this, ThankYouActivity.class);
        intent.putExtra("OrderId", orderId);
        startActivity(intent);
    }

    private void getDeliveryType() {

        Cursor cursorOutletId = databaseHelper.selectOutletId();

        if (cursorOutletId != null) {
            if (cursorOutletId.moveToFirst()) {
                outletId = cursorOutletId.getString(0);
            }
        }


        if (CheckInternetHelper.checkInternetConnection(this)) {
            new GetDeliveryType().execute();
        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.internet_connection_message), "Alert");
        }
    }

    private class GetDeliveryType extends AsyncTask<String, String, String> {


        String message;
        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(FinalChkoutActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            JsonParser jsonParser = new JsonParser();

            arrayListDeliveryName = new ArrayList<>();

            String url = Const.BASE_URL + Const.GET_DELIVERY_TYPE + "outletId=" + outletId;

            String userId = userSession.getUserID();


            Log.e("userId", userId);

            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT, userId);

            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("json", jsonString);
                    status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");
                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Const.TAG_JsonObj);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            String deliveryTypeId = jsonObj.getString(Const.TAG_DELIVERY_ID);
                            String deliveryTypeName = jsonObj.getString(Const.TAG_DELIVERY_NAME);

                            arrayListDeliveryName.add(deliveryTypeName);
                        }


                    }
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                status = -1;
            } catch (JSONException e) {

                e.printStackTrace();
                status = -1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            ProgresBar.stop();

            if (status == 1) {

                AddChkOutCatfrag();

            } else if (status == 0) {
                AlertDialogHelper.showAlertDialog(FinalChkoutActivity.this,
                        message, "Alert");
            } else {
                AlertDialogHelper.showAlertDialog(FinalChkoutActivity.this,
                        getString(R.string.server_message), "Alert");
            }

            if(getIntent().hasExtra("gcm"))
            {
                if (userSession.getLogin()) {

                    userSession.saveLogin(false);
                    //Check if the device has not been registered to GCM
                    if (userSession.getGcmToken().equals("null")) {
                        Log.e("insde", "registerToGCM");
                        registerToGCM();
                    } else {
                        GCMHelper gcmHelper = new GCMHelper(FinalChkoutActivity.this);
                        gcmHelper.checkRegisterDevice();
                    }
                }
            }
        }
    }

    private void registerToGCM() {
        ProgresBar.start(this);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ProgresBar.stop();

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {

                    Log.e("gcm message", getString(R.string.gcm_send_message));



                } else {
                    Log.e("gcm message", getString(R.string.token_error_message));
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}
