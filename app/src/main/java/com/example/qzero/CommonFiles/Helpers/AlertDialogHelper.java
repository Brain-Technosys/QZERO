package com.example.qzero.CommonFiles.Helpers;

import android.app.Activity;
import android.util.Log;
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
import com.example.qzero.R;

public class AlertDialogHelper {

    static CustomDialog dialog;

    static TextView txtViewTitle;
    static TextView txtViewAlertMsg;

    static TextView txtViewOk;
    static TextView txtViewCancel;
    static UserSession userSession;

    public static void showAlertDialog(Activity context, String message,
                                       String title) {

        setLayout(context, message, title);

        txtViewOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        dialog.show();

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

        setLayoutGetLocation(context, message, title);

        userSession = new UserSession(context);

        txtViewOk.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View view) {
                userSession.saveAppLaunchStatus(true);

                userSession.saveUserLocationPermission(true);

                ((HomeActivity) context).getUserLocation();

                //Alert box to ask delivery type
                alertBoxDeliveryType(context);

                dialog.dismiss();

            }
        });


        txtViewCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                userSession.saveAppLaunchStatus(true);
                userSession.saveUserLocationPermission(false);

                //Alert box to ask delivery type
                alertBoxDeliveryType(context);
                dialog.dismiss();
            }
        });

        dialog.show();

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

        setLayoutDeliveryType(context);

        dialog.show();

    }

    public static void setLayoutDeliveryType(Activity context) {
        final String[] deliveryType = new String[1];

        dialog = new CustomDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_deliverytype);

        txtViewTitle = (TextView) dialog.findViewById(R.id.txtViewTitle);
        RadioGroup rg_deliveryType = (RadioGroup) dialog.findViewById(R.id.rg_deliveryType);
        final RadioButton radioBtnShipment = (RadioButton) dialog.findViewById(R.id.radioBtnShipment);
        final RadioButton radioBtnPickUp = (RadioButton) dialog.findViewById(R.id.radioBtnPickUp);
        final RadioButton radioBtnInhouse = (RadioButton) dialog.findViewById(R.id.radioBtnInhouse);
        final TextView txt_deliveryType = (TextView) dialog.findViewById(R.id.txt_deliveryType);
        // txtViewAlertMsg = (TextView) dialog.findViewById(R.id.txtViewAlertMsg);
        txtViewOk = (TextView) dialog.findViewById(R.id.txtViewChange);

        FontHelper.applyFont(context, txtViewTitle, FontType.FONT);
        //  FontHelper.applyFont(context, txtViewAlertMsg, FontType.FONT);

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

//                Log.e("deliveryType",deliveryType[0]);
                //save user choice abt delivery type
                if (deliveryType != null) {
                    userSession.saveDeliveryType(deliveryType[0]);
                }
                dialog.dismiss();

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
}
