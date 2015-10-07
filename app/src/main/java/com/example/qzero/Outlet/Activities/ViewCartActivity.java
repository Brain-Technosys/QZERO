package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.Adapters.CustomAdapterCartItem;
import com.example.qzero.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ViewCartActivity extends Activity {

    @InjectView(R.id.listCartItem)
    ListView listCartItem;

    @InjectView(R.id.txtViewHeading)
    TextView txtViewHeading;

    Button continue_shopping;
    Button placeOrder;

    ArrayList<HashMap<String, String>> mainCartItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        ButterKnife.inject(this);

        txtViewHeading.setText("Shopping Cart");

        mainCartItem = new ArrayList<>();

        setFont();
    }

    private void setFont() {
        FontHelper.setFontFace(txtViewHeading, FontHelper.FontType.FONT, this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> mySampleHashmap = new HashMap<>();
            mySampleHashmap.put("number", String.valueOf(i));
            mainCartItem.add(mySampleHashmap);
        }

        CustomAdapterCartItem adapterCartItem = new CustomAdapterCartItem(this, mainCartItem);


        // Adding footer
        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_final_chkout, null, false);
        continue_shopping = (Button) footerView.findViewById(R.id.continue_shopping);
        placeOrder = (Button) footerView.findViewById(R.id.placeOrder);

        clickEventOfContinueShopping();
        clickEventOfPlaceOrder();

        listCartItem.addFooterView(footerView);
        listCartItem.setAdapter(adapterCartItem);

    }


    private void clickEventOfContinueShopping() {
        continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void clickEventOfPlaceOrder() {
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewCartActivity.this, FinalChkoutActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }

    @OnClick(R.id.imgViewBack)
    void imgViewBack(){
        finish();
    }
}
