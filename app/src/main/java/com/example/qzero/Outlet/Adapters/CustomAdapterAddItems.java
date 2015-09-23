package com.example.qzero.Outlet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.ObjectClasses.AddItems;
import com.example.qzero.Outlet.ObjectClasses.Items;
import com.example.qzero.R;

import java.util.ArrayList;

/**
 * Created by Braintech on 9/23/2015.
 */
public class CustomAdapterAddItems extends BaseAdapter {

    Context context;
    ArrayList<AddItems> rowItems;

    public CustomAdapterAddItems(Context context,ArrayList<AddItems> rowItems) {


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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_addcart, null);

            holder = new ViewHolder();

            holder.txtViewQty= (TextView) convertView.findViewById(R.id.txtViewQty);



            Log.e("po", "" + position);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        AddItems m = rowItems.get(position);

        Log.e("qty",m.getQuantity());
        holder.txtViewQty.setText(m.getQuantity());

        holder.imgViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rowItems.remove(position);
                notifyDataSetChanged();

            }
        });


        return convertView;
    }

    static class ViewHolder {


        TextView txtViewQty;

        ImageView imgViewDelete;
    }


}
