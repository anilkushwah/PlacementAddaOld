package com.dollop.placementadda.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.TopicWiseLeaderShipActivity;
import com.dollop.placementadda.model.FilteredHistory;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FilteredHistoryAdapter extends RecyclerView.Adapter<FilteredHistoryAdapter.MyViewHolder> {
    Context context;
    List<FilteredHistory> filteredHistoryList;

    public FilteredHistoryAdapter(Context context, List<FilteredHistory> filteredHistoryList) {
        this.context = context;
        this.filteredHistoryList = filteredHistoryList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_list_of_filtered_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        FilteredHistory filteredHistory = filteredHistoryList.get(position);
        if (filteredHistory.getCategory_image().equals("")) {
            holder.testIcon.setImageResource(R.drawable.sbi_logo);
        } else {
            S.E("Image"+filteredHistory.getCategory_image());
            Picasso.with(context).load(Const.URL.Categry_Image+filteredHistory.getCategory_image()).into(holder.testIcon);

        }
        holder.testTitleTv.setText(filteredHistory.getTopic_name());
        holder.testTimeTv.setText(filteredHistory.getTest_time());
        holder.testDateTv.setText(filteredHistory.getTest_date());

        holder.testPathTv.setText(filteredHistory.getCategory_name()+" => "+filteredHistory.getSubject_name()
        +" => "+filteredHistory.getTopic_name());
       holder.filterHistoryCardview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, TopicWiseLeaderShipActivity.class);
               Bundle b = new Bundle();
               b.putSerializable("AllData",filteredHistoryList.get(position));
               intent.putExtras(b);
               context.startActivity(intent);
           }
       });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView testIcon;
        TextView testTitleTv,testTimeTv,testDateTv,testPathTv;
        CardView filterHistoryCardview;
        public MyViewHolder(View itemView) {
            super(itemView);
            testIcon=itemView.findViewById(R.id.testIcon);
            testTitleTv=itemView.findViewById(R.id.testTitleTv);
            testTimeTv=itemView.findViewById(R.id.testTimeTv);
            testDateTv=itemView.findViewById(R.id.testDateTv);
            testPathTv=itemView.findViewById(R.id.testPathTv);
            filterHistoryCardview=itemView.findViewById(R.id.filterHistoryCardview);
        }
    }

    @Override
    public int getItemCount() {
        return filteredHistoryList.size();
    }


}