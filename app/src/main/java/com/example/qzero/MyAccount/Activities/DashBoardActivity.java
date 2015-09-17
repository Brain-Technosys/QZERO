package com.example.qzero.MyAccount.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Fragments.DashboardFragment;
import com.example.qzero.MyAccount.Fragments.OrderFragment;
import com.example.qzero.MyAccount.Fragments.ProfileInfoFragment;
import com.example.qzero.MyAccount.Fragments.SettingFragment;
import com.example.qzero.MyAccount.Fragments.WalletFragment;
import com.example.qzero.R;

import butterknife.ButterKnife;

import static com.example.qzero.R.drawable;
import static com.example.qzero.R.id;
import static com.example.qzero.R.layout;
import static com.example.qzero.R.string;

public class DashBoardActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private LinearLayout headerLayout;

    UserSession userSession;
    // Edited on 16-Jul-2015 by Ashish


    TextView userNameTextView;

    String userNameString;

    Boolean isDashboard=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(layout.activity_dashboard);
        ButterKnife.inject(this);

        userSession = new UserSession(getApplicationContext());
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(id.nvView);

        //Inflating header layout
        headerLayout = (LinearLayout) LayoutInflater.from(this).inflate(layout.nav_header, null);
        // Creating TextView dynamically
        userNameTextView = new TextView(DashBoardActivity.this);
        userNameTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // Getting user name from session
        userNameString = userSession.getUserName();
        userNameTextView.setText(userNameString);
        // Getting Intent
        Intent intent = getIntent();
        // Getting value from intent and setting to header
        if (intent != null) {


            headerLayout.addView(userNameTextView);
            nvDrawer.addHeaderView(headerLayout);
            nvDrawer.setItemIconTintList(null);
        }


        // Setup drawer view

        setupDrawerContent(nvDrawer);

        // Set the menu icon instead of the launcher icon.
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        //ab.setDisplayShowTitleEnabled(false);


        Menu menu = nvDrawer.getMenu();
        MenuItem item = menu.findItem(id.nav_home_fragment);
        selectDrawerItem(item);


    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        //Checking if the item is in checked state or not, if not make it in checked state
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case id.nav_home_fragment:
                fragmentClass = DashboardFragment.class;
                isDashboard=true;
                break;
            case id.nav_wallet_fragment:
                fragmentClass = WalletFragment.class;
                break;
            case id.nav_order_fragment:
                fragmentClass = OrderFragment.class;
                break;
            case id.nav_profile_fragment:
                fragmentClass = ProfileInfoFragment.class;
                break;
            case id.nav_setting:
                fragmentClass = SettingFragment.class;
                break;
            default:
                fragmentClass = DashboardFragment.class;
                isDashboard=true;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id.flContent, fragment);
        if (!isDashboard) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction .commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, string.openDrawer, string.closeDrawer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.e("backpressed", "pressed");
    }
}
