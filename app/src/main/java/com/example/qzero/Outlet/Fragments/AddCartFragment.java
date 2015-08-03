package com.example.qzero.Outlet.Fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCartFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_cart,
                container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.txtViewModifiers)
    void openModifiersDialog()
    {
        openDialog();
    }

    private void openDialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_modifiers);
        LinearLayout linLayChckBox=(LinearLayout) dialog.findViewById(R.id.linLayChckBox);
        for(int i=0;i<5;i++) {
            CheckBox checkBox = new CheckBox(getActivity());
            checkBox.setTag(i);
            checkBox.setText("Soda Masala");
            checkBox.setTextColor(Color.parseColor("#000000"));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    Toast.makeText(getActivity(),"checj",Toast.LENGTH_LONG);
                }
            });

            linLayChckBox.addView(checkBox);
        }
        dialog.show();

    }
}
