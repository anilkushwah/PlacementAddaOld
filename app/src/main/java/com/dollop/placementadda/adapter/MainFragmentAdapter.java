package com.dollop.placementadda.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.model.MainFragModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kasim on 17-01-2018.
 */

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.MyViewHolder> {
    MainFragModel mainFragModel;
    Context context;
    List<MainFragModel> mainFragModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public MainFragmentAdapter(Context context, List<MainFragModel> mainFragModelList) {
        this.context = context;
        this.mainFragModelList = mainFragModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public MainFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_gridlayout, parent, false);
        MainFragmentAdapter.MyViewHolder viewHolder = new MainFragmentAdapter.MyViewHolder(v);
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
    public void onBindViewHolder(final MainFragmentAdapter.MyViewHolder holder, final int position) {
        MainFragModel mainFragModel = mainFragModelList.get(position);
        holder.quizLogo.setImageDrawable(context.getResources().getDrawable(mainFragModel.getQuixImage()));
        holder.UserName.setText(mainFragModel.getNameTV());
        holder.rankTV.setText(mainFragModel.getRankTV());
        holder.totalNumberTV.setText(mainFragModel.getTotalNumberTV());

    }

    @Override
    public int getItemCount() {
        return (null != mainFragModelList ? mainFragModelList.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView quizLogo;
        TextView UserName, rankTV, totalNumberTV;
        CardView quizMainCardView;

        public MyViewHolder(View itemView) {

            super(itemView);
            quizMainCardView = (CardView) itemView.findViewById(R.id.quizMainCardView);
            quizLogo = (CircleImageView) itemView.findViewById(R.id.quizLogo);
            UserName = (TextView) itemView.findViewById(R.id.UserName);
            rankTV = (TextView) itemView.findViewById(R.id.rankTV);
            totalNumberTV = (TextView) itemView.findViewById(R.id.totalNumberTV);
        }
    }
}
