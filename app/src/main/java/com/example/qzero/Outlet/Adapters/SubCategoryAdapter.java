package com.example.qzero.Outlet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.qzero.Outlet.ObjectClasses.Category;
import com.example.qzero.Outlet.ObjectClasses.SubCategory;
import com.example.qzero.R;

import java.util.ArrayList;


public class SubCategoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<SubCategory> rowItems;

    public SubCategoryAdapter(Context context,
                              ArrayList<SubCategory> rowItems) {


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
            convertView = mInflater.inflate(R.layout.list_subcategory, null);

            holder = new ViewHolder();

            holder.txtViewSubCategory = (TextView) convertView.findViewById(R.id.txtViewSubCategory);

           // FontHelper.setFontFace(holder.txtViewSubCategory, FontHelper.FontType.FONTSANSBOLD, context);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SubCategory subCategory = rowItems.get(position);

         holder.txtViewSubCategory.setText(subCategory.getSub_cat_name());

        return convertView;
    }

    static class ViewHolder {
        TextView txtViewSubCategory;
    }
}
