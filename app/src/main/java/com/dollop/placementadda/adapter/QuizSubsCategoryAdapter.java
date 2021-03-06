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
import com.dollop.placementadda.activity.QuizSubCategoryActivity;
import com.dollop.placementadda.model.QuizCategoryModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kasim on 22-01-2018.
 */

public class QuizSubsCategoryAdapter extends RecyclerView.Adapter<QuizSubsCategoryAdapter.MyViewHolder> {
    Context context;
    List<QuizCategoryModel> quizCategoryModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public QuizSubsCategoryAdapter(Context context, List<QuizCategoryModel> quizCategoryModelList) {
        this.context = context;
        this.quizCategoryModelList = quizCategoryModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public QuizSubsCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_quiz_category, parent, false);
        QuizSubsCategoryAdapter.MyViewHolder viewHolder = new QuizSubsCategoryAdapter.MyViewHolder(v);
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
    public void onBindViewHolder(final QuizSubsCategoryAdapter.MyViewHolder holder, final int position) {
        final QuizCategoryModel quizCategoryModel = quizCategoryModelList.get(position);
/*
        holder.categoryLogoIv.setImageDrawable(context.getResources().getDrawable(quizCategoryModel.getCategoryLogo()));
*/
        if (quizCategoryModel.getCategory_Image().equals("")) {
            holder.categoryLogoIv.setImageResource(R.drawable.sbi_logo);
        } else {
            S.E("Image"+quizCategoryModel.getCategory_Image());
            Picasso.with(context).load(Const.URL.Categry_Image+quizCategoryModel.getCategory_Image()).into(holder.categoryLogoIv);

        }
        holder.categryNameTv.setText(quizCategoryModel.getCategoryName());


        holder.categryLayoutCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((QuizSubCategoryActivity)context).getSelectedCategerySubject(quizCategoryModel.getCategoryId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != quizCategoryModelList ? quizCategoryModelList.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryLogoIv,categoryViewsTv;
        TextView categryNameTv, categoryQuizzTv;
        CardView categryLayoutCardview;

        public MyViewHolder(View itemView) {

            super(itemView);
            categryLayoutCardview = (CardView) itemView.findViewById(R.id.categryLayoutCardview);
            categoryLogoIv = (ImageView) itemView.findViewById(R.id.categoryLogoIv);
            categryNameTv = (TextView) itemView.findViewById(R.id.categryNameTv);
            categoryQuizzTv = (TextView) itemView.findViewById(R.id.categoryQuizzTv);
            categoryViewsTv = (ImageView) itemView.findViewById(R.id.categoryViewsTv);

        }
    }}
