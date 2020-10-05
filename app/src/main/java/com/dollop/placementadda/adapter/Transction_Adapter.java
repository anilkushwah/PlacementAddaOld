package com.dollop.placementadda.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.model.TransctionModel;
import com.dollop.placementadda.sohel.S;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Transction_Adapter extends RecyclerView.Adapter<Transction_Adapter.MyViewHolder> {
    Context context;
    List<TransctionModel> TransctionModelList;
    String userId;
    private SparseBooleanArray mSelectedItemsIds;
    String dateStringCheck = "";

    public Transction_Adapter(Context context, List<TransctionModel> TransctionModelList) {
        this.context = context;
        this.TransctionModelList = TransctionModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public Transction_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transcation_itemcardview, parent, false);
        Transction_Adapter.MyViewHolder viewHolder = new Transction_Adapter.MyViewHolder(v);
        return viewHolder;
    }

    //Toggle selection methods

    @Override
    public void onBindViewHolder(final Transction_Adapter.MyViewHolder holder, final int position) {
        final TransctionModel TransctionModel = TransctionModelList.get(position);
        if (TransctionModel.getTransction_type().equals("Deduction For accept Game")) {
            holder.transction_pic.setImageResource(R.drawable.ic_privatequiz);
            holder.transction_type.setText(TransctionModel.getTransction_type());
            holder.time_tv.setText(TransctionModel.getTransction_date());
            holder.transction_amount.setTextColor(context.getResources().getColor(R.color.red));
            holder.transction_amount.setText("- " + TransctionModel.getTransction_amount());
            holder.coinIconeId.setImageResource(R.drawable.red_money);
        } else {
            if (TransctionModel.getTransction_type().equals("Watch a video")) {
                holder.transction_pic.setImageResource(R.drawable.ic_play_button);
            } else if (TransctionModel.getTransction_type().equals("Online")) {
                holder.transction_pic.setImageResource(R.drawable.ic_shopping_green);
            } else if (TransctionModel.getTransction_type().equals("Get Daily Coins")) {
                holder.transction_pic.setImageResource(R.drawable.dollor);
            } else if (TransctionModel.getTransction_type().equals("Add For Win Game")) {
                holder.transction_pic.setImageResource(R.drawable.ic_privatequiz);
            }
            holder.transction_type.setText(TransctionModel.getTransction_type());
            holder.time_tv.setText(TransctionModel.getTransction_date());
            holder.transction_amount.setTextColor(context.getResources().getColor(R.color.green));
            holder.transction_amount.setText("+ " + TransctionModel.getTransction_amount());
            holder.coinIconeId.setImageResource(R.drawable.green_money);
            S.E("check Transaction History::" + TransctionModel.getDateChangeCheck() + " datachangeCheck::" + dateStringCheck);
            if (TransctionModel.getDateChangeCheck().equals("SameDate")) {
                holder.layoutForDateId.setVisibility(View.GONE);

            } else {

                holder.tvDateId.setText(TransctionModel.getDateChangeCheck());
                holder.layoutForDateId.setVisibility(View.VISIBLE);

            }

        }
    }

    @Override
    public int getItemCount() {
        return (null != TransctionModelList ? TransctionModelList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView transction_pic;
        TextView tvDateId, transction_type, time_tv, transction_amount;
        CardView categryLayoutCardview;
        LinearLayout layoutForDateId;
        ImageView coinIconeId;

        public MyViewHolder(View itemView) {
            super(itemView);

            transction_pic = (CircleImageView) itemView.findViewById(R.id.transction_pic);
            transction_type = (TextView) itemView.findViewById(R.id.transction_type);
            time_tv = (TextView) itemView.findViewById(R.id.time_tv);
            transction_amount = (TextView) itemView.findViewById(R.id.transction_amount);

            coinIconeId = (ImageView) itemView.findViewById(R.id.coinIconeId);
            tvDateId = (TextView) itemView.findViewById(R.id.tvDateId);
            layoutForDateId = (LinearLayout) itemView.findViewById(R.id.layoutForDateId);


        }
    }
}
