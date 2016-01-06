package com.example.qzero.CommonFiles.Helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.example.qzero.CommonFiles.Common.CustomDialog;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;

import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.Outlet.Activities.HomeActivity;
import com.example.qzero.Outlet.Activities.OutletCategoryActivity;
import com.example.qzero.R;

import butterknife.InjectView;

public class AlertDialogHelper {

    static CustomDialog dialog;

    static TextView txtViewTitle;
    static TextView txtViewAlertMsg;

    static TextView txtViewOk;
    static TextView txtViewCancel;
    static UserSession userSession;

    public static void showAlertDialog(Activity context, String message,
                                       String title) {

        try {

            setLayout(context, message, title);

            txtViewOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }

            });
            dialog.show();
        }
        catch (RuntimeException ex)
        {
            ex.printStackTrace();
        }

    }

    public static void showAlertDialogFinish(final Activity context,
                                             String message, String title) {

        txtViewOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
                context.finish();
            }

        });
        dialog.show();

    }

    //Alert Dialog box to get user permission about location

    public static void showAlertBoxLocationPermission(final Activity context, String message, String title) {

        try {

            setLayoutGetLocation(context, message, title);

            userSession = new UserSession(context);

            txtViewOk.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    userSession.saveAppLaunchStatus(true);

                    userSession.saveUserLocationPermission(true);

                    ((HomeActivity) context).getUserLocation();



                    dialog.dismiss();

                    //Alert box to ask delivery type
                    alertBoxDeliveryType(context);

                }
            });


            txtViewCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    userSession.saveAppLaunchStatus(true);
                    userSession.saveUserLocationPermission(false);


                    dialog.dismiss();

                    //Alert box to ask delivery type
                    alertBoxDeliveryType(context);
                }
            });

            dialog.show();
        }
        catch(RuntimeException ex)
        {
            ex.printStackTrace();
        }

    }


    public static void setLayout(Activity context, String message, String title) {
        dialog = new CustomDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_alert_box);

        txtViewTitle = (TextView) dialog.findViewById(R.id.txtViewTitle);
        FontHelper.applyFont(context, txtViewTitle, FontType.FONT);
        txtViewTitle.setText(title);

        txtViewAlertMsg = (TextView) dialog
                .findViewById(R.id.txtViewAlertMsg);
        txtViewAlertMsg.setText(message);

        FontHelper.applyFont(context, txtViewAlertMsg, FontType.FONT);

        txtViewOk = (TextView) dialog.findViewById(R.id.txtViewOk);


        FontHelper.applyFont(context, txtViewOk, FontType.FONT);

    }

    //Alert Dialog box to get user permission about location

    public static void alertBoxDeliveryType(final Activity context) {

        try {

            setLayoutDeliveryType(context);

            dialog.show();
        }
        catch(RuntimeException ex)
        {
            ex.printStackTrace();
        }

    }

    public static void setLayoutDeliveryType(final Activity context) {

        final String[] deliveryType = new String[1];

        dialog = new CustomDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_deliverytype);
        dialog.setCanceledOnTouchOutside(false);
        txtViewTitle = (TextView) dialog.findViewById(R.id.txtViewTitle);

        RadioGroup rg_deliveryType = (RadioGroup) dialog.findViewById(R.id.rg_deliveryType);

        final RadioButton radioBtnShipment = (RadioButton) dialog.findViewById(R.id.radioBtnShipment);
        final RadioButton radioBtnPickUp = (RadioButton) dialog.findViewById(R.id.radioBtnPickUp);
        final RadioButton radioBtnInhouse = (RadioButton) dialog.findViewById(R.id.radioBtnInhouse);
        final TextView txt_deliveryType = (TextView) dialog.findViewById(R.id.txt_deliveryType);

        txtViewOk = (TextView) dialog.findViewById(R.id.txtViewChange);

        FontHelper.applyFont(context, txtViewTitle, FontType.FONT);


        rg_deliveryType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {



                // find which radio button is selected

                if (checkedId == R.id.radioBtnShipment) {
                    deliveryType[0] = radioBtnShipment.getText().toString();
                } else if (checkedId == R.id.radioBtnPickUp) {
                    deliveryType[0] = radioBtnPickUp.getText().toString();
                } else if (checkedId == R.id.radioBtnInhouse) {
                    deliveryType[0] = radioBtnInhouse.getText().toString();
                }


            }

        });


        txtViewOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                 String service_type=null;
                //save user choice abt delivery type
                if (deliveryType != null) {

                    if(deliveryType[0].equals("In-Venue"))
                    {
                        service_type=context.getString(R.string.in_venue);
                    }
                    else
                    if(deliveryType[0].equalsIgnoreCase("pick up"))
                    {
                        service_type=context.getString(R.string.pick_up);
                    }
                    else
                    if(deliveryType[0].equalsIgnoreCase("Delivery"))
                    {
                        service_type=context.getString(R.string.delivery);
                    }
                    userSession.saveDeliveryType(service_type);
                }
                dialog.dismiss();

                if(context instanceof OutletCategoryActivity){
                    Intent intent=new Intent(context,HomeActivity.class);
                    context.startActivity(intent);
                }

            }
        });

        FontHelper.applyFont(context, txtViewOk, FontType.FONT);

        userSession = new UserSession(context);
        if (userSession.getDeliveryType() != null) {
            txt_deliveryType.setVisibility(View.VISIBLE);
            txt_deliveryType.setText("Your selected delivery type is  :  "+userSession.getDeliveryType());
        }else{
            txt_deliveryType.setVisibility(View.INVISIBLE);
        }
    }

    //Alert Dialog box set Layout for get location
    public static void setLayoutGetLocation(Activity context, String message, String title) {
        dialog = new CustomDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_alert_box);
        dialog.setCanceledOnTouchOutside(false);

        txtViewTitle = (TextView) dialog.findViewById(R.id.txtViewTitle);
        FontHelper.applyFont(context, txtViewTitle, FontType.FONT);
        txtViewTitle.setText(title);

        txtViewAlertMsg = (TextView) dialog
                .findViewById(R.id.txtViewAlertMsg);
        txtViewAlertMsg.setText(message);

        FontHelper.applyFont(context, txtViewAlertMsg, FontType.FONT);

        txtViewOk = (TextView) dialog.findViewById(R.id.txtViewOk);
        txtViewOk.setText(context.getString(R.string.alertbox_allow));

        txtViewCancel = (TextView) dialog.findViewById(R.id.txtViewCancel);
        txtViewCancel.setVisibility(View.VISIBLE);
        txtViewCancel.setText(context.getString(R.string.alertbox_block));

        FontHelper.applyFont(context, txtViewOk, FontType.FONT);
        FontHelper.applyFont(context, txtViewCancel, FontType.FONT);
    }

    public static void showAlertDialogSettings(final Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setTitle("Alert");

        // Setting OK Button
        alertDialog.setPositiveButton("Go to settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentapiVersion >= Build.VERSION_CODES.M) {

                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent.setData(uri);
                            context.startActivity(intent);
                        } else {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                        dialog.dismiss();
                    }
                });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        try {
            // Showing Alert Message
            AlertDialog alert = alertDialog.show();
            TextView messageText = (TextView) alert
                    .findViewById(android.R.id.message);
            // Fonts.myriadProRegular(context, messageText);
            messageText.setGravity(Gravity.CENTER);

            alert.show();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
}
