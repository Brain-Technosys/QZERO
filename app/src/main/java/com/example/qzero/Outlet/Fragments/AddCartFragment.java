package com.example.qzero.Outlet.Fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.Outlet.Adapters.CustomAdapterAddItems;
import com.example.qzero.Outlet.Adapters.ItemDetailAdapter;
import com.example.qzero.Outlet.ExpandableListView.ExpandableListView;
import com.example.qzero.Outlet.ObjectClasses.AddItems;
import com.example.qzero.Outlet.ObjectClasses.Category;
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.Outlet.ObjectClasses.SubCategory;
import com.example.qzero.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddCartFragment extends Fragment {

    @InjectView(R.id.txtViewItemName)
    TextView txtViewItemName;

    @InjectView(R.id.txtViewTitleDesc)
    TextView txtViewTitleDesc;

    @InjectView(R.id.txtViewDesc)
    TextView txtViewDesc;

    @InjectView(R.id.txtViewAddItem)
    TextView txtViewAddItem;

    @InjectView(R.id.txtViewOrigPrice)
    TextView txtViewOrigPrice;

    @InjectView(R.id.txtViewDiscount)
    TextView txtViewDiscount;

    @InjectView(R.id.txtViewDiscPrice)
    TextView txtViewDiscPrice;

    @InjectView(R.id.imgViewItem)
    ImageView imgViewItem;

   /* @InjectView(R.id.linLayItem)
    LinearLayout linLayItem;*/

    @InjectView(R.id.listViewItems)
    ExpandableListView listViewItems;


    TableLayout tableLayoutModifiers;

    Dialog dialog;

    LinearLayout linLayModifiers;

    TextView txtViewTitle;
    TextView txtViewCancel;
    TextView txtViewOk;

    TextView txtViewModList;

    TextView txtViewTotal;
    TextView txtViewPrice;

    Boolean isRadioButtonClicked = false;
    Boolean isCheckBoxClicked = false;

    CheckBox checkBox[];
    RadioGroup radioGroup[];
    RadioButton radioButton[];

    int count=0;

    ArrayList<AddItems> arrayListAddItems;

    CustomAdapterAddItems adapter;

    int status;
    int jsonLength;
    int pos = 0;

    String message;

    JsonParser jsonParser;
    JSONObject jsonObject;
    JSONArray jsonArray;

    String venue_id;
    String itemId;
    String outletId;
    String subCatId;

    String item_name;
    String item_desc;
    String item_price;
    String discount_details;
    String item_image;

    String[] modifier_title;
    String[] modifier;
    String[] mod_price;

    String discountDesc;
    String afterDiscPrice;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_cart,
                container, false);
        ButterKnife.inject(this, rootView);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setFonts();

        getArgData();

        getItemDetails();


    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewItemName, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewTitleDesc, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewDesc, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewOrigPrice, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewDiscount, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewDiscPrice, FontHelper.FontType.FONT, getActivity());

    }

    public void getArgData()
    {
        Bundle args=getArguments();
        if(args!=null)
        {
            venue_id=args.getString("venue_id");
            outletId=args.getString("outlet_id");
            itemId=args.getString("item_id");
        }
    }

    private void getItemDetails() {
        if (CheckInternetHelper.checkInternetConnection(getActivity())) {
            new GetItemDetail().execute();
        } else {
            AlertDialogHelper.showAlertDialog(getActivity(), getString(R.string.internet_connection_message), "Alert");
        }
    }

    private class GetItemDetail extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("inside", "do in");
            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_ITEM_DETAILS+"/"+venue_id + "/?outletId=" + outletId + "&itemId=" +itemId
                    + "&subCatId=" +subCatId;

            Log.e("url",url);


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            Log.e("jsonvenue", jsonString);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("inside", "json");

                    status = jsonObject.getInt(Const.TAG_STATUS);
                    message = jsonObject.getString(Const.TAG_MESSAGE);
                    afterDiscPrice=jsonObject.getString(Const.TAG_AFTER_DISC);
                    discountDesc=jsonObject.getString(Const.TAG_DISC_DETAIL);

                    Log.d("status", "" + status);
                    if (status == 1) {

                        JSONObject jsonObj = jsonObject.getJSONObject(Const.TAG_JsonObj);

                        //Get json Array for items
                        jsonArray = new JSONArray();
                        jsonArray = jsonObj.getJSONArray(Const.TAG_JsonDetailObj);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjItem = jsonArray.getJSONObject(i);


                            String item_id=jsonObjItem.getString(Const.TAG_ITEM_ID);
                            item_name = jsonObjItem.getString(Const.TAG_NAME);
                            item_price = jsonObjItem.getString(Const.TAG_PRICE);
                            item_desc = jsonObjItem.getString(Const.TAG_DESC);
                            item_image = Const.BASE_URL + Const.IMAGE_URL + item_id;

                        }

                        //Get json array for categories
                        JSONArray jsonArrayModifiers = new JSONArray();

                        jsonArrayModifiers = jsonObj.getJSONArray(Const.TAG_JsonChoiceObj);

                        modifier_title=new String[jsonArrayModifiers.length()];

                        for (int i = 0; i < jsonArrayModifiers.length(); i++) {
                            JSONObject jsonObjGroup = jsonArrayModifiers.getJSONObject(i);

                            modifier_title[i] = jsonObjGroup.getString(Const.TAG_NAME);

                            JSONArray jsonArrayMod = jsonObjGroup.getJSONArray(Const.TAG_JsonModObj);

                            modifier=new String[jsonArrayMod.length()];
                            mod_price=new String[jsonArrayMod.length()];

                            for (int j = 0; j < jsonArrayMod.length(); j++) {
                                JSONObject jsonObjSubCat = jsonArrayMod.getJSONObject(j);
                                modifier[i] = jsonObjSubCat.getString(Const.TAG_NAME);
                                mod_price[i] = jsonObjSubCat.getString(Const.TAG_PRICE);
                            }
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ProgresBar.stop();

            inflateQtyLayout();
        }
    }


    @OnClick(R.id.txtViewAddItem)
    void addItem() {
        addItemLayout();
    }

    private void addItemLayout() {

        count++;
        String[] modifiers={};

        AddItems md = new AddItems(modifiers,String.valueOf(count),"10");

        arrayListAddItems.add(0, md);
        //modelList.add(md);
        adapter.notifyDataSetChanged();
        //inflateQtyLayout();
    }

    private void inflateQtyLayout() {

        setLayout();

        arrayListAddItems = new ArrayList<AddItems>();

        adapter = new CustomAdapterAddItems(getActivity(),arrayListAddItems,modifier_title,modifier,mod_price);
        listViewItems.setAdapter(adapter);
    }

    public void setLayout()
    {
        txtViewItemName.setText(item_name);
        txtViewDesc.setText(item_desc);
        txtViewOrigPrice.setText(item_price);
        txtViewDiscount.setText(discountDesc);
        txtViewDiscPrice.setText(afterDiscPrice);

        //Load Image
        Picasso.with(getActivity()).load(item_image).error(R.drawable.q2x).into(imgViewItem);

    }


    private void setAddItemFonts() {
        FontHelper.setFontFace(txtViewTotal, FontHelper.FontType.FONTROBOLD, getActivity());
    }

    private void BuildTable(int rows) {

        txtViewModList.setVisibility(View.VISIBLE);

        FontHelper.setFontFace(txtViewModList, FontHelper.FontType.FONTROBOLD, getActivity());
        // outer for loop
        for (int i = 1; i <= rows; i++) {

            TableRow row = new TableRow(getActivity());
            row.setPadding(10, 10, 10, 10);

            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));


            TextView txtView = new TextView(getActivity());

            txtView.setText("Extra fry with butter");
            txtView.setTextColor(Color.parseColor("#000000"));
            txtView.setGravity(Gravity.LEFT);

            row.addView(txtView);

            TextView txtView1 = new TextView(getActivity());

            txtView1.setText("$10");
            txtView.setGravity(Gravity.CENTER);
            txtView1.setPadding(20, 0, 0, 0);
            txtView1.setTextColor(Color.parseColor("#000000"));

            row.addView(txtView1);

            tableLayoutModifiers.addView(row);

        }
    }

}
