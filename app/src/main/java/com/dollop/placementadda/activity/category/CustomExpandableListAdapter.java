package com.dollop.placementadda.activity.category;

import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;

import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.CreateGroupActivity;
import com.dollop.placementadda.activity.QuizMultiplierActivity;
import com.dollop.placementadda.model.QuizSubCategoryModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<QuizSubCategoryModel>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<QuizSubCategoryModel>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final QuizSubCategoryModel expandedListText = (QuizSubCategoryModel) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.data_subcat_layout, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.tvSubCategoryNameId);
        TextView tvGoId = (TextView) convertView.findViewById(R.id.tvGoId);
        ImageView ivSubCategoryImageId = (ImageView) convertView.findViewById(R.id.ivSubCategoryImageId);
        TextView tvPayandBeatOtherTd = (TextView) convertView.findViewById(R.id.tvPayandBeatOtherTd);

       tvPayandBeatOtherTd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_play_and_beat_player);
               // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                WindowManager.LayoutParams layoutParams =new WindowManager.LayoutParams();
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(layoutParams);
                dialog.show();
                CardView tvPlayWithFriend = dialog.findViewById(R.id.PlayWithFriendCardview);
                CardView tvMultiplayer =dialog.findViewById(R.id.multiplayerCardViewId);
                ImageView closebtnId =dialog.findViewById(R.id.closebtnId);
                closebtnId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tvPlayWithFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        S.I(context, CreateGroupActivity.class, null);

                    }
                });
                tvMultiplayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       S.I(context, QuizMultiplierActivity.class, null);
                    }
                });
            }
        });


        Picasso.with(context).load(Const.URL.Categry_Image+expandedListText.category_image).into(ivSubCategoryImageId);


//        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText.categoryName);


        tvGoId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((QuizCategoryActivity)context).getSelectedCategerySubject(expandedListText.categoty_id,expandedListText.actual_categoty_id);

            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}