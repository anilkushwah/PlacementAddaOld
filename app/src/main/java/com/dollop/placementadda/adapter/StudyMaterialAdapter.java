package com.dollop.placementadda.adapter;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.model.StudyMaterialModel;

import java.util.List;

/**
 * Created by Kasim on 22-01-2018.
 */

public class StudyMaterialAdapter extends RecyclerView.Adapter<StudyMaterialAdapter.MyViewHolder> {
    Context context;
    List<StudyMaterialModel> StudyMaterialModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public StudyMaterialAdapter(Context context, List<StudyMaterialModel> StudyMaterialModelList) {
        this.context = context;
        this.StudyMaterialModelList = StudyMaterialModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public StudyMaterialAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_study_material, parent, false);
        StudyMaterialAdapter.MyViewHolder viewHolder = new StudyMaterialAdapter.MyViewHolder(v);
        return viewHolder;
    }

    /***
     * Methods required for do selections, remove selections, etc.
     */

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


    @Override
    public void onBindViewHolder(final StudyMaterialAdapter.MyViewHolder holder, final int position) {
        final StudyMaterialModel StudyMaterialModel = StudyMaterialModelList.get(position);
        holder.tvHeaderID.setText(StudyMaterialModel.getSubjectTheoryHeading());
        String htlmLoadData = StudyMaterialModel.getSubjectTheoryContant();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.tvDetailId.setText(Html.fromHtml(StudyMaterialModelList.get(position).getSubjectTheoryContant(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.tvDetailId.setText(Html.fromHtml(StudyMaterialModelList.get(position).getSubjectTheoryContant()));
        }
     //   holder.tvDetailId.loadData(htlmLoadData, "text/html", "UTF-8");

    }

    @Override
    public int getItemCount() {
        return (null != StudyMaterialModelList ? StudyMaterialModelList.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
         final TextView tvDetailId;
        ImageView categoryLogoIv;
        TextView tvHeaderID;
        CardView categryLayoutCardview;

        public MyViewHolder(View itemView) {

            super(itemView);
            categryLayoutCardview = (CardView) itemView.findViewById(R.id.categryLayoutCardview);


            tvHeaderID = (TextView) itemView.findViewById(R.id.tvHeaderID);
            tvDetailId = (TextView) itemView.findViewById(R.id.tvContentId);

        }
    }
}
