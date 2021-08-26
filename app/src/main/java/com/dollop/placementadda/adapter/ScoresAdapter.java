package com.dollop.placementadda.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.LeaderBoardModel;
import com.dollop.placementadda.sohel.Const;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ScoresAdapter extends ArrayAdapter<LeaderBoardModel> {
    Activity activity;
    int layoutResourceId;
    List<LeaderBoardModel> data = new ArrayList<>();
    LeaderBoardModel pdf;
    private int postionForselected = 0;

//    SmartUser currentUser;

    public ScoresAdapter(Activity activity, int layoutResourceId, List<LeaderBoardModel> data) {
        super(activity, layoutResourceId, data);
        this.activity = activity;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PdfHolder holder = null;
//        currentUser = UserSessionManager.getCurrentUser(activity);
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new PdfHolder();
            for (int i = position; i < 3; i++) {
                row.setBackgroundColor(Color.parseColor("#000000"));
            }
            holder.quizMainCardView = (CardView) row.findViewById(R.id.quizMainCardView);
            holder.quizLogo = (CircleImageView) row.findViewById(R.id.ic_user);
            holder.UserName = (TextView) row.findViewById(R.id.UserName);
            holder.rankTV = (TextView) row.findViewById(R.id.rankTV);
            holder.quizMainCardViewLinear = (RelativeLayout) row.findViewById(R.id.quizMainCardViewLinear);
            holder.totalNumberTV = (TextView) row.findViewById(R.id.totalNumberTV);
            holder.quizLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getContext());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    LeaderBoardModel mainFragModel = data.get(position);
                    dialog.setContentView(R.layout.item_dialog_images);
                    dialog.setTitle("Image");
                    ImageView myImage = (ImageView) dialog.findViewById(R.id.dialogImageView);
                    Picasso.with(activity)
                            .load(Const.URL.IMAGE_URL + mainFragModel.getUserProfilePic())
                            .error(R.drawable.ic_user).into(myImage);
                    dialog.show();
                }
            });
            row.setTag(holder);
        } else {
            holder = (PdfHolder) row.getTag();
        }

        pdf = data.get(position);
        // Chcek for empty status message
        LeaderBoardModel mainFragModel = data.get(position);
        holder.UserName.setText(mainFragModel.getUserName());
        int showRanks = position + 1;
        holder.rankTV.setText("" + showRanks);
        holder.totalNumberTV.setText(mainFragModel.getRankingPoints());

        Log.e("Psition", "" + position);

        if (UserDataHelper.getInstance().getList().get(0).getUserId().equals(pdf.getUser_id())) {
            holder.quizMainCardViewLinear.setBackgroundResource(R.drawable.selectededitview);
        }

        if (mainFragModel.getUserProfilePic() != null) {
            Picasso.with(activity).load(Const.URL.IMAGE_URL + mainFragModel.getUserProfilePic()).error(R.drawable.ic_user).into(holder.quizLogo);

        } else {
        }

        return row;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class PdfHolder {
        CircleImageView quizLogo;
        TextView UserName, rankTV, totalNumberTV;
        CardView quizMainCardView;
        RelativeLayout quizMainCardViewLinear;
    }

}