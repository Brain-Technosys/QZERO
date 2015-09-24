package com.example.qzero.Outlet.Fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.Outlet.Adapters.ItemDetailAdapter;
import com.example.qzero.Outlet.ExpandableListView.ExpandableListView;
import com.example.qzero.Outlet.ObjectClasses.AddItems;
import com.example.qzero.Outlet.ObjectClasses.Category;
import com.example.qzero.Outlet.ObjectClasses.ChoiceGroup;
import com.example.qzero.Outlet.ObjectClasses.ItemOutlet;
import com.example.qzero.Outlet.ObjectClasses.Modifier;
import com.example.qzero.Outlet.ObjectClasses.SubCategory;
import com.example.qzero.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    @InjectView(R.id.txtViewOrigPrice)
    TextView txtViewOrigPrice;

    @InjectView(R.id.txtViewDiscount)
    TextView txtViewDiscount;

    @InjectView(R.id.txtViewDiscPrice)
    TextView txtViewDiscPrice;

    @InjectView(R.id.txtViewTitDisc)
    TextView txtViewTitDisc;

    @InjectView(R.id.imgViewItem)
    ImageView imgViewItem;

    @InjectView(R.id.relLayItems)
    LinearLayout relLayItems;


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

    ArrayList<AddItems> arrayListAddItems;
    ArrayList<Modifier> modifierList;

    int status;
    int jsonLength;
    int pos = 0;

    String message;

    JsonParser jsonParser;
    JSONObject jsonObject;
    JSONArray jsonArray;

    String venue_id;
    String itemId;
    String outletId;
    String subCatId;

    String item_name;
    String item_desc;
    String item_price;
    String discount_details;
    String item_image;

    ArrayList<ChoiceGroup> modifier_title;
    HashMap<Integer, ArrayList<Modifier>> hashMapModifiers;

    HashMap<Integer, ArrayList<Modifier>> hashMapChoosenMod;

    String discountDesc;
    Double afterDiscPrice;

    int countLength = 1;

    int index;

    RelativeLayout relativeLay;

    String choice;

    View[] view;


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

        getArgData();

        getItemDetails();

        hashMapChoosenMod = new HashMap<Integer, ArrayList<Modifier>>();

    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewItemName, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewTitleDesc, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewDesc, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewOrigPrice, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewDiscount, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewTitDisc, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewDiscPrice, FontHelper.FontType.FONT, getActivity());

    }

    public void getArgData() {
        Bundle args = getArguments();
        if (args != null) {
            venue_id = args.getString("venue_id");
            outletId = args.getString("outlet_id");
            itemId = args.getString("item_id");
        }
    }

    private void getItemDetails() {
        if (CheckInternetHelper.checkInternetConnection(getActivity())) {
            new GetItemDetail().execute();
        } else {
            AlertDialogHelper.showAlertDialog(getActivity(), getString(R.string.internet_connection_message), "Alert");
        }
    }


    @OnClick(R.id.txtViewAddItem)
    void addItem() {
        inflateQtyLayout();
        countLength++;
    }


    private void inflateQtyLayout() {

        relLayItems.removeAllViews();
        view = new View[countLength];
        Log.e("len", "" + view.length);
        for (int i = countLength - 1; i >= 0; i--) {

            Log.e("i", "" + i);
            view[i] = getActivity().getLayoutInflater().inflate(R.layout.list_addcart, null);

            relLayItems.addView(view[i]);

            TextView txtViewAddModifiers = (TextView) view[i].findViewById(R.id.txtViewAddModifiers);

            txtViewAddModifiers.setTag(i);

            TextView txtViewQty = (TextView) view[i].findViewById(R.id.txtViewQty);

            txtViewQty.setText(String.valueOf(i));


            txtViewAddModifiers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    index = Integer.parseInt(v.getTag().toString());
                    Log.e("count", "" + relLayItems.getChildCount());
                    Log.e("index", "" + index);
                    openDialog();
                }
            });

            TableLayout tableLayoutModifiers = (TableLayout) view[i].findViewById(R.id.tableLayoutModifiers);

            TextView txtViewModList = (TextView) view[i].findViewById(R.id.txtViewModList);

            txtViewTotal = (TextView) view[i].findViewById(R.id.txtViewTotal);

            txtViewPrice = (TextView) view[i].findViewById(R.id.txtViewPrice);


        }
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

        hashMapChoosenMod.put(index, modifierList);

        setOnClick();
    }

    //create dynamic checkbox and radiogroup
    private void createModifierLayout() {

        checkBox = new CheckBox[modifier_title.size()];

        radioGroup = new RadioGroup[modifier_title.size()];

        for (int i = 0; i < modifier_title.size(); i++) {

            createCheckBox(i);

            modifierList = new ArrayList<Modifier>();

            modifierList = hashMapModifiers.get(i);

            radioButton = new RadioButton[modifierList.size()];

            for (int j = 0; j < modifierList.size(); j++) {

                createRadioButton(i, j);
            }
        }


    }

    private void createCheckBox(final int i) {


        checkBox[i] = new CheckBox(getActivity());

        checkBox[i].setTag(i);
        checkBox[i].setText(modifier_title.get(i).getChoice_name());
        checkBox[i].setTextColor(Color.parseColor("#000000"));
        FontHelper.setFontFace(checkBox[i], FontHelper.FontType.FONT, getActivity());
        checkBox[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (checkBox[i].isChecked()) {

                    choice = checkBox[i].getText().toString();

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

                modifierList = new ArrayList<Modifier>();

                if (radioGroup[i].getCheckedRadioButtonId() != -1) {
                    Log.e("inside", "check");
                    try {
                        int radioButtonID = radioGroup[i].getCheckedRadioButtonId();
                        RadioButton radioBtn = (RadioButton) radioGroup[i].findViewById(radioButtonID);

                        Modifier modifier = new Modifier(radioBtn.getText().toString(), "", choice);
                        modifierList.add(modifier);
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
        radioButton[j].setText(modifierList.get(i).getMod_name());
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
               /* View views =

                        TextView txtViewModL = (TextView) views.findViewById(R.id.txtViewModList);

                TableLayout tableLayoutMod = (TableLayout) views.findViewById(R.id.tableLayoutModifiers);
                BuildTable(hashMapChoosenMod.size(), tableLayoutMod, txtViewModL);
*/
                dialog.dismiss();
            }
        });

    }


    public void setLayout() {
        txtViewItemName.setText(item_name);

        if (item_desc.equals("null") || item_desc == null) {
            txtViewTitleDesc.setVisibility(View.INVISIBLE);
        } else
            txtViewDesc.setText(item_desc);


        if (afterDiscPrice == 0.0) {
            txtViewDiscPrice.setText(item_price);
            txtViewOrigPrice.setVisibility(View.GONE);
            txtViewTitDisc.setVisibility(View.INVISIBLE);
        } else {
            txtViewOrigPrice.setText(item_price);
            txtViewDiscount.setText(discountDesc);
            txtViewDiscPrice.setText(String.valueOf(afterDiscPrice));
        }

        //Load Image
        Picasso.with(getActivity()).load(item_image).error(R.drawable.q2x).into(imgViewItem);

    }


    private void setAddItemFonts() {
        FontHelper.setFontFace(txtViewTotal, FontHelper.FontType.FONTROBOLD, getActivity());
    }

    private void BuildTable(int rows, TableLayout tableLayoutModifiers, TextView txtViewModList) {

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

    private class GetItemDetail extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("inside", "do in");
            status = -1;
            jsonParser = new JsonParser();
            String url = Const.BASE_URL + Const.GET_ITEM_DETAIL + "/" + venue_id + "?outletId=" + outletId + "&itemId=" + itemId
                    + "&subCatId=" + subCatId;

            Log.e("url", url);


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            Log.e("jsonvenue", jsonString);

            try {
                jsonObject = new JSONObject(jsonString);

                if (jsonObject != null) {
                    Log.e("inside", "json");

                    status = jsonObject.getInt(Const.TAG_STATUS);
                    message = jsonObject.getString(Const.TAG_MESSAGE);


                    Log.d("status", "" + status);
                    if (status == 1) {

                        JSONObject jsonObj = jsonObject.getJSONObject(Const.TAG_JsonObj);

                        afterDiscPrice = jsonObj.getDouble(Const.TAG_AFTER_DISC);
                        discountDesc = jsonObj.getString(Const.TAG_DISC_DETAIL);

                        String item_id = jsonObj.getString(Const.TAG_ITEM_ID);

                        item_image = Const.BASE_URL + Const.IMAGE_URL + item_id;

                        //Get json Object for item details

                        JSONObject jsonObjItem = jsonObj.getJSONObject(Const.TAG_JsonDetailObj);


                        item_name = jsonObjItem.getString(Const.TAG_NAME);
                        item_price = jsonObjItem.getString(Const.TAG_PRICE);
                        item_desc = jsonObjItem.getString(Const.TAG_DESC);

                        //Get json array for categories
                        JSONArray jsonArrayModifiers = new JSONArray();

                        jsonArrayModifiers = jsonObj.getJSONArray(Const.TAG_JsonChoiceObj);

                        modifier_title = new ArrayList<ChoiceGroup>();

                        hashMapModifiers = new HashMap<Integer, ArrayList<Modifier>>();

                        for (int i = 0; i < jsonArrayModifiers.length(); i++) {
                            JSONObject jsonObjGroup = jsonArrayModifiers.getJSONObject(i);

                            String choice_name = jsonObjGroup.getString(Const.TAG_NAME);

                            Boolean isCompulsory = jsonObjGroup.getBoolean(Const.TAG_IS_COMPULSORY);

                            ChoiceGroup choice = new ChoiceGroup(choice_name, isCompulsory);

                            modifier_title.add(choice);

                            JSONArray jsonArrayMod = jsonObjGroup.getJSONArray(Const.TAG_JsonModObj);

                            ArrayList<Modifier> arrayListMod = new ArrayList<Modifier>();

                            for (int j = 0; j < jsonArrayMod.length(); j++) {
                                JSONObject jsonObjSubCat = jsonArrayMod.getJSONObject(j);

                                String mod_name = jsonObjSubCat.getString(Const.TAG_NAME);
                                String mod_price = jsonObjSubCat.getString(Const.TAG_PRICE);

                                Modifier modifier = new Modifier(mod_name, mod_price, "");

                                arrayListMod.add(modifier);


                            }

                            hashMapModifiers.put(i, arrayListMod);
                        }
                    }

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ProgresBar.stop();
            setLayout();
            inflateQtyLayout();
        }
    }


}
