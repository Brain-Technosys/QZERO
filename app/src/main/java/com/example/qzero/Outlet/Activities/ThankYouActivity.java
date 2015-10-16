package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.R;
import com.paypal.android.sdk.payments.PayPalService;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Braintech on 10/16/2015.
 */
public class ThankYouActivity extends Activity {

    @InjectView(R.id.txtViewThankYou)
    TextView txtViewThankYou;

    @InjectView(R.id.txtViewOrderId)
    TextView txtViewOrderId;

    @InjectView(R.id.btn_home)
    Button btn_home;

    String orderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        ButterKnife.inject(this);
        if (getIntent().hasExtra("OrderId")) {

            Bundle bundle = getIntent().getExtras();
            orderId = String.valueOf(bundle.getInt("OrderId"));

            txtViewOrderId.setText("Your order id is " + orderId + ".");
        }

        setFont();


    }

    private void setFont() {
        FontHelper.applyFont(this, txtViewThankYou, FontHelper.FontType.FONTSANSBOLD);
        FontHelper.applyFont(this, txtViewThankYou, FontHelper.FontType.FONT);
    }

    @OnClick(R.id.btn_home)
    void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);


    }

}
