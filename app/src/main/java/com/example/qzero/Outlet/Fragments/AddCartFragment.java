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
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.CommonFiles.RequestResponse.JsonParser;
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

    int status;
    int jsonLength;
    int pos = 0;

    int countLength = 1;
    int index;

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

    Double afterDiscPrice;
    Double totPrice = 0.00;

    Double totalPrices[];

    ArrayList<ChoiceGroup> modifier_title;

    HashMap<Integer, ArrayList<Modifier>> hashMapModifiers;

    HashMap<Integer, ArrayList<Modifier>> hashMapChoosenMod;

    HashMap<Integer, HashMap<String, String>> arrayListViewData;

    ArrayList<Modifier> modifierList;

    ArrayList<Modifier> choosenModList;

    Dialog dialog;

    LinearLayout linLayModifiers;

    TextView txtViewTitle;
    TextView txtViewCancel;
    TextView txtViewOk;


    TextView tableTotPrice[];

    CheckBox checkBox[];
    RadioGroup radioGroup[];
    RadioButton radioButton[];

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

        choosenModList = new ArrayList<Modifier>();

        arrayListViewData = new HashMap<Integer, HashMap<String, String>>();

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

        Toast.makeText(getActivity(), "Item Added", Toast.LENGTH_SHORT).show();
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

            ImageView imgViewSub = (ImageView) view[i].findViewById(R.id.imgViewSub);
            ImageView imgViewAdd = (ImageView) view[i].findViewById(R.id.imgViewAdd);

            TableLayout tableLayoutModifiers = (TableLayout) view[i].findViewById(R.id.tableLayoutModifiers);

            //set font
            FontHelper.setFontFace(txtViewAddModifiers, FontHelper.FontType.FONT, getActivity());
            FontHelper.setFontFace(txtViewQty, FontHelper.FontType.FONT, getActivity());
            FontHelper.setFontFace(txtViewPrice, FontHelper.FontType.FONT, getActivity());

            //set tag
            txtViewAddModifiers.setTag(i);

            imgViewSub.setTag(i);

            imgViewAdd.setTag(i);

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

                    int tag = Integer.parseInt(v.getTag().toString());

                    //Remove the tag clicked
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

                        BuildTable(tableLayoutModifiers, txtViewModList,txtViewAddModifiers,i);
                    }
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

        if (modifier_title.get(i).getIsComplusory()) {
            checkBox[i].setChecked(true);
        }

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
                            //do nothing
                        } else {
                            String mod_price = modifierList.get(i).getMod_price();
                            Modifier modifier = new Modifier(radioChoice, mod_price, false, choice);
                            choosenModList.add(modifier);
                        }

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

                TextView txtViewAddModifiers=(TextView) view[index].findViewById(R.id.txtViewAddModifiers);

                BuildTable(tableLayoutMod, txtViewModL,txtViewAddModifiers, index);

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

    private void BuildTable(TableLayout tableLayoutModifiers, TextView txtViewModList,TextView txtViewAddModifiers,int pos) {

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

            FontHelper.setFontFace(txtViewModList, FontHelper.FontType.FONTSANSBOLD, getActivity());
            // outer for loop
            for (int i = 1; i <= newArrayList.size(); i++) {

                TableRow row = new TableRow(getActivity());
                row.setPadding(10, 10, 10, 10);

                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        0,1f));


                //Create textview for modifers name

                TextView txtViewName = new TextView(getActivity());

                txtViewName.setLayoutParams(new TableRow.LayoutParams(0,
                        TableRow.LayoutParams.WRAP_CONTENT,1f));

                txtViewName.setText(newArrayList.get(i - 1).getMod_name());
                txtViewName.setTextColor(Color.parseColor("#000000"));
                txtViewName.setTextSize(R.dimen.text_table_mod);
                txtViewName.setGravity(Gravity.LEFT);

                row.addView(txtViewName);

                //Create textview for modifers price

                TextView txtViewModPrice = new TextView(getActivity());

                txtViewModPrice.setLayoutParams(new TableRow.LayoutParams(0,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f));

                String modifierPrice = Utility.formatDecimalByString(newArrayList.get(i - 1).getMod_price());

                txtViewModPrice.setText("$" + modifierPrice);
                txtViewModPrice.setTextSize(R.dimen.text_table_mod);
                txtViewModPrice.setGravity(Gravity.CENTER);
                txtViewModPrice.setPadding(40, 0, 0, 0);
                txtViewModPrice.setTextColor(Color.parseColor("#000000"));

                row.addView(txtViewModPrice);

                tableLayoutModifiers.addView(row);

                //set fonts of table layout
                FontHelper.setFontFace(txtViewName, FontHelper.FontType.FONT, getActivity());
                FontHelper.setFontFace(txtViewModPrice, FontHelper.FontType.FONT, getActivity());

                if (i == newArrayList.size()) {

                    createTotalRow(tableLayoutModifiers, pos);

                    Double modPrice = Double.parseDouble(newArrayList.get(i - 1).getMod_price());

                    sendDataToHashMap(pos, modPrice);
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
        txtViewTotal.setTextSize(R.dimen.text_table_mod);
        txtViewTotal.setTextColor(Color.parseColor("#000000"));
        txtViewTotal.setGravity(Gravity.LEFT);

        rowTotal.addView(txtViewTotal);

        //Create textview for modifers price

        tableTotPrice[pos] = new TextView(getActivity());

        tableTotPrice[pos].setLayoutParams(new TableRow.LayoutParams(0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));

        tableTotPrice[pos].setGravity(Gravity.CENTER);
        tableTotPrice[pos].setPadding(40, 0, 0, 0);
        tableTotPrice[pos].setTextSize(R.dimen.text_table_mod);
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
