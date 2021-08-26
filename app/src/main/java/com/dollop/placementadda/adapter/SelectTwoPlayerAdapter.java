package com.dollop.placementadda.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.SelectTwoPlayerActivity;
import com.dollop.placementadda.model.LeaderBoardModel;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kasim on 02-04-2018.
 */

public class SelectTwoPlayerAdapter extends RecyclerView.Adapter<SelectTwoPlayerAdapter.ViewHolder>{
    Context context;String UserID;
    private List<LeaderBoardModel> stList;
    private int lastSelectedPosition = -1;
    String Member_count;
 //   private List<LeaderBoardModel> mFilteredList;
    public SelectTwoPlayerAdapter(Context context,List<LeaderBoardModel> stList,String UserID,String Member_count) {
        this.stList=stList;
        this.context=context;
        this.UserID=UserID;
        this.Member_count=Member_count;
       // mFilteredList=  arraylist;
    }
    // Create new views
    @Override
    public SelectTwoPlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.data_select_two_player, null);
        // create ViewHolder
        SelectTwoPlayerAdapter.ViewHolder viewHolder = new SelectTwoPlayerAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final SelectTwoPlayerAdapter.ViewHolder viewHolder, final int position) {
        final LeaderBoardModel leaderBoardModel=stList.get(position);
        S.E("Memeber count=="+Member_count);
        viewHolder.chkSelected.setChecked(leaderBoardModel.getSelected());
        if (Member_count.equals("2")){
           // viewHolder.selectradio.setVisibility(View.VISIBLE);
            viewHolder.chkSelected.setVisibility(View.VISIBLE);
        }else {
          //  viewHolder.selectradio.setVisibility(View.GONE);
            viewHolder.chkSelected.setVisibility(View.VISIBLE);
        }
        if (UserID.equals("")){
            S.E("SelectedTwo if is working");
          //  viewHolder.selectradio.setChecked(false);
        }else if (UserID.equals(leaderBoardModel.getUser_id())){
            S.E("SelectedTwo else is working");
          //  viewHolder.selectradio.setChecked(true);
        }
        viewHolder.tvName.setText(leaderBoardModel.getUserName());
        viewHolder.userIDsTv.setText(leaderBoardModel.getUser_id());
        if (viewHolder.ic_user != null) {
            Picasso.with(context).
                    load(leaderBoardModel
                            .getUserProfilePic())
                    .into(viewHolder.ic_user);
        }
    /*    viewHolder.selectradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   }
        });*/

        viewHolder.chkSelected.setOnCheckedChangeListener(null);
        viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    viewHolder.chkSelected.setSelected(true);
                    if (Member_count.equals("2")) {
                        ((SelectTwoPlayerActivity) context).GetUserId(position, leaderBoardModel.getUser_id(), leaderBoardModel.getUserName(), leaderBoardModel.getUserProfilePic(), leaderBoardModel.getUserCity(), leaderBoardModel.getUserGender(),"Checked");
                    } else if (Member_count.equals("3")){
                        ((SelectTwoPlayerActivity)context).ThreeGetUserId(leaderBoardModel.getUser_id(),leaderBoardModel.getUserName(),leaderBoardModel.getUserProfilePic(),leaderBoardModel.getUserCity(),leaderBoardModel.getUserGender(),"Checked",position);

                    }
                    else if (Member_count.equals("4")){
                        ((SelectTwoPlayerActivity)context).GetFourUser_id(leaderBoardModel.getUser_id(),leaderBoardModel.getUserName(),leaderBoardModel.getUserProfilePic(),leaderBoardModel.getUserCity(),leaderBoardModel.getUserGender(),"Checked",position);
                    }
                }else {
                    viewHolder.chkSelected.setSelected(true);
                    if (Member_count.equals("2")){
                        ((SelectTwoPlayerActivity)context).GetUserId(position, "","","","","","notChecked");}
                    else if (Member_count.equals("3")){
                        ((SelectTwoPlayerActivity)context).ThreeGetUserId(leaderBoardModel.getUser_id(),leaderBoardModel.getUserName(),leaderBoardModel.getUserProfilePic(),leaderBoardModel.getUserCity(),leaderBoardModel.getUserGender(),"notChecked",position);
                    }else if(Member_count.equals("4")){
                        ((SelectTwoPlayerActivity)context).GetFourUser_id(leaderBoardModel.getUser_id(),leaderBoardModel.getUserName(),leaderBoardModel.getUserProfilePic(),leaderBoardModel.getUserCity(),leaderBoardModel.getUserGender(),"notChecked",position);
                    }
                }

            }
        });
        }
    // Return the size arraylist
    @Override
    public int getItemCount() {
        return stList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView tvName,userIDsTv;
        public CheckBox chkSelected;
        public CircleImageView ic_user;

        //  private int lastSelectedPosition=-1;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.setIsRecyclable(false);
            tvName = (TextView) itemLayoutView.findViewById(R.id.UserName);
            userIDsTv = (TextView) itemLayoutView.findViewById(R.id.userIDsTv);
            ic_user = (CircleImageView) itemLayoutView.findViewById(R.id.ic_user);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.selectCheckBox);

        }
    }

    public void setFilter(List<LeaderBoardModel> countryModels){
        stList = new ArrayList<>();
        stList.addAll(countryModels);
        notifyDataSetChanged();
    }
}
