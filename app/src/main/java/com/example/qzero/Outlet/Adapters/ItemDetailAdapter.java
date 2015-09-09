package com.example.qzero.Outlet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.ObjectClasses.Items;
import com.example.qzero.R;

import java.util.ArrayList;

/**
 * Created by Braintech on 9/9/2015.
 */
public class ItemDetailAdapter extends BaseAdapter {

    Context context;

    String[] items;
    ArrayList<Items> rowItems;

    public ItemDetailAdapter(Context context,
                             String[] items) {


        this.context = context;
        this.items = items;

    }

    @Override
    public int getCount() {

        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
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
            convertView = mInflater.inflate(R.layout.list_addcart, null);

            holder = new ViewHolder();


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

      return  convertView;



    }

    static class ViewHolder {



    }

}
