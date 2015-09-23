package com.example.qzero.Outlet.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.ObjectClasses.AddItems;
import com.example.qzero.Outlet.ObjectClasses.Items;
import com.example.qzero.R;

import java.util.ArrayList;

/**
 * Created by Braintech on 9/23/2015.
 */
public class CustomAdapterAddItems extends BaseAdapter {

    Context context;
    ArrayList<AddItems> rowItems;

    Dialog dialog;

    CheckBox checkBox[];
    RadioGroup radioGroup[];
    RadioButton radioButton[];

    LinearLayout linLayModifiers;

    TextView txtViewTitle;
    TextView txtViewCancel;
    TextView txtViewOk;

    String[] modifier_title;
    String[] modifier;
    String[] modifier_price;

    public CustomAdapterAddItems(Context context,ArrayList<AddItems> rowItems,String[] modifier_title,String[] modifier, String[] modifier_price) {

        this.context = context;
        this.rowItems = rowItems;
        this.modifier_price=modifier_price;
    }

    @Override
    public int getCount() {

        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_addcart, null);

            holder = new ViewHolder();

            holder.txtViewQty= (TextView) convertView.findViewById(R.id.txtViewQty);
            holder.txtViewAddModifiers= (TextView) convertView.findViewById(R.id.txtViewAddModifiers);

            holder.imgViewDelete=(ImageView)convertView.findViewById(R.id.imgViewDelete);



            Log.e("po", "" + position);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        AddItems m = rowItems.get(position);

        Log.e("qty",m.getQuantity());
        holder.txtViewQty.setText(m.getQuantity());

        //Delete the item added
        holder.imgViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rowItems.remove(position);
                notifyDataSetChanged();

            }
        });

        //open modifiers dialog box on click
        holder.txtViewAddModifiers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();
            }
        });


        return convertView;
    }

    private void openDialog() {
        dialog = new Dialog(context);
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


        checkBox[i] = new CheckBox(context);

        checkBox[i].setTag(i);
        checkBox[i].setText(modifier_title[i]);
        checkBox[i].setTextColor(Color.parseColor("#000000"));
        FontHelper.setFontFace(checkBox[i], FontHelper.FontType.FONT,context);
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
        radioGroup[i] = new RadioGroup(context);
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
        radioButton[j] = new RadioButton(context);
        radioButton[j].setText(modifier[j]);
        radioButton[j].setTextColor(Color.parseColor("#000000"));
        radioButton[j].setId(j);
        radioButton[j].setTag(i);
        FontHelper.setFontFace(radioButton[j], FontHelper.FontType.FONT, context);

        radioGroup[i].addView(radioButton[j]);
    }


    private void setDialogFonts() {
        FontHelper.setFontFace(txtViewTitle, FontHelper.FontType.FONT, context);
        FontHelper.setFontFace(txtViewCancel, FontHelper.FontType.FONT, context);
        FontHelper.setFontFace(txtViewOk, FontHelper.FontType.FONT, context);
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
              //  BuildTable(1);

                dialog.dismiss();
            }
        });

    }

    static class ViewHolder {


        TextView txtViewQty;
        TextView txtViewAddModifiers;

        ImageView imgViewDelete;
    }


}
