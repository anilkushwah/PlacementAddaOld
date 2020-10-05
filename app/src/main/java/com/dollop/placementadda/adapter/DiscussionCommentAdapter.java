package com.dollop.placementadda.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.CommentActivity;
import com.dollop.placementadda.activity.DiscussionForumActivity;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.DiscussionCommentModel;
import com.dollop.placementadda.model.DiscussionCommentraiting_Model;
import com.dollop.placementadda.model.DiscussionForumModel;
import com.dollop.placementadda.model.TimeLineCommentModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class DiscussionCommentAdapter extends RecyclerView.Adapter<DiscussionCommentAdapter.MyViewHolder> {
    Context context;
    private List<DiscussionCommentModel> timeLineModelList;
    int likeCount;
    String post_id;RecyclerView comment_recycleview;
    EmojiconEditText emojiconEditText;
    private static final int DELAY_SHOWING_SMILE_PANEL = 200;
    ArrayList<DiscussionCommentraiting_Model> discussionCommentraiting_models;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtViewCommentUserName, Comment_time_tv,txtViewSingleComment;
        CircleImageView comment_user_circullerimage;
        SimpleRatingBar ansRating;
        LinearLayout mainlayout_layout;RelativeLayout mainmessagelayout_layout;

        public MyViewHolder(View view) {
            super(view);
            txtViewCommentUserName = (TextView) itemView.findViewById(R.id.txtViewCommentUserName);
            Comment_time_tv = (TextView) itemView.findViewById(R.id.Comment_time_tv);
            txtViewSingleComment = (TextView) itemView.findViewById(R.id.txtViewSingleComment);
            comment_user_circullerimage = (CircleImageView) itemView.findViewById(R.id.comment_user_circullerimage);
            ansRating = (SimpleRatingBar) itemView.findViewById(R.id.ansRating);
            mainlayout_layout = (LinearLayout) itemView.findViewById(R.id.mainlayout_layout);
            mainmessagelayout_layout = (RelativeLayout) itemView.findViewById(R.id.mainmessagelayout_layout);
        }
    }

    public DiscussionCommentAdapter(Context context, List<DiscussionCommentModel> timeLineModelList) {
        this.timeLineModelList = timeLineModelList;
        this.context = context;

    }

    @Override
    public DiscussionCommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discussion_comment_item, parent, false);

        return new DiscussionCommentAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final DiscussionCommentAdapter.MyViewHolder holder, int position) {
        final DiscussionCommentModel timeLineModel = timeLineModelList.get(position);
        if (timeLineModel.getUser_id().equals(UserDataHelper.getInstance().getList().get(0).getUserId())){
            holder.mainlayout_layout.setGravity(Gravity.RIGHT);
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
            layout.gravity = Gravity.RIGHT;
            holder.mainlayout_layout.setLayoutParams(layout);
        }else {

            holder.mainlayout_layout.setGravity(Gravity.LEFT);
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.LEFT);
            layout.gravity = Gravity.LEFT;
            holder.mainlayout_layout.setLayoutParams(layout);
        }
        holder.txtViewCommentUserName.setText(timeLineModel.getUser());
        holder.Comment_time_tv.setText(timeLineModel.getCreated_date());
        holder.txtViewSingleComment.setText(timeLineModel.getComment());
        Picasso.with(context).load(timeLineModel.getProfile_pic()).into(holder.comment_user_circullerimage);
         S.E("raiting"+timeLineModel.getRating());
         holder.ansRating.setRating(Float.parseFloat(timeLineModel.getRating()));

     // LayerDrawable stars = (LayerDrawable) holder.ansRating.getProgressDrawable();

        holder.ansRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RaitingPopup(timeLineModel.getDiscussion_comment_id(),timeLineModel.getUser_id());
            }
        });
        holder.comment_user_circullerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmPopup(timeLineModel.getProfile_pic());
            }
        });

    }

    @Override
    public int getItemCount() {
        return timeLineModelList.size();
    }
    public void RaitingPopup(final String Discussion_comment_id, final String User_id) {
        notifyDataSetChanged();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.taiting_confirm_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        ImageView cancel_img;
        final RatingBar ansRating;Button send;
        //getDiscussionComment(Discussion_comment_id);
        CircleImageView  comment_user_circullerimage;TextView txtViewCommentUserName;
        comment_user_circullerimage = (CircleImageView) dialog.findViewById(R.id.comment_user_circullerimage);
        txtViewCommentUserName = (TextView) dialog.findViewById(R.id.txtViewCommentUserName);
        cancel_img = (ImageView) dialog.findViewById(R.id.cancel_img);
        ansRating = (RatingBar) dialog.findViewById(R.id.ansRating);
        send = (Button) dialog.findViewById(R.id.send);

        new JSONParser(context).parseVollyStringRequest(Const.URL.getRatingOnComment, 1, getPostParams(Discussion_comment_id), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("getDiscussionComment response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        discussionCommentraiting_models=new ArrayList<>();
                        discussionCommentraiting_models.clear();
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
                            DiscussionCommentraiting_Model contestlistModel=new DiscussionCommentraiting_Model();
                            contestlistModel.setUser_id(jsonObject2.getString("user_id"));
                            contestlistModel.setRating(jsonObject2.getString("rating"));
                            discussionCommentraiting_models.add(contestlistModel);
                        }
                        if (discussionCommentraiting_models.size()>0){
                            if (User_id.equals(UserDataHelper.getInstance().getList().get(0).getUserId())){
                            for (int i=0;i<discussionCommentraiting_models.size();i++){
                                if (UserDataHelper.getInstance().getList().get(0).getUserId().equals(discussionCommentraiting_models.get(i).getUser_id())){
                                    S.E("User_id"+UserDataHelper.getInstance().getList().get(0).getUserId());
                                    S.E("User_id_discussion"+discussionCommentraiting_models.get(i).getUser_id());
                                    ansRating.setRating(Float.parseFloat(discussionCommentraiting_models.get(i).getRating()));
                                }
                            }
                        }
                        }
                    } else {


                        S.T(context, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    S.E("errror"+e);
                }
            }/*167222*/
        });


        if (User_id.equals(UserDataHelper.getInstance().getList().get(0).getUserId())){
            send.setVisibility(View.GONE);
        }
        else {
            send.setVisibility(View.VISIBLE);
        }
        try{
            String image=UserDataHelper.getInstance().getList().get(0).getProfile_pic();
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            comment_user_circullerimage.setImageBitmap(bitmap);
            txtViewCommentUserName.setText(UserDataHelper.getInstance().getList().get(0).getUserName());

        }catch(Exception e){
            e.getMessage();

        }

        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // SubmitCommentRaiting(String.valueOf(ansRating.getRating()),Discussion_comment_id);
                if (!String.valueOf(ansRating.getRating()).equals("")){
                new JSONParser(context).parseVollyStringRequest(Const.URL.submitDiscussionRating, 1, getCommentParams(String.valueOf(ansRating.getRating()),Discussion_comment_id), new Helper() {
                    @Override
                    public void backResponse(String response) {
                        S.E("getContest response" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("message").equals("success")) {
                                ((CommentActivity)context).getRefreshRaiting();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            S.E("errror"+e);
                        }
                    }/*167222*/
                });}
                else {
                    S.T(context,"Please Give Raiting First !");
                }
                }
        });
        dialog.show();
    }
    protected Map<String, String> getCommentParams(String rating,String Discussion_comment_id) {
        Map<String, String> param = new HashMap<>();
        param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        param.put("discussion_comment_id", Discussion_comment_id);
        param.put("rating", rating);
        S.E("SubmitCommentRaiting prams" + param);
        return param;
    }
    public void ConfirmPopup(final String Profile_pic) {
        notifyDataSetChanged();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.confirmfull_image_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        ImageView cancel_img,full_image;
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
    private void getDiscussionComment(String Discussion_comment_id) {
        new JSONParser(context).parseVollyStringRequest(Const.URL.getRatingOnComment, 1, getPostParams(Discussion_comment_id), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("getDiscussionComment response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        discussionCommentraiting_models=new ArrayList<>();
                        discussionCommentraiting_models.clear();
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
                            DiscussionCommentraiting_Model contestlistModel=new DiscussionCommentraiting_Model();
                            contestlistModel.setUser_id(jsonObject2.getString("user_id"));
                            contestlistModel.setRating(jsonObject2.getString("rating"));
                            discussionCommentraiting_models.add(contestlistModel);
                            }

                        } else {


                        S.T(context, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    S.E("errror"+e);
                }
            }/*167222*/
        });

    }

    protected Map<String, String> getPostParams(String Discussion_comment_id) {
        Map<String, String> param = new HashMap<>();
        param.put("discussion_comment_id", Discussion_comment_id);
        S.E("prams : : " + param);
        return param;
    }
}
