package com.vivogaming.livecasino.screens.register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.vivogaming.livecasino.R;

import java.util.ArrayList;

/**
 * Created by Sofia on 9/27/13.
 */
public class CountryAdapter extends ArrayAdapter<CountryObject> {

    private Context context;
    private ArrayList<CountryObject> countryList;
    private LayoutInflater inflater;

    public CountryAdapter(Context context, int resource, ArrayList<CountryObject> objects) {
        super(context, resource, objects);

        this.context = context;
        this.countryList = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public CountryObject getItem(int i) {
        return countryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       return getCustomDropDownView(position, convertView, parent);
    }

    public View getCustomDropDownView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_country, parent, false);

            viewHolder.ivCountryFlag_LIC = (ImageView) convertView.findViewById(R.id.ivFlag_LIC);
            viewHolder.tvCountryName_LIC = (TextView) convertView.findViewById(R.id.tvName_LIC);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivCountryFlag_LIC.setImageResource(countryList.get(position).flag);
        viewHolder.tvCountryName_LIC.setText(countryList.get(position).name);

        return convertView;
    }
}
