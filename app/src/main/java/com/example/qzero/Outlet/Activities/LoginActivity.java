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

import com.example.qzero.CommonFiles.Sessions.UserSession;
import com.example.qzero.MyAccount.Activities.DashBoardActivity;
import com.example.qzero.Outlet.Fragments.LoginTabFragment;
import com.example.qzero.R;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);


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

    @OnClick(R.id.relLayLogin)
    public void finishLoginActivity() {
        finish();
    }

    @OnClick(R.id.btn_myProfile)
    public void goto_myProfile() {
        Intent intent = new Intent(LoginActivity.this,
                DashBoardActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_logOut)
    public void logout() {
        userSession.logout();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
