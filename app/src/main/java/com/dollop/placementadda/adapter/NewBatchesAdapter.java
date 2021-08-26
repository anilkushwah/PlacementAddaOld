package com.dollop.placementadda.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.model.NewBatchesModel;
import com.dollop.placementadda.sohel.S;

import java.util.List;

/**
 * Created by Kasim on 22-01-2018.
 */

public class NewBatchesAdapter extends RecyclerView.Adapter<NewBatchesAdapter.MyViewHolder> {
    Context context;
    List<NewBatchesModel> NewBatchesModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public NewBatchesAdapter(Context context, List<NewBatchesModel> NewBatchesModelList) {
        this.context = context;
        this.NewBatchesModelList = NewBatchesModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public NewBatchesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_batches, parent, false);
        NewBatchesAdapter.MyViewHolder viewHolder = new NewBatchesAdapter.MyViewHolder(v);
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
    public void onBindViewHolder(final NewBatchesAdapter.MyViewHolder holder, final int position) {
        final NewBatchesModel newBatchesModel = NewBatchesModelList.get(position);
        holder.tvContentId.setTextColor(context.getResources().getColor(R.color.red));
        holder.tvHeaderID.setText(newBatchesModel.getBatch_title());

        holder.tvContentId.setText(newBatchesModel.getBatch_detail());
        holder.tvStartDateId.setText(newBatchesModel.getBatch_start_date());
        S.E("Check new batch TIME::" + newBatchesModel.getStart_time());

        holder.tvTimeId.setText(newBatchesModel.getNewBacthTime());
        if (newBatchesModel.getNotificationorBatch().equals("0")) {
            holder.interstedBtnId.setVisibility(View.GONE);
        }else if(newBatchesModel.getNotificationorBatch().equals("1")){
            holder.interstedBtnId.setVisibility(View.VISIBLE);

        }
        if (newBatchesModel.getDateChange().equals("SameDate")) {
            holder.layoutForDateId.setVisibility(View.GONE);

        } else {
            holder.tvDateId.setText(newBatchesModel.getDateChange());
            holder.layoutForDateId.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return (null != NewBatchesModelList ? NewBatchesModelList.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvStartDateId, tvTimeId, tvDateId;
        TextView tvHeaderID, tvContentId;
        LinearLayout layoutForDateId;
        Button interstedBtnId;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvHeaderID = (TextView) itemView.findViewById(R.id.tvHeaderID);
            tvContentId = (TextView) itemView.findViewById(R.id.tvContentId);
            tvStartDateId = (TextView) itemView.findViewById(R.id.tvStartDateId);
            tvTimeId = (TextView) itemView.findViewById(R.id.tvStartTimeId);
            tvDateId = (TextView) itemView.findViewById(R.id.tvDateId);
            layoutForDateId = (LinearLayout) itemView.findViewById(R.id.layoutForDateId);
            interstedBtnId = (Button) itemView.findViewById(R.id.interstedBtnId);

        }
    }
}
