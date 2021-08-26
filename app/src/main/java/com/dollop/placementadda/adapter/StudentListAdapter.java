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
import com.dollop.placementadda.model.PlacedStudentModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kasim on 22-01-2018.
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {
    Context context;
    List<PlacedStudentModel> QuizListModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public StudentListAdapter(Context context, List<PlacedStudentModel> QuizListModelList) {
        this.context = context;
        this.QuizListModelList = QuizListModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public StudentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_student_name, parent, false);
        StudentListAdapter.MyViewHolder viewHolder = new StudentListAdapter.MyViewHolder(v);
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
    public void onBindViewHolder(final StudentListAdapter.MyViewHolder holder, final int position) {
        final PlacedStudentModel subCatModel = QuizListModelList.get(position);
        if (subCatModel.profile_pic == null||subCatModel.profile_pic.equals("null")) {


        } else {
            Picasso.with(context).load(subCatModel.profile_pic).error(R.drawable.ic_user).into(holder.ivStudentId);
        }
        holder.tvStudentNameId.setText(subCatModel.full_name);
        holder.tvYearId.setText(subCatModel.year);
        holder.tvPackageId.setText(subCatModel.packg);


    }

    @Override
    public int getItemCount() {
        return QuizListModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvYearId;
        ImageView ivStudentId;
        TextView tvPackageId, tvStudentNameId, tvCompanyNameId;
        CardView mainCardview;

        public MyViewHolder(View itemView) {

            super(itemView);
            mainCardview = (CardView) itemView.findViewById(R.id.mainCardview);
            ivStudentId = (ImageView) itemView.findViewById(R.id.ivStudentId);
            tvStudentNameId = (TextView) itemView.findViewById(R.id.tvStudentNameId);
            tvYearId = (TextView) itemView.findViewById(R.id.tvYearId);
            tvPackageId = (TextView) itemView.findViewById(R.id.tvPackageId);


        }
    }
}
