package com.example.qzero.Outlet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.qzero.Outlet.Activities.OutletCategoryActivity;
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CategoryItemFragment extends Fragment {

    @InjectView(R.id.txtViewSubHeading)
    TextView txtViewSubHeading;

    @InjectView(R.id.tableLayoutItems)
    TableLayout tableLayoutItems;

    TextView txtViewItemName;

    TextView txtViewItemPrice;

    TextView txtViewTitleOverlay;

    ImageView imgViewItem;

    RelativeLayout relLayItem;

    TableRow tableRow;

    View child;

    int length;
    int pos = 0;

    ArrayList<ItemOutlet> arrayListItems;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item,
                container, false);
        ButterKnife.inject(this, rootView);
        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getIntentData();

        setTableLayout();
    }

    private void getIntentData() {
        arrayListItems = new ArrayList<ItemOutlet>();

        if (getArguments().containsKey("arraylistitem")) {

            arrayListItems = (ArrayList<ItemOutlet>) getArguments().getSerializable("arraylistitem");
            Log.e("arraylistlen", "" + arrayListItems.size());
            txtViewSubHeading.setText(getArguments().getString("title"));
        }

    }

    private void setTableLayout() {

        int row;
        length = arrayListItems.size();
        if (length % 2 == 0) {
            row = length / 2;

        } else {
            row = length / 2 + 1;
            Log.e("row",""+row);

        }
        tableLayoutItems.removeAllViews();
        BuildTable(row, 2);

    }

    private void BuildTable(int rows, int cols) {

        // outer for loop
        for (int i = 1; i <= rows; i++) {

            TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            if (length % 2 != 0) {
                if (i == rows) {
                    Log.e("i",""+i);
                    Log.e("inrows",""+rows);
                    cols = 1;
                }
            } else {
                cols = 2;
            }

            Log.e("cols",""+cols);
            // inner for loop
            for (int j = 0; j < cols; j++) {

                child = getActivity().getLayoutInflater().inflate(R.layout.item_category, null);
                child.setPadding(0, 0, 10, 0);
                getItemId();
                inflateData();
                // child.setOnClickListener(this);

                row.addView(child);
            }

            tableLayoutItems.addView(row);

        }
    }

    private void getItemId() {
        relLayItem = (RelativeLayout) child.findViewById(R.id.relLayItem);

        imgViewItem = (ImageView) child.findViewById(R.id.imgViewItem);

        txtViewItemName = (TextView) child.findViewById(R.id.txtViewItemName);
        txtViewTitleOverlay = (TextView) child.findViewById(R.id.txtViewTitleOverlay);
        txtViewItemPrice = (TextView) child.findViewById(R.id.txtViewItemPrice);

        initializeLayoutWidth();
        setOnClick();
    }

    private void initializeLayoutWidth() {
        ViewGroup.LayoutParams paramsLeft = relLayItem.getLayoutParams();

        // Changes the height and width to the specified *pixels*
        paramsLeft.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsLeft.width = 340;
    }

    public void setOnClick()
    {
        ((OutletCategoryActivity)getActivity()).replaceFragment();
    }

    private void inflateData() {

        ItemOutlet itemOutlet = arrayListItems.get(pos);

        txtViewItemName.setText(itemOutlet.getName());
        txtViewTitleOverlay.setText(itemOutlet.getName());
        txtViewItemPrice.setText("$" + itemOutlet.getPrice());
        pos++;

        //Load Image
        //Picasso.with(this).load(itemOutlet.getItem_image()).into(imgViewItem);
    }

}
