package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;

import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ConstVarIntent;
import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.Outlet.ObjectClasses.Advertisement;
import com.example.qzero.Outlet.ObjectClasses.Category;
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.Outlet.ObjectClasses.Outlet;
import com.example.qzero.Outlet.ObjectClasses.SubCategory;
import com.example.qzero.Outlet.ObjectClasses.Venue;
import com.example.qzero.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class OutletActivity extends Activity implements SearchView.OnQueryTextListener {

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

    @InjectView(R.id.viewOutlet)
    CardView viewOutlet;

    @InjectView(R.id.viewOutletRight)
    CardView viewOutletRight;

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

    @InjectView(R.id.imgViewRight)
    ImageView imgViewRight;

    @InjectView(R.id.imgViewLeft)
    ImageView imgViewLeft;

    @InjectView(R.id.imgViewLand)
    ImageView imgViewLand;


    TextView txtViewHeading;
    TextView txtViewSubHeading;

    String venue_id;
    String outletId;
    String itemId;
    String subCatId;
    String outletTitle;

    String oldOutletId = "null";

    int status;
    int jsonLength;
    int pos = 0;

    int categoryId = 0;
    int item_id = 0;

    String message;

    ArrayList<Outlet> orig;

    ArrayList<Outlet> arrayListOutlet;
    ArrayList<ItemOutlet> arrayListItem;
    ArrayList<Category> arrayListCat;
    ArrayList<Advertisement> arrayListAdminAdvertisement;
    ArrayList<Advertisement> arrayListAdvertisement;

    HashMap<Integer, ArrayList<SubCategory>> hashMapSubCat;

    JsonParser jsonParser;
    JSONObject jsonObject;
    JSONArray jsonArray;

    View child;

    Outlet outlet;
    Category category;

    SearchView search_view;
    LinearLayout item;

    LinearLayout linLayAdvertisement;
    ImageView imgViewAdAdmin;
    ImageView imgViewAdVenue;

    DatabaseHelper databaseHelper;

    Context context;

    public int currentImageIndexAdmin = 0;
    public int currentImagePosAdmin = 0;

    public int currentImageIndex = 0;
    public int currentImagePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet);

        databaseHelper = new DatabaseHelper(this);

        getIntents();

        setText();


        getOutletData();


        onFinishActivity();

        setupSearchView();



        context = OutletActivity.this;
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

        search_view = (SearchView) findViewById(R.id.search_view);

        imgViewAdAdmin = (ImageView) findViewById(R.id.imgViewAdAdmin);
        imgViewAdVenue = (ImageView) findViewById(R.id.imgViewAdVenue);
        linLayAdvertisement = (LinearLayout) findViewById(R.id.linLayAdvertisement);

        imgViewAdVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arrayListAdvertisement.size() != 0) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayListAdvertisement.get(currentImagePos).getImgUrl()));
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        imgViewAdAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arrayListAdminAdvertisement.size() != 0) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayListAdminAdvertisement.get(currentImagePosAdmin).getImgUrl()));
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        //Set title
        txtViewHeading.setText("Outlets");
    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewHeading, FontType.FONT, this);
        FontHelper.setFontFace(txtViewSubHeading, FontType.FONTROBOLD, this);
    }

    private void setupSearchView() {

        search_view.clearFocus();
        search_view.setIconifiedByDefault(false);
        search_view.setOnQueryTextListener(this);
        search_view.setSubmitButtonEnabled(true);
        search_view.setQueryHint("Search Outlets");
        search_view.setSubmitButtonEnabled(false);

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_view.getWindowToken(), 0);
    }

    public boolean onQueryTextChange(String newText) {

        //Log.e("newtext", newText);
        if (TextUtils.isEmpty(newText)) {

            filterJson(newText);

        } else {

            filterJson(newText);
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {


        return false;
    }


    public void filterJson(String newText) {
        final ArrayList<Outlet> results = new ArrayList<Outlet>();
        if (orig == null)
            orig = arrayListOutlet;

        if (newText != null) {
            if (orig != null && orig.size() > 0) {
                for (final Outlet order : orig) {
                    if (String.valueOf(order.getOutlet_name()).toLowerCase()
                            .contains(newText.toString()) || String.valueOf(order.getOutlet_name())
                            .contains(newText.toString()))
                        results.add(order);
                }
            }
            arrayListOutlet = results;

            item.removeAllViews();
            pos = 0;
        }

        setLayout();
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

    public void getAdvertisement() {
        if (CheckInternetHelper.checkInternetConnection(this)) {
            new GetAdvertisement().execute();
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
                            String outlet_image = outletObj.getString(Const.TAG_OUTLET_IMAGE);
                            Boolean isActive = outletObj.getBoolean(Const.TAG_OUTLET_ACTIVE);

                            Outlet outlet = new Outlet(outlet_id, outlet_name, outlet_desc, phone_num, mobile_num, outlet_image, isActive);
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
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }

            } else if (status == 0) {

                AlertDialogHelper.showAlertDialog(OutletActivity.this, message, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(OutletActivity.this,
                        getString(R.string.server_message), "Alert");
            }

            getAdvertisement();
        }
    }


    public void setLayout() {

        int arrayLength = arrayListOutlet.size();

        int mod = arrayLength % 3;

        //Log.e("mod", "" + mod);
        if (mod == 0) {
            int length = arrayLength / 3;

            //Log.e("length", "" + length);

            for (int i = 0; i < length; i++) {

                inflateLayout();
                inflateLandscapeValues();
                inflateLeftLayout();
                inflateRightLayout();


            }//end of for loop

        }//end of if mod
        else {
            int length = arrayLength / 3 + 1;

            for (int i = 0; i < length; i++) {

                inflateLayout();


                if (i == length - 1) {
                    if (mod == 1) { //check if the last layout contains only one data

                        viewOutlet.setVisibility(View.INVISIBLE);
                        viewOutletRight.setVisibility(View.INVISIBLE);

                        inflateLandscapeValues();
                    } else if (mod == 2) {//check if the last layout contains only two data

                        viewOutletRight.setVisibility(View.INVISIBLE);

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


        item = (LinearLayout) findViewById(R.id.linLayMain);

        child = getLayoutInflater().inflate(R.layout.item_outlet, null);//child.xml

        ButterKnife.inject(this, child);

        item.addView(child);

        //get width of the screen
        // initializeLayoutWidth();

        //set fonts
        setOutletFonts();


    }

   /* private void initializeLayoutWidth() {


        ViewGroup.LayoutParams paramsLeft = relLayDescOutletLeft.getLayoutParams();

        // Changes the height and width to the specified *pixels*
        paramsLeft.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsLeft.width = getScreenWidth();

        ViewGroup.LayoutParams paramsRight = relLayDescOutletRight.getLayoutParams();

        // Changes the height and width to the specified *pixels*
        paramsRight.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsRight.width = getScreenWidth();
    }*/


    private void inflateLandscapeValues() {
        getValues();
        relLayDesc.setTag(outlet.getOutlet_id());

        ViewTreeObserver observer = relLayDesc.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                int a = relLayDesc.getHeight();

                imgViewLand.requestLayout();
                imgViewLand.getLayoutParams().height = a;


                relLayDesc.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });
        Picasso.with(OutletActivity.this).load(outlet.getOutlet_image()).into(imgViewLand);
        txtViewTitleLandscape.setText(outlet.getOutlet_name());

        if (outlet.getOutlet_desc().equals("null")) {

        } else
            txtViewDescLand.setText(outlet.getOutlet_desc());

        if (outlet.getPhone_num().equals("null") || outlet.getPhone_num().equalsIgnoreCase("n/a") || outlet.getPhone_num().equalsIgnoreCase("na")) {
            linLayPhLand.setVisibility(View.INVISIBLE);
        } else {
            txtViewPhnoneLand.setText(outlet.getPhone_num());
        }

        if (outlet.getMobile_num().equals("null") || outlet.getMobile_num().equalsIgnoreCase("n/a") || outlet.getMobile_num().equalsIgnoreCase("na")) {
            linLayMobLand.setVisibility(View.INVISIBLE);
        } else {
            txtViewMobLand.setText(outlet.getMobile_num());
        }
        setOutletFonts();
    }

    private void inflateLeftLayout() {
        getValues();
        relLayDescOutletLeft.setTag(outlet.getOutlet_id());


        ViewTreeObserver observer = relLayDescOutletLeft.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                int a = relLayDescOutletLeft.getHeight();

                imgViewLeft.requestLayout();
                imgViewLeft.getLayoutParams().height = a;


                relLayDescOutletLeft.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });
        Picasso.with(OutletActivity.this).load(outlet.getOutlet_image()).into(imgViewLeft);
        txtViewtitlePotraitLeft.setText(outlet.getOutlet_name());


        if (outlet.getOutlet_desc().equals("null")) {

        } else
            txtViewDesPotLeft.setText(outlet.getOutlet_desc());

        if (outlet.getMobile_num().equals("null") || outlet.getMobile_num().equalsIgnoreCase("n/a") || outlet.getMobile_num().equalsIgnoreCase("na")) {
            linLayMobLeft.setVisibility(View.INVISIBLE);
        } else {

            txtViewMobPotLeft.setText(outlet.getMobile_num());
        }

        if (outlet.getPhone_num().equals("null") || outlet.getPhone_num().equalsIgnoreCase("n/a") || outlet.getPhone_num().equalsIgnoreCase("na")) {
            linLayPhLeft.setVisibility(View.INVISIBLE);
        } else {
            txtViewPhPotLeft.setText(outlet.getPhone_num());
        }
        setOutletFonts();

    }

    private void inflateRightLayout() {
        getValues();
        relLayDescOutletRight.setTag(outlet.getOutlet_id());

        ViewTreeObserver observer = relLayDescOutletRight.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                int a = relLayDescOutletRight.getHeight();

                imgViewRight.requestLayout();
                imgViewRight.getLayoutParams().height = a;


                relLayDescOutletRight.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });
        Picasso.with(OutletActivity.this).load(outlet.getOutlet_image()).into(imgViewRight);
        txtViewtitlePotraitRight.setText(outlet.getOutlet_name());

        if (outlet.getOutlet_desc().equals("null")) {

        } else
            txtViewDesPotRight.setText(outlet.getOutlet_desc());


        if (outlet.getPhone_num().equals("null") || outlet.getPhone_num().equalsIgnoreCase("n/a") || outlet.getPhone_num().equalsIgnoreCase("na")) {
            linLayPhoneRight.setVisibility(View.INVISIBLE);
        } else {
            txtViewPhPotRight.setText(outlet.getPhone_num());
        }

        if (outlet.getMobile_num().equals("null") || outlet.getMobile_num().equalsIgnoreCase("n/a") || outlet.getMobile_num().equalsIgnoreCase("na")) {
            linLayMobRight.setVisibility(View.INVISIBLE);
        } else {
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
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        // Note, screenHeightDp isn't reliable
        // (it seems to be too small by the height of the status bar),
        // but we assume screenWidthDp is reliable.
        // Note also, dm.widthPixels,dm.heightPixels aren't reliably pixels
        // (they get confused when in screen compatibility mode, it seems),
        // but we assume their ratio is correct.
        double screenWidthInPixels = (double) config.screenWidthDp * dm.density;
        double screenHeightInPixels = screenWidthInPixels * dm.heightPixels / dm.widthPixels;
        int dpWidth = (int) (screenWidthInPixels + .5);
        //widthHeightInPixels[1] = (int)(screenHeightInPixels + .5);

       /* DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);*/
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 35, resources.getDisplayMetrics());
        int layoutWidth = dpWidth / 2 - px;
        //Log.e("layoutwidth", "" + layoutWidth);

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


        Cursor outletCursor = databaseHelper.selectOutletId();

        if (outletCursor != null) {
            if (outletCursor.moveToFirst()) {

                int index = outletCursor.getColumnIndex(databaseHelper.OUTLET_ID);
                oldOutletId = outletCursor.getString(index);
            } else {
                oldOutletId = "null";
            }
        }


        //Log.e("outletId", oldOutletId);

        if (oldOutletId.equals("null")) {
            getOutletItems();
        } else if (oldOutletId.equals(outletId)) {
            getOutletItems();
        } else {
            openDialog();
        }
    }

    public class GetAdvertisement extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(OutletActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_VENUE_ADVERTISEMENT + venue_id + "?" + Const.TAG_CAT_ID + "=" + categoryId + "&" + Const.TAG_ITEM_ID + "=" + item_id;
            //Log.e("url", url);

            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            try {
                jsonObject = new JSONObject(jsonString);
                //Log.e("jsonobject", jsonObject.toString());

                arrayListAdvertisement = new ArrayList<Advertisement>();
                arrayListAdminAdvertisement = new ArrayList<Advertisement>();

                if (jsonObject != null) {


                    status = jsonObject.getInt(Const.TAG_STATUS);

                    if (status == 1) {

                        JSONObject jsonObj = jsonObject.getJSONObject(Const.TAG_RESULT);

                        JSONArray jsonArrayAd = jsonObj.getJSONArray(Const.TAG_JsonAd);

                        if (jsonArrayAd.length() != 0) {


                            for (int i = 0; i < jsonArrayAd.length(); i++) {

                                JSONObject advertisementObj = jsonArrayAd.getJSONObject(i);

                                String advertisementId = advertisementObj.getString(Const.TAG_ADD_ID);
                                String image = Const.BASE_URL + Const.AD_IMAGE_URL + advertisementId;

                                String linkUrl = advertisementObj.getString(Const.TAG_ADD_URL);
                                Boolean isAdminAdd = advertisementObj.getBoolean(Const.TAG_ADD_ISADMIN);

                                Advertisement advertisement = new Advertisement(image, advertisementId, linkUrl);

                                if (isAdminAdd) {
                                    arrayListAdminAdvertisement.add(advertisement);
                                } else {
                                    arrayListAdvertisement.add(advertisement);
                                }


                            }
                        } else {
                            status = 0;
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

                if (arrayListAdminAdvertisement.size() == 0 && arrayListAdvertisement.size() == 0) {

//            Picasso.with(OutletCategoryActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdAdmin);
//            Picasso.with(OutletCategoryActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdVenue);


                    linLayAdvertisement.setVisibility(View.GONE);

                } else {
                    //adding image to admin if image is not from admin
                    if (arrayListAdminAdvertisement.size() == 0) {
                        imgViewAdAdmin.setVisibility(View.GONE);
                        //Picasso.with(OutletActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdAdmin);
                    } else if (arrayListAdminAdvertisement.size() == 1) {
                        Picasso.with(OutletActivity.this).load(arrayListAdminAdvertisement.get(0).getImageAd()).error(R.drawable.noimage).into(imgViewAdAdmin);
                    } else {

                        autoSlideImagesAdmin();
                    }

                    //adding image to admin if image is from admin
                    if (arrayListAdvertisement.size() == 0) {
                        imgViewAdVenue.setVisibility(View.GONE);
                        //Picasso.with(OutletActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdVenue);
                    } else if (arrayListAdvertisement.size() == 1) {
                        Picasso.with(OutletActivity.this).load(arrayListAdvertisement.get(0).getImageAd()).error(R.drawable.noimage).into(imgViewAdVenue);
                    } else {

                        autoSlideImages();
                    }
                }
            } else if (status == 0) {

//                Picasso.with(OutletActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdVenue);
//                Picasso.with(OutletActivity.this).load(R.drawable.noimage).error(R.drawable.noimage).into(imgViewAdAdmin);
                linLayAdvertisement.setVisibility(View.GONE);
            } else {
                AlertDialogHelper.showAlertDialog(OutletActivity.this,
                        getString(R.string.server_message), "Alert");
            }
        }
    }

    private void autoSlideImages() {
        final Handler mHandler = new Handler();

        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            public void run() {

                AnimateandSlideShow();

            }
        };

        int delay = 1000; // delay for 1 sec.

        int period = 4000; // repeat every 4 sec.

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                mHandler.post(mUpdateResults);

            }

        }, delay, period);


    }


    private void autoSlideImagesAdmin() {
        final Handler mHandler = new Handler();

        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            public void run() {

                AnimateandSlideShowAdmin();

            }
        };

        int delay = 1000; // delay for 1 sec.

        int period = 4000; // repeat every 4 sec.

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                mHandler.post(mUpdateResults);

            }

        }, delay, period);


    }

    private void AnimateandSlideShow() {

        Picasso.with(this).load(arrayListAdvertisement.get(currentImageIndex % arrayListAdvertisement.size()).getImageAd()).into(imgViewAdVenue);
        currentImagePos = currentImageIndex % arrayListAdvertisement.size();
        currentImageIndex++;
        //Log.e("cu1", "" + currentImagePos);
    }

    private void AnimateandSlideShowAdmin() {

        Picasso.with(this).load(arrayListAdminAdvertisement.get(currentImageIndexAdmin % arrayListAdminAdvertisement.size()).getImageAd()).into(imgViewAdAdmin);
        currentImagePosAdmin = currentImageIndexAdmin % arrayListAdminAdvertisement.size();
        currentImageIndexAdmin++;

    }


    private void openDialog() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_outlet);

        TextView txtViewTitle = (TextView) dialog.findViewById(R.id.txtViewTitle);
        TextView txtViewCancel = (TextView) dialog.findViewById(R.id.txtViewCancel);
        TextView txtViewChange = (TextView) dialog.findViewById(R.id.txtViewChange);

        FontHelper.setFontFace(txtViewTitle, FontHelper.FontType.FONT, this);
        FontHelper.setFontFace(txtViewCancel, FontHelper.FontType.FONT, this);
        FontHelper.setFontFace(txtViewChange, FontHelper.FontType.FONT, this);

        txtViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtViewChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper databaseHelper = new DatabaseHelper(OutletActivity.this);

                databaseHelper.deleteModifierTable();
                databaseHelper.deleteItemTable();
                ;
                databaseHelper.deleteCheckOutTable();

                dialog.dismiss();

                getOutletItems();
            }
        });

        dialog.show();
    }


    public void getOutletItems() {
        if (CheckInternetHelper.checkInternetConnection(this)) {
            new GetOutletItems().execute();
        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.internet_connection_message), "Alert");
        }
    }

    //Get outlet items on outlet click
    public class GetOutletItems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(OutletActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            // Log.e("inside", "do in");
            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_ITEMS + venue_id + "/?outletId=" + outletId + "&itemId=" + itemId
                    + "&subCatId=" + subCatId;


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            try {

                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {


                    arrayListItem = new ArrayList<ItemOutlet>(jsonObject.length());

                    arrayListCat = new ArrayList<Category>(jsonObject.length());

                    hashMapSubCat = new HashMap<Integer, ArrayList<SubCategory>>();

                    status = jsonObject.getInt(Const.TAG_STATUS);
                    message = jsonObject.getString(Const.TAG_MESSAGE);


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


                            ArrayList<SubCategory> subCatArrayList = new ArrayList<SubCategory>();

                            for (int j = 0; j < jsonArraySubCat.length(); j++) {
                                JSONObject jsonObjSubCat = jsonArraySubCat.getJSONObject(j);
                                String sub_cat_id = jsonObjSubCat.getString(Const.TAG_SUB_CAT_ID);
                                String sub_cat_name = jsonObjSubCat.getString(Const.TAG_SUB_CAT_NAME);

                                SubCategory subCategory = new SubCategory(sub_cat_id, sub_cat_name);
                                subCatArrayList.add(subCategory);


                            }

                            hashMapSubCat.put(i, subCatArrayList);

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

            //Log.e("inside", "postexecute");

            if (status == 1) {

                passIntent();

                oldOutletId = outletId;

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
        bundle.putString("venue_id", venue_id);
        bundle.putString("outlet_id", outletId);
        bundle.putSerializable("arraylistitem", arrayListItem);
        bundle.putSerializable("arrayListCat", arrayListCat);
        bundle.putSerializable("hashMapSubCat", hashMapSubCat);
        bundle.putSerializable("arrayListAd", arrayListAdvertisement);
        bundle.putSerializable("arrayListAdAdmin", arrayListAdminAdvertisement);
        bundle.putString(ConstVarIntent.TAG_CLASSNAME, "outlet");
        bundle.putString(Const.TAG_OUTLET_NAME, outletTitle);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}

