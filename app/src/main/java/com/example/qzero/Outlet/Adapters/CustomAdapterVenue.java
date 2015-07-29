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

import com.example.qzero.Outlet.ObjectClasses.Venue;
import com.example.qzero.R;

import java.util.ArrayList;

public class CustomAdapterVenue extends BaseAdapter {

    Context context;
    ArrayList<Venue> rowItems;

    public CustomAdapterVenue(Context context,
                              ArrayList<Venue> rowItems) {


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
            convertView = mInflater.inflate(R.layout.list_item_venue, null);

            holder = new ViewHolder();

            holder.txtViewName = (TextView) convertView.findViewById(R.id.txtViewName);
            holder.txtViewAddress = (TextView) convertView.findViewById(R.id.txtViewAddress);
            holder.txtViewCity = (TextView) convertView.findViewById(R.id.txtViewCity);
            holder.txtViewZip = (TextView) convertView.findViewById(R.id.txtViewZip);
            holder.txtViewPhone = (TextView) convertView.findViewById(R.id.txtViewPhone);
            holder.txtViewMobile = (TextView) convertView.findViewById(R.id.txtViewMobile);


            FontHelper.setFontFace(holder.txtViewName, FontType.FONTSANSBOLD, context);
            FontHelper.setFontFace(holder.txtViewAddress, FontType.FONTSANSREGULAR, context);
            FontHelper.setFontFace(holder.txtViewCity, FontType.FONTSANSREGULAR, context);
            FontHelper.setFontFace(holder.txtViewZip, FontType.FONTSANSREGULAR, context);
            FontHelper.setFontFace(holder.txtViewPhone, FontType.FONTSANSREGULAR, context);
            FontHelper.setFontFace(holder.txtViewMobile, FontType.FONTSANSREGULAR, context);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Venue venue = rowItems.get(position);


        holder.txtViewName.setText(venue.getVenue_name());
        holder.txtViewAddress.setText(venue.getVenue_address());
        holder.txtViewCity.setText(venue.getVenue_city());
        holder.txtViewZip.setText(venue.getVenue_zip());
        holder.txtViewPhone.setText(venue.getVenue_phone());
        holder.txtViewMobile.setText(venue.getVenue_mobile());
        return convertView;
    }

    static class ViewHolder {


        TextView txtViewName;
        TextView txtViewAddress;
        TextView txtViewCity;
        TextView txtViewZip;
        TextView txtViewPhone;
        TextView txtViewMobile;
    }
}
