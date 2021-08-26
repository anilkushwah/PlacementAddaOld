package com.dollop.placementadda.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.OurPlacementStudentsActivity;
import com.dollop.placementadda.model.CompanyModel;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kasim on 22-01-2018.
 */

public class CompanyLIstAdapter extends RecyclerView.Adapter<CompanyLIstAdapter.MyViewHolder> {
    Context context;
    List<CompanyModel> QuizListModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public CompanyLIstAdapter(Context context, List<CompanyModel> QuizListModelList) {
        this.context = context;
        this.QuizListModelList = QuizListModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public CompanyLIstAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_company_name, parent, false);
        CompanyLIstAdapter.MyViewHolder viewHolder = new CompanyLIstAdapter.MyViewHolder(v);
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
    public void onBindViewHolder(final CompanyLIstAdapter.MyViewHolder holder, final int position) {
        final CompanyModel subCatModel = QuizListModelList.get(position);

        holder.tvCompanyNameId.setText(subCatModel.strCompanyName);
        if (subCatModel.strCompanyImage != null) {
            Picasso.with(context).load(subCatModel.strCompanyImage).error(R.drawable.ic_user).into(holder.ivCompnayImageId);

        } else {
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("companyId", subCatModel.strCompanyId);
                bundle.putString("companyName", subCatModel.strCompanyName);
                S.I(context, OurPlacementStudentsActivity.class, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return QuizListModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCompnayImageId;
        TextView tvCompanyNameId;
        CardView mainCardview;

        public MyViewHolder(View itemView) {

            super(itemView);
            mainCardview = (CardView) itemView.findViewById(R.id.mainCardview);
            ivCompnayImageId = (ImageView) itemView.findViewById(R.id.ivCompanyNameId);
            tvCompanyNameId = (TextView) itemView.findViewById(R.id.tvCompanyNameId);


        }
    }
}
