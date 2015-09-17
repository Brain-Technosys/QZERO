package com.example.qzero.Outlet.Fragments;

import android.os.AsyncTask;
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

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.Outlet.Activities.OutletActivity;
import com.example.qzero.Outlet.Activities.OutletCategoryActivity;
import com.example.qzero.Outlet.ObjectClasses.Category;
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.Outlet.ObjectClasses.SubCategory;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    JsonParser jsonParser;
    JSONObject jsonObject;
    JSONArray jsonArray;

    String venue_id;
    String outlet_id;
    String category_id;
    String sub_cat_id;



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
            FontHelper.setFontFace(txtViewSubHeading, FontType.FONT, getActivity());
        }

    }

    public void getSubCatItems(String venue_id, String outlet_id, String category_id, String sub_cat_id) {
        this.venue_id=venue_id;
        this.outlet_id=outlet_id;
        this.category_id=category_id;
        this.sub_cat_id=sub_cat_id;

        new GetItems().execute();
    }

    public class GetItems extends AsyncTask<String, String, String> {

        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("inside", "do in");
            status = -1;
            pos=0;

          /*  String venue_id = params[0];
            String outletId = params[1];
            String itemId = params[2];
            String subCatId = params[3];*/

            /*Log.e("venue", venue_id);
            Log.e("outletId", outletId);
            Log.e("itemId", itemId);
            Log.e("subCatId", subCatId);*/

            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_ITEMS + venue_id + "/?outletId="+ outlet_id + "&itemId="+ category_id
                    + "&subCatId="+ sub_cat_id;


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            Log.e("jsonvenue", jsonString);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("inside", "json");

                    arrayListItems = new ArrayList<ItemOutlet>(jsonObject.length());

                    status = jsonObject.getInt(Const.TAG_STATUS);
                    Const.TAG_MESSAGE = jsonObject.getString(Const.TAG_MESSAGE);

                    if (status == 1) {

                        JSONObject jsonObj = jsonObject.getJSONObject(Const.TAG_JsonObj);

                        //Get json Array for items
                        jsonArray = new JSONArray();
                        jsonArray = jsonObj.getJSONArray(Const.TAG_JsonItemObj);




                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjItem = jsonArray.getJSONObject(i);

                            String item_id = jsonObjItem.getString(Const.TAG_ITEM_ID);
                            String item_name = jsonObjItem.getString(Const.TAG_CAT_ITEM_NAME);
                            String item_price = jsonObjItem.getString(Const.TAG_PRICE);
                            String item_desc = jsonObjItem.getString(Const.TAG_DESC);
                            String sub_item_id = jsonObjItem.getString(Const.TAG_SUB_ID);
                            String item_image = Const.BASE_URL + Const.IMAGE_URL + item_id;

                            ItemOutlet ItemOutlet = new ItemOutlet(item_id, item_name, item_image, item_price, item_desc, sub_item_id);
                            arrayListItems.add(ItemOutlet);
                        }

                    }

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            ProgresBar.stop();

            Log.e("inside", "postexecute");

            if (status == 1) {
                setTableLayout();
            } else if (status == 0) {

                AlertDialogHelper.showAlertDialog(getActivity(), Const.TAG_MESSAGE, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(getActivity(), getString(R.string.server_message), "Alert");
            }
        }
    }

    private void setTableLayout() {

        int row;
        Log.e("length", "" + arrayListItems.size());
        length = arrayListItems.size();
        if (length % 2 == 0) {
            row = length / 2;

        } else {
            row = length / 2 + 1;
            Log.e("row", "" + row);

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
                Log.e("odd",""+i);

                if (i == rows) {
                    Log.e("i", "" + i);
                    Log.e("inrows", "" + rows);
                    cols = 1;
                }
            } else {
                cols = 2;
            }

            Log.e("cols", "" + cols);
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
        setFonts();
        initializeLayoutWidth();
        setOnClick();
    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewItemName, FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewTitleOverlay, FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewItemPrice, FontType.FONT, getActivity());
    }

    private void initializeLayoutWidth() {
        ViewGroup.LayoutParams paramsLeft = relLayItem.getLayoutParams();

        // Changes the height and width to the specified *pixels*
        paramsLeft.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsLeft.width = 340;
    }

    public void setOnClick() {
        relLayItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OutletCategoryActivity) getActivity()).replaceFragment();
            }
        });

    }

    private void inflateData() {

        ItemOutlet itemOutlet = arrayListItems.get(pos);

        txtViewItemName.setText(itemOutlet.getName());
        txtViewTitleOverlay.setText(itemOutlet.getName());
        txtViewItemPrice.setText("$" + itemOutlet.getPrice());
        pos++;

        //Load Image
        //Picasso.with(this).load(itemOutlet.getItem_image()).into(imgViewItem);}

    }
}
