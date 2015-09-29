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
import com.example.qzero.CommonFiles.Common.Utility;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddCartFragment extends Fragment {

    @InjectView(R.id.txtViewItemName)
    TextView txtViewItemName;

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

    TextView txtViewAddModifiers;

    TextView tableTotPrice;

    CheckBox checkBox[];
    RadioGroup radioGroup[];
    RadioButton radioButton[];

    ArrayList<Modifier> modifierList;

    ArrayList<Modifier> choosenModList;

    int status;
    int jsonLength;
    int pos = 0;

    String message;

    JsonParser jsonParser;
    JSONObject jsonObject;

    String venue_id;
    String itemId;
    String outletId;
    String subCatId;

    String item_name;
    String item_desc;
    String item_price;
    String item_image;

    String price;

    ArrayList<ChoiceGroup> modifier_title;

    HashMap<Integer, ArrayList<Modifier>> hashMapModifiers;

    HashMap<Integer, ArrayList<Modifier>> hashMapChoosenMod;

    HashMap<Integer, HashMap<String, String>> arrayListViewData;

    String discountDesc;
    Double afterDiscPrice;

    int countLength = 1;

    int index;

    String choice;

    View[] view;

    Double totPrice = 0.00;

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

        choosenModList = new ArrayList<Modifier>();

        arrayListViewData = new HashMap<Integer, HashMap<String, String>>();

    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewItemName, FontHelper.FontType.FONT, getActivity());
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


    private void initalizeArrayItem(int position, String qty, String price) {
        HashMap<String, String> hashmap = new HashMap<String, String>();
        hashmap.put("qty", qty);
        hashmap.put("price", price);

        arrayListViewData.put(position, hashmap);
    }

    @OnClick(R.id.txtViewAddItem)
    void addItem() {

        //increase the array size
        countLength++;

        initalizeArrayItem(countLength - 1, "1", price);

        //Inflate the layout reverse
        inflateQtyLayout();
    }


    private void inflateQtyLayout() {

        relLayItems.removeAllViews();//clear layout

        view = new View[countLength];

        for (int i = countLength - 1; i >= 0; i--) {

            Log.e("i", "" + i);
            view[i] = getActivity().getLayoutInflater().inflate(R.layout.list_addcart, null);

            relLayItems.addView(view[i]);


            txtViewAddModifiers = (TextView) view[i].findViewById(R.id.txtViewAddModifiers);
            final TextView txtViewQty = (TextView) view[i].findViewById(R.id.txtViewQty);
            TextView txtViewPrice = (TextView) view[i].findViewById(R.id.txtViewPrice);
            TextView txtViewModList = (TextView) view[i].findViewById(R.id.txtViewModList);
            ImageView imgViewSub = (ImageView) view[i].findViewById(R.id.imgViewSub);
            ImageView imgViewAdd = (ImageView) view[i].findViewById(R.id.imgViewAdd);

            TableLayout tableLayoutModifiers = (TableLayout) view[i].findViewById(R.id.tableLayoutModifiers);


            txtViewAddModifiers.setTag(i);


            HashMap<String, String> hashmap = arrayListViewData.get(i);

            txtViewQty.setText(hashmap.get("qty"));


            txtViewPrice.setText("$" + hashmap.get("price"));

            totPrice = Double.parseDouble(hashmap.get("price"));

            //Decrease item count on click of subtract button


            imgViewSub.setTag(i);

            imgViewSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int tag = Integer.parseInt(v.getTag().toString());

                    int qty = Integer.parseInt(txtViewQty.getText().toString());

                    if (qty == 1) {
                        //do nothing
                    } else {
                        qty--;

                        txtViewQty.setText(String.valueOf(qty));
                    }

                    initalizeArrayItem(tag, String.valueOf(qty), price);

                }
            });


            //Increase item count on click of subtract button


            imgViewAdd.setTag(i);

            imgViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int tag = Integer.parseInt(v.getTag().toString());

                    int qty = Integer.parseInt(txtViewQty.getText().toString());


                    qty++;

                    txtViewQty.setText(String.valueOf(qty));

                    initalizeArrayItem(tag, String.valueOf(qty), price);
                }


            });

            //find id of delete button
            ImageView imgViewDelete = (ImageView) view[i].findViewById(R.id.imgViewDelete);

            //set tag to delete button

            imgViewDelete.setTag(i);

            //Delete view on click
            imgViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int tag = Integer.parseInt(v.getTag().toString());

                    Log.e("tag", "" + tag);
                    view[tag].setVisibility(View.GONE);
                    hashMapChoosenMod.remove(tag);
                    countLength--;
                }
            });


            txtViewAddModifiers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    index = Integer.parseInt(v.getTag().toString());
                    Log.e("count", "" + relLayItems.getChildCount());
                    Log.e("index", "" + index);
                    openDialog();
                }
            });


            if (hashMapChoosenMod.size() != 0) {
                if (hashMapChoosenMod.containsKey(i)) {
                    choosenModList = hashMapChoosenMod.get(i);

                    if (choosenModList.size() == 0) {
                        //do nothing
                    } else {

                        BuildTable(tableLayoutModifiers, txtViewModList, i);
                    }
                }
            }


        }
    }

    //open dialog box for modifiers
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

        if (modifier_title.get(i).getIsComplusory()) {
            checkBox[i].setChecked(true);
        }

        FontHelper.setFontFace(checkBox[i], FontHelper.FontType.FONT, getActivity());
        checkBox[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!modifier_title.get(i).getIsComplusory()) {
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

                        String radioChoice = radioBtn.getText().toString();

                        if (choosenModList.contains(radioChoice)) {
                            //do nohting
                        } else {
                            String mod_price = modifierList.get(i).getMod_price();
                            Modifier modifier = new Modifier(radioChoice, mod_price, false, choice);
                            choosenModList.add(modifier);
                        }


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

        if (modifierList.get(j).getIsDefault()) {
            radioButton[j].setChecked(true);
        }

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

                hashMapChoosenMod.put(index, choosenModList);

                TextView txtViewModL = (TextView) view[index].findViewById(R.id.txtViewModList);

                TableLayout tableLayoutMod = (TableLayout) view[index].findViewById(R.id.tableLayoutModifiers);

                BuildTable(tableLayoutMod, txtViewModL, index);

                dialog.dismiss();
            }
        });

    }


    public void setLayout() {

        String formattedPrice = Utility.formatDecimalByString(item_price);

        String formatDiscPrice = Utility.formatDecimalByString(String.valueOf(afterDiscPrice));

        txtViewItemName.setText(item_name);

        if (item_desc.equals("null") || item_desc == null) {

        } else
            txtViewDesc.setText(item_desc);


        if (afterDiscPrice == 0.0) {
            txtViewDiscPrice.setText("$" + formattedPrice);
            txtViewOrigPrice.setVisibility(View.GONE);
            txtViewTitDisc.setVisibility(View.INVISIBLE);
            price = formattedPrice;


        } else {
            txtViewOrigPrice.setText("$" + formattedPrice);
            txtViewDiscount.setText(discountDesc);
            txtViewDiscPrice.setText("$" + formatDiscPrice);

            price = formatDiscPrice;
        }

        totPrice = totPrice + Double.parseDouble(price);

        //Load Image
        Picasso.with(getActivity()).load(item_image).error(R.drawable.q2x).into(imgViewItem);

        initalizeArrayItem(0, "1", price);

    }

    private void BuildTable(TableLayout tableLayoutModifiers, TextView txtViewModList, int pos) {

        //Find the id's of total and price of current view added
        TextView txtViewTotal = (TextView) view[pos].findViewById(R.id.txtViewTotal);
        TextView txtViewPrice = (TextView) view[pos].findViewById(R.id.txtViewPrice);

        txtViewPrice.setVisibility(View.GONE);
        txtViewTotal.setVisibility(View.GONE);

        txtViewAddModifiers.setText("Edit Modifier");


        tableLayoutModifiers.removeAllViews();

        Boolean isDuplicate = false;

        choosenModList = hashMapChoosenMod.get(pos);

        ArrayList<Modifier> newArrayList = new ArrayList<>();


        for (int i = 0; i < choosenModList.size(); i++) {
            for (int j = 0; j < newArrayList.size(); j++) {
                if (choosenModList.get(i).getMod_name().equals(newArrayList.get(j).getMod_name())) {
                    isDuplicate = true;
                    break;
                } else {
                    isDuplicate = false;
                }
            }

            if (!isDuplicate) {
                newArrayList.add(choosenModList.get(i));
            }
        }

        if (newArrayList.size() != 0) {


            txtViewModList.setVisibility(View.VISIBLE);

            FontHelper.setFontFace(txtViewModList, FontHelper.FontType.FONTROBOLD, getActivity());
            // outer for loop
            for (int i = 1; i <= newArrayList.size(); i++) {

                TableRow row = new TableRow(getActivity());
                row.setPadding(10, 10, 10, 10);

                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));


                //Create textview for modifers name

                TextView txtViewName = new TextView(getActivity());

                txtViewName.setText(newArrayList.get(i - 1).getMod_name());
                txtViewName.setTextColor(Color.parseColor("#000000"));
                txtViewName.setGravity(Gravity.LEFT);

                row.addView(txtViewName);

                //Create textview for modifers price

                TextView txtViewModPrice = new TextView(getActivity());

                String modifierPrice = Utility.formatDecimalByString(newArrayList.get(i - 1).getMod_price());

                txtViewModPrice.setText("$" + modifierPrice);

                txtViewModPrice.setGravity(Gravity.CENTER);
                txtViewModPrice.setPadding(40, 0, 0, 0);
                txtViewModPrice.setTextColor(Color.parseColor("#000000"));

                row.addView(txtViewModPrice);

                tableLayoutModifiers.addView(row);

                if (i == newArrayList.size()) {

                    createTotalRow(tableLayoutModifiers);

                    Double modPrice = Double.parseDouble(newArrayList.get(i - 1).getMod_price());

                    sendDataToHashMap(pos, modPrice);
                }

            }
        }
    }


    private void createTotalRow(TableLayout tableLayoutModifiers) {
        TableRow rowTotal = new TableRow(getActivity());
        rowTotal.setPadding(10, 10, 10, 10);

        rowTotal.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


        //Create textview for modifers name

        TextView txtViewTotal = new TextView(getActivity());

        txtViewTotal.setText("Total");
        txtViewTotal.setTextColor(Color.parseColor("#000000"));
        txtViewTotal.setGravity(Gravity.LEFT);

        rowTotal.addView(txtViewTotal);

        //Create textview for modifers price

        tableTotPrice = new TextView(getActivity());
        tableTotPrice.setGravity(Gravity.CENTER);
        tableTotPrice.setPadding(40, 0, 0, 0);
        tableTotPrice.setTextColor(Color.parseColor("#000000"));

        tableTotPrice.setText("$" + totPrice);

        rowTotal.addView(tableTotPrice);

        tableLayoutModifiers.addView(rowTotal);
    }

    public void sendDataToHashMap(int pos, Double modPrice) {


        TextView txtViewQty = (TextView) view[pos].findViewById(R.id.txtViewQty);

        String tot_price = tableTotPrice.getText().toString();

        int priceLen = tot_price.length();

        String substrPrice = tot_price.substring(1, priceLen);

        Log.e("substr", substrPrice);

        Double totalPrice = Double.parseDouble(substrPrice);

        String qty = txtViewQty.getText().toString();

        totalPrice = (totalPrice + modPrice)*Integer.parseInt(qty);

        tableTotPrice.setText("$" + Utility.formatDecimalByString(String.valueOf(totalPrice)));

        String total_Price = Utility.formatDecimalByString(String.valueOf(totalPrice));

        initalizeArrayItem(pos, qty, total_Price);
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

                                Boolean isDefault = jsonObjSubCat.getBoolean(Const.TAG_IS_DEFAULT);
                                Modifier modifier = new Modifier(mod_name, mod_price, isDefault, "");

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
