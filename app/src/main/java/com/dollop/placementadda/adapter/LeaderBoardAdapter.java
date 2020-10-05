package com.dollop.placementadda.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.database.datahelper.UserDataHelper;

import com.dollop.placementadda.model.LeaderBoardModel;
import com.dollop.placementadda.model.MainFragModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kasim on 17-01-2018.
 */

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {
    private int postionForselected = 0;

    MainFragModel mainFragModel;
    Context context;
    List<LeaderBoardModel> mainFragModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public LeaderBoardAdapter(Context context, ArrayList<LeaderBoardModel> mainFragModelList, int postionForselected) {
        Log.e("Position",""+ postionForselected);
        this.context = context;
        this.mainFragModelList = mainFragModelList;
        this.postionForselected = postionForselected;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leader_list, parent, false);
        return new MyViewHolder(itemView);
    }




    //Put or delete selected position into SparseBooleanArray


    @Override
    public void onBindViewHolder(final LeaderBoardAdapter.MyViewHolder holder,int position) {
        LeaderBoardModel mainFragModel = mainFragModelList.get(position);
        holder.UserName.setText(mainFragModel.getUserName());
        int showRanks = position + 1;
        holder.rankTV.setText("" + showRanks);
        holder.totalNumberTV.setText(mainFragModel.getRankingPoints());

      /*  Log.e("Psition",""+position);*/
        S.E("leaderboard adapter"+postionForselected);
       /* if (position == postionForselected) {
            Log.e("POsition",""+position);
            holder.quizMainCardViewLinear.setBackgroundResource(R.drawable.selectededitview);
        }*/
       if (position<3){
           holder.quizMainCardViewLinear.setBackgroundResource(R.drawable.selectededitview);
       }else{
           holder.quizMainCardViewLinear.setBackgroundColor(context.getResources().getColor(R.color.coloraccent));
       }


        if (mainFragModel.getUserProfilePic()!=null) {
            Picasso.with(context).load(Const.URL.IMAGE_URL + mainFragModel.getUserProfilePic()).error(R.drawable.ic_user).into(holder.quizLogo);

        } else {
        }
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
//        CircleImageView quizLogo;
//        TextView UserName, rankTV, totalNumberTV;
//        CardView quizMainCardView;
//        LinearLayout quizMainCardViewLinear;

        CircleImageView quizLogo;
        TextView UserName, rankTV, totalNumberTV;
        CardView quizMainCardView;
        RelativeLayout quizMainCardViewLinear;
        public MyViewHolder(View itemView) {

            super(itemView);
            quizMainCardView = (CardView) itemView.findViewById(R.id.quizMainCardView);
            quizLogo = (CircleImageView) itemView.findViewById(R.id.ic_user);
            UserName = (TextView) itemView.findViewById(R.id.UserName);
            rankTV = (TextView) itemView.findViewById(R.id.rankTV);
            quizMainCardViewLinear = (RelativeLayout) itemView.findViewById(R.id.quizMainCardViewLinear);
            totalNumberTV = (TextView) itemView.findViewById(R.id.totalNumberTV);
        }
    }
    @Override
    public int getItemCount() {
        return mainFragModelList.size();
    }
}
