package com.dollop.placementadda.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.dollop.placementadda.activity.QuizMainActivity;
import com.dollop.placementadda.activity.QuizPlayDetailsActivity;
import com.dollop.placementadda.activity.QuizPlayFourPlayerDetails;
import com.dollop.placementadda.activity.QuizPlayThreePlayerDetail;
import com.dollop.placementadda.activity.QuizesActivity;
import com.dollop.placementadda.activity.SimilarTestActivity;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.QuizListModel;
import com.dollop.placementadda.model.QuizListModel;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;

import java.util.List;

/**
 * Created by Kasim on 22-01-2018.
 */

public class SimilarQuizesListAdapter extends RecyclerView.Adapter<SimilarQuizesListAdapter.MyViewHolder> {
    Context context;
    List<QuizListModel> QuizListModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public SimilarQuizesListAdapter(Context context, List<QuizListModel> QuizListModelList) {
        this.context = context;
        this.QuizListModelList = QuizListModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public SimilarQuizesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_quiz_sub_category, parent, false);
        SimilarQuizesListAdapter.MyViewHolder viewHolder = new SimilarQuizesListAdapter.MyViewHolder(v);
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
        if (value) mSelectedItemsIds.put(position, value);
        else mSelectedItemsIds.delete(position);

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
    public void onBindViewHolder(final SimilarQuizesListAdapter.MyViewHolder holder, final int position) {
        final QuizListModel subCatModel = QuizListModelList.get(position);

        holder.titleTV.setText(subCatModel.getQuizName());

        holder.durationTv.setText(subCatModel.getQuizTime());
        holder.numOfQueTv.setText(subCatModel.getQuizTotalQuestion());
        if (subCatModel.getQuizService_paid().equals("0")) {
            holder.typeTV.setText("Free");

        } else {
            holder.typeTV.setText("₹ " + subCatModel.getAmount());
        }
        holder.mainCardview.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                ((SimilarTestActivity)context).getSelectedQuiz(subCatModel.getQuiz_id(),subCatModel.getQuizTime(),subCatModel.getQuizCategory_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != QuizListModelList ? QuizListModelList.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV, typeTV, durationTv, numOfQueTv;
        CardView mainCardview;

        public MyViewHolder(View itemView) {

            super(itemView);
            mainCardview = (CardView) itemView.findViewById(R.id.mainCardview);
            titleTV = (TextView) itemView.findViewById(R.id.titleTV);
            typeTV = (TextView) itemView.findViewById(R.id.typeTV);
            durationTv = (TextView) itemView.findViewById(R.id.durationTv);
            numOfQueTv = (TextView) itemView.findViewById(R.id.numOfQueTv);
        }
    }
}
