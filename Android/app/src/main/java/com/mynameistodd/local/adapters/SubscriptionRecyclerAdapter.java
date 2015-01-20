package com.mynameistodd.local.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mynameistodd.local.models.Business;
import com.mynameistodd.local.R;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by todd on 1/11/15.
 */
public class SubscriptionRecyclerAdapter extends RecyclerView.Adapter<SubscriptionRecyclerAdapter.ViewHolder> {

    private List<Business> mBusinesses;
    private Context mContext;
    private IAdapterClicks mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mLogo;
        public TextView mName;
        public TextView mSnippet;
        public IViewHolderClicks mListener;

        public ViewHolder(View itemView, IViewHolderClicks listener) {
            super(itemView);
            mListener = listener;
            mLogo = (ImageView) itemView.findViewById(R.id.business_logo);
            mName = (TextView) itemView.findViewById(R.id.business_name);
            mSnippet = (TextView) itemView.findViewById(R.id.business_snippet);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(getPosition());
        }
    }

    public SubscriptionRecyclerAdapter(Context context, List<Business> businesses, IAdapterClicks listener) {
        this.mContext = context;
        this.mBusinesses = businesses;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_subscription_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view, new IViewHolderClicks() {
            @Override
            public void onItemClick(int position) {
                mListener.onItemClick(position);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Business business = mBusinesses.get(i);

        ParseFile file = business.getLogo();
        if (file != null) {
            String logoUrl = file.getUrl();
            Picasso.with(mContext).load(logoUrl).into(viewHolder.mLogo);
        }

        viewHolder.mName.setText(business.getName());
        viewHolder.mSnippet.setText(business.getSnippet());
    }

    @Override
    public int getItemCount() {
        return mBusinesses.size();
    }

    public static interface IViewHolderClicks {
        public void onItemClick(int position);
    }
    public static interface IAdapterClicks {
        public void onItemClick(int position);
    }
}
