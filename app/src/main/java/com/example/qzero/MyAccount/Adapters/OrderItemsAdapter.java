package com.example.qzero.MyAccount.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.ObjectClasses.Items;
import com.example.qzero.Outlet.ObjectClasses.OrderItems;
import com.example.qzero.R;

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

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderItems items = rowItems.get(position);

        holder.tv_item_name.setText(items.getItemName());
        holder.tv_item_status.setText(items.getItemStatus());
        holder.tv_item_price.setText(items.getItemPrice());

        setFonts(holder);


        return convertView;
    }

    private void setFonts(ViewHolder holder) {
        FontHelper.applyFont(context, holder.tv_item_name, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.tv_item_status, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, holder.tv_item_price, FontHelper.FontType.FONT);
    }

    static class ViewHolder {

        TextView tv_item_name;
        TextView tv_item_status;
        TextView tv_item_price;
    }
}
