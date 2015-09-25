package com.example.qzero.MyAccount.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.RequestResponse.Const;
import com.example.qzero.Outlet.ObjectClasses.Items;
import com.example.qzero.Outlet.ObjectClasses.OrderItems;
import com.example.qzero.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class OrderItemsAdapter extends BaseAdapter {

    Context context;
    ArrayList<OrderItems> rowItems;

    public OrderItemsAdapter(Context context,
                             ArrayList<OrderItems> rowItems) {


        this.context = context;
        this.rowItems = rowItems;

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
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_items_order_items, null);

            holder = new ViewHolder();

            holder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            holder.tv_item_status = (TextView) convertView.findViewById(R.id.tv_item_status);
            holder.tv_item_price = (TextView) convertView.findViewById(R.id.tv_item_price);
            holder.tv_quantity = (TextView) convertView.findViewById(R.id.tv_item_qty);
            holder.discountTextView = (TextView) convertView.findViewById(R.id.tv_discount);
            holder.discountAmountTextView = (TextView) convertView.findViewById(R.id.tv_discountAmount);
            holder.totalAmountTextView = (TextView) convertView.findViewById(R.id.tv_totalAmount);
            holder.netAmountTextView = (TextView) convertView.findViewById(R.id.tv_netAmount);

            holder.itemImageView = (ImageView) convertView.findViewById(R.id.img_item);


            holder.discontLayout = (LinearLayout) convertView.findViewById(R.id.ll_discount);
            holder.discontAmountLayout = (LinearLayout) convertView.findViewById(R.id.ll_discountAmount);
            holder.totalAmountLayout = (LinearLayout) convertView.findViewById(R.id.ll_totalAmount);
            holder.netAmountLayout = (LinearLayout) convertView.findViewById(R.id.ll_netAmount);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderItems items = rowItems.get(position);

        //Load Image
        String item_image = Const.BASE_URL + Const.IMAGE_URL + items.getItemId();
        Picasso.with(context).load(item_image).into(holder.itemImageView);

        holder.tv_item_name.setText(items.getItemName());
        holder.tv_item_status.setText(items.getItemStatus());
        holder.tv_item_price.setText("$" + items.getItemPrice());
        holder.tv_quantity.setText(items.getQuantitiy());

        holder.discountTextView.setText("$" + items.getDiscount());
        holder.discountAmountTextView.setText("$" + items.getDiscountAmount());
        holder.totalAmountTextView.setText("$" + items.getTotalAmount());
        holder.netAmountTextView.setText("$" + items.getNetAmount());
        setFonts(holder);


        return convertView;
    }

    private void setFonts(ViewHolder holder) {
        FontHelper.applyFont(context, holder.tv_item_name, FontHelper.FontType.FONTROBOLD);
        FontHelper.applyFont(context, holder.tv_item_status, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.tv_item_price, FontHelper.FontType.FONT);

        FontHelper.applyFont(context, holder.discountTextView, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.discountAmountTextView, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.totalAmountTextView, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.netAmountTextView, FontHelper.FontType.FONT);
    }

    static class ViewHolder {

        TextView tv_item_name;
        TextView tv_item_status;
        TextView tv_item_price;
        TextView tv_quantity;
        TextView discountTextView;
        TextView discountAmountTextView;
        TextView totalAmountTextView;
        TextView netAmountTextView;

        ImageView itemImageView;

        LinearLayout discontLayout;
        LinearLayout discontAmountLayout;
        LinearLayout totalAmountLayout;
        LinearLayout netAmountLayout;
    }
}
