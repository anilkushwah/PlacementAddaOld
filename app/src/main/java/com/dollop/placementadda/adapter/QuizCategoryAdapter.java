package com.dollop.placementadda.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.model.QuizCategoryModel;

import java.util.List;

/**
 * Created by Kasim on 22-01-2018.
 */

public class QuizCategoryAdapter extends RecyclerView.Adapter<QuizCategoryAdapter.MyViewHolder> {
    Context context;
    List<QuizCategoryModel> quizCategoryModelList;
    String userId;
    private SparseBooleanArray mSelectedItemsIds;

    public QuizCategoryAdapter(Context context, List<QuizCategoryModel> quizCategoryModelList) {
        this.context = context;
        this.quizCategoryModelList = quizCategoryModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public QuizCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_category_subcategory, parent, false);
        QuizCategoryAdapter.MyViewHolder viewHolder = new QuizCategoryAdapter.MyViewHolder(v);
        return viewHolder;
    }

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
    public void onBindViewHolder(final QuizCategoryAdapter.MyViewHolder holder, final int position) {
        final QuizCategoryModel quizCategoryModel = quizCategoryModelList.get(position);

        holder.categryNameTv.setText(quizCategoryModel.getCategoryName());
       // QuizCatSubcateAdapter quizCatSubcateAdapter = new QuizCategorySubcategoryAdapter(context, quizCategoryModel.getQuizSubCatgoryModelsList());

    // onBindViewHolder(  );
       /* holder.categryLayoutCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((QuizCategoryActivity) context).getSelected(quizCategoryModel.getCategoryId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return (null != quizCategoryModelList ? quizCategoryModelList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvSubcategoryLIstId;
        ImageView categoryLogoIv, categoryViewsTv;
        TextView categryNameTv, categoryQuizzTv;
        CardView categryLayoutCardview;

        public MyViewHolder(View itemView) {
            super(itemView);

            categryNameTv = (TextView) itemView.findViewById(R.id.tvCategoryId);


        }
    }
}