package com.dollop.placementadda.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.CreateGroupActivity;
import com.dollop.placementadda.activity.QuizMultiplierActivity;
import com.dollop.placementadda.activity.QuizSubCategoryActivity;
import com.dollop.placementadda.model.QuizCategoryModel;
import com.dollop.placementadda.model.QuizSubCategoryModel;
import com.dollop.placementadda.model.QuizSubCatgoryModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kasim on 22-01-2018.
 */

public class QuizCatSubcateAdapter extends RecyclerView.Adapter<QuizCatSubcateAdapter.MyViewHolder> {
    Context context;
    List<QuizSubCategoryModel> quizCategoryModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public QuizCatSubcateAdapter(Context context, List<QuizSubCategoryModel> quizCategoryModelList) {
        this.context = context;
        this.quizCategoryModelList = quizCategoryModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public QuizCatSubcateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_subcat_layout, parent, false);
        QuizCatSubcateAdapter.MyViewHolder viewHolder = new QuizCatSubcateAdapter.MyViewHolder(v);
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
    public void onBindViewHolder(final QuizCatSubcateAdapter.MyViewHolder holder, final int position) {
        final QuizSubCategoryModel quizCategoryModel = quizCategoryModelList.get(position);
/*
        holder.categoryLogoIv.setImageDrawable(context.getResources().getDrawable(quizCategoryModel.getCategoryLogo()));
*/
        if (quizCategoryModel.category_image.equals("")) {
            holder.categoryLogoIv.setImageResource(R.drawable.sbi_logo);
        } else {
            S.E("Image" + quizCategoryModel.category_image);
            Picasso.with(context).load(Const.URL.Categry_Image + quizCategoryModel.category_image).into(holder.categoryLogoIv);

        }
        holder.categryNameTv.setText(quizCategoryModel.categoryName);
        S.E("HoldertvPayandBeatOtherTd");


        holder.categryLayoutCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((QuizSubCategoryActivity) context).getSelectedCategerySubject(quizCategoryModel.categoty_id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != quizCategoryModelList ? quizCategoryModelList.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryLogoIv, ivSubCategoryImageId;
        TextView tvSubCategoryNameId, categryNameTv;
        CardView categryLayoutCardview;

        public MyViewHolder(View itemView) {

            super(itemView);
            // categryLayoutCardview = (CardView) itemView.findViewById(R.id.categryLayoutCardview);
            ivSubCategoryImageId = (ImageView) itemView.findViewById(R.id.ivSubCategoryImageId);
            tvSubCategoryNameId = (TextView) itemView.findViewById(R.id.tvSubCategoryNameId);

            /*      categoryViewsTv = (ImageView) itemView.findViewById(R.id.categoryViewsTv);*/

        }
    }
}
