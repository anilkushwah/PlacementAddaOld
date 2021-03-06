package com.dollop.placementadda.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.MoreCoinsActivity;
import com.dollop.placementadda.model.MoreCoinsModel;

import java.util.List;

/**
 * Created by Kasim on 03-04-2018.
 */

public class MoreCoinsAdapter extends RecyclerView.Adapter<MoreCoinsAdapter.MyViewHolder> {

    Context context;
    private List<MoreCoinsModel> moviesList;

    public MoreCoinsAdapter(Context context, List<MoreCoinsModel> moviesList) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.more_coins_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MoreCoinsModel movie = moviesList.get(position);
        holder.rupeesTv.setText(movie.getRupees());
        holder.coinsTV.setText(movie.getCoins());
        holder.rupees_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MoreCoinsActivity) context).getCoinsDialogBox(movie.getRupees(), "Adapter");
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView coinsTV, rupeesTv;
        LinearLayout rupees_linearlayout;

        public MyViewHolder(View view) {
            super(view);

            coinsTV = (TextView) view.findViewById(R.id.coinsTV);
            rupeesTv = (TextView) view.findViewById(R.id.rupeesTv);
            rupees_linearlayout = (LinearLayout) view.findViewById(R.id.rupees_linearlayout);

        }
    }


}