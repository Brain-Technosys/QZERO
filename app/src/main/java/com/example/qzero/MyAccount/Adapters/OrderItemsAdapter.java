package com.example.qzero.MyAccount.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.Outlet.ObjectClasses.Items;
import com.example.qzero.Outlet.ObjectClasses.OrderItems;
import com.example.qzero.Outlet.SlidingUpPanel.SlidingUpPanelLayout;
import com.example.qzero.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class OrderItemsAdapter extends BaseAdapter {

    Context context;
    ArrayList<OrderItems> rowItems;
    LayoutInflater mInflater = null;

    public OrderItemsAdapter(Context context,
                             ArrayList<OrderItems> rowItems) {


        this.context = context;
        this.rowItems = rowItems;
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

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
    public View getView(int position, View convertView, ViewGroup parent) {

        double subTotal = 0;
        double itemPrice = 0;
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_items_order_items, null);
            holder = new ViewHolder();

            holder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            holder.tv_item_status = (TextView) convertView.findViewById(R.id.tv_item_status);
            holder.tv_item_price = (TextView) convertView.findViewById(R.id.tv_item_price);
            holder.tv_quantity = (TextView) convertView.findViewById(R.id.tv_item_qty);

            holder.discountAmountTextView = (TextView) convertView.findViewById(R.id.tv_discountAmount);
            holder.totalAmountTextView = (TextView) convertView.findViewById(R.id.tv_totalAmount);
            holder.netAmountTextView = (TextView) convertView.findViewById(R.id.tv_netAmount);

            holder.itemImageView = (ImageView) convertView.findViewById(R.id.img_item);


            holder.discontAmountLayout = (LinearLayout) convertView.findViewById(R.id.ll_discountAmount);
            holder.totalAmountLayout = (LinearLayout) convertView.findViewById(R.id.ll_totalAmount);
            holder.netAmountLayout = (LinearLayout) convertView.findViewById(R.id.ll_netAmount);
            holder.modifiersLayout = (LinearLayout) convertView.findViewById(R.id.ll_modifiers);
            holder.dividerView = convertView.findViewById(R.id.dividerView1);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

/// Setting values

        OrderItems items = rowItems.get(position);
        holder.totalAmount = Double.valueOf(items.getTotalAmount());

        // Getting modifiers list
        ArrayList<HashMap<String, String>> modifiersList = items.getModifiersList();

        //Load Image
        String item_image = Const.BASE_URL + Const.IMAGE_URL + items.getItemId();
        Picasso.with(context).load(item_image).placeholder(R.drawable.ic_placeholder).into(holder.itemImageView);


        holder.tv_item_name.setText(items.getItemName());
        holder.tv_item_status.setText(items.getItemStatus());

        itemPrice = Double.valueOf(items.getItemPrice()) * Double.valueOf(items.getQuantitiy());
        holder.tv_item_price.setText(Utility.formatCurrency(String.valueOf(itemPrice)));

        holder.tv_quantity.setText(items.getQuantitiy());


        if (holder.modifiersLayout.getChildCount() > 0)
            holder.modifiersLayout.removeAllViews();
        // If modifier(s) exist with product
        if (modifiersList != null) {
            holder.modifiersLayout.setVisibility(View.VISIBLE);
            holder.dividerView.setVisibility(View.VISIBLE);
            // Calculating
            subTotal = (Double.valueOf(items.getItemPrice()) + drawModifiers(holder.modifiersLayout, modifiersList, items, holder)) * Double.valueOf(items.getQuantitiy());
        } else {

            // Calculating
            subTotal = Double.valueOf(items.getItemPrice()) * Double.valueOf(items.getQuantitiy());
            holder.modifiersLayout.setVisibility(View.GONE);
            holder.dividerView.setVisibility(View.GONE);
        }

        if (Double.valueOf(items.getDiscountAmount()) > 0.0) {
            holder.discountAmountTextView.setText(Utility.formatCurrency(items.getDiscountAmount()));
            //holder.totalAmountTextView.setText(Utility.formatCurrency(items.getTotalAmount()));
            holder.totalAmountTextView.setText(Utility.formatCurrency(String.valueOf(holder.totalAmount)));
            subTotal = subTotal - Double.valueOf(items.getDiscountAmount());
            holder.discontAmountLayout.setVisibility(View.VISIBLE);
            holder.totalAmountLayout.setVisibility(View.VISIBLE);

        } else {
            holder.discontAmountLayout.setVisibility(View.GONE);
            holder.totalAmountLayout.setVisibility(View.GONE);
        }

        //holder.netAmountTextView.setText(Utility.formatCurrency(items.getNetAmount()));
        holder.netAmountTextView.setText(Utility.formatCurrency(String.valueOf(subTotal)));
        setFonts(holder);


        return convertView;
    }

    private void setFonts(ViewHolder holder) {
        FontHelper.applyFont(context, holder.tv_item_name, FontHelper.FontType.FONTROBOLD);
        FontHelper.applyFont(context, holder.tv_item_status, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.tv_item_price, FontHelper.FontType.FONT);


        FontHelper.applyFont(context, holder.discountAmountTextView, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.totalAmountTextView, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.netAmountTextView, FontHelper.FontType.FONT);
    }

    private double drawModifiers(LinearLayout tableLayout, ArrayList<HashMap<String, String>> modifiers, OrderItems items, ViewHolder viewHolder) {
        int noOfModifiers = modifiers.size();
        double modifierPrice = 0;
        LinearLayout[] tableRow = new LinearLayout[noOfModifiers];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView[] modifierNameTextViews = new TextView[noOfModifiers];
        TextView[] modifierPriceTextViews = new TextView[noOfModifiers];

        for (int i = 0; i < noOfModifiers; i++) {

            tableRow[i] = new LinearLayout(context);
            modifierNameTextViews[i] = new TextView(context);
            modifierPriceTextViews[i] = new TextView(context);
            HashMap<String, String> map = modifiers.get(i);

            tableRow[i].setLayoutParams(layoutParams);
            tableRow[i].setOrientation(LinearLayout.HORIZONTAL);
            modifierNameTextViews[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            modifierPriceTextViews[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));

            // Setting text to text views
            modifierNameTextViews[i].setText(map.get(Const.TAG_NAME));
            double modiferP = Double.valueOf(map.get(Const.TAG_PRICE)) * Double.valueOf(items.getQuantitiy());
            viewHolder.totalAmount = viewHolder.totalAmount + Double.valueOf(map.get(Const.TAG_PRICE));

            modifierPriceTextViews[i].setText(Utility.formatCurrency(String.valueOf(modiferP)));
            modifierPrice = modifierPrice + Double.valueOf(map.get(Const.TAG_PRICE));

            modifierNameTextViews[i].setTextColor(Color.BLACK);
            modifierPriceTextViews[i].setTextColor(Color.BLACK);

//            modifierNameTextViews[i].setText("Name");
//            modifierPriceTextViews[i].setText("Price");

            // Adding text views to table row
            tableRow[i].addView(modifierNameTextViews[i]);
            tableRow[i].addView(modifierPriceTextViews[i]);

            // Adding table row to table layout
            tableLayout.addView(tableRow[i], new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

        }

        return modifierPrice;
    }

    static class ViewHolder {

        TextView tv_item_name;
        TextView tv_item_status;
        TextView tv_item_price;
        TextView tv_quantity;
        TextView discountAmountTextView;
        TextView totalAmountTextView;
        TextView netAmountTextView;

        ImageView itemImageView;

        LinearLayout discontAmountLayout;
        LinearLayout totalAmountLayout;
        LinearLayout netAmountLayout;
        LinearLayout modifiersLayout;

        View dividerView;
        double totalAmount = 0;

    }
}
