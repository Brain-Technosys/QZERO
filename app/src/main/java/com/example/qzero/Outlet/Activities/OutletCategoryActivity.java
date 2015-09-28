package com.example.qzero.Outlet.Activities;


import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.ConstVarIntent;
import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Activities.DashBoardActivity;
import com.example.qzero.Outlet.Adapters.SubCategoryAdapter;
import com.example.qzero.Outlet.ExpandableListView.ExpandableListView;
import com.example.qzero.Outlet.Fragments.AddCartFragment;
import com.example.qzero.Outlet.Fragments.CategoryItemFragment;
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
import butterknife.OnClick;


public class OutletCategoryActivity extends AppCompatActivity {

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.navigationView)
    LinearLayout navigationView;

    @InjectView((R.id.txtViewProfile))
    TextView txtViewProfile;

    @InjectView(R.id.txtViewUserName)
    TextView txtViewUserName;

    @InjectView(R.id.txtViewLogout)
    TextView txtViewLogout;

    ImageView imgViewUpArrow;
    ImageView imgViewDownArrow;

    ArrayAdapter adapter;

    int list;
    int lastPos;
    int noSubCatPos = -1;

    String title;
    String venue_id;
    String outlet_id;
    String categoryId;
    String subCategoryId;
    String item_id;

    String classname;

    Boolean isSubCatPresent;

    ActionBar actionBar;
    ActionBarDrawerToggle drawerToggle;

    ArrayList<ItemOutlet> arrayListItems;
    ArrayList<Category> arrayListCat;
    ArrayList<SubCategory> arrayListSubCat;

    HashMap<Integer, ArrayList<SubCategory>> hashMapSubCat;

    UserSession userSession;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ExpandableListView[] subCatListView;

    LinearLayout.LayoutParams params;

    View child[];

    Boolean isAddToCartOpen = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_category);

        ButterKnife.inject(this);


        //Initialize user session
        userSession = new UserSession(this);


        setSupportActionBar(toolbar);

        drawerToggle = setupDrawerToggle();

        getIntentData();
        if (!classname.equals("items")) {
            //Add category item initially if switching from category items
            addItemFragment();
        } else {
            //Add category item initially if switching from search items
            addCartFragment();
        }

        setIconsToActionBar();

        createCategoryItem();

    }//end of onCreate()

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    public void setSupportActionBar(Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    private void setIconsToActionBar() {
        //getting left side menu image
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.selector_menu);

        //getting right side cart menu image by inflating action_bar_layout Layout file
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);

        LayoutInflater inflater = LayoutInflater.from(OutletCategoryActivity.this);
        View inflatedLayout = inflater.inflate(R.layout.action_bar_layout, null, false);

        TextView txtViewTitle = (TextView) inflatedLayout.findViewById(R.id.txtViewTitle);

        txtViewTitle.setText("Outlet Items");

        actionBar.setCustomView(inflatedLayout);


    }//end of setIconsToActionBar()

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void getIntentData() {
        arrayListItems = new ArrayList<ItemOutlet>();

        hashMapSubCat = new HashMap<Integer, ArrayList<SubCategory>>();
        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra("arraylistitem")) {

            arrayListItems = (ArrayList<ItemOutlet>) bundle.getSerializable("arraylistitem");
            arrayListCat = (ArrayList<Category>) bundle.getSerializable("arrayListCat");

            hashMapSubCat = (HashMap<Integer, ArrayList<SubCategory>>) bundle.getSerializable("hashMapSubCat");


            title = bundle.getString(Const.TAG_OUTLET_NAME);
            venue_id = bundle.getString("venue_id");
            outlet_id = bundle.getString("outlet_id");

        }

        if (getIntent().hasExtra(ConstVarIntent.TAG_CLASSNAME)) {
            classname = bundle.getString(ConstVarIntent.TAG_CLASSNAME);
        }

        if (getIntent().hasExtra(Const.TAG_ITEM_ID)) {
            item_id = bundle.getString(Const.TAG_ITEM_ID);
        }

        child = new View[arrayListCat.size()];
    }

    public void addItemFragment() {
        isAddToCartOpen = false;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager
                .beginTransaction();
        CategoryItemFragment categoryItemFragment = new CategoryItemFragment();

        Bundle bundle = new Bundle();
//call method to put string into bundle
        createBundle(bundle);

        categoryItemFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.frameLayItem, categoryItemFragment, "item");
        fragmentTransaction.commit();

    }

    public void replaceAddItem() {
        if (CheckInternetHelper.checkInternetConnection(this)) {
            new GetOutletItems().execute();
        } else {
            AlertDialogHelper.showAlertDialog(this, getString(R.string.internet_connection_message), "Alert");
        }
    }

    public void replaceFragment(String venue_id, String outlet_id, String item_id) {

        isAddToCartOpen = true;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager
                .beginTransaction();
        AddCartFragment addCartFragment = new AddCartFragment();
        fragmentTransaction.replace(R.id.frameLayItem, addCartFragment,
                "addcart");

        Bundle bundle = new Bundle();
        bundle.putString("venue_id", venue_id);
        bundle.putString("outlet_id", outlet_id);
        bundle.putString("item_id", item_id);
        addCartFragment.setArguments(bundle);

        fragmentTransaction.commit();
    }

    //if switching from search items add the add to cart layout
    private void addCartFragment() {
        isAddToCartOpen = true;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager
                .beginTransaction();
        AddCartFragment addCartFragment = new AddCartFragment();
        fragmentTransaction.add(R.id.frameLayItem, addCartFragment,
                "addcart");

        Bundle bundle = new Bundle();
        bundle.putString("venue_id", venue_id);
        bundle.putString("outlet_id", outlet_id);
        bundle.putString("item_id", item_id);
        addCartFragment.setArguments(bundle);

        fragmentTransaction.commit();
    }


    public void createCategoryItem() {

        subCatListView = new ExpandableListView[arrayListCat.size()];
        //Check if a user is logged in or not
        checkUserSession();

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //dynamically adding category item as TextView
        for (int i = 0; i < arrayListCat.size(); i++) {

            //Create categories
            createCategories(i);

            //Create sub cateory listview
            createSubCaList(i);
        }

    }// end of create category Item

    private void checkUserSession() {

        if (userSession.isUserLoggedIn()) {

            txtViewUserName.setText(userSession.getUserName());

        } else {

            toggleLogout();

        }
    }

    private void createCategories(final int pos) {

        //Create textview dynamically

        child[pos] = getLayoutInflater().inflate(R.layout.item_nav_categories, null);
        navigationView.addView(child[pos]);

        RelativeLayout relLayCategories = (RelativeLayout) child[pos].findViewById(R.id.relLayCategories);

        TextView txtViewCategories = (TextView) child[pos].findViewById(R.id.txtViewCategories);


        txtViewCategories.setText(arrayListCat.get(pos).getCategory_name());

        relLayCategories.setTag(R.string.Tag, pos);
        relLayCategories.setTag(R.string.ID, arrayListCat.get(pos).getCategory_id());

        subCatListView[pos] = new ExpandableListView(this);

        relLayCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                categoryId = view.getTag(R.string.ID).toString();

                Log.e("id", categoryId);
                int tag = Integer.parseInt(view.getTag(R.string.Tag).toString());

                if (noSubCatPos != lastPos) {
                    if (lastPos != tag) {


                        subCatListView[lastPos].setVisibility(View.GONE);

                        imgViewUpArrow = (ImageView) child[lastPos].findViewById(R.id.imgViewUpArrow);
                        imgViewDownArrow = (ImageView) child[lastPos].findViewById(R.id.imgViewDownArrow);

                        imgViewUpArrow.setVisibility(View.GONE);
                        imgViewDownArrow.setVisibility(View.VISIBLE);

                    }
                }


                lastPos = tag;

                if (noSubCatPos != tag) {


                    if (subCatListView[tag].getVisibility() == View.VISIBLE) {
                        subCatListView[tag].setVisibility(View.GONE);

                        imgViewUpArrow = (ImageView) child[tag].findViewById(R.id.imgViewUpArrow);
                        imgViewDownArrow = (ImageView) child[tag].findViewById(R.id.imgViewDownArrow);

                        imgViewUpArrow.setVisibility(View.GONE);
                        imgViewDownArrow.setVisibility(View.VISIBLE);
                    } else {

                        subCatListView[tag].setVisibility(View.VISIBLE);

                        imgViewUpArrow = (ImageView) child[tag].findViewById(R.id.imgViewUpArrow);
                        imgViewDownArrow = (ImageView) child[tag].findViewById(R.id.imgViewDownArrow);

                        imgViewDownArrow.setVisibility(View.GONE);
                        imgViewUpArrow.setVisibility(View.VISIBLE);
                    }
                }
            }


        });

    }

    private void createSubCaList(int pos) {

        //dynamically add List View to show subItem of different Category
        subCatListView[pos].setTag(R.string.Tag, pos);
        subCatListView[pos].setBackgroundColor(Color.parseColor("#4b4b4b"));
        subCatListView[pos].setVisibility(View.GONE);

        subCatListView[pos].setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView txtViewSubCategory = (TextView) view.findViewById(R.id.txtViewSubCategory);

                subCategoryId = txtViewSubCategory.getTag(R.string.ID).toString();

                Log.e("item", subCategoryId);

                if (isAddToCartOpen) {
                    replaceAddItem();
                } else {
                    setItemsInFragment();
                }
            }
        });

        arrayListSubCat = new ArrayList<SubCategory>();

        arrayListSubCat = hashMapSubCat.get(pos);

        //If categories have no sub categories hide the arrow
        if (arrayListSubCat.size() == 0) {
            imgViewDownArrow = (ImageView) child[pos].findViewById(R.id.imgViewDownArrow);
            imgViewDownArrow.setVisibility(View.GONE);

            //Capture the position of categories containing no sub categories
            noSubCatPos = pos;

            isSubCatPresent = false;
        }

        //add adapter to listview
        SubCategoryAdapter subCatAdapter = new SubCategoryAdapter(this, arrayListSubCat);
        subCatListView[pos].setAdapter(subCatAdapter);
        navigationView.addView(subCatListView[pos], params);
    }

    public void setItemsInFragment() {
        Log.e("frag", "method");
        CategoryItemFragment categoryFragment = (CategoryItemFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayItem);
        categoryFragment.getSubCatItems(venue_id, outlet_id, categoryId, subCategoryId);
    }

    @OnClick(R.id.relLayProfile)
    void navigate() {
        if (userSession.isUserLoggedIn()) {
            Intent intent = new Intent(this, DashBoardActivity.class);
            startActivity(intent);
        } else {

            passIntentHome();
        }
    }

    @OnClick(R.id.relLayChangeOutlet)
    void changeOutlet() {
        finish();
    }

    @OnClick(R.id.txtViewLogout)
    void logout() {
        userSession.ClearUserName();
        toggleLogout();
    }

    private void toggleLogout() {
        txtViewProfile.setText("Login");
        txtViewLogout.setVisibility(View.GONE);
        txtViewUserName.setText(" ");
    }

    private void passIntentHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public class GetOutletItems extends AsyncTask<String, String, String> {

        int status;
        JsonParser jsonParser;
        JSONObject jsonObject;
        JSONArray jsonArray;

        String message;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgresBar.start(OutletCategoryActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("inside", "do in");
            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_ITEMS + "/" + venue_id + "/?outletId=" + outlet_id + "&itemId=" + ""
                    + "&subCatId=" + "";


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            Log.e("jsonvenue", jsonString);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("inside", "json");

                    arrayListItems = new ArrayList<ItemOutlet>(jsonObject.length());

                    arrayListCat = new ArrayList<Category>(jsonObject.length());

                    hashMapSubCat = new HashMap<Integer, ArrayList<SubCategory>>();

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
                isAddToCartOpen = false;
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager
                        .beginTransaction();
                CategoryItemFragment categoryItemFragment = new CategoryItemFragment();

                Bundle bundle = new Bundle();
                createBundle(bundle);

                categoryItemFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frameLayItem, categoryItemFragment, "item");
                fragmentTransaction.commit();

            } else if (status == 0) {

                AlertDialogHelper.showAlertDialog(OutletCategoryActivity.this, message, "Alert");

            } else {
                AlertDialogHelper.showAlertDialog(OutletCategoryActivity.this, getString(R.string.server_message), "Alert");
            }
        }
    }

    private void createBundle(Bundle bundle) {
        bundle.putString("venue_id", venue_id);
        bundle.putString("outlet_id", outlet_id);
        bundle.putSerializable("arraylistitem", arrayListItems);
        bundle.putString("title", title);
    }


}