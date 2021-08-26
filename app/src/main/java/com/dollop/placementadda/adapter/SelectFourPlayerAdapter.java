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
import com.dollop.placementadda.model.LeaderBoardModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kasim on 03-04-2018.
 */

public class SelectFourPlayerAdapter extends RecyclerView.Adapter<SelectFourPlayerAdapter.ViewHolder> {
    Context context;
    public static ArrayList<LeaderBoardModel> leaderBoardModelList;
    private int lastSelectedPosition = -1;
    private int lenghtCount = 0;
    private ArrayList<String> stringsSelectedPlayer=new ArrayList<String>();
    int trueCount=0;
    public SelectFourPlayerAdapter(Context context, List<LeaderBoardModel> leaderBoardModelList) {
        this.context = context;
        this.leaderBoardModelList = (ArrayList<LeaderBoardModel>) leaderBoardModelList;
        //this.stringsSelectedPlayer=stringsSelectedPlayer;

    }

    // Create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_select_four_player, null);
        // create ViewHolder
        SelectFourPlayerAdapter.ViewHolder viewHolder = new SelectFourPlayerAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tvName.setText(leaderBoardModelList.get(position).getUserName());
        viewHolder.uidsTV.setText(leaderBoardModelList.get(position).getUser_id());
        if (viewHolder.ic_user != null) {
            Picasso.with(context).load(Const.URL.IMAGE_URL + leaderBoardModelList.get(position).getUserProfilePic()).error(R.drawable.ic_user).into(viewHolder.ic_user);
        }
        viewHolder.chkSelected.setChecked(leaderBoardModelList.get(position).getSelected());
        viewHolder.chkSelected.setTag(position);
        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) viewHolder.chkSelected.getTag();
             /*   Toast.makeText(context, leaderBoardModelList.get(pos).getUser_id() + " clicked!", Toast.LENGTH_SHORT).show();
*/
                if (leaderBoardModelList.get(pos).getSelected()) {
                    leaderBoardModelList.get(pos).setSelected(false);
                } else {
                    leaderBoardModelList.get(pos).setSelected(true);
                }
            }
        });

    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return leaderBoardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName,uidsTV;
        public CheckBox chkSelected;
        public CircleImageView ic_user;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvName = (TextView) itemLayoutView.findViewById(R.id.UserName);
            uidsTV = (TextView) itemLayoutView.findViewById(R.id.uidsTV);
            ic_user = (CircleImageView) itemLayoutView.findViewById(R.id.ic_user);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.selectCheckBox);

            chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    lastSelectedPosition = getItemCount();
                    if (lenghtCount == 3) {
                        chkSelected.setChecked(false);
                        chkSelected.setSelected(false);
                    } else if (b) {
                        lenghtCount++;
                        LeaderBoardModel modelsofPlayer=new LeaderBoardModel();
                        modelsofPlayer.setUser_id(uidsTV.getText().toString());
                        S.E("string select players"+stringsSelectedPlayer.size());
                        S.E("selected item  chek" + tvName.getText());
                        S.E("selected item  chek" + uidsTV.getText());
                    } else if (!b) {
                        lenghtCount--;
                    }
                }
            });
        }
    }

    // method to access in activity after updating selection
    public List<LeaderBoardModel> getStudentist() {
        return leaderBoardModelList;
    }

    public void setFilter(List<LeaderBoardModel> countryModels) {
        leaderBoardModelList = new ArrayList<>();
        leaderBoardModelList.addAll(countryModels);
        notifyDataSetChanged();
    }
}
