package com.example.qzero.Outlet.Fragments;


import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzero.CommonFiles.Common.ProgresBar;
import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.AlertDialogHelper;
import com.example.qzero.CommonFiles.Helpers.CheckInternetHelper;
import com.example.qzero.CommonFiles.Helpers.DatabaseHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.GetCartCountHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
import com.example.qzero.Outlet.Activities.OutletCategoryActivity;
import com.example.qzero.Outlet.ObjectClasses.ChoiceGroup;
import com.example.qzero.Outlet.ObjectClasses.Modifier;
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

    @InjectView(R.id.txtViewDesc)
    TextView txtViewDesc;

    @InjectView(R.id.txtViewAddItem)
    TextView txtViewAddItem;

    @InjectView(R.id.txtViewPriceTit)
    TextView txtViewPriceTit;

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

    @InjectView(R.id.btnAddToCart)
    Button btnAddToCart;

    int status;
    int jsonLength;
    int pos = 0;

    int countLength = 1;
    int index;
    int quantity;

    JsonParser jsonParser;
    JSONObject jsonObject;

    String message;

    String venue_id;
    String itemId;
    String outletId;
    String subCatId;

    String item_name;
    String item_desc;
    String item_price;
    String item_image;

    String price;
    String choice;
    String discountDesc;

    Boolean isDuplicate = false;

    Double afterDiscPrice;
    Double totPrice = 0.00;

    Double totalPrices[];

    HashMap<Integer, ArrayList<Modifier>> hashMapModifiers;

    HashMap<Integer, ArrayList<Modifier>> hashMapChoosenMod;

    HashMap<Integer, ArrayList<Modifier>> hashMapDefaultMod;

    HashMap<Integer, HashMap<String, String>> arrayListViewData;

    HashMap<Integer, HashMap<Integer, String>> hashMapSelectedMod;

    HashMap<Integer, ArrayList<Modifier>> hashMaUniqueMod;

    HashMap<Integer, String> hashMap;

    ArrayList<Modifier> modifierList;

    ArrayList<Modifier> choosenModList;

    ArrayList<ChoiceGroup> modifier_title;

    ArrayList<Modifier> modifierSaved;

    Dialog dialog;

    LinearLayout linLayModifiers;

    TextView txtViewTitle;
    TextView txtViewCancel;
    TextView txtViewOk;

    TextView tableTotPrice[];

    CheckBox checkBox[];

    RadioGroup radioGroup[];
    RadioButton radioButton[];

    RadioButton radioBtn;

    View[] view;

    CategoryItemFragment categoryItemFragment;

    //Reference of DatabaseHelper class to access its components
    private DatabaseHelper databaseHelper = null;


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

        databaseHelper = new DatabaseHelper(getActivity());

        setFonts();

        getArgData();

        getItemDetails();

        //initialization of hashmap for the modifiers choosen by user
        hashMapChoosenMod = new HashMap<Integer, ArrayList<Modifier>>();

        //contains data for each view
        arrayListViewData = new HashMap<Integer, HashMap<String, String>>();

        //contains the modifers that are to be diaplay as selected on opening dialog
        hashMapSelectedMod = new HashMap<>();

    }

    private void setFonts() {
        FontHelper.setFontFace(txtViewItemName, FontHelper.FontType.FONTSANSBOLD, getActivity());
        FontHelper.setFontFace(txtViewDesc, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewOrigPrice, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewDiscount, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewTitDisc, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewDiscPrice, FontHelper.FontType.FONT, getActivity());
        FontHelper.setFontFace(txtViewPriceTit, FontHelper.FontType.FONT, getActivity());

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


    private void initalizeArrayItem(int position, String qty) {

        //store individuals items for each view according to position

        HashMap<String, String> hashmap = new HashMap<String, String>();
        hashmap.put("qty", qty);

        arrayListViewData.put(position, hashmap);
    }

    @OnClick(R.id.txtViewAddItem)
    void addItem() {

        //increase the array size
        countLength++;

        initalizeArrayItem(countLength - 1, "1");

        //Inflate the layout reverse
        inflateQtyLayout();

        Toast.makeText(getActivity(), "Item Added.", Toast.LENGTH_SHORT).show();
    }


    private void inflateQtyLayout() {

        relLayItems.removeAllViews();//clear add item layout

        view = new View[countLength];

        tableTotPrice = new TextView[countLength];

        totalPrices = new Double[countLength];

        for (int i = countLength - 1; i >= 0; i--) {

            view[i] = getActivity().getLayoutInflater().inflate(R.layout.list_addcart, null);

            relLayItems.addView(view[i]); //inflate relative layout with custom layout for add items

            //find id's of the widgets of inflated view
            TextView txtViewAddModifiers = (TextView) view[i].findViewById(R.id.txtViewAddModifiers);
            final TextView txtViewQty = (TextView) view[i].findViewById(R.id.txtViewQty);
            final TextView txtViewPrice = (TextView) view[i].findViewById(R.id.txtViewPrice);
            TextView txtViewModList = (TextView) view[i].findViewById(R.id.txtViewModList);
            TextView txtViewTotal = (TextView) view[i].findViewById(R.id.txtViewTotal);

            ImageView imgViewSub = (ImageView) view[i].findViewById(R.id.imgViewSub);
            ImageView imgViewAdd = (ImageView) view[i].findViewById(R.id.imgViewAdd);

            TableLayout tableLayoutModifiers = (TableLayout) view[i].findViewById(R.id.tableLayoutModifiers);

            //set font
            FontHelper.setFontFace(txtViewTotal, FontHelper.FontType.FONTSANSBOLD, getActivity());
            FontHelper.setFontFace(txtViewAddModifiers, FontHelper.FontType.FONT, getActivity());
            FontHelper.setFontFace(txtViewQty, FontHelper.FontType.FONT, getActivity());
            FontHelper.setFontFace(txtViewPrice, FontHelper.FontType.FONT, getActivity());

            //set tag
            txtViewAddModifiers.setTag(i);

            imgViewSub.setTag(i);

            imgViewAdd.setTag(i);


            if(hashMapModifiers.size()==0)
            {
                txtViewAddModifiers.setVisibility(View.INVISIBLE);
            }
            //Hashmap for the data to be inflated in the view
            HashMap<String, String> hashmap = arrayListViewData.get(i);

            txtViewQty.setText(hashmap.get("qty"));

            //set price of total amount textview shown initially
            txtViewPrice.setText("$" + Utility.formatDecimalByString(String.valueOf(totPrice)));

            //Decrease item count on click of subtract button

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

                        onChangeSetPrice(qty, txtViewPrice, tag);
                    }

                    //Intialize the hashmap for the values to be inflated in the view
                    initalizeArrayItem(tag, String.valueOf(qty));

                }
            });


            //Increase item count on click of subtract button
            imgViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int tag = Integer.parseInt(v.getTag().toString());

                    int qty = Integer.parseInt(txtViewQty.getText().toString());


                    qty++;

                    onChangeSetPrice(qty, txtViewPrice, tag);

                    txtViewQty.setText(String.valueOf(qty));

                    //Intialize the hashmap for the values to be inflated in the view
                    initalizeArrayItem(tag, String.valueOf(qty));
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

                    if (countLength == 1) {

                        //do not delete
                        Toast.makeText(getActivity(), "This item cannot be deleted.", Toast.LENGTH_SHORT).show();
                    } else {
                        int tag = Integer.parseInt(v.getTag().toString());

                        //Remove the tag clicked
                        view[tag].setVisibility(View.GONE);

                        hashMapChoosenMod.remove(tag);
                        countLength--;
                    }
                }
            });



            txtViewAddModifiers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    index = Integer.parseInt(v.getTag().toString());

                    openDialog();


                }
            });

            if (hashMapChoosenMod.size() != 0 && hashMapChoosenMod.containsKey(i)) { //check if a user has already selected the modifiers

                ArrayList<Modifier> choosenModListTable = hashMapChoosenMod.get(i);
                BuildTable(tableLayoutModifiers, txtViewModList, txtViewAddModifiers, choosenModListTable, i);

            } else if (hashMapDefaultMod.size() != 0) { //if no modifier has been selected and default mofifiers present
                ArrayList<Modifier> defaultModList = hashMapDefaultMod.get(0);

                if (defaultModList.size() == 0) {
                    //do nothing
                } else {

                    BuildTable(tableLayoutModifiers, txtViewModList, txtViewAddModifiers, defaultModList, i);
                }
            }
        }
    }


    private void onChangeSetPrice(int qty, TextView txtViewPrice, int tag) {
        //if the initial total price textview present
        if (txtViewPrice.getVisibility() == View.VISIBLE) {

            Double newPrice = totPrice * qty;

            txtViewPrice.setText("$" + Utility.formatDecimalByString(String.valueOf(newPrice)));
        } else {

            //change the amount of the total prive textview of table layout
            Double newPrice = totalPrices[tag] * qty;

            tableTotPrice[tag].setText("$" + Utility.formatDecimalByString(String.valueOf(newPrice)));
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

        choosenModList = new ArrayList<Modifier>();//Arraylist for modifiers choosen by the user hashMapChoosenModifiers<>

        checkBox = new CheckBox[modifier_title.size()];

        radioGroup = new RadioGroup[modifier_title.size()];

        hashMap = new HashMap<Integer, String>(); //hashmap for showing modifier selected

        for (int i = 0; i < modifier_title.size(); i++) {

            createCheckBox(i);

            modifierList = new ArrayList<Modifier>();//arrylist for modifiers present

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

                    try {
                        int radioButtonID = radioGroup[i].getCheckedRadioButtonId();

                        radioBtn = (RadioButton) radioGroup[i].findViewById(radioButtonID);

                        if (checkBox[i].isChecked()) {
                            //do nothing
                        } else {
                            checkBox[i].setChecked(true);
                        }

                        if (checkBox[i].isChecked()) {

                            String radioChoice = radioBtn.getText().toString();

                            int tag = Integer.parseInt(radioBtn.getTag().toString());

                            hashMap.put(i, radioChoice);
                        } else {
                            hashMap.remove(i);
                            choosenModList.remove(i);
                        }


                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }


                } else {
                    checkBox[i].setChecked(false);
                    hashMap.remove(i);
                }

            }
        });


        //Add radiogroup to linear layout
        linLayModifiers.addView(radioGroup[i]);
    }

    private void createRadioButton(final int i, final int j) {

        //Create radio buttons
        radioButton[j] = new RadioButton(getActivity());
        radioButton[j].setText(modifierList.get(j).getMod_name());
        radioButton[j].setTextColor(Color.parseColor("#000000"));
        radioButton[j].setId(j);
        radioButton[j].setTag(R.string.key_posmod, j);
        radioButton[j].setTag(R.string.key_modid, modifierList.get(j).getMod_id());


        if (modifier_title.get(i).getIsComplusory()) {//if choice is compulsory
            checkBox[i].setChecked(true);
            checkBox[i].setEnabled(false);

            radioButton[0].setChecked(true);// check the first radiobutton automatically
        }

        if (hashMapSelectedMod.containsKey(index)) { //if a user has manually selected the modifier

            hashMap = hashMapSelectedMod.get(index);

            if (hashMap.size() != 0) {

                if (hashMap.containsKey(i)) {
                    if (modifierList.get(j).getMod_name().equals(hashMap.get(i))) {
                        // isDefault = true;
                        radioButton[j].setChecked(true);
                    }
                }
            }
        } else {
            if (modifierList.get(j).getIsDefault()) {//else show the default modifier if present
                radioButton[j].setChecked(true);
            }
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

                for (int i = 0; i < radioGroup.length; i++) {//get alll the selected radiobuttons

                    if (radioGroup[i].getCheckedRadioButtonId() != -1) {

                        int id = radioGroup[i].getCheckedRadioButtonId();
                        View radioButton = radioGroup[i].findViewById(id);
                        int radioId = radioGroup[i].indexOfChild(radioButton);
                        RadioButton btn = (RadioButton) radioGroup[i].getChildAt(radioId);
                        String selection = (String) btn.getText();

                        int tag = Integer.parseInt(btn.getTag(R.string.key_posmod).toString());

                        String mod_id = btn.getTag(R.string.key_modid).toString();

                        modifierList = hashMapModifiers.get(i);

                        String mod_price = modifierList.get(tag).getMod_price();
                        Modifier modifier = new Modifier(mod_id, selection, mod_price, true, choice);
                        choosenModList.add(modifier);
                    }
                }

                hashMapChoosenMod.put(index, choosenModList);

                hashMapSelectedMod.put(index, hashMap);

                TextView txtViewModL = (TextView) view[index].findViewById(R.id.txtViewModList);

                TableLayout tableLayoutMod = (TableLayout) view[index].findViewById(R.id.tableLayoutModifiers);

                TextView txtViewAddModifiers = (TextView) view[index].findViewById(R.id.txtViewAddModifiers);

                BuildTable(tableLayoutMod, txtViewModL, txtViewAddModifiers, choosenModList, index);

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
            txtViewDiscPrice.setTextColor(Color.parseColor("#742314"));
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
        Picasso.with(getActivity()).load(item_image).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder).into(imgViewItem);

        initalizeArrayItem(0, "1");

    }

    private void BuildTable(TableLayout tableLayoutModifiers, TextView txtViewModList, TextView txtViewAddModifiers, ArrayList<Modifier> choosenModifiers, int pos) {


        //Find the id's of total and price of current view added
        TextView txtViewTotal = (TextView) view[pos].findViewById(R.id.txtViewTotal);
        TextView txtViewPrice = (TextView) view[pos].findViewById(R.id.txtViewPrice);

        if (choosenModifiers.size() == 0) {
            txtViewModList.setVisibility(View.GONE);
            txtViewPrice.setVisibility(View.VISIBLE);
            txtViewTotal.setVisibility(View.VISIBLE);
        }


        txtViewAddModifiers.setText("Edit Modifier");


        tableLayoutModifiers.removeAllViews();

        Boolean isDuplicate = false;

        ArrayList<Modifier> newArrayList = new ArrayList<>();


       /* for (int i = 0; i < choosenModifiers.size(); i++) {//remove duplicate elements from arraylist
            for (int j = 0; j < newArrayList.size(); j++) {
                if (choosenModifiers.get(i).getMod_name().equals(newArrayList.get(j).getMod_name())) {
                    isDuplicate = true;
                    break;
                } else {
                    isDuplicate = false;
                }
            }

            if (!isDuplicate) {
                newArrayList.add(choosenModifiers.get(i));
            }
        }*///code commented by neha on 8 oct 2015

        if (choosenModifiers.size() != 0) {

            Double totModPrice = 0.0;

            txtViewModList.setVisibility(View.VISIBLE);
            txtViewPrice.setVisibility(View.GONE);
            txtViewTotal.setVisibility(View.GONE);

            FontHelper.setFontFace(txtViewModList, FontHelper.FontType.FONTSANSBOLD, getActivity());
            // outer for loop
            for (int i = 1; i <= choosenModifiers.size(); i++) {

                TableRow row = new TableRow(getActivity());
                row.setPadding(10, 10, 10, 10);

                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        0, 1f));


                //Create textview for modifers name

                TextView txtViewName = new TextView(getActivity());

                txtViewName.setLayoutParams(new TableRow.LayoutParams(0,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f));
                txtViewName.setTextSize(16);
                txtViewName.setText(choosenModifiers.get(i - 1).getMod_name());
                txtViewName.setTextColor(Color.parseColor("#000000"));
                txtViewName.setGravity(Gravity.LEFT);

                row.addView(txtViewName);

                //Create textview for modifers price

                TextView txtViewModPrice = new TextView(getActivity());

                txtViewModPrice.setLayoutParams(new TableRow.LayoutParams(0,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f));

                String modifierPrice = Utility.formatDecimalByString(choosenModifiers.get(i - 1).getMod_price());

                txtViewModPrice.setText("$" + modifierPrice);
                txtViewModPrice.setTextSize(16);
                txtViewModPrice.setGravity(Gravity.CENTER);
                txtViewModPrice.setPadding(40, 0, 0, 0);
                txtViewModPrice.setTextColor(Color.parseColor("#000000"));

                row.addView(txtViewModPrice);

                tableLayoutModifiers.addView(row);

                //set fonts of table layout
                FontHelper.setFontFace(txtViewName, FontHelper.FontType.FONT, getActivity());
                FontHelper.setFontFace(txtViewModPrice, FontHelper.FontType.FONT, getActivity());

                Double modPrice = Double.parseDouble(choosenModifiers.get(i - 1).getMod_price());

                totModPrice = totModPrice + modPrice;

                if (i == choosenModifiers.size()) {

                    createTotalRow(tableLayoutModifiers, pos);
                    sendDataToHashMap(pos, totModPrice);
                }

            }
        }
    }


    private void createTotalRow(TableLayout tableLayoutModifiers, int pos) {
        TableRow rowTotal = new TableRow(getActivity());
        rowTotal.setPadding(10, 10, 10, 10);

        rowTotal.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


        //Create textview for modifers name

        TextView txtViewTotal = new TextView(getActivity());

        txtViewTotal.setLayoutParams(new TableRow.LayoutParams(0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));

        txtViewTotal.setText("Total");
        txtViewTotal.setTextSize(18);
        txtViewTotal.setTextColor(Color.parseColor("#000000"));
        txtViewTotal.setGravity(Gravity.LEFT);

        rowTotal.addView(txtViewTotal);

        //Create textview for modifers price

        tableTotPrice[pos] = new TextView(getActivity());

        tableTotPrice[pos].setLayoutParams(new TableRow.LayoutParams(0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tableTotPrice[pos].setTextSize(16);
        tableTotPrice[pos].setGravity(Gravity.CENTER);
        tableTotPrice[pos].setPadding(40, 0, 0, 0);
        tableTotPrice[pos].setTextColor(Color.parseColor("#000000"));

        tableTotPrice[pos].setText("$" + totPrice);

        rowTotal.addView(tableTotPrice[pos]);

        tableLayoutModifiers.addView(rowTotal);

        //set font of total row in table layout
        FontHelper.setFontFace(txtViewTotal, FontHelper.FontType.FONTSANSBOLD, getActivity());
        FontHelper.setFontFace(tableTotPrice[pos], FontHelper.FontType.FONT, getActivity());
    }

    public void sendDataToHashMap(int pos, Double modPrice) {

        Double totalPrice;
        TextView txtViewQty = (TextView) view[pos].findViewById(R.id.txtViewQty);

        String qty = txtViewQty.getText().toString();

        totalPrices[pos] = totPrice + modPrice;

        totalPrice = (totPrice + modPrice) * Integer.parseInt(qty);

        String total_Price = Utility.formatDecimalByString(String.valueOf(totalPrice));

        tableTotPrice[pos].setText("$" + total_Price);


        initalizeArrayItem(pos, qty);
    }

    private class GetItemDetail extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgresBar.start(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {

            status = -1;

            jsonParser = new JsonParser();

            String url = Const.BASE_URL + Const.GET_ITEM_DETAIL + "/" + venue_id + "?outletId=" + outletId + "&itemId=" + itemId
                    + "&subCatId=" + subCatId;


            String jsonString = jsonParser.getJSONFromUrl(url, Const.TIME_OUT);

            Log.e("jsonitem", jsonString);

            hashMapDefaultMod = new HashMap<Integer, ArrayList<Modifier>>();

            ArrayList<Modifier> modListDefault = new ArrayList<>();

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

                            String choice_id = jsonObjGroup.getString(Const.TAG_ID);

                            String choice_name = jsonObjGroup.getString(Const.TAG_NAME);

                            Boolean isCompulsory = jsonObjGroup.getBoolean(Const.TAG_IS_COMPULSORY);

                            ChoiceGroup choice = new ChoiceGroup(choice_name, isCompulsory);

                            modifier_title.add(choice);

                            JSONArray jsonArrayMod = jsonObjGroup.getJSONArray(Const.TAG_JsonModObj);

                            ArrayList<Modifier> arrayListMod = new ArrayList<Modifier>();

                            for (int j = 0; j < jsonArrayMod.length(); j++) {
                                JSONObject jsonObjSubCat = jsonArrayMod.getJSONObject(j);

                                String mod_id = jsonObjSubCat.getString(Const.TAG_ID);
                                String mod_name = jsonObjSubCat.getString(Const.TAG_NAME);
                                String mod_price = jsonObjSubCat.getString(Const.TAG_PRICE);

                                Boolean isDefault = jsonObjSubCat.getBoolean(Const.TAG_IS_DEFAULT);
                                Modifier modifier = new Modifier(mod_id, mod_name, mod_price, isDefault, "");

                                arrayListMod.add(modifier);

                                if (isDefault) {
                                    Modifier choosenmod = new Modifier(mod_id, mod_name, mod_price, false, choice_name);
                                    modListDefault.add(choosenmod);
                                }
                            }

                            hashMapModifiers.put(i, arrayListMod);

                        }
                        hashMapDefaultMod.put(0, modListDefault);
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


    @OnClick(R.id.btnAddToCart)
    void addToCart() {

        saveItemDetails();

        getTotalQty();

        saveItemDetailsInCheckout();

        Toast.makeText(getActivity(), "Items added to cart.", Toast.LENGTH_LONG).show();
    }


    private void saveItemDetails() {

        for (int i = 0; i < countLength; i++) {

            HashMap<String, String> hashmap = arrayListViewData.get(i);

            Cursor itemIdCursor = databaseHelper.selectItems(item_name);

            if (itemIdCursor != null) {
                if (itemIdCursor.getCount() != 0) {
                    while (itemIdCursor.moveToNext()) {

                        String item_id = itemIdCursor.getString(0);

                        Log.e("itemk_id", "" + item_id);
                        Log.e("i", "" + i);

                        saveModifierItems(i, item_id, hashmap);
                        Log.e("while", "" + isDuplicate);
                        if (isDuplicate)
                            break;
                    }

                    if (!isDuplicate) {
                        Log.e("not", "duplicate");
                        long itemId = databaseHelper.insertIntoItem(item_name, item_price, String.valueOf(afterDiscPrice), item_image);
                        for (int mod = 0; mod < modifierSaved.size(); mod++) {

                            databaseHelper.insertIntoModifiers(modifierSaved.get(mod).getMod_id(), modifierSaved.get(mod).getMod_name(), modifierSaved.get(mod).getMod_price(), hashmap.get("qty"), String.valueOf(itemId), item_name);
                        }
                    }
                } else {

                    Log.e("inside", "item not present");
                    long item_id = databaseHelper.insertIntoItem(item_name, item_price, String.valueOf(afterDiscPrice), item_image);
                    saveModDb(i, String.valueOf(item_id));
                }
            }
        }

        resetCart();
    }

    private void resetCart() {
        countLength = 1;

        hashMapChoosenMod.clear();
        hashMapSelectedMod.clear();

        arrayListViewData.clear();

        initalizeArrayItem(0, "1");
        inflateQtyLayout();

    }

    private void saveModifierItems(int i, String item_id, HashMap<String, String> hashmap) {

        modifierSaved = new ArrayList<Modifier>();

        int k;

        if (hashMapChoosenMod.containsKey(i)) {
            modifierSaved = hashMapChoosenMod.get(i);
        }
        else if(hashMapDefaultMod.size()!=0)
        {
            modifierSaved = hashMapDefaultMod.get(0);
        }

        if (modifierSaved.size() == 0) {
            isDuplicate = true;
            Cursor nullModCursor = databaseHelper.getNullModifiers(item_name, "null");

            if (nullModCursor.getCount() == 0) {
                long itemId = databaseHelper.insertIntoItem(item_name, item_price, String.valueOf(afterDiscPrice), item_image);
                databaseHelper.insertIntoModifiers("null", "null", "null", hashmap.get("qty"), String.valueOf(itemId), item_name);
            } else {
                while (nullModCursor.moveToNext()) {

                    int qtyIndex = nullModCursor.getColumnIndex(databaseHelper.QUANTITY);
                    String quantity = nullModCursor.getString(qtyIndex);
                    String qty = String.valueOf(Integer.parseInt(quantity) + Integer.parseInt(hashmap.get("qty")));
                    databaseHelper.updateNullModifiers(item_name, "null", qty);
                }

            }
        } else {

            Cursor modCursor = databaseHelper.getModifiers(item_id);
            if (modCursor != null) {
                if (modCursor.getCount() != 0) {

                    Log.e("cursorcount", "" + modCursor.getCount());
                    ArrayList<String> arrayListMod = new ArrayList<>();
                    while (modCursor.moveToNext()) {

                        String mod_name = modCursor.getString(2);

                        Log.e("mod", mod_name);
                        arrayListMod.add(mod_name);
                    }

                    if (modifierSaved.size() == arrayListMod.size()) {
                        for (k = 0; k < modifierSaved.size(); k++) {
                            if (arrayListMod.contains(modifierSaved.get(k).getMod_name())) {
                                isDuplicate = true;
                            } else {
                                isDuplicate = false;
                            }
                        }
                        Log.e("isDup", "" + isDuplicate);
                        if (!isDuplicate) {
                            //do nothing
                        } else {
                            Log.e("upfate", "update");
                            Log.e("item_id", item_id);

                            Cursor modQtyCursor = databaseHelper.getModifiersQty(item_id);
                            if (modQtyCursor != null) {
                                if (modQtyCursor.moveToFirst()) {
                                    String qty = modQtyCursor.getString(0);
                                    String quantity = String.valueOf(Integer.parseInt(qty) + Integer.parseInt(hashmap.get("qty")));
                                    databaseHelper.updateModifiers(item_id, quantity);
                                }
                            }

                        }
                    } else {
                        isDuplicate = false;
                    }
                }

            }
        }
    }

    private void saveModDb(int i, String item_id) {


        ArrayList<Modifier> modifierSavedUnique = new ArrayList<Modifier>();

        if (hashMapChoosenMod.containsKey(i)) {
            modifierSavedUnique = hashMapChoosenMod.get(i);
        }
        else if(hashMapDefaultMod.size()!=0)
        {
            modifierSavedUnique = hashMapDefaultMod.get(i);
        }

        HashMap<String, String> hashmap = arrayListViewData.get(i);
        if (modifierSavedUnique.size() != 0) {
            for (int j = 0; j < modifierSavedUnique.size(); j++) {
                databaseHelper.insertIntoModifiers(modifierSavedUnique.get(j).getMod_id(), modifierSavedUnique.get(j).getMod_name(), modifierSavedUnique.get(j).getMod_price(), hashmap.get("qty"), item_id, item_name);
            }
        } else {
            databaseHelper.insertIntoModifiers("null", "null", "null", hashmap.get("qty"), item_id, item_name);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getTotalQty();
    }

    public void getTotalQty() {
        quantity = GetCartCountHelper.getTotalQty(getActivity());

        ((OutletCategoryActivity) getActivity()).setCountToBadge(String.valueOf(quantity));
    }

    private void saveItemDetailsInCheckout() {
        String discountAmt;

        if(afterDiscPrice==0.0)
        {
            discountAmt="0.0";
        }
        else
        {
            discountAmt = String.valueOf(Double.parseDouble(item_price) - afterDiscPrice);
        }

        databaseHelper.insertIntoCheckout(itemId, outletId, String.valueOf(countLength), discountAmt, String.valueOf(afterDiscPrice));
    }


}