package com.dollop.placementadda.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.Job_Alert_Detail_Activity;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.JobAlertModel;
import com.dollop.placementadda.model.LeaderBoardModel;
import com.dollop.placementadda.model.MainFragModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobAlertAdapter extends RecyclerView.Adapter<JobAlertAdapter.MyViewHolder> {
    private int postionForselected = 0;

    MainFragModel mainFragModel;
    Context context;
    List<JobAlertModel> mainFragModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public JobAlertAdapter(Context context, List<JobAlertModel> mainFragModelList) {
        Log.e("Position", "" + postionForselected);
        this.context = context;
        this.mainFragModelList = mainFragModelList;
        this.postionForselected = postionForselected;
    }

    @Override
    public JobAlertAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_list_item_job_alert, parent, false);
        return new JobAlertAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final JobAlertAdapter.MyViewHolder holder, final int position) {
        JobAlertModel mainFragModel = mainFragModelList.get(position);
        holder.job_title_tv.setText(mainFragModel.getJob_title() + "-(" + mainFragModel.getJob_location() + ")");
        holder.companyname_tv.setText(mainFragModel.getCompany_name());
        holder.expriance_tv.setText(mainFragModel.getExperience());
        holder.skill_tv.setText(mainFragModel.getSkill_required());
        holder.currentdate_yv.setText(mainFragModel.getCreated_date());


        S.E("checkImageMainModel::"+mainFragModel.getImage());

     if (!mainFragModel.getImage().equals("") && mainFragModel != null) {
            Picasso.with(context).load(mainFragModel.getImage()).into(holder.ivCompnayImageId);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Job_Alert_Detail_Activity.class);
                Bundle b = new Bundle();
                b.putSerializable("AllData", mainFragModelList.get(position));
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView job_title_tv, companyname_tv, expriance_tv, Address_tv, skill_tv, currentdate_yv, View_btn;
        ImageView ivCompnayImageId;

        public MyViewHolder(View itemView) {

            super(itemView);
            View_btn = (TextView) itemView.findViewById(R.id.View_btn);
            job_title_tv = (TextView) itemView.findViewById(R.id.job_title_tv);
            companyname_tv = (TextView) itemView.findViewById(R.id.companyname_tv);
            expriance_tv = (TextView) itemView.findViewById(R.id.expriance_tv);
            skill_tv = (TextView) itemView.findViewById(R.id.skill_tv);
            currentdate_yv = (TextView) itemView.findViewById(R.id.currentdate_yv);
            ivCompnayImageId = (ImageView) itemView.findViewById(R.id.ivCompnayImageId);



        }
    }

    @Override
    public int getItemCount() {
        return mainFragModelList.size();
    }


}