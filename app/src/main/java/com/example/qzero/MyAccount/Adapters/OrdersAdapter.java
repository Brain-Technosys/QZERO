package com.example.qzero.MyAccount.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Common.Utility;
import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.ObjectClasses.Order;
import com.example.qzero.R;

import java.util.ArrayList;

/**
 * Created by Braintech on 8/12/2015.
 */
public class OrdersAdapter extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<Order> orderArrayList;
    public ArrayList<Order> orig;

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
            holder.txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);

            // Labels
            holder.lblDiscount = (TextView) convertView.findViewById(R.id.lblDiscount);

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


        if (Double.valueOf(discount) > 0) {
            holder.txtDiscount.setText(Utility.formateCurrency(discount));
        } else {
            holder.txtDiscount.setVisibility(View.GONE);
            holder.lblDiscount.setVisibility(View.GONE);
        }

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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Order> results = new ArrayList<Order>();

                if (orig == null)
                    orig = orderArrayList;


                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Order order : orig) {
                            if (String.valueOf(order.getOrderId()).toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(order);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }

    static class ViewHolder {


        TextView txtOrderId;
        TextView txtPurchaseDate;
        TextView txtStatus;
        TextView txtItemCount;
        TextView txtDiscount;
        TextView txtAmount;

        TextView lblDiscount;

    }

    private void setFonts(ViewHolder viewHolder) {
        FontHelper.applyFont(context, viewHolder.txtOrderId, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, viewHolder.txtPurchaseDate, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, viewHolder.txtStatus, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, viewHolder.txtItemCount, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, viewHolder.txtDiscount, FontHelper.FontType.FONT);
        FontHelper.applyFont(context, viewHolder.txtAmount, FontHelper.FontType.FONT);
    }
}
