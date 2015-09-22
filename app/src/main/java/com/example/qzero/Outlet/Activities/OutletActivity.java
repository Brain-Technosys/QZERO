package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.Outlet.ObjectClasses.Category;
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.Outlet.ObjectClasses.Outlet;
import com.example.qzero.Outlet.ObjectClasses.SubCategory;
import com.example.qzero.Outlet.ObjectClasses.Venue;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class OutletActivity extends Activity {

    @InjectView(R.id.txtViewTitleLandscape)
    TextView txtViewTitleLandscape;

    @InjectView(R.id.txtViewtitlePotraitLeft)
    TextView txtViewtitlePotraitLeft;

    @InjectView(R.id.txtViewtitlePotraitRight)
    TextView txtViewtitlePotraitRight;

    @InjectView(R.id.txtViewDescLand)
    TextView txtViewDescLand;

    @InjectView(R.id.txtViewMobLand)
    TextView txtViewMobLand;

    @InjectView(R.id.txtViewPhnoneLand)
    TextView txtViewPhnoneLand;

    @InjectView(R.id.txtViewDesPotLeft)
    TextView txtViewDesPotLeft;

    @InjectView(R.id.txtViewMobPotLeft)
    TextView txtViewMobPotLeft;

    @InjectView(R.id.txtViewPhPotLeft)
    TextView txtViewPhPotLeft;

    @InjectView(R.id.txtViewDesPotRight)
    TextView txtViewDesPotRight;

    @InjectView(R.id.txtViewMobPotRight)
    TextView txtViewMobPotRight;

    @InjectView(R.id.txtViewPhPotRight)
    TextView txtViewPhPotRight;

    @InjectView(R.id.relLayDesc)
    RelativeLayout relLayDesc;

    @InjectView(R.id.relLayDescOutletLeft)
    RelativeLayout relLayDescOutletLeft;

    @InjectView((R.id.relLayDescOutletRight))
    RelativeLayout relLayDescOutletRight;

    @InjectView(R.id.linLayPhLand)
            LinearLayout linLayPhLand;


    @InjectView(R.id.linLayMobLand)
    LinearLayout linLayMobLand;


    @InjectView(R.id.linLayPhoneLeft)
    LinearLayout linLayPhLeft;


    @InjectView(R.id.linLayPhoneRight)
    LinearLayout linLayPhoneRight;


    @InjectView(R.id.linLayMobRight)
    LinearLayout linLayMobRight;


    @InjectView(R.id.linLayMobLeft)
    LinearLayout linLayMobLeft;

    TextView txtViewHeading;
    TextView txtViewSubHeading;

    String venue_id;
    String outletId;
    String outletTitle;

    int status;
    int jsonLength;
    int pos = 0;

    String message;

    ArrayList<Outlet> arrayListOutlet;
    ArrayList<ItemOutlet> arrayListItem;
    ArrayList<Category> arrayListCat;

    HashMap<Integer,ArrayList<SubCategory>> hashMapSubCat;

    JsonParser jsonParser;
    JSONObject jsonObject;
    JSONArray jsonArray;

    View child;

    Outlet outlet;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet);
        // ButterKnife.inject(this);

        getIntents();

        setText();


        getOutletData();

        onFinishActivity();

    }

    public void getIntents() {
        if (getIntent().hasExtra("venue_id")) {
            Bundle bundle = getIntent().getExtras();
            venue_id = bundle.getString("venue_id");
        }
    }


    private void setText() {
        //Find id
        txtViewHeading = (TextView) findViewById(R.id.txtViewHeading);
        txtViewSubHeading = (TextView) findViewById(R.id.txtViewSubHeading);

        //Set title
        txtViewHeading.setText("Outlets");
    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewHeading, FontType.FONT, this);
        FontHelper.setFontFace(txtViewSubHeading, FontType.FONTROBOLD, this);
    }


    public void getOutletData() {

        if (CheckInternetHelper.checkInternetConnection(this)) {
            new GetOutlet().execute();
        } else {
            AlertDialogHelper.showAlertDialog(this,
                    getString(R.string.internet_connection_message),
                    "Alert");
        }


    }

    public class GetOutlet extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(OutletActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_OUTLETS + "?venueId=" + venue_id;


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            Log.e("json",jsonString);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {

                    arrayListOutlet = new ArrayList<Outlet>(jsonObject.length());

                    status = jsonObject.getInt(Const.TAG_STATUS);
                    message = jsonObject.getString(Const.TAG_MESSAGE);

                    if (status == 1) {

                        JSONObject jsonObj = jsonObject.getJSONObject(Const.TAG_JsonObj);

                        jsonArray = new JSONArray();
                        jsonArray = jsonObj.getJSONArray(Const.TAG_JsonOutletObj);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject outletObj = jsonArray.getJSONObject(i);

                            String outlet_id = outletObj.getString(Const.TAG_OUTLET_ID);
                            String outlet_name = outletObj.getString(Const.TAG_NAME);
                            String outlet_desc = outletObj.getString(Const.TAG_OUTLET_DESC);
                            String phone_num = outletObj.getString(Const.TAG_PH_NUM);
                            String mobile_num = outletObj.getString(Const.TAG_MOB_NUM);
                            Boolean isActive = outletObj.getBoolean(Const.TAG_OUTLET_ACTIVE);

                            Outlet outlet = new Outlet(outlet_id, outlet_name, outlet_desc, phone_num, mobile_num, isActive);
                            arrayListOutlet.add(outlet);
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



            if (status == 1) {
                try {
                    jsonLength = jsonArray.length();
                    setLayout();
                }catch (NullPointerException ex)
                {
                    ex.printStackTrace();
                }

            } else if (status == 0) {

                AlertDialogHelper.showAlertDialog(OutletActivity.this, message, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(OutletActivity.this,
                        getString(R.string.server_message), "Alert");
            }
        }
    }


    public void setLayout() {
        int mod = jsonLength % 3;

        Log.e("mod", "" + mod);
        if (mod == 0) {
            int length = jsonLength / 3;

            Log.e("length",""+length);

            for (int i = 0; i < length; i++) {

                inflateLayout();
                inflateLandscapeValues();
                inflateLeftLayout();
                inflateRightLayout();


            }//end of for loop

        }//end of if mod
        else {
            int length = jsonLength / 3 + 1;

            for (int i = 0; i < length; i++) {

                inflateLayout();


                if (i == length - 1) {
                    if (mod == 1) { //check if the last layout contains only one data

                        relLayDescOutletLeft.setVisibility(View.GONE);
                        relLayDescOutletRight.setVisibility(View.GONE);

                        inflateLandscapeValues();
                    } else if (mod == 2) {//check if the last layout contains only two data

                        relLayDescOutletRight.setVisibility(View.GONE);

                        inflateLandscapeValues();
                        inflateLeftLayout();
                    }
                }//end of if len-1
                else {

                    inflateLandscapeValues();
                    inflateLeftLayout();
                    inflateRightLayout();
                }

            }//end of manin for
        }//end of main else
    }//end of layout

    public void inflateLayout() {
        LinearLayout item = (LinearLayout) findViewById(R.id.linLayMain);

        child = getLayoutInflater().inflate(R.layout.item_outlet, null);//child.xml

        ButterKnife.inject(this, child);

        item.addView(child);

        //get width of the screen
        initializeLayoutWidth();

        //set fonts
        setOutletFonts();


    }

    private void initializeLayoutWidth() {
        ViewGroup.LayoutParams paramsLeft = relLayDescOutletLeft.getLayoutParams();

        // Changes the height and width to the specified *pixels*
        paramsLeft.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsLeft.width = getScreenWidth();

        ViewGroup.LayoutParams paramsRight = relLayDescOutletRight.getLayoutParams();

        // Changes the height and width to the specified *pixels*
        paramsRight.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsRight.width = getScreenWidth();
    }


    private void inflateLandscapeValues() {
        getValues();
        relLayDesc.setTag(outlet.getOutlet_id());
        txtViewTitleLandscape.setText(outlet.getOutlet_name());
        txtViewDescLand.setText(outlet.getOutlet_desc());

        if(outlet.getPhone_num().equals("null")||outlet.getPhone_num().equalsIgnoreCase("n/a"))
        {
            linLayPhLand.setVisibility(View.INVISIBLE);
        }
        else {
            txtViewPhnoneLand.setText(outlet.getPhone_num());
        }

        if(outlet.getMobile_num().equals("null")||outlet.getMobile_num().equalsIgnoreCase("n/a")) {
            linLayMobLand.setVisibility(View.INVISIBLE);
        }
        else
        {
            txtViewMobLand.setText(outlet.getMobile_num());
        }
        setOutletFonts();
    }

    private void inflateLeftLayout() {
        getValues();
        relLayDescOutletLeft.setTag(outlet.getOutlet_id());
        txtViewtitlePotraitLeft.setText(outlet.getOutlet_name());
        txtViewDesPotLeft.setText(outlet.getOutlet_desc());

        if(outlet.getMobile_num().equals("null")||outlet.getMobile_num().equalsIgnoreCase("n/a")) {
            linLayMobLeft.setVisibility(View.INVISIBLE);
        }
        else {

            txtViewMobPotLeft.setText(outlet.getMobile_num());
        }

        if(outlet.getPhone_num().equals("null")||outlet.getPhone_num().equalsIgnoreCase("n/a")) {
            linLayPhLeft.setVisibility(View.INVISIBLE);
        }
        else {
            txtViewPhPotLeft.setText(outlet.getPhone_num());
        }
        setOutletFonts();

    }

    private void inflateRightLayout() {
        getValues();
        relLayDescOutletRight.setTag(outlet.getOutlet_id());
        txtViewtitlePotraitRight.setText(outlet.getOutlet_name());
        txtViewDesPotRight.setText(outlet.getOutlet_desc());
        if(outlet.getPhone_num().equals("null")||outlet.getPhone_num().equalsIgnoreCase("n/a")||outlet.getPhone_num().equalsIgnoreCase("na")) {
            linLayPhoneRight.setVisibility(View.INVISIBLE);
        }
        else {
            txtViewPhPotRight.setText(outlet.getPhone_num());
        }

        if(outlet.getMobile_num().equals("null")||outlet.getMobile_num().equalsIgnoreCase("n/a")||outlet.getMobile_num().equalsIgnoreCase("na")) {
            linLayMobRight.setVisibility(View.INVISIBLE);
        }
        else {
            txtViewMobPotRight.setText(outlet.getMobile_num());
        }
        setOutletFonts();
    }

    private void getValues() {
        outlet = arrayListOutlet.get(pos);
        pos++;
    }

    private int getScreenWidth() {
       /* DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int layoutWidth=(int)(dpWidth- (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics())));
        Log.e("layoutwidth",""+layoutWidth);
*/
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int layoutWidth = metrics.widthPixels / 2 - 35;
        Log.e("layoutwidth", "" + layoutWidth);
        return layoutWidth;
    }

    private void setOutletFonts() {


        FontHelper.setFontFace(txtViewTitleLandscape, FontType.FONTROBOLD, this);
        FontHelper.setFontFace(txtViewDescLand, FontType.FONT, this);
        FontHelper.setFontFace(txtViewMobLand, FontType.FONT, this);
        FontHelper.setFontFace(txtViewPhnoneLand, FontType.FONT, this);

        FontHelper.setFontFace(txtViewtitlePotraitLeft, FontType.FONTROBOLD, this);
        FontHelper.setFontFace(txtViewDesPotLeft, FontType.FONT, this);
        FontHelper.setFontFace(txtViewMobPotLeft, FontType.FONT, this);
        FontHelper.setFontFace(txtViewPhPotLeft, FontType.FONT, this);

        FontHelper.setFontFace(txtViewtitlePotraitRight, FontType.FONTROBOLD, this);
        FontHelper.setFontFace(txtViewDesPotRight, FontType.FONT, this);
        FontHelper.setFontFace(txtViewMobPotRight, FontType.FONT, this);
        FontHelper.setFontFace(txtViewPhPotRight, FontType.FONT, this);

    }

    private void onFinishActivity() {
        ImageView imgViewBack = (ImageView) findViewById(R.id.imgViewBack);
        imgViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.relLayDesc)
    void categoryData(View v) {

        TextView txtViewTitle = (TextView) v.findViewById(R.id.txtViewTitleLandscape);
        outletTitle = txtViewTitle.getText().toString();

        getCategoryValues(v);

    }


    @OnClick(R.id.relLayDescOutletLeft)
    void getCategoryData(View v) {

        TextView txtViewTitle = (TextView) v.findViewById(R.id.txtViewtitlePotraitLeft);
        outletTitle = txtViewTitle.getText().toString();

        getCategoryValues(v);
    }

    @OnClick(R.id.relLayDescOutletRight)
    void getCategory(View v) {

        TextView txtViewTitle = (TextView) v.findViewById(R.id.txtViewtitlePotraitRight);
        outletTitle = txtViewTitle.getText().toString();
        getCategoryValues(v);
    }

    public void getCategoryValues(View view) {
        outletId = view.getTag().toString();

        getOutletItems();
    }

    public void getOutletItems() {
        if (CheckInternetHelper.checkInternetConnection(this)) {
            new GetOutletItems().execute();
        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.internet_connection_message), "Alert");
        }
    }

    public class GetOutletItems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(OutletActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("inside", "do in");
            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_ITEMS + venue_id + "/?outletId=" + outletId + "&itemId=" +""
                    + "&subCatId=" + "";


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            Log.e("jsonvenue", jsonString);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("inside", "json");

                    arrayListItem = new ArrayList<ItemOutlet>(jsonObject.length());

                    arrayListCat = new ArrayList<Category>(jsonObject.length());

                    hashMapSubCat=new HashMap<Integer,ArrayList<SubCategory>>();

                    status = jsonObject.getInt(Const.TAG_STATUS);
                    message = jsonObject.getString(Const.TAG_MESSAGE);

                    Log.d("status", "" + status);
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
                            arrayListItem.add(ItemOutlet);
                        }

                        //Get json array for categories
                        JSONArray jsonArrayCategory = new JSONArray();

                        jsonArrayCategory = jsonObj.getJSONArray(Const.TAG_JsonCatObj);

                        for (int i = 0; i < jsonArrayCategory.length(); i++) {
                            JSONObject jsonObjCat = jsonArrayCategory.getJSONObject(i);

                            String category_id = jsonObjCat.getString(Const.TAG_CAT_ID);
                            String category_name = jsonObjCat.getString(Const.TAG_CAT_NAME);

                            category = new Category(category_id, category_name);

                            arrayListCat.add(category);

                            JSONArray jsonArraySubCat = jsonObjCat.getJSONArray(Const.TAG_JsonSubCatObj);





                            ArrayList<SubCategory> subCatArrayList=new ArrayList<SubCategory>();

                            for (int j = 0; j < jsonArraySubCat.length(); j++) {
                                JSONObject jsonObjSubCat = jsonArraySubCat.getJSONObject(j);
                                String sub_cat_id = jsonObjSubCat.getString(Const.TAG_SUB_CAT_ID);
                                String sub_cat_name = jsonObjSubCat.getString(Const.TAG_SUB_CAT_NAME);

                                SubCategory subCategory=new SubCategory(sub_cat_id,sub_cat_name);
                                subCatArrayList.add(subCategory);


                            }

                            hashMapSubCat.put(i,subCatArrayList);

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

                passIntent();

            } else if (status == 0) {

                AlertDialogHelper.showAlertDialog(OutletActivity.this, message, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(OutletActivity.this, getString(R.string.server_message), "Alert");
            }
        }
    }

    public void passIntent() {

        Intent intent = new Intent(OutletActivity.this, OutletCategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("arraylistitem", arrayListItem);
        bundle.putSerializable("arrayListCat", arrayListCat);
        bundle.putSerializable("hashMapSubCat",hashMapSubCat);
        bundle.putString("title", outletTitle);
        bundle.putString("venue_id",venue_id);
        bundle.putString("outlet_id",outletId);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
