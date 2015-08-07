package com.example.qzero.Outlet.Activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

   /* @InjectView(R.id.linLayCatogries)
    LinearLayout linLayCatogries;
*/
    @InjectView((R.id.txtViewProfile))
    TextView txtViewProfile;

    @InjectView(R.id.txtViewUserName)
    TextView txtViewUserName;

    @InjectView(R.id.txtViewLogout)
    TextView txtViewLogout;


    ArrayAdapter adapter;

    int list;
    int lastPos;

    String title;

    ActionBar actionBar;
    ActionBarDrawerToggle drawerToggle;

    ArrayList<ItemOutlet> arrayListItems;
    ArrayList<Category> arrayListCat;
    ArrayList<SubCategory> arrayListSubCat;

    HashMap<Integer, ArrayList<SubCategory>> hashMapSubCat;

    UserSession userSession;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String[] menu = {"Beverage", "Sea Food", "Continental"};
    String[] submenu = {"Chinese", "Fish", "Chicken"};

    ExpandableListView[] subCatListView;

    LinearLayout.LayoutParams params;

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

        addItemFragment();

        setIconsToActionBar();

        createCategoryItem();

    }//end of onCreate()

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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

        if (getIntent().hasExtra("arraylistitem")) {
            Bundle bundle = getIntent().getExtras();
            arrayListItems = (ArrayList<ItemOutlet>) bundle.getSerializable("arraylistitem");
            arrayListCat = (ArrayList<Category>) bundle.getSerializable("arrayListCat");

            hashMapSubCat = (HashMap<Integer, ArrayList<SubCategory>>) bundle.getSerializable("hashMapSubCat");


            title = bundle.getString("title");
        }
    }

    public void addItemFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager
                .beginTransaction();
        CategoryItemFragment categoryItemFragment = new CategoryItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("arraylistitem", arrayListItems);
        bundle.putString("title", title);
        categoryItemFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.frameLayItem, categoryItemFragment, "item");
        fragmentTransaction.commit();
    }

    public void replaceFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager
                .beginTransaction();
        AddCartFragment addCartFragment = new AddCartFragment();
        fragmentTransaction.replace(R.id.frameLayItem, addCartFragment,
                "addcart");
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
            txtViewProfile.setText("Login");
            txtViewLogout.setVisibility(View.GONE);
        }
    }

    private void createCategories(final int pos) {

        //Create textview dynamically

        TextView txtViewCategory = new TextView(this);
        txtViewCategory.setText(arrayListCat.get(pos).getCategory_name());
        txtViewCategory.setTag(pos);
        txtViewCategory.setTextSize(18);
        txtViewCategory.setTextColor(getResources().getColor(R.color.primary_material_dark));
        txtViewCategory.setPadding(120, 20, 0, 20);
        txtViewCategory.setTextColor(getResources().getColor(R.color.navigation_text_color));
        txtViewCategory.setBackgroundResource(R.drawable.selector_navigation_menu_item);

        navigationView.addView(txtViewCategory, params);

        subCatListView[pos] = new ExpandableListView(this);

        txtViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = Integer.parseInt(view.getTag().toString());
                if(lastPos!=tag) {
                    subCatListView[lastPos].setVisibility(View.GONE);

                }
                lastPos = tag;
                if (subCatListView[tag].getVisibility() == View.VISIBLE) {
                    subCatListView[tag].setVisibility(View.GONE);
                } else {

                    subCatListView[tag].setVisibility(View.VISIBLE);
                }

            }

        });

    }

    private void createSubCaList(int pos) {

        //dynamically add LismenuItemview to show subItem of different Category

        subCatListView[pos].setTag(pos);
        subCatListView[pos].setBackgroundColor(Color.parseColor("#4b4b4b"));
        subCatListView[pos].setVisibility(View.GONE);

        subCatListView[pos].setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        arrayListSubCat = new ArrayList<SubCategory>();

        arrayListSubCat = hashMapSubCat.get(pos);

        //add adapter to listview
        SubCategoryAdapter subCatAdapter = new SubCategoryAdapter(this, arrayListSubCat);
        subCatListView[pos].setAdapter(subCatAdapter);
        navigationView.addView(subCatListView[pos], params);
    }


    @OnClick(R.id.relLayProfile)
    void navigate() {
        if (userSession.isUserLoggedIn()) {
            Intent intent = new Intent(this, DashBoardActivity.class);
            startActivity(intent);
            finish();
        } else {

            passIntentHome();
        }
    }

    @OnClick(R.id.txtViewLogout)
    void logout() {
        userSession.ClearUserName();
        passIntentHome();

    }

    private void passIntentHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}