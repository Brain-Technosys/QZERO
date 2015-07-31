package com.example.qzero.Outlet.Activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Activities.DashBoardActivity;
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.Outlet.ObjectClasses.Venue;
import com.example.qzero.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class OutletCategoryActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawer;


    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.navigationView)
    LinearLayout navigationView;

    @InjectView(R.id.tableLayoutItems)
    TableLayout tableLayoutItems;

    @InjectView((R.id.txtViewProfile))
    TextView txtViewProfile;


    TableRow tableRow;
    View child;
    View child2;
    int childId = 2000;

    ActionBar actionBar;
    ActionBarDrawerToggle drawerToggle;

    String[] menu = {"Beverage", "Sea Food", "Continental"};
    String[] submenu = {"Chinese", "Fish", "Chicken"};

    LinearLayout categoryLayout;

    ArrayAdapter adapter;

    ListView[] lists = new ListView[menu.length];

    int list;
    int length = 11;

    ArrayList<ItemOutlet> arrayListItems;

    UserSession  userSession;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_category);

        ButterKnife.inject(this);

        //Initialize user session
        userSession=new UserSession(this);

        setSupportActionBar(toolbar);

        drawerToggle = setupDrawerToggle();

        setIconsToActionBar();

        createCategoryItem();

        //getOutletItems();

        getIntentData();

        setTableLayout();

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
        }
    }

    private void setTableLayout() {

        int row;
        int length = arrayListItems.size();
        if (length % 2 == 0) {
            row = length / 2;

        } else {
            row = length / 2 + 1;

        }
        tableLayoutItems.removeAllViews();
        BuildTable(row, 2);

    }

    private void BuildTable(int rows, int cols) {

        // outer for loop
        for (int i = 1; i <= rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            if (length % 2 != 0) {
                if (i == rows) {
                    cols = 1;
                }
            } else {
                cols = 2;
            }

            // inner for loop
            for (int j = 1; j <= cols; j++) {

                child = getLayoutInflater().inflate(R.layout.item_category, null);
                child.setId(childId++);
                child.setOnClickListener(this);

                row.addView(child);
            }

            tableLayoutItems.addView(row);

        }
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

            menuItem.setOnClickListener(this);
            lists[i].setOnItemClickListener(this);

        }

    }// end of create category Item

    private void checkUserSession()
    {

        if(userSession.isUserLoggedIn())
        {
           //do nothing

        }
        else
        {
            txtViewProfile.setText("Login");
        }
    }

    @Override
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mDrawer.closeDrawer(Gravity.LEFT);
        int mod = length % 2;
        int time = length / 2;

        for (int i = 0; i < (time); i++) {
            addTableRowItem();

            child2 = getLayoutInflater().inflate(R.layout.item_category, null);
            child2.setId(childId++);
            child2.setOnClickListener(this);
            tableRow.addView(child2);
            //tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        if (mod == 1) {
            addTableRowItem();
            // tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

    }

    public void addTableRowItem() {

        tableRow = new TableRow(OutletCategoryActivity.this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        child = getLayoutInflater().inflate(R.layout.item_category, null);
        child.setId(childId++);
        child.setOnClickListener(this);
        tableRow.addView(child);
    }

    @OnClick(R.id.changeOutlet)
    public void selectOutlet() {
    }

    @OnClick(R.id.relLayProfile)
    void navigate()
    {
        if(userSession.isUserLoggedIn())
        {
            Intent intent=new Intent(this, DashBoardActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent=new Intent(this,HomeActivity.class);
            startActivity(intent);
        }
    }




}