package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Braintech on 06-Oct-15.
 */
public class FinalChkoutActivity extends Activity {

    TableLayout[] tableItems;
    TableLayout tableModifiers;

    @InjectView(R.id.layout_your_order)
    LinearLayout linearTableContent;

    String[] srNum = {"1", "2"};
    String[] productName = {"Roasted Stuffed Mushroom", "Honey Chilli Potato"};
    String[] price = {"10.00", "10.00"};
    String[] qty = {"5", "4"};

    View child[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_chkout);
        ButterKnife.inject(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        createTableItems();
    }

    private void createTableItems() {
        child=new View[qty.length];
        tableItems=new TableLayout[qty.length];

        for(int i=0;i<qty.length;i++) {

            child[i] = getLayoutInflater().inflate(R.layout.chkout_item_table, null);

            tableItems[i]=(TableLayout)child[i].findViewById(R.id.items);

            LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            TableRow row=new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT);
//            row.setLayoutParams(tableItems.getLayoutParams());
            TextView txtsrNum = new TextView(this);
     //       txtsrNum.setLayoutParams(row.getLayoutParams());
//            txtsrNum.setTextColor(Color.parseColor("#000000"));
            txtsrNum.setText(srNum[i]);
            row.addView(txtsrNum,param);

            TextView txtName = new TextView(this);
           // txtName.setLayoutParams(row.getLayoutParams());
//            txtName.setTextColor(Color.parseColor("#000000"));
            txtName.setText(productName[i]);
            row.addView(txtName,param);

            TextView txtPrice = new TextView(this);
         //   txtPrice.setLayoutParams(row.getLayoutParams());
//            txtPrice.setTextColor(Color.parseColor("#000000"));
            txtPrice.setText(price[i] + "$ * " + qty[i]+" $");
            row.addView(txtPrice,param);

            TextView txtTotal = new TextView(this);
           // txtTotal.setLayoutParams(row.getLayoutParams());
//            txtTotal.setTextColor(Color.parseColor("#000000"));
            txtTotal.setText(String.valueOf(Double.parseDouble(price[i]) * Double.parseDouble(qty[i]))+" $");
            row.addView(txtTotal, param);

            tableItems[i].addView(row,lp);
            linearTableContent.addView(child[i]);
        }




    }

    @OnClick(R.id.next)public void next(){

    }
}
