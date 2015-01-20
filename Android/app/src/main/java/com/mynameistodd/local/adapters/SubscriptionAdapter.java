package com.mynameistodd.local.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mynameistodd.local.models.Business;
import com.mynameistodd.local.R;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

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

        ImageView logo = (ImageView) view.findViewById(R.id.business_logo);
        TextView name = (TextView) view.findViewById(R.id.business_name);
        TextView snippet = (TextView) view.findViewById(R.id.business_snippet);

        Business business = businesses.get(position);

        ParseFile file = business.getLogo();
        String logoUrl = file.getUrl();

        Picasso.with(context).load(logoUrl).into(logo);
        name.setText(business.getName());
        snippet.setText(business.getSnippet());

        return view;
    }
}
