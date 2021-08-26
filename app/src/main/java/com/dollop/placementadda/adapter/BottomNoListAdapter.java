package com.dollop.placementadda.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.model.ItemSelectedModel;

import java.util.List;

/**
 * Created by Kasim on 22-01-2018.
 */

public class BottomNoListAdapter extends RecyclerView.Adapter<BottomNoListAdapter.MyViewHolder> {
    private final SharedPreferences mPref;
    private final SharedPreferences.Editor mEditor;
    private final SharedPreferences.Editor mEditor2;
    private final SharedPreferences mPref2;
    Context context;
    List<ItemSelectedModel> quizSubCatModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public BottomNoListAdapter(Context context, List<ItemSelectedModel> quizSubCatModelList) {
        this.context = context;
        this.quizSubCatModelList = quizSubCatModelList;
        mSelectedItemsIds = new SparseBooleanArray();
        mPref = context.getSharedPreferences("person", Context.MODE_PRIVATE);
        mEditor = mPref.edit();
        mPref2 = context.getSharedPreferences("person2", Context.MODE_PRIVATE);
        mEditor2 = mPref2.edit();

    }

    public void setSelected(int pos) {
        try {
            if (quizSubCatModelList.size() > 1) {
                quizSubCatModelList.get(mPref.getInt("position", 0)).setSelected(false);
                mEditor.putInt("position", pos);
                mEditor.commit();
            }
            quizSubCatModelList.get(pos).setSelected(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAttemped(int pos) {
        try {

            quizSubCatModelList.get(pos).setAttemped(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BottomNoListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_position, parent, false);
        BottomNoListAdapter.MyViewHolder viewHolder = new BottomNoListAdapter.MyViewHolder(v);
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
    public void onBindViewHolder(final BottomNoListAdapter.MyViewHolder holder, final int position) {

        holder.titleTV.setText(quizSubCatModelList.get(position).getSelectedListNo());
        if (quizSubCatModelList.get(position).isSelected()) {
            holder.setSelectedId.setBackgroundColor(Color.parseColor("#FF9800"));
        } else {

            if (quizSubCatModelList.get(position).isAttemped()) {
                holder.setSelectedId.setBackgroundColor(Color.GREEN);
            } else {
                holder.setSelectedId.setBackgroundColor(Color.parseColor("#90cFE0B2"));
            }

        }



    }

    @Override
    public int getItemCount() {
        return (null != quizSubCatModelList ? quizSubCatModelList.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout setSelectedId;
        TextView titleTV, typeTV, durationTv, numOfQueTv;
        CardView mainCardview;

        public MyViewHolder(View itemView) {

            super(itemView);
            mainCardview = (CardView) itemView.findViewById(R.id.mainCardview);
            setSelectedId = (LinearLayout) itemView.findViewById(R.id.setSelectedId);
            titleTV = (TextView) itemView.findViewById(R.id.categryNameTv);

        }
    }
}
