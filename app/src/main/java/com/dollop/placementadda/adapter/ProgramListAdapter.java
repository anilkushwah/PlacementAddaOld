package com.dollop.placementadda.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.ProgramDetailActivity;
import com.dollop.placementadda.model.ProgramListModel;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProgramListAdapter extends RecyclerView.Adapter<ProgramListAdapter.MyViewHolder> {
    Context context;
    private List<ProgramListModel> programListModels;

    public ProgramListAdapter(Context context, List<ProgramListModel> programListModels) {
        this.context = context;
        this.programListModels = programListModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_program_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProgramListModel programListModel =programListModels.get(position);
        holder.cdProgramId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("CodeImage",programListModel.CodeImage);
                bundle.putString("CodeId",programListModel.CodeId);
                bundle.putString("CodeTitle",programListModel.CodeTitle);
                S.I(context, ProgramDetailActivity.class,bundle);
            }
        });
        Picasso.with(context).load(programListModel.CodeImage).into(holder.ProgramIcon);
        holder.ProgramTitle.setText(programListModel.CodeTitle);
    }

    @Override
    public int getItemCount() {
        return programListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ProgramIcon;
        TextView ProgramTitle;
        CardView cdProgramId;
        public MyViewHolder(View itemView) {
            super(itemView);
            cdProgramId=itemView.findViewById(R.id.cdProgramId);
            ProgramIcon=itemView.findViewById(R.id.ivProgramIconId);
            ProgramTitle=itemView.findViewById(R.id.tvProgramTitleId);
        }
    }
}
