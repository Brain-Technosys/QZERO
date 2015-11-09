package com.example.qzero.Outlet.Fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.Outlet.Activities.OutletActivity;
import com.example.qzero.Outlet.Activities.OutletCategoryActivity;
import com.example.qzero.Outlet.ObjectClasses.Category;
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.Outlet.ObjectClasses.Outlet;
import com.example.qzero.Outlet.ObjectClasses.SubCategory;
import com.example.qzero.Outlet.SlidingUpPanel.SlidingUpPanelLayout;
import com.example.qzero.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CategoryItemFragment extends Fragment implements SearchView.OnQueryTextListener {

    @InjectView(R.id.txtViewSubHeading)
    TextView txtViewSubHeading;

    @InjectView(R.id.tableLayoutItems)
    TableLayout tableLayoutItems;

    @InjectView(R.id.search_view)
    SearchView search_view;

    TextView txtViewItemName;

    TextView txtViewItemPrice;

    TextView txtViewTitleOverlay;

    ImageView imgViewItem;

    LinearLayout relLayItem;

    TextView txtViewItemNameRight;

    TextView txtViewItemPriceRight;

    TextView txtViewTitleOverlayRight;

    ImageView imgViewItemRight;

    LinearLayout relLayItemRight;

    CardView cardViewRight;

    TableRow tableRow;

    View child;

    int length;
    int pos = 0;

    ArrayList<ItemOutlet> arrayListItems;
    ArrayList<ItemOutlet> orig;

    JsonParser jsonParser;
    JSONObject jsonObject;
    JSONArray jsonArray;

    String venue_id;
    String outlet_id;
    String category_id;
    String sub_cat_id;

    String item_id;


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

        setupSearchView();

        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });


    }

    private void getIntentData() {
        arrayListItems = new ArrayList<ItemOutlet>();

        if (getArguments().containsKey("arraylistitem")) {

            venue_id = getArguments().getString("venue_id");
            outlet_id = getArguments().getString("outlet_id");


            arrayListItems = (ArrayList<ItemOutlet>) getArguments().getSerializable("arraylistitem");
            Log.e("arraylistlen", "" + arrayListItems.size());

            txtViewSubHeading.setText(getArguments().getString("title"));

            FontHelper.setFontFace(txtViewSubHeading, FontType.FONT, getActivity());
        }

    }

    private void setupSearchView() {
        search_view.setIconifiedByDefault(false);
        search_view.setOnQueryTextListener(this);
        search_view.setSubmitButtonEnabled(true);
        search_view.setQueryHint("Search Items");
        search_view.setSubmitButtonEnabled(false);
    }

    public boolean onQueryTextChange(String newText) {

        Log.e("newtext", newText);
        if (TextUtils.isEmpty(newText)) {

            filterJson(newText);

        } else {

            filterJson(newText);
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {

        Log.e("qyuery", query);
        return false;
    }

    public void filterJson(String newText) {
        final ArrayList<ItemOutlet> results = new ArrayList<ItemOutlet>();
        if (orig == null)
            orig = arrayListItems;

        if (newText != null) {
            if (orig != null && orig.size() > 0) {
                for (final ItemOutlet item : orig) {
                    if (String.valueOf(item.getName()).toLowerCase()
                            .contains(newText.toString()) || String.valueOf(item.getName())
                            .contains(newText.toString()))
                        results.add(item);
                }
            }
            arrayListItems = results;
        }

        pos = 0;
        setTableLayout();
    }

    public void getSubCatItems(String venue_id, String outlet_id, String category_id, String sub_cat_id) {

        search_view.setFocusable(false);

        this.venue_id = venue_id;
        this.outlet_id = outlet_id;
        this.category_id = category_id;
        this.sub_cat_id = sub_cat_id;

        Log.e("venue_id",venue_id);
        Log.e("outlet_id",outlet_id);
        Log.e("category_id",category_id);


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

            //  Log.e("inside", "do in");
            status = -1;
            pos = 0;


            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_ITEMS + venue_id + "/?outletId=" + outlet_id + "&itemId=" +item_id+ "&catId="+category_id
                     + "&subCatId=" + sub_cat_id;

            Log.e("url", url);


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            Log.e("jsonitem", jsonString);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("inside", "json");

                    arrayListItems = new ArrayList<ItemOutlet>(jsonObject.length());

                    status = jsonObject.getInt(Const.TAG_STATUS);

                    Log.e("status", "" + status);
                    //  ConstVarIntent.TAG_MESSAGE = jsonObject.getString(ConstVarIntent.TAG_MESSAGE);

                    if (status == 1) {

                        JSONObject jsonObj = jsonObject.getJSONObject(Const.TAG_JsonObj);

                        //Get json Array for items
                        jsonArray = new JSONArray();
                        jsonArray = jsonObj.getJSONArray(Const.TAG_JsonItemObj);

                        Log.e("jsonaaa", "" + jsonArray.length());


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

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenHeight = metrics.heightPixels/4;



            TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    screenHeight));

           /* if (length % 2 != 0) {
                Log.e("odd", "" + i);


            } else {
                cols = 2;
            }

            Log.e("cols", "" + cols);
            // inner for loop
            for (int j = 0; j < cols; j++) {*/


            child = getActivity().getLayoutInflater().inflate(R.layout.item_category, null);
            getItemId();

            inflateData();
            inflateRightData();
            // child.setOnClickListener(this);

            row.addView(child);


            //}

            tableLayoutItems.addView(row);

            imgViewItem.requestLayout();

            imgViewItem.getLayoutParams().height=screenHeight;

            imgViewItemRight.requestLayout();
            imgViewItemRight.getLayoutParams().height=screenHeight;

            if (pos>length-1) {
                Log.e("i", "" + i);
                Log.e("inrows", "" + rows);


            }

        }
    }

    private void getItemId() {
        relLayItem = (LinearLayout) child.findViewById(R.id.relLayItem);

        imgViewItem = (ImageView) child.findViewById(R.id.imgViewItem);




        txtViewItemName = (TextView) child.findViewById(R.id.txtViewItemName);
        txtViewTitleOverlay = (TextView) child.findViewById(R.id.txtViewTitleOverlay);
        txtViewItemPrice = (TextView) child.findViewById(R.id.txtViewItemPrice);

        relLayItemRight = (LinearLayout) child.findViewById(R.id.relLayItemRight);

        imgViewItemRight = (ImageView) child.findViewById(R.id.imgViewItemRight);

        txtViewItemNameRight = (TextView) child.findViewById(R.id.txtViewItemNameRight);
        txtViewTitleOverlayRight = (TextView) child.findViewById(R.id.txtViewTitleOverlayRight);
        txtViewItemPriceRight = (TextView) child.findViewById(R.id.txtViewItemPriceRight);

        cardViewRight = (CardView) child.findViewById(R.id.cardViewRight);

        setFonts();
        //  initializeLayoutWidth();
        setOnClick();
    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewItemName, FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewTitleOverlay, FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewItemPrice, FontType.FONT, getActivity());
    }

    private void initializeLayoutWidth() {

        ViewGroup.LayoutParams paramsLeft = relLayItem.getLayoutParams();
        ViewGroup.LayoutParams paramsImage = imgViewItem.getLayoutParams();

        DisplayMetrics dm = getResources().getDisplayMetrics();

        float fwidth = dm.density * dm.widthPixels;
        float fheight = dm.density * dm.heightPixels;

        Log.e("dm", "" + dm.density);

        //for devices whose density is 0.75 mainly idpi ex 320 *240
        if (dm.density == 0.75 || dm.density < 0.75) {

            paramsLeft.width = Math.round(fwidth / 3) + 35;
            paramsLeft.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            //Image Category icon
            paramsImage.width = Math.round(fwidth / 3) + 20;
            paramsImage.height = Math.round(fheight / 3);

            //for devices whose density is 1 mainly mdpi 320 *480
        } else if (dm.density == 1 || (dm.density > 0.75 && dm.density < 1)) {

            paramsLeft.width = Math.round(fwidth / 2) - 10;
            paramsLeft.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            //Image Category icon
            paramsImage.width = Math.round(fwidth / 2) - 20;
            paramsImage.height = Math.round(fheight / 2) - 80;

        }

        //for devices whose density is 1.5 mainly hdpi
        else if (dm.density == 1.5 || (dm.density > 1 && dm.density < 1.5)) {

            paramsLeft.width = Math.round(fwidth / 4) + 30;
            paramsLeft.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            //Image Category icon
            paramsImage.width = Math.round(fwidth / 4) + 20;
            paramsImage.height = Math.round(fheight / 4) - 100;
        }
        //for devices whose density is 2 mainly xhdpi
        else if (dm.density == 2 || (dm.density > 1.5 && dm.density < 2)) {

            paramsLeft.width = Math.round(fwidth / 4) - 40;
            paramsLeft.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            //Image Category icon
            paramsImage.width = Math.round(fwidth / 4) - 30;
            paramsImage.height = Math.round(fheight / 4) - 250;

            //for devices whose density is 3 mainly xxhdpi
        } else if (dm.density == 3 || (dm.density > 2 && dm.density < 3)) {

            paramsLeft.width = Math.round(fwidth / 4) + 100;
            paramsLeft.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            //Image Category icon
            paramsImage.width = Math.round(fwidth / 4) + 90;
            paramsImage.height = Math.round(fheight / 4) - 540;

        } else if ((dm.density == 4) || (dm.density > 3 && dm.density < 4)) {

            paramsLeft.width = Math.round(fwidth / 8) - 80;
            paramsLeft.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            //Image Category icon
            paramsImage.width = Math.round(fwidth / 8) - 60;
            paramsImage.height = Math.round(fheight / 8) - 560;

        } else {

            paramsLeft.width = Math.round(fwidth / 8) - 80;
            paramsLeft.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            //Image Category icon
            paramsImage.width = Math.round(fwidth / 8) - 60;
            paramsImage.height = Math.round(fheight / 8) - 560;


        }
    }

    public void setOnClick() {
        relLayItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item_id = v.getTag().toString();

                Log.e("tag", v.getTag().toString());

                ((OutletCategoryActivity) getActivity()).replaceFragment(venue_id, outlet_id, item_id);
            }
        });

        relLayItemRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item_id = v.getTag().toString();

                Log.e("tag", v.getTag().toString());

                ((OutletCategoryActivity) getActivity()).replaceFragment(venue_id, outlet_id, item_id);
            }
        });

    }

    private void inflateData() {

        ItemOutlet itemOutlet = arrayListItems.get(pos);

        Log.e("position1",""+pos);

        relLayItem.setTag(itemOutlet.getItem_id());

        txtViewItemName.setText(itemOutlet.getName());
        txtViewTitleOverlay.setText(itemOutlet.getName());
        txtViewItemPrice.setText(Utility.formatCurrency(itemOutlet.getPrice()));
        pos++;

        //Load Image
        Picasso.with(getActivity()).load(itemOutlet.getItem_image()).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder).into(imgViewItem);

    }

    private void inflateRightData() {
        Log.e("position2",""+pos);
        if (pos <= length - 1) {

            ItemOutlet itemOutlet = arrayListItems.get(pos);

            relLayItemRight.setTag(itemOutlet.getItem_id());

            txtViewItemNameRight.setText(itemOutlet.getName());
            txtViewTitleOverlayRight.setText(itemOutlet.getName());
            txtViewItemPriceRight.setText(Utility.formatCurrency(itemOutlet.getPrice()));
            pos++;

            //Load Image
            Picasso.with(getActivity()).load(itemOutlet.getItem_image()).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder).into(imgViewItemRight);

        }
        else
        {
            cardViewRight.setVisibility(View.INVISIBLE);
        }
    }
}
