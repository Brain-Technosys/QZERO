package com.example.qzero.Outlet.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.GCMHelper;
import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Activities.DashBoardActivity;
import com.example.qzero.Outlet.Fragments.LoginRegisterFragment;
import com.example.qzero.Outlet.Fragments.LoginTabFragment;
import com.example.qzero.Outlet.Fragments.LoginUserFragment;
import com.example.qzero.Outlet.ObjectClasses.Advertisement;
import com.example.qzero.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    UserSession userSession;

    @InjectView(R.id.layout_myProfile)
    LinearLayout layout_myProfile;

    @InjectView(R.id.loginFrameLay)
    FrameLayout loginFrameLay;

    @InjectView(R.id.txtViewLogin)
    TextView txtViewLogin;

    @InjectView(R.id.txt_userName)
    TextView txt_userName;

    String LOGINTYPE = "SIMPLELOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);

        //getting intent from HomeScreen and CartScreen, LOGINTYPE will differenciate
        Bundle bundle = getIntent().getExtras();

        if (getIntent().hasExtra("LOGINTYPE")) {

            LOGINTYPE = bundle.getString("LOGINTYPE");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        userSession = new UserSession(LoginActivity.this);

        if (userSession.isUserLoggedIn()) {
            txtViewLogin.setText(getString(R.string.txt_my_profile));
            layout_myProfile.setVisibility(View.VISIBLE);
            loginFrameLay.setVisibility(View.GONE);
            txt_userName.setText("User: " + userSession.getUserName());
        } else {

            txtViewLogin.setText(getString(R.string.txt_login));
            loginFrameLay.setVisibility(View.VISIBLE);
            layout_myProfile.setVisibility(View.GONE);
            addLoginFragment();
        }
    }

    private void addLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        LoginTabFragment loginTabFragment = new LoginTabFragment();

        fragmentTransaction.add(R.id.loginFrameLay, loginTabFragment, "login");
        fragmentTransaction.commit();

    }

    public String getloginType() {
        return LOGINTYPE;
    }

    @OnClick(R.id.relLayLogin)
    public void finishLoginActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("LOGINTYPE", LOGINTYPE);
        finish();
        startActivity(intent);

    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, R.anim.slide_down);
    }

    @OnClick(R.id.btn_myProfile)
    public void goto_myProfile() {

        userSession.saveLogin(false);
        Intent intent = new Intent(LoginActivity.this,
                DashBoardActivity.class);
        intent.putExtra("LOGINTYPE", "myProfile");
        startActivity(intent);

    }

    @OnClick(R.id.btn_logOut)
    public void logout() {
        GCMHelper gcmHelper = new GCMHelper(this);
        gcmHelper.changeLoginBit(userSession.getUserID(), false);
        userSession.logout();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
