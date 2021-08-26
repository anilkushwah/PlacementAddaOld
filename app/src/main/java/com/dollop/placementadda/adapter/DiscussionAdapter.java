package com.dollop.placementadda.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.CommentActivity;
import com.dollop.placementadda.activity.basic.ImagePreviewActivity2;
import com.dollop.placementadda.model.DiscussionForumModel;
import com.dollop.placementadda.sohel.S;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.MyViewHolder> {
    private static final int DELAY_SHOWING_SMILE_PANEL = 200;
    Context context;
    int likeCount;
    String post_id;
    RecyclerView comment_recycleview;
    EditText emojiconEditText;
    private List<DiscussionForumModel> timeLineModelList;


    public DiscussionAdapter(Context context, List<DiscussionForumModel> timeLineModelList) {
        this.timeLineModelList = timeLineModelList;
        this.context = context;

    }

    @Override
    public DiscussionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discussionadapter, parent, false);

        return new DiscussionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DiscussionAdapter.MyViewHolder holder, int position) {
        final DiscussionForumModel timeLineModel = timeLineModelList.get(position);
        holder.timeLineUserNameTv.setText(timeLineModel.getUser());
        holder.timeLineDateTv.setText(timeLineModel.getCreated_date());
        holder.post_tv.setText(timeLineModel.getDiscussion_topic());
        holder.comment_count_tv.setText(timeLineModel.getComment_count());

        /*post_id=timeLineModel.getPost_id();*/
        Picasso.with(context)
                .load(timeLineModel.getProfile_pic())
                .error(R.drawable.ic_user).into(holder.timeLineUserImage);

        holder.timeLineUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmPopup(timeLineModel.getProfile_pic());
            }
        });

        if (timeLineModel.getIs_image().equals("1")) {
            if (!timeLineModel.getImage().equals("null") && !timeLineModel.getImage().equals("")) {
                Picasso.with(context)
                        .load(timeLineModel.getImage())
                        .error(R.drawable.ic_user).into(holder.postImageView);
            } else {
                holder.postImageView.setVisibility(View.GONE);
                holder.TitleText_tv.setVisibility(View.VISIBLE);
                if (!timeLineModel.getDiscussion_topic().equals("")) {
                    holder.TitleText_tv.setText(timeLineModel.getDiscussion_topic());
                } else {
                    holder.postImageView.setVisibility(View.GONE);
                    holder.TitleText_tv.setVisibility(View.GONE);
                }
            }
        } else {
            holder.postImageView.setVisibility(View.GONE);
            holder.TitleText_tv.setVisibility(View.VISIBLE);
            if (!timeLineModel.getDiscussion_topic().equals("")) {
                holder.TitleText_tv.setText(timeLineModel.getDiscussion_topic());
            } else {
                holder.postImageView.setVisibility(View.GONE);
                holder.TitleText_tv.setVisibility(View.GONE);
            }
        }
        holder.postImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* ConfirmPopup(timeLineModel.getImage());*/
                Bundle bundle = new Bundle();
                bundle.putString("userProfilePic2", timeLineModel.getImage());
                S.I(context, ImagePreviewActivity2.class, bundle);
            }
        });


        holder.comment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CommentPopup();
                Bundle bundle = new Bundle();
                bundle.putString("discussion_id", timeLineModel.getDiscussion_id());
                S.I(context, CommentActivity.class, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeLineModelList.size();
    }

    public void ConfirmPopup(final String Profile_pic) {
        notifyDataSetChanged();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.confirmfull_image_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        ImageView cancel_img, full_image;
        cancel_img = (ImageView) dialog.findViewById(R.id.cancel_img);
        full_image = (ImageView) dialog.findViewById(R.id.full_image);

        Picasso.with(context).load(Profile_pic).into(full_image);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView timeLineUserNameTv, timeLineDateTv, TitleText_tv, post_tv, likecount_tv, comment_count_tv;
        public CircleImageView timeLineUserImage;
        public ImageView postImageView;
        ShineButton po_image1;
        LinearLayout comment_layout;
        //  public com.like.LikeButton facebookLikeBtn;

        public MyViewHolder(View view) {
            super(view);
            timeLineUserNameTv = (TextView) view.findViewById(R.id.timeLineUserNameTv);
            timeLineDateTv = (TextView) view.findViewById(R.id.timeLineDateTv);
            TitleText_tv = (TextView) view.findViewById(R.id.TitleText_tv);
            comment_count_tv = (TextView) view.findViewById(R.id.comment_count_tv);
            post_tv = (TextView) view.findViewById(R.id.post_tv);
            timeLineUserImage = (CircleImageView) view.findViewById(R.id.timeLineUserImage);
            postImageView = (ImageView) view.findViewById(R.id.postImageView);
            comment_layout = (LinearLayout) view.findViewById(R.id.comment_layout);
        }
    }
}
