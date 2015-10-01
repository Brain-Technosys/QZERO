package com.example.qzero.Outlet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        //  if (convertView == null) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.list_search_item, null);

        holder = new ViewHolder();

        holder.txtViewName = (TextView) convertView.findViewById(R.id.txtViewName);
        holder.txtViewVenue = (TextView) convertView.findViewById(R.id.txtViewVenue);
        holder.txtViewAddress = (TextView) convertView.findViewById(R.id.txtViewAddress);
        holder.txtOutletName = (TextView) convertView.findViewById(R.id.txtViewOutlet);

        holder.txtViewPhone = (TextView) convertView.findViewById(R.id.txtViewPhone);
        holder.txtViewMobile = (TextView) convertView.findViewById(R.id.txtViewMobile);

        holder.imgViewPhone = (ImageView) convertView.findViewById(R.id.imgViewPhone);
        holder.imgViewMobile = (ImageView) convertView.findViewById(R.id.imgViewMobile);

        holder.dividerView = (View) convertView.findViewById(R.id.vw_line);


        FontHelper.setFontFace(holder.txtViewName, FontType.FONTSANSBOLD, context);
        FontHelper.setFontFace(holder.txtViewVenue, FontType.FONTSANSREGULAR, context);
        FontHelper.setFontFace(holder.txtViewAddress, FontType.FONTSANSREGULAR, context);
        FontHelper.setFontFace(holder.txtOutletName, FontType.FONTSANSREGULAR, context);

        FontHelper.setFontFace(holder.txtViewMobile, FontType.FONTSANSREGULAR, context);

        convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        Items items = rowItems.get(position);

        String fullAddress = items.getVenue_address().trim();
        if (items.getVenue_city().trim().length() != 0) {
            fullAddress = fullAddress + ", " + items.getVenue_city().trim();
        }


        holder.txtViewName.setText(items.getItem_name().trim());
        holder.txtViewVenue.setText(items.getVenue_name().trim());
        holder.txtViewAddress.setText(fullAddress);
        holder.txtOutletName.setText("(" + items.getOutlet_name() + ")");
        //  holder.txtOutletName.setText(items.getVenue_city());

        if (items.getVenue_phone().equals("null")) {
            holder.imgViewPhone.setVisibility(View.GONE);
        } else {
            holder.txtViewPhone.setText(items.getVenue_phone());
            holder.imgViewPhone.setVisibility(View.VISIBLE);
        }

        if (items.getVenue_mobile().equals("null")) {
            holder.imgViewMobile.setVisibility(View.GONE);
        } else {
            holder.txtViewMobile.setText(items.getVenue_mobile());
            holder.imgViewMobile.setVisibility(View.VISIBLE);
        }

        if (items.getVenue_phone().equals("null") && items.getVenue_mobile().equals("null")) {
            holder.dividerView.setVisibility(View.GONE);
        } else {
            holder.dividerView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {


        TextView txtViewName;
        TextView txtViewVenue;
        TextView txtViewAddress;
        TextView txtOutletName;

        TextView txtViewPhone;
        TextView txtViewMobile;

        ImageView imgViewPhone;
        ImageView imgViewMobile;

        View dividerView;

    }
}
