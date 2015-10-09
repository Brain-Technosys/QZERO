package com.example.qzero.Outlet.Activities;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.example.qzero.Outlet.Fragments.ChkoutCatFragment;

import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Braintech on 06-Oct-15.
 */
public class FinalChkoutActivity extends AppCompatActivity {

    TableLayout[] tableItems;
    TableLayout tableModifiers;

    @InjectView(R.id.layout_your_order)
    LinearLayout linearTableContent;

    String[] srNum = {"1", "2"};
    String[] productName = {"Roasted Stuffed Mushroom", "Honey Chilli Potato"};
    String[] price = {"10.00", "10.00"};
    String[] qty = {"5", "4"};

    @InjectView(R.id.chkout_detail_frag)
    FrameLayout chkout_detail_frag;

    View child[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_chkout);
        ButterKnife.inject(this);

        AddChkOutCatfrag();

    }

    private void AddChkOutCatfrag() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        ChkoutCatFragment chkoutCatFragment = new ChkoutCatFragment();
        fragmentTransaction.add(R.id.chkout_detail_frag, chkoutCatFragment, "login");
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void createTableItems() {
        child = new View[qty.length];
        tableItems = new TableLayout[qty.length];

        for (int i = 0; i < qty.length; i++) {

            child[i] = getLayoutInflater().inflate(R.layout.chkout_item_table, null);

            tableItems[i] = (TableLayout) child[i].findViewById(R.id.items);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


//            tableItems[i].addView(row,lp);
            linearTableContent.addView(child[i]);
        }


    }


}
