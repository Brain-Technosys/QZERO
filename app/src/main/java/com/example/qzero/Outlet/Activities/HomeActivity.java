package com.example.qzero.Outlet.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.Outlet.Fragments.LoginTabFragment;
import com.example.qzero.Outlet.Fragments.SearchTabFragment;
import com.example.qzero.Outlet.SlidingUpPanel.SlidingUpPanelLayout;
import com.example.qzero.Outlet.SlidingUpPanel.SlidingUpPanelLayout.PanelState;
import com.example.qzero.R;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HomeActivity extends FragmentActivity {

    @InjectView(R.id.frameLaySliding)
    FrameLayout frameLaySliding;

    @InjectView(R.id.txtViewSearch)
    TextView txtViewSearch;

    @InjectView(R.id.txtViewLogin)
    TextView txtViewLogin;

    @InjectView(R.id.imgViewSearchButton)
    ImageView imgViewSearchButton;

    @InjectView(R.id.imgViewLoginButton)
    ImageView imgViewLoginButton;

    @InjectView(R.id.searchFrameLay)
    FrameLayout searchFrameLay;

    @InjectView(R.id.loginFrameLay)
    FrameLayout loginFrameLay;

    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout sliding_layout;

    Boolean isLayoutVisible;

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);

        setFonts();

        addSearchFragment();
        addLoginFragment();
    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewSearch, FontType.FONT, this);
        FontHelper.setFontFace(txtViewLogin, FontType.FONT, this);

    }

    @SuppressLint("NewApi")
    @OnClick(R.id.imgViewSearchButton)
    void openSearchFragment() {

        isLayoutVisible = checkLoginVisibility();
        if (isLayoutVisible) {
            // do nothing
        } else {

            expandPanel();
            changeSearchButtons();
        }

    }

    @OnClick(R.id.imgViewLoginButton)
    void openLoginFragment() {

        isLayoutVisible = checkSearchVisibility();
        if (isLayoutVisible) {
            // do nothing
        } else {
            expandPanel();
            changeLoginButtons();
        }

    }

    private void changeSearchButtons() {

        isLayoutVisible = checkSearchVisibility();
        if (!isLayoutVisible) {

            searchFrameLay.setVisibility(View.VISIBLE);
            imgViewSearchButton.setImageResource(R.drawable.upbutton_selector);
        } else {

            searchFrameLay.setVisibility(View.GONE);
            imgViewSearchButton
                    .setImageResource(R.drawable.down_button_selector);
        }

    }

    public void expandPanel() {
        if (sliding_layout != null
                && (sliding_layout.getPanelState() == PanelState.EXPANDED || sliding_layout
                .getPanelState() == PanelState.ANCHORED)) {
            sliding_layout.setPanelState(PanelState.COLLAPSED);
        } else {
            sliding_layout.setPanelState(PanelState.EXPANDED);
        }
    }

    public Boolean checkSearchVisibility() {
        if (searchFrameLay.getVisibility() == View.VISIBLE) {

            return true;
        } else {
            return false;
        }
    }

    private void changeLoginButtons() {

        isLayoutVisible = checkLoginVisibility();
        if (!isLayoutVisible) {

            loginFrameLay.setVisibility(View.VISIBLE);
            imgViewLoginButton.setImageResource(R.drawable.upbutton_selector);
        } else {

            loginFrameLay.setVisibility(View.GONE);
            imgViewLoginButton
                    .setImageResource(R.drawable.down_button_selector);
        }

    }

    public Boolean checkLoginVisibility() {
        if (loginFrameLay.getVisibility() == View.VISIBLE) {

            return true;
        } else {
            return false;
        }
    }

    private void addSearchFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        SearchTabFragment searchTabFragment = new SearchTabFragment();
        fragmentTransaction.add(R.id.searchFrameLay, searchTabFragment,
                "search");
        fragmentTransaction.commit();

    }

    private void addLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        LoginTabFragment loginTabFragment = new LoginTabFragment();
        fragmentTransaction.add(R.id.loginFrameLay, loginTabFragment, "login");
        fragmentTransaction.commit();

    }

}
