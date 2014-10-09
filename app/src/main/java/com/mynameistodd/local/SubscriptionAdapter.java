package com.mynameistodd.local;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by todd on 10/9/14.
 */
public class SubscriptionAdapter extends ArrayAdapter<Business> {

    private Context context;
    private List<Business> businesses;

    public SubscriptionAdapter(Context context, int resource, List<Business> objects) {
        super(context, resource, objects);
        this.context = context;
        this.businesses = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_subscription_list_item, null);

        TextView name = (TextView) view.findViewById(R.id.business_name);

        Business business = businesses.get(position);

        name.setText(business.getName());

        return view;
    }
}
