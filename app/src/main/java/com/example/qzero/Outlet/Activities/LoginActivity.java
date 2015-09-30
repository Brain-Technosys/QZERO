package com.example.qzero.Outlet.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.qzero.Outlet.Fragments.LoginTabFragment;
import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);

        addLoginFragment();
    }

   private void addLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        LoginTabFragment loginTabFragment = new LoginTabFragment();
        fragmentTransaction.add(R.id.loginFrameLay, loginTabFragment, "login");
        fragmentTransaction.commit();

    }

    @OnClick(R.id.relLayLogin)public void finishLoginActivity(){finish();}

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
