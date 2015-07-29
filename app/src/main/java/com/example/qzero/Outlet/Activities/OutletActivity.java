package com.example.qzero.Outlet.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import android.view.ViewGroup;
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
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.Outlet.ObjectClasses.Outlet;
import com.example.qzero.Outlet.ObjectClasses.Venue;
import com.example.qzero.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    JsonParser jsonParser;
    JSONObject jsonObject;
    JSONArray jsonArray;

    View child;

    Outlet outlet;

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

            Log.e("inside", "do in");
            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_OUTLETS + "?venueId=" + venue_id;


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            Log.e("json", jsonString);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {

                    arrayListOutlet = new ArrayList<Outlet>(jsonObject.length());

                    /*status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");*/

                    //Log.d("status", "" + status);
                    //if (status == 1) {

                    jsonArray = new JSONArray();
                    jsonArray = jsonObject.getJSONArray("outlets");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);

                        String outlet_id = jsonObj.getString(Const.TAG_OUTLET_ID);
                        String outlet_name = jsonObj.getString(Const.TAG_NAME);
                        Boolean isActive = jsonObj.getBoolean(Const.TAG_OUTLET_ACTIVE);
                        Outlet outlet = new Outlet(outlet_id, outlet_name, isActive);
                        arrayListOutlet.add(outlet);
                    }
                    //}

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

            jsonLength = jsonArray.length();

            setLayout();


            // if (status == 1) {

            // passIntent();

            //} else if (status == 0) {

            //  AlertDialogHelper.showAlertDialog(OutletActivity.this, "message", "Alert");

            //} else {
            //AlertDialogHelper.showAlertDialog(OutletActivity.this,
            // getString(R.string.server_message), "Alert");
            //}
        }
    }


    public void setLayout() {
        int mod = jsonLength % 3;
        if (mod == 0) {
            int length = jsonLength / 3;

            for (int i = 0; i < length; i++) {

                inflateLayout();
                inflateLeftLayout();
                inflateRightLayout();


            }//end of for loop

        }//end of if mod
        else {
            int length = jsonLength / 3 + 1;

            for (int i = 0; i < length; i++) {

                inflateLayout();


                if (i == length - 1) {
                    if (mod == 1) {

                        relLayDescOutletLeft.setVisibility(View.GONE);
                        relLayDescOutletRight.setVisibility(View.GONE);

                        inflateLandscapeValues();
                    } else if (mod == 2) {

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
        setOutletFonts();
    }

    private void inflateLeftLayout() {
        getValues();
        relLayDescOutletLeft.setTag(outlet.getOutlet_id());
        txtViewtitlePotraitLeft.setText(outlet.getOutlet_name());
        setOutletFonts();

    }

    private void inflateRightLayout() {
        getValues();
        relLayDescOutletRight.setTag(outlet.getOutlet_id());
        txtViewtitlePotraitRight.setText(outlet.getOutlet_name());
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

    @OnClick(R.id.relLayDescOutletRight)
    void categoryData(View v) {

        getCategoryValues(v);

    }


    @OnClick(R.id.relLayDescOutletLeft)
    void getCategoryData(View v) {
        getCategoryValues(v);
    }

    @OnClick(R.id.relLayDescOutletRight)
    void getCategory(View v) {
        getCategoryValues(v);
    }

    public void getCategoryValues(View view) {
        outletId = view.getTag().toString();
        TextView txtViewTitle = (TextView) view.findViewById(R.id.txtViewtitlePotraitRight);
        outletTitle = txtViewTitle.getText().toString();
        getOutletItems();
    }

    public void getOutletItems() {
        if (CheckInternetHelper.checkInternetConnection(this))
        {
            new GetOutletItems().execute();
        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.internet_connection_message), "Alert");
        }
    }

    public class GetOutletItems extends AsyncTask<String,String,String>
    {

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
            String url = Const.BASE_URL + Const.GET_ITEMS +venue_id+"/?outletId=" + outletId + "&itemId=" + ""
                    + "&subCatId=" + "";


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            Log.e("json", jsonString);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {

                    arrayListItem= new ArrayList<ItemOutlet>(jsonObject.length());

                    /*status = jsonObject.getInt("status");
                    message = jsonObject.getString("message");

                    Log.d("status", "" + status);*/
                  //  if (status == 1) {

                        jsonArray = new JSONArray();
                        jsonArray = jsonObject.getJSONArray(Const.TAG_JsonItemObj);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            String item_id = jsonObj.getString(Const.TAG_ITEM_ID);
                            String item_name = jsonObj.getString(Const.TAG_ITEM_NAME);
                            String item_price = jsonObj.getString(Const.TAG_PRICE);
                            String item_desc = jsonObj.getString(Const.TAG_DESC);
                            //String item_image = jsonObj.getString(Const.TAG_IMAGE);
                            String sub_item_id = jsonObj.getString(Const.TAG_SUB_ID);

                            ItemOutlet ItemOutlet = new ItemOutlet(item_id, item_name, item_price, item_desc,sub_item_id);
                            arrayListItem.add(ItemOutlet);
                       // }
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

           // if (status == 1) {

                passIntent();

           //} else if (status == 0) {

                AlertDialogHelper.showAlertDialog(OutletActivity.this, "message", "Alert");

           // } else {
             //   AlertDialogHelper.showAlertDialog(OutletActivity.this,
                        //getString(R.string.server_message), "Alert");
           // }
        }
    }
    public void passIntent() {
        Intent intent = new Intent(OutletActivity.this, OutletCategoryActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("arraylistitem",arrayListItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
