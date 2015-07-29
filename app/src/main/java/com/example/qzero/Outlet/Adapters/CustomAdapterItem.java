package com.example.qzero.Outlet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.CommonFiles.Helpers.FontHelper.FontType;

import com.example.qzero.Outlet.ObjectClasses.Items;
import com.example.qzero.Outlet.ObjectClasses.Venue;
import com.example.qzero.R;

import java.util.ArrayList;

public class CustomAdapterItem extends BaseAdapter {

    Context context;
    ArrayList<Items> rowItems;

    public CustomAdapterItem(Context context,
                              ArrayList<Items> rowItems) {


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
            convertView = mInflater.inflate(R.layout.list_search_item, null);

            holder = new ViewHolder();

            holder.txtViewName = (TextView) convertView.findViewById(R.id.txtViewName);
            holder.txtViewVenue=(TextView) convertView.findViewById(R.id.txtViewVenue);
            holder.txtViewAddress = (TextView) convertView.findViewById(R.id.txtViewAddress);
            holder.txtViewCity = (TextView) convertView.findViewById(R.id.txtViewCity);

            holder.txtViewPhone = (TextView) convertView.findViewById(R.id.txtViewPhone);
            holder.txtViewMobile = (TextView) convertView.findViewById(R.id.txtViewMobile);


            FontHelper.setFontFace(holder.txtViewName, FontType.FONTSANSBOLD, context);
            FontHelper.setFontFace(holder.txtViewVenue, FontType.FONTSANSREGULAR, context);
            FontHelper.setFontFace(holder.txtViewAddress, FontType.FONTSANSREGULAR, context);
            FontHelper.setFontFace(holder.txtViewCity, FontType.FONTSANSREGULAR, context);

            FontHelper.setFontFace(holder.txtViewMobile, FontType.FONTSANSREGULAR, context);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Items items = rowItems.get(position);


        holder.txtViewName.setText(items.getItem_name());
        holder.txtViewVenue.setText(items.getVenue_name());
        holder.txtViewAddress.setText(items.getVenue_address());
        holder.txtViewCity.setText(items.getVenue_city());

        holder.txtViewPhone.setText(items.getVenue_phone());
        holder.txtViewMobile.setText(items.getVenue_mobile());
        return convertView;
    }

    static class ViewHolder {


        TextView txtViewName;
        TextView txtViewVenue;
        TextView txtViewAddress;
        TextView txtViewCity;

        TextView txtViewPhone;
        TextView txtViewMobile;
    }
}
