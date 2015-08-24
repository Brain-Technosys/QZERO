package com.example.qzero.MyAccount.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.ObjectClasses.Order;
import com.example.qzero.R;

import java.util.ArrayList;

/**
 * Created by Braintech on 8/12/2015.
 */
public class OrdersAdapter extends BaseAdapter {
    Context context;
    ArrayList<Order> orderArrayList;

    public OrdersAdapter(Context context, ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_order, null);

            holder = new ViewHolder();

            holder.txtOrderId = (TextView) convertView.findViewById(R.id.txtOrderID);
            holder.txtPurchaseDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.txtItemCount = (TextView) convertView.findViewById(R.id.txtItemCount);
            holder.txtDiscount = (TextView) convertView.findViewById(R.id.txtDiscount);
            holder.txtAmount =  (TextView) convertView.findViewById(R.id.txtAmount);

            setFonts(holder);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = new Order();
       order = orderArrayList.get(position);

        String oid = String.valueOf(order.getOrderId());
        String pDate = order.getPurchaseDate();
        String status = order.getOrderStatus();
        String itemCount = String.valueOf(order.getItemsCount());
        String discount = order.getDiscount();
        String amount = order.getAmount();

        holder.txtOrderId.setText(String.valueOf(order.getOrderId()));
        holder.txtPurchaseDate.setText(order.getPurchaseDate());
        holder.txtStatus.setText(order.getOrderStatus());
        holder.txtItemCount.setText(String.valueOf(order.getItemsCount()));
        holder.txtDiscount.setText(Utility.formateCurrency(discount));
        holder.txtAmount.setText(Utility.formateCurrency(amount));
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return orderArrayList.get(position);
    }

    @Override
    public int getCount() {
        return orderArrayList.size();
    }

    static class ViewHolder {


        TextView txtOrderId;
        TextView txtPurchaseDate;
        TextView txtStatus;
        TextView txtItemCount;
        TextView txtDiscount;
        TextView txtAmount;

    }

    private void setFonts(ViewHolder viewHolder)
    {
        FontHelper.applyFont(context, viewHolder.txtOrderId, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, viewHolder.txtPurchaseDate, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, viewHolder.txtStatus, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, viewHolder.txtItemCount, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, viewHolder.txtDiscount, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, viewHolder.txtAmount, FontHelper.FontType.FONT);
    }
}
