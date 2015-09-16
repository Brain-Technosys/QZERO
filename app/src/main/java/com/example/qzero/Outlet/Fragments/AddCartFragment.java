package com.example.qzero.Outlet.Fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.Adapters.ItemDetailAdapter;
import com.example.qzero.Outlet.ExpandableListView.ExpandableListView;
import com.example.qzero.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddCartFragment extends Fragment {

    @InjectView(R.id.txtViewItemName)
    TextView txtViewItemName;

    @InjectView(R.id.txtViewTitleDesc)
    TextView txtViewTitleDesc;

    @InjectView(R.id.txtViewDesc)
    TextView txtViewDesc;

    @InjectView(R.id.txtViewAddItem)
    TextView txtViewAddItem;

    @InjectView(R.id.linLayItem)
    LinearLayout linLayItem;


    TableLayout tableLayoutModifiers;

    Dialog dialog;

    LinearLayout linLayModifiers;

    TextView txtViewTitle;
    TextView txtViewCancel;
    TextView txtViewOk;

    TextView txtViewModList;

    TextView txtViewTotal;
    TextView txtViewPrice;

    Boolean isRadioButtonClicked = false;
    Boolean isCheckBoxClicked = false;

    CheckBox checkBox[];
    RadioGroup radioGroup[];
    RadioButton radioButton[];

    String[] modifier_title = {"Freeze/Frying", "Boiled"};
    String[] modifier = {"Extra fry with butter", "Steak"};


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

        inflateQtyLayout();
    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewItemName, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewTitleDesc, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewDesc, FontHelper.FontType.FONT, getActivity());
    }


    @OnClick(R.id.txtViewAddItem)
    void addItem() {
        addItemLayout();
    }

    private void addItemLayout() {

        inflateQtyLayout();
    }

    private void inflateQtyLayout() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.list_addcart, null);
        linLayItem.addView(view);

        TextView txtViewAddModifiers = (TextView) view.findViewById(R.id.txtViewAddModifiers);

        txtViewAddModifiers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        tableLayoutModifiers = (TableLayout) view.findViewById(R.id.tableLayoutModifiers);

        txtViewModList = (TextView) view.findViewById(R.id.txtViewModList);

        txtViewTotal = (TextView) view.findViewById(R.id.txtViewTotal);

        txtViewPrice = (TextView) view.findViewById(R.id.txtViewPrice);

        setAddItemFonts();

    }

    private void openDialog() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_modifiers);

        getDialogIds();


        dialog.show();

    }

    private void getDialogIds() {

        linLayModifiers = (LinearLayout) dialog.findViewById(R.id.linLayModifiers);

        txtViewTitle = (TextView) dialog.findViewById(R.id.txtViewTitle);
        txtViewCancel = (TextView) dialog.findViewById(R.id.txtViewCancel);
        txtViewOk = (TextView) dialog.findViewById(R.id.txtViewOk);

        setDialogFonts();

        createModifierLayout();

        setOnClick();
    }

    //create dynamic checkbox and radiogroup
    private void createModifierLayout() {


        checkBox = new CheckBox[modifier_title.length];

        radioGroup = new RadioGroup[modifier_title.length];

        radioButton = new RadioButton[modifier.length];

        for (int i = 0; i < modifier_title.length; i++) {

            createCheckBox(i);

            for (int j = 0; j < modifier.length; j++) {

                createRadioButton(i, j);
            }
        }

    }

    private void createCheckBox(final int i) {


        checkBox[i] = new CheckBox(getActivity());

        checkBox[i].setTag(i);
        checkBox[i].setText(modifier_title[i]);
        checkBox[i].setTextColor(Color.parseColor("#000000"));
        FontHelper.setFontFace(checkBox[i], FontHelper.FontType.FONT, getActivity());
        checkBox[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (checkBox[i].isChecked()) {

                    Log.e("chck", checkBox[i].getText().toString());

                    if (radioGroup[i].getCheckedRadioButtonId() == -1) {
                        radioGroup[i].check(radioButton[0].getId());
                    }

                } else {
                    radioGroup[i].clearCheck();

                    Log.e("notcheck", "check" + i);

                }

            }
        });

        linLayModifiers.addView(checkBox[i]);

        //Create radio group in linear layout
        radioGroup[i] = new RadioGroup(getActivity());
        radioGroup[i].setTag(i);
        radioGroup[i].setPadding(15, 0, 0, 0);

        radioGroup[i].setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (radioGroup[i].getCheckedRadioButtonId() != -1) {
                    Log.e("inside", "check");
                    try {
                        int radioButtonID = radioGroup[i].getCheckedRadioButtonId();
                        RadioButton radioBtn = (RadioButton) radioGroup[i].findViewById(radioButtonID);
                        Log.e("textradio", radioBtn.getText().toString());
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }

                    if (checkBox[i].isChecked()) {
                        //do nothing
                    } else {
                        checkBox[i].setChecked(true);
                    }
                } else {
                    checkBox[i].setChecked(false);
                }

            }
        });


        //Add radiogroup to linear layout
        linLayModifiers.addView(radioGroup[i]);
    }

    private void createRadioButton(final int i, final int j) {


        //Create radio buttons
        radioButton[j] = new RadioButton(getActivity());
        radioButton[j].setText(modifier[j]);
        radioButton[j].setTextColor(Color.parseColor("#000000"));
        radioButton[j].setId(j);
        radioButton[j].setTag(i);
        FontHelper.setFontFace(radioButton[j], FontHelper.FontType.FONT, getActivity());

        radioGroup[i].addView(radioButton[j]);
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
                BuildTable(1);

                dialog.dismiss();
            }
        });

    }

    private void setAddItemFonts() {
        FontHelper.setFontFace(txtViewTotal, FontHelper.FontType.FONTROBOLD, getActivity());
    }

    private void BuildTable(int rows) {

        txtViewModList.setVisibility(View.VISIBLE);

        FontHelper.setFontFace(txtViewModList, FontHelper.FontType.FONTROBOLD, getActivity());
        // outer for loop
        for (int i = 1; i <= rows; i++) {

            TableRow row = new TableRow(getActivity());
            row.setPadding(10, 10, 10, 10);

            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));


            TextView txtView = new TextView(getActivity());

            txtView.setText("Extra fry with butter");
            txtView.setTextColor(Color.parseColor("#000000"));
            txtView.setGravity(Gravity.LEFT);

            row.addView(txtView);

            TextView txtView1 = new TextView(getActivity());

            txtView1.setText("$10");
            txtView.setGravity(Gravity.CENTER);
            txtView1.setPadding(20, 0, 0, 0);
            txtView1.setTextColor(Color.parseColor("#000000"));

            row.addView(txtView1);

            tableLayoutModifiers.addView(row);

        }
    }

}
