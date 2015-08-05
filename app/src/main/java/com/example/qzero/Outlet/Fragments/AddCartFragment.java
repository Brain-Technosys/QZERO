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
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddCartFragment extends Fragment {

    @InjectView(R.id.txtViewItemName)
    TextView txtViewItemName;

    @InjectView(R.id.txtViewQty)
    TextView txtViewQty;

    @InjectView(R.id.txtViewModifiers)
    TextView txtViewModifiers;

    @InjectView(R.id.txtViewAddCart)
    TextView txtViewAddCart;

    @InjectView(R.id.txtViewTitleDesc)
    TextView txtViewTitleDesc;

    @InjectView(R.id.txtViewDesc)
    TextView txtViewDesc;

    Dialog dialog;

    LinearLayout linLayChckBox;

    TextView txtViewTitle;
    TextView txtViewCancel;
    TextView txtViewOk;

    CheckBox checkBox;


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

        setFonts();
    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewItemName, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewQty, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewModifiers, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewAddCart, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewTitleDesc, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewDesc, FontHelper.FontType.FONT, getActivity());
    }

    @OnClick(R.id.txtViewModifiers)
    void openModifiersDialog() {
        openDialog();
    }

    private void openDialog() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_modifiers);

        getDialogIds();

        for (int i = 0; i < 5; i++) {
            checkBox = new CheckBox(getActivity());
            checkBox.setTag(i);
            checkBox.setText("Soda Masala");
            checkBox.setTextColor(Color.parseColor("#000000"));
            FontHelper.setFontFace(checkBox, FontHelper.FontType.FONT, getActivity());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    Toast.makeText(getActivity(), "check", Toast.LENGTH_LONG);
                }
            });

            linLayChckBox.addView(checkBox);
        }
        dialog.show();

    }

    private void getDialogIds() {
        linLayChckBox = (LinearLayout) dialog.findViewById(R.id.linLayChckBox);
        txtViewTitle = (TextView) dialog.findViewById(R.id.txtViewTitle);
        txtViewCancel = (TextView) dialog.findViewById(R.id.txtViewCancel);
        txtViewOk = (TextView) dialog.findViewById(R.id.txtViewOk);

        setDialogFonts();
        setOnClick();
    }

    private void setDialogFonts() {
        FontHelper.setFontFace(txtViewTitle, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewCancel, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewOk, FontHelper.FontType.FONT, getActivity());
    }

    private void setOnClick() {
        txtViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
