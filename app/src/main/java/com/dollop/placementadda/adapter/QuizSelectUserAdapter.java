package com.dollop.placementadda.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.ExamInstructionActivity;

import com.dollop.placementadda.model.LeaderBoardModel;
import com.dollop.placementadda.model.QuizListModel;
import com.dollop.placementadda.sohel.S;

import java.util.List;

/**
 * Created by Kasim on 22-03-2018.
 */

public class QuizSelectUserAdapter extends RecyclerView.Adapter<QuizSelectUserAdapter.MyViewHolder> {
    Context context;
    Bundle bundle;

    List<LeaderBoardModel> QuizListModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public QuizSelectUserAdapter(Context context, LeaderBoardModel QuizListModelList) {
        this.context = context;
        this.QuizListModelList = (List<LeaderBoardModel>) QuizListModelList;
    }

    @Override
    public QuizSelectUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_user, parent, false);
        QuizSelectUserAdapter.MyViewHolder viewHolder = new QuizSelectUserAdapter.MyViewHolder(v);
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
    public void onBindViewHolder(final QuizSelectUserAdapter.MyViewHolder holder, final int position) {
         LeaderBoardModel subCatModel = QuizListModelList.get(position);

        holder.titleTV.setText(subCatModel.getUserName());
        S.E("daata"+holder.titleTV);

    }

    @Override
    public int getItemCount() {
        return (null != QuizListModelList ? QuizListModelList.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleTV=(TextView)itemView.findViewById(R.id.selectUserName);

        }
    }
}
