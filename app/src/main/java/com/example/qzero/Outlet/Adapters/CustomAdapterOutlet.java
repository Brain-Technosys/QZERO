package com.example.qzero.Outlet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qzero.CommonFiles.Helpers.FontHelper;
import com.example.qzero.Outlet.ObjectClasses.Outlet;
import com.example.qzero.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Braintech on 9/21/2015.
 */
public class CustomAdapterOutlet extends BaseAdapter implements Filterable {

    Context context;

    ArrayList<Outlet> arrayListOutlet;
    ArrayList<Outlet> orig;

    HashMap<Integer, ArrayList<Outlet>> hashMapOutlet;

    int arrayLength;

    int count = 0;

    public CustomAdapterOutlet(Context context, HashMap<Integer, ArrayList<Outlet>> hashMapOutlet) {

        this.context = context;
        this.hashMapOutlet = hashMapOutlet;

    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Outlet> results = new ArrayList<Outlet>();
                if (orig == null)
                    orig = arrayListOutlet;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Outlet g : orig) {
                            if (g.getOutlet_name().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                count = 0;
                arrayListOutlet = (ArrayList<Outlet>) results.values;
                Log.e("outlet", "" + arrayListOutlet.size());
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override

    public int getCount() {
        return hashMapOutlet.size();
    }

    @Override
    public Object getItem(int position) {

        return hashMapOutlet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("position", "" + position);


        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();


            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_outlet, null);

            holder.txtViewTitleLandscape = (TextView) convertView.findViewById(R.id.txtViewTitleLandscape);
            holder.txtViewDescLand = (TextView) convertView.findViewById(R.id.txtViewDescLand);
            holder.txtViewMobLand = (TextView) convertView.findViewById(R.id.txtViewMobLand);
            holder.txtViewPhnoneLand = (TextView) convertView.findViewById(R.id.txtViewPhnoneLand);


            holder.txtViewtitlePotraitLeft = (TextView) convertView.findViewById(R.id.txtViewtitlePotraitLeft);
            holder.txtViewDesPotLeft = (TextView) convertView.findViewById(R.id.txtViewDesPotLeft);
            holder.txtViewMobPotLeft = (TextView) convertView.findViewById(R.id.txtViewMobPotLeft);
            holder.txtViewPhPotLeft = (TextView) convertView.findViewById(R.id.txtViewPhPotLeft);


            holder.txtViewtitlePotraitRight = (TextView) convertView.findViewById(R.id.txtViewtitlePotraitRight);
            holder.txtViewDesPotRight = (TextView) convertView.findViewById(R.id.txtViewDesPotRight);
            holder.txtViewMobPotRight = (TextView) convertView.findViewById(R.id.txtViewMobPotRight);
            holder.txtViewPhPotRight = (TextView) convertView.findViewById(R.id.txtViewPhPotRight);

            holder.relLayDesc = (RelativeLayout) convertView.findViewById(R.id.relLayDesc);
            holder.relLayDescOutletLeft = (RelativeLayout) convertView.findViewById(R.id.relLayDescOutletLeft);
            holder.relLayDescOutletRight = (RelativeLayout) convertView.findViewById(R.id.relLayDescOutletRight);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ArrayList<Outlet> arrayListOutlet = new ArrayList<>();
        arrayListOutlet = hashMapOutlet.get(position);


        int count = arrayListOutlet.size();

        int i = 0;

        if(count==1)
        {
            holder.relLayDescOutletLeft.setVisibility(View.GONE);
            holder.relLayDescOutletRight.setVisibility(View.GONE);
        }
        else
        if(count==2)
        {
            holder.relLayDescOutletRight.setVisibility(View.GONE);
        }

        if (i < count) {

            holder.txtViewTitleLandscape.setText(arrayListOutlet.get(i).getOutlet_name());
            holder.txtViewDescLand.setText(arrayListOutlet.get(i).getOutlet_desc());
            holder.txtViewPhnoneLand.setText(arrayListOutlet.get(i).getPhone_num());
            holder.txtViewMobLand.setText(arrayListOutlet.get(i).getMobile_num());

            i++;


        }

        if (i < count) {

            holder.txtViewtitlePotraitLeft.setText(arrayListOutlet.get(i).getOutlet_name());
            holder.txtViewDesPotLeft.setText(arrayListOutlet.get(i).getOutlet_desc());
            holder.txtViewPhPotLeft.setText(arrayListOutlet.get(i).getPhone_num());
            holder.txtViewMobPotLeft.setText(arrayListOutlet.get(i).getMobile_num());

            i++;

        }

        if (i < count) {

            holder.txtViewtitlePotraitRight.setText(arrayListOutlet.get(i).getOutlet_name());
            holder.txtViewDesPotRight.setText(arrayListOutlet.get(i).getOutlet_desc());
            holder.txtViewPhPotRight.setText(arrayListOutlet.get(i).getPhone_num());
            holder.txtViewMobPotRight.setText(arrayListOutlet.get(i).getMobile_num());

            i++;
        }





/*

      int i = arrayListOutlet.size();



        if (i == 0) {
            holder.relLayDesc.setVisibility(View.GONE);
            holder.relLayDescOutletLeft.setVisibility(View.GONE);
            holder.relLayDescOutletRight.setVisibility(View.GONE);
        }


        if (count < i) {

            Log.e("count1", "" + count);
            Log.e("count1tit", arrayListOutlet.get(count).getOutlet_name());
            holder.txtViewTitleLandscape.setText(arrayListOutlet.get(count).getOutlet_name());
            holder.txtViewDescLand.setText(arrayListOutlet.get(count).getOutlet_desc());
            holder.txtViewPhnoneLand.setText(arrayListOutlet.get(count).getPhone_num());
            holder.txtViewMobLand.setText(arrayListOutlet.get(count).getMobile_num());
            count++;

            Log.e("inside",""+count);
            Log.e("insidei",""+count);
            if (count == i) {
                Log.e("inside","in");
                holder.relLayDescOutletLeft.setVisibility(View.GONE);
                holder.relLayDescOutletRight.setVisibility(View.GONE);
            }
        }

        if (count < i && count!=i) {
            Log.e("count2", "" + count);

            holder.txtViewtitlePotraitLeft.setText(arrayListOutlet.get(count).getOutlet_name());
            holder.txtViewDesPotLeft.setText(arrayListOutlet.get(count).getOutlet_desc());
            holder.txtViewPhPotLeft.setText(arrayListOutlet.get(count).getPhone_num());
            holder.txtViewMobPotLeft.setText(arrayListOutlet.get(count).getMobile_num());
            count++;

            if (count == i) {
                holder.relLayDescOutletRight.setVisibility(View.GONE);
            }
        }

        if (count < i && count!=i) {

            Log.e("count3", "" + count);
            holder.txtViewtitlePotraitRight.setText(arrayListOutlet.get(count).getOutlet_name());
            holder.txtViewDesPotRight.setText(arrayListOutlet.get(count).getOutlet_desc());
            holder.txtViewPhPotRight.setText(arrayListOutlet.get(count).getPhone_num());
            holder.txtViewMobPotRight.setText(arrayListOutlet.get(count).getMobile_num());
            count++;
        }

*/

        return convertView;
    }

    class ViewHolder {

        TextView txtViewTitleLandscape;
        TextView txtViewDescLand;
        TextView txtViewMobLand;
        TextView txtViewPhnoneLand;

        TextView txtViewtitlePotraitLeft;
        TextView txtViewDesPotLeft;
        TextView txtViewMobPotLeft;
        TextView txtViewPhPotLeft;

        TextView txtViewtitlePotraitRight;
        TextView txtViewDesPotRight;
        TextView txtViewMobPotRight;
        TextView txtViewPhPotRight;

        RelativeLayout relLayDesc;
        RelativeLayout relLayDescOutletLeft;
        RelativeLayout relLayDescOutletRight;

    }


}
