package com.dollop.placementadda.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.SelectTwoPlayerActivity;
import com.dollop.placementadda.model.CreateGroupModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.SavedData;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kasim on 20-03-2018.
 */

public class CreateGroupAdapter extends RecyclerView.Adapter<CreateGroupAdapter.MyViewHolder> {
    Context context;
    List<CreateGroupModel> quizCategoryModelList;
    private SparseBooleanArray mSelectedItemsIds;
    int balance;

    public CreateGroupAdapter(Context context, List<CreateGroupModel> quizCategoryModelList, int amount) {
        this.balance=amount;
        this.context = context;
        this.quizCategoryModelList = quizCategoryModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public CreateGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list, parent, false);
        CreateGroupAdapter.MyViewHolder viewHolder = new CreateGroupAdapter.MyViewHolder(v);
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
    public void onBindViewHolder(final CreateGroupAdapter.MyViewHolder holder, final int position) {
        final CreateGroupModel quizCategoryModel = quizCategoryModelList.get(position);
/*
        holder.categoryLogoIv.setImageDrawable(context.getResources().getDrawable(quizCategoryModel.getCategoryLogo()));
*/
        holder.playerNameTv.setText(quizCategoryModel.getName());
        if (quizCategoryModel.getName().equals("2 Players")) {
            holder.categoryLogoIv.setImageDrawable(context.getResources().getDrawable(R.drawable.twoplayer));
            } else if (quizCategoryModel.getName().equals("3 Players")) {
            holder.categoryLogoIv.setImageDrawable(context.getResources().getDrawable(R.drawable.threeplayer));
        } else if (quizCategoryModel.getName().equals("4 Players")) {
            holder.categoryLogoIv.setImageDrawable(context.getResources().getDrawable(R.drawable.fourplayer));
        }

        if (quizCategoryModel.getUser_img() != null) {
            Picasso.with(context).
                    load(Const.URL.GROUP_IMAGES + quizCategoryModel.getUser_img())
                    .into(holder.categoryLogoIv);
        }

        holder.playerCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavedData.saveTwoPlayerGroupID(quizCategoryModel.getPlayerGroup_id());
                S.E("player id" + SavedData.getTwoPlayerGroupID());
                if (quizCategoryModel.getTotal_member().equals("2")) {
                    getCoinsDialogBox(quizCategoryModel.getTotal_member());
                    SavedData.saveTwoPlayerGroupID(quizCategoryModel.getPlayerGroup_id());

                } else if (quizCategoryModel.getTotal_member().equals("3")) {
                    getCoinsDialogBox(quizCategoryModel.getTotal_member());
                    SavedData.saveTwoPlayerGroupID(quizCategoryModel.getPlayerGroup_id());

                } else if (quizCategoryModel.getTotal_member().equals("4")) {
                    getCoinsDialogBox(quizCategoryModel.getTotal_member());
                    SavedData.saveTwoPlayerGroupID(quizCategoryModel.getPlayerGroup_id());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != quizCategoryModelList ? quizCategoryModelList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryLogoIv;
        TextView playerNameTv;
        CardView playerCardview;

        public MyViewHolder(View itemView) {
            super(itemView);
            playerCardview = (CardView) itemView.findViewById(R.id.playerCardview);
            playerNameTv = (TextView) itemView.findViewById(R.id.playerNameTv);
            categoryLogoIv = (ImageView) itemView.findViewById(R.id.categoryLogoIv);

        }
    }

    public void getCoinsDialogBox(final String Member_number) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.challege_table_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView cancel_btn = (ImageView) dialog.findViewById(R.id.cancel_img);
        LinearLayout play25 = (LinearLayout) dialog.findViewById(R.id.play25);
        LinearLayout play50 = (LinearLayout) dialog.findViewById(R.id.play50);
        LinearLayout play100 = (LinearLayout) dialog.findViewById(R.id.play100);
        LinearLayout play200 = (LinearLayout) dialog.findViewById(R.id.play200);
        LinearLayout play400 = (LinearLayout) dialog.findViewById(R.id.play400);
        LinearLayout play500 = (LinearLayout) dialog.findViewById(R.id.play500);
        LinearLayout play1000 = (LinearLayout) dialog.findViewById(R.id.play1000);
        LinearLayout play2500 = (LinearLayout) dialog.findViewById(R.id.play2500);
        LinearLayout play5000 = (LinearLayout) dialog.findViewById(R.id.play5000);
        S.E("balance"+balance);
        play25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (balance>=25) {
                    Bundle bundle = new Bundle();
                    bundle.putString("points", "25");
                    bundle.putString("Member_number", Member_number);
                    S.I(context, SelectTwoPlayerActivity.class, bundle);
                    dialog.dismiss();
                }
                else {
                    S.T(context,"You don't have enough coins to play");
                }
            }
        });
        play50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balance >= 50) {
                    Bundle bundle = new Bundle();
                    bundle.putString("points", "50");
                    bundle.putString("Member_number", Member_number);
                    S.I(context, SelectTwoPlayerActivity.class, bundle);
                    dialog.dismiss();
                }
                else {
                    S.T(context,"You don't have enough coins to play");
                }
            }
        });
        play100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balance >= 100) {
                    Bundle bundle = new Bundle();
                    bundle.putString("points", "100");
                    bundle.putString("Member_number", Member_number);
                    S.I(context, SelectTwoPlayerActivity.class, bundle);
                    dialog.dismiss();
                }
                else {
                    S.T(context,"You don't have enough coins to play");
                }
            }
        });
        play200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balance>=200){
                Bundle bundle = new Bundle();
                bundle.putString("points", "200");
                bundle.putString("Member_number", Member_number);
                S.I(context, SelectTwoPlayerActivity.class, bundle);
                dialog.dismiss();
            }
            else {
                    S.T(context,"You don't have enough coins to play");
                }
            }
        });
        play400.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balance >= 400) {
                    Bundle bundle = new Bundle();
                    bundle.putString("points", "400");
                    bundle.putString("Member_number", Member_number);
                    S.I(context, SelectTwoPlayerActivity.class, bundle);
                    dialog.dismiss();
                }
                else {
                    S.T(context,"You don't have enough coins to play");
                }
            }
        });
        play500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balance >= 500) {
                    Bundle bundle = new Bundle();
                    bundle.putString("points", "500");
                    bundle.putString("Member_number", Member_number);
                    S.I(context, SelectTwoPlayerActivity.class, bundle);
                    dialog.dismiss();
                }
                else {
                    S.T(context,"You don't have enough coins to play");
                }
            }
        });
        play1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balance >= 1000) {
                    Bundle bundle = new Bundle();
                    bundle.putString("points", "1000");
                    bundle.putString("Member_number", Member_number);
                    S.I(context, SelectTwoPlayerActivity.class, bundle);
                    dialog.dismiss();
                }
                else {
                    S.T(context,"You don't have enough coins to play");
                }
            }
        });
        play2500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balance >= 2500) {
                    Bundle bundle = new Bundle();
                    bundle.putString("points", "2500");
                    bundle.putString("Member_number", Member_number);
                    S.I(context, SelectTwoPlayerActivity.class, bundle);
                    dialog.dismiss();
                }
            }
        });
        play5000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balance >= 5000) {
                    Bundle bundle = new Bundle();
                    bundle.putString("points", "5000");
                    bundle.putString("Member_number", Member_number);
                    S.I(context, SelectTwoPlayerActivity.class, bundle);
                    dialog.dismiss();
                }
                else {
                    S.T(context,"You don't have enough coins to play");
                }
            }
        });


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}