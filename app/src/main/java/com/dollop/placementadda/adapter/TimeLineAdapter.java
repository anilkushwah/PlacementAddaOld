package com.dollop.placementadda.adapter;

import android.app.Dialog;
import android.content.Context;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.TimeLineActivity;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.TimeLineCommentModel;
import com.dollop.placementadda.model.TimeLineModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
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



public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.MyViewHolder> {
    Context context;
    private List<TimeLineModel> timeLineModelList;
    int likeCount;
    String post_id;
    RecyclerView comment_recycleview;
    EditText emojiconEditText;
    private static final int DELAY_SHOWING_SMILE_PANEL = 200;
    ArrayList<TimeLineCommentModel> timeLineCommentModelslist = new ArrayList<>();
    private RelativeLayout relativeLayoutId;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView timeLineUserNameTv, timeLineDateTv, TitleText_tv, post_tv, likecount_tv, comment_count_tv;
        public CircleImageView timeLineUserImage;
        public ImageView postImageView;
        ShineButton po_image1;
        LinearLayout comment_layout;
        ImageView tvRemoveId;
        //  public com.like.LikeButton facebookLikeBtn;

        public MyViewHolder(View view) {
            super(view);
            timeLineUserNameTv = (TextView) view.findViewById(R.id.timeLineUserNameTv);
            timeLineDateTv = (TextView) view.findViewById(R.id.timeLineDateTv);
            tvRemoveId = (ImageView) view.findViewById(R.id.tvRemoveId);
            TitleText_tv = (TextView) view.findViewById(R.id.TitleText_tv);
            likecount_tv = (TextView) view.findViewById(R.id.likecount_tv);
            comment_count_tv = (TextView) view.findViewById(R.id.comment_count_tv);
            post_tv = (TextView) view.findViewById(R.id.post_tv);
            timeLineUserImage = (CircleImageView) view.findViewById(R.id.timeLineUserImage);
            postImageView = (ImageView) view.findViewById(R.id.postImageView);
            po_image1 = (ShineButton) view.findViewById(R.id.po_image1);
            comment_layout = (LinearLayout) view.findViewById(R.id.comment_layout);
        }
    }

    public TimeLineAdapter(Context context, List<TimeLineModel> timeLineModelList) {
        this.timeLineModelList = timeLineModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_line_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TimeLineModel timeLineModel = timeLineModelList.get(position);
        holder.timeLineUserNameTv.setText(timeLineModel.getUserName());
        holder.timeLineDateTv.setText(timeLineModel.getCreated_date());
        holder.post_tv.setText(timeLineModel.getPost());
        holder.likecount_tv.setText(timeLineModel.getLike_count());
        holder.comment_count_tv.setText(timeLineModel.getComment_count());
        post_id = timeLineModel.getPost_id();

        if (timeLineModel.getUser_id().equals(UserDataHelper.getInstance().getList().get(0).getUserId())) {
            holder.tvRemoveId.setVisibility(View.VISIBLE);
        } else {
            holder.tvRemoveId.setVisibility(View.GONE);
        }

        Picasso.with(context)
                .load(timeLineModel.getUserProfilePic())
                .error(R.drawable.ic_user).into(holder.timeLineUserImage);
        holder.timeLineUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmPopup(timeLineModel.getUserProfilePic());
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
                if (!timeLineModel.getPost().equals("")) {
                    holder.TitleText_tv.setText(timeLineModel.getPost());
                } else {
                    holder.postImageView.setVisibility(View.GONE);
                    holder.TitleText_tv.setVisibility(View.GONE);
                }
            }
        }
        if (timeLineModel.getIs_like().equals("1")) {
            holder.po_image1.setChecked(true);
        } else {
            holder.po_image1.setChecked(false);
        }
        holder.po_image1.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                if (checked) {
                    S.E("checked if");
                    new JSONParser(context).parseVollyStringRequest(Const.URL.submitLike, 1, getPostParams(timeLineModel.getPost_id()), new Helper() {
                        @Override
                        public void backResponse(String response) {
                            S.E("getContest response" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("message").equals("success")) {

                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    holder.likecount_tv.setText(jsonObject1.getString("like_count"));

                                } else {
                                    S.T(context, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                S.E("errror" + e);
                            }
                        }/*167222*/
                    });

                } else {
                    S.E("checked else");
                    new JSONParser(context).parseVollyStringRequest(Const.URL.submitLike, 1, getPostParams(timeLineModel.getPost_id()), new Helper() {
                        @Override
                        public void backResponse(String response) {
                            S.E("getContest response" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("message").equals("success")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    holder.likecount_tv.setText(jsonObject1.getString("like_count"));
                                } else {
                                    S.T(context, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                S.E("errror" + e);
                            }
                        }/*167222*/
                    });

                }

            }
        });
        holder.comment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentPopup(timeLineModel.getPost_id());

            }
        });
        holder.postImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmPopup(timeLineModel.getImage());
            }
        });
        holder.tvRemoveId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.tvRemoveId);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Edit")){
                            ((TimeLineActivity) context).editTest(timeLineModel);

                        } if(item.getTitle().equals("Delete")) {
                            ((TimeLineActivity) context).deletPost(timeLineModel.getPost_id());
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeLineModelList.size();
    }

    protected Map<String, String> getPostParams(String Post_id) {
        Map<String, String> param = new HashMap<>();
        param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        param.put("post_id", Post_id);
        S.E("prams : : " + param);
        return param;
    }

    public void CommentPopup(final String post_id) {
        notifyDataSetChanged();
        //EmojIconActions emojIcon;
        View rootView;
        FloatingActionButton send;
        final Dialog dialog = new Dialog(context);
        S.E("checkPostId::" + post_id);
        dialog.setContentView(R.layout.comment_dailog_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        emojiconEditText = (EditText) dialog.findViewById(R.id.editEmojicon);
        send = (FloatingActionButton) dialog.findViewById(R.id.send);
        rootView = dialog.findViewById(R.id.root_view);
        ImageView cancel_img, emojiImageView;

        cancel_img = (ImageView) dialog.findViewById(R.id.cancel_img);
        emojiImageView = (ImageView) dialog.findViewById(R.id.emojiIcon);
        relativeLayoutId = (RelativeLayout) dialog.findViewById(R.id.relativeLayoutId);
        comment_recycleview = (RecyclerView) dialog.findViewById(R.id.comment_recycleview);

        getCommentTimeLine(post_id);


        if (timeLineCommentModelslist.size() == 0) {
            relativeLayoutId.getLayoutParams().height = 200;
            cancel_img.setVisibility(View.GONE);
            //relativeLayoutId.setMa

        } else {
            relativeLayoutId.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            cancel_img.setVisibility(View.VISIBLE);
        }
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TimeLineActivity) context).getRefresh();
                dialog.dismiss();
            }
        });

     /*   emojIcon = new EmojIconActions(context, rootView, emojiconEditText, emojiImageView);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("", "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("", "Keyboard closed");
            }
        });*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitCommentTimeLineOnServer(post_id);
            }
        });

        dialog.show();
    }

    private void getCommentTimeLine(String post_id) {
        new JSONParser(context).parseVollyStringRequest(Const.URL.getComments, 1, getCommentParams(post_id), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("getContest response" + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        timeLineCommentModelslist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            TimeLineCommentModel contestlistModel = new TimeLineCommentModel();
                            contestlistModel.setUser_name(jsonObject2.getString("user"));
                            contestlistModel.setComment(jsonObject2.getString("comment"));
                            contestlistModel.setProfile_pic(jsonObject2.getString("profile_pic"));
                            contestlistModel.setCreated_date(jsonObject2.getString("created_date"));
                            timeLineCommentModelslist.add(contestlistModel);
                        }
                        S.E("list size" + timeLineModelList.size());
                        if (timeLineCommentModelslist.size()== 0) {
//                            comment_recycleview.setVisibility(View.GONE);
                            relativeLayoutId.getLayoutParams().height = 200;

                        } else {
                            relativeLayoutId.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
//                            comment_recycleview.setVisibility(View.VISIBLE);
                            TimeLineCommentAdapter timeLineAdapter = new TimeLineCommentAdapter(context, timeLineCommentModelslist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                            comment_recycleview.setLayoutManager(mLayoutManager);
                            comment_recycleview.setItemAnimator(new DefaultItemAnimator());
                            comment_recycleview.setAdapter(timeLineAdapter);
                            timeLineAdapter.notifyDataSetChanged();
                        }


                    } else {

                        comment_recycleview.setVisibility(View.GONE);
                        S.T(context, jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    S.E("errror" + e);
                }
            }/*167222*/
        });

    }

    protected Map<String, String> getCommentParams(String post_id) {
        Map<String, String> param = new HashMap<>();
        param.put("post_id", post_id);

        return param;
    }

    private void SubmitCommentTimeLineOnServer(final String post_id) {
        new JSONParser(context).parseVollyStringRequest(Const.URL.submitComment, 1, getSubmitCommentParams(post_id), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("SubmitCommentTimeLineOnServer response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        emojiconEditText.setText("");
                        getCommentTimeLine(post_id);
                        ((TimeLineActivity) context).getRefresh();
                    } else {
                        S.T(context, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    S.E("errror" + e);
                }
            }/*167222*/
        });
    }

    protected Map<String, String> getSubmitCommentParams(String post_id) {
        Map<String, String> param = new HashMap<>();
        param.put("post_id", post_id);
        param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        param.put("comment", emojiconEditText.getText().toString());
        S.E("prams : : " + param);
        return param;
    }

    public void ConfirmPopup(String Profile_pic) {
        S.E("Profile_pic::"+Profile_pic);
        notifyDataSetChanged();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.confirmfull_image_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        ImageView cancel_img, full_image;
        cancel_img = (ImageView) dialog.findViewById(R.id.cancel_img);
        full_image = (ImageView) dialog.findViewById(R.id.full_image);

        if(Profile_pic.equals("") ){
            S.E("Profile_pic:: NULL");
            dialog.dismiss();
        }else {
            S.E("Profile_pic:: "+Profile_pic);
            Picasso.with(context).load(Profile_pic).into(full_image);
        }
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}