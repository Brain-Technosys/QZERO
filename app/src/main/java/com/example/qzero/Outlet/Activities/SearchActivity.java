package com.example.qzero.Outlet.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.qzero.Outlet.Fragments.SearchTabFragment;
import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.inject(this);

        addSearchFragment();
    }


    private void addSearchFragment() {
        Log.e("addSearchFragment", "addSearchFragment");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        SearchTabFragment searchTabFragment = new SearchTabFragment();
        fragmentTransaction.add(R.id.searchFrameLay, searchTabFragment,
                "search");
        fragmentTransaction.commit();

    }

    @OnClick(R.id.relLaySearch)
    public void closeSearchActivity() {
        finish();
    }


}
