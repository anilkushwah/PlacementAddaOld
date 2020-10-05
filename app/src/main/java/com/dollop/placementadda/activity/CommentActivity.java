package com.dollop.placementadda.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.DiscussionAdapter;
import com.dollop.placementadda.adapter.DiscussionCommentAdapter;
import com.dollop.placementadda.adapter.TimeLineAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.DiscussionCommentModel;
import com.dollop.placementadda.model.DiscussionForumModel;
import com.dollop.placementadda.model.TimeLineModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class CommentActivity extends BaseActivity {
String discussion;
RecyclerView discussionCommentRecyclerView;
ArrayList<DiscussionCommentModel> discussionCommentModelslist=new ArrayList<>();
    EmojIconActions emojIcon;
    EmojiconEditText emojiconEditText;
    ImageView emojiImageView;
    View rootView;FloatingActionButton send;
    @Override
    protected int getContentResId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(CommentActivity.this);
        setToolbarWithBackButton("Comment");
        emojiconEditText = (EmojiconEditText)findViewById(R.id.editEmojicon);
        emojiImageView = (ImageView)findViewById(R.id.emojiIcon);
        discussionCommentRecyclerView=(RecyclerView)findViewById(R.id.discussionCommentRecyclerView);
        send=(FloatingActionButton)findViewById(R.id.send);
        Bundle bundle=getIntent().getExtras();
        discussion=bundle.getString("discussion_id");
        getDiscussionComment();
        rootView = findViewById(R.id.root_view);
        emojIcon = new EmojIconActions(CommentActivity.this, rootView, emojiconEditText, emojiImageView);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
            }

            @Override
            public void onKeyboardClose() {
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emojiconEditText.getText().toString().length()==0){
                    S.T(CommentActivity.this,"Please Enter a value.");
                }
                else {
                SubmitDiscussionComment();}
            }
        });
        BroadcastReceiver broadcastReceiver= S.LocalBroadcastReciver(CommentActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }
    private void getDiscussionComment() {
        new JSONParser(CommentActivity.this).parseVollyStringRequest(Const.URL.getDiscussionComments, 1, getPostParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        discussionCommentModelslist.clear();
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
                            DiscussionCommentModel contestlistModel=new DiscussionCommentModel();
                            contestlistModel.setDiscussion_comment_id(jsonObject2.getString("discussion_comment_id"));
                            contestlistModel.setUser(jsonObject2.getString("user"));
                            contestlistModel.setUser_id(jsonObject2.getString("user_id"));
                            contestlistModel.setComment(jsonObject2.getString("comment"));
                            contestlistModel.setRating(jsonObject2.getString("rating"));
                            contestlistModel.setProfile_pic(jsonObject2.getString("profile_pic"));
                            contestlistModel.setCreated_date(jsonObject2.getString("created_date"));
                            discussionCommentModelslist.add(contestlistModel);
                        }
                        if (discussionCommentModelslist.size()==0){
                            discussionCommentRecyclerView.setVisibility(View.GONE);
                        }
                        else {
                            discussionCommentRecyclerView.setVisibility(View.VISIBLE);
                            DiscussionCommentAdapter timeLineAdapter = new DiscussionCommentAdapter(CommentActivity.this,discussionCommentModelslist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            discussionCommentRecyclerView.setLayoutManager(mLayoutManager);
                            discussionCommentRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            discussionCommentRecyclerView.setAdapter(timeLineAdapter);
                            timeLineAdapter.notifyDataSetChanged();
                        }
                        } else {
                        discussionCommentRecyclerView.setVisibility(View.GONE);
                        S.T(CommentActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                }
            }/*167222*/
        });

    }

    protected Map<String, String> getPostParams() {
        Map<String, String> param = new HashMap<>();
        param.put("discussion_id", discussion);
        return param;
    }
    private void SubmitDiscussionComment() {
        new JSONParser(CommentActivity.this).parseVollyStringRequest(Const.URL.submitDiscussionComment, 1, getSubmitDiscussionCommentParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        emojiconEditText.setText("");
                        emojIcon.closeEmojIcon();
                        getDiscussionComment();
                        } else {
                        S.T(CommentActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                }
            }/*167222*/
        });

    }//user_id,discussion_id,comment
    protected Map<String, String> getSubmitDiscussionCommentParams() {
        Map<String, String> param = new HashMap<>();
        param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        param.put("discussion_id", discussion);
        param.put("comment", emojiconEditText.getText().toString());
        return param;
    }
    public void getRefreshRaiting(){
        getDiscussionComment();
    }
}
