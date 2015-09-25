package com.example.qzero.Outlet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;
import com.example.qzero.R;

import butterknife.ButterKnife;

public class LoginTabFragment extends Fragment {

	FragmentTabHost mTabHost;

	View tabIndicatorUser;
	View tabIndicatorRegister;

	TextView titleUser;
	TextView titleClub;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tab_login,
				container, false);
		ButterKnife.inject(this, rootView);

		mTabHost = (FragmentTabHost) rootView
				.findViewById(android.R.id.tabhost);

		addTabHost();

		return rootView;

	}

	public void addTabHost() {

		mTabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.realtabcontent);

		inflateLayouts();
		setText();
		setFonts();

		mTabHost.addTab(
				mTabHost.newTabSpec("user").setIndicator(tabIndicatorUser),
				LoginUserFragment.class, null);

		mTabHost.addTab(
				mTabHost.newTabSpec("register").setIndicator(tabIndicatorRegister),
				LoginRegisterFragment.class, null);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mTabHost = null;
	}

	public void inflateLayouts() {
		tabIndicatorUser = LayoutInflater.from(getActivity()).inflate(
				R.layout.tabtabtheme_tab_indicator_holo, mTabHost.getTabWidget(),
				false);

		tabIndicatorRegister = LayoutInflater.from(getActivity()).inflate(
				R.layout.tabtabtheme_tab_indicator_holo, mTabHost.getTabWidget(),
				false);
	}

	public void setText() {
		titleUser = (TextView) tabIndicatorUser
				.findViewById(android.R.id.title);
		titleUser.setText("User");

		titleClub = (TextView) tabIndicatorRegister
				.findViewById(android.R.id.title);
		titleClub.setText("Sign Up");
	}

	public void setFonts() {
		FontHelper.applyFont(getActivity(), titleUser, FontType.FONT);
		FontHelper.applyFont(getActivity(), titleClub, FontType.FONT);

	}


}