package com.example.qzero.Outlet.Activities;


import android.content.Intent;
import android.content.res.Configuration;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Activities.DashBoardActivity;
import com.example.qzero.Outlet.Fragments.AddCartFragment;
import com.example.qzero.Outlet.Fragments.CategoryItemFragment;
import com.example.qzero.Outlet.Fragments.SearchTabFragment;
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.Outlet.ObjectClasses.Venue;
import com.example.qzero.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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


    LinearLayout categoryLayout;

    View child2;
    int childId = 2000;

    ArrayAdapter adapter;

    int list;

    String title;

    ActionBar actionBar;
    ActionBarDrawerToggle drawerToggle;

    ArrayList<ItemOutlet> arrayListItems;

    UserSession userSession;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String[] menu = {"Beverage", "Sea Food", "Continental"};
    String[] submenu = {"Chinese", "Fish", "Chicken"};

    ListView[] lists = new ListView[menu.length];


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
        if (getIntent().hasExtra("arraylistitem")) {
            Bundle bundle = getIntent().getExtras();
            arrayListItems = (ArrayList<ItemOutlet>) bundle.getSerializable("arraylistitem");
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

    public void replaceFragment()
    {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager
                .beginTransaction();
        AddCartFragment addCartFragment = new AddCartFragment();
        fragmentTransaction.replace(R.id.frameLayItem, addCartFragment,
                "addcart");
        fragmentTransaction.commit();
    }


    public void createCategoryItem() {

        //Check if a user is logged in or not
        checkUserSession();

        categoryLayout = (LinearLayout) findViewById(R.id.navigationView);
        adapter = new ArrayAdapter(this, R.layout.list_subitem_category, R.id.tv, submenu);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //dynamically adding category item as TextView
        for (int i = 0; i < menu.length; i++) {
            TextView menuItem = new TextView(this);
            menuItem.setText(menu[i]);
            menuItem.setTag(i);
            menuItem.setId(i);
            menuItem.setTextSize(18);
            menuItem.setTextColor(getResources().getColor(R.color.primary_material_dark));
            menuItem.setPadding(120, 20, 0, 20);
            menuItem.setTextColor(getResources().getColor(R.color.navigation_text_color));
            menuItem.setBackgroundResource(R.drawable.selector_navigation_menu_item);
            categoryLayout.addView(menuItem, params);

            //dynamically add LismenuItemiew to show subItem of different Category
            lists[i] = new ListView(this);
            lists[i].setVisibility(View.GONE);
            lists[i].setId(i + 1000);
            lists[i].setAdapter(adapter);
            categoryLayout.addView(lists[i], params);

            // menuItem.setOnClickListener(this);
            // lists[i].setOnItemClickListener(this);

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

   /* @Override
    public void onClick(View v) {
        int viewId = v.getId();

        //Click listener for TextView Menu
        if (viewId < 1000) {
            for (int i = 0; i < menu.length; i++) {
                list = v.getId();
                if (i == v.getId()) {

                    if (lists[v.getId()].getVisibility() == View.GONE)
                        lists[v.getId()].setVisibility(View.VISIBLE);
                    else
                        lists[v.getId()].setVisibility(View.GONE);
                } else lists[i].setVisibility(View.GONE);
            }
        }
        if (viewId > 1000 && viewId < 2000) {
            return;

        }
        //Click listener for latmenu Items
        if (viewId >= 2000) {


        }
    }*/


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