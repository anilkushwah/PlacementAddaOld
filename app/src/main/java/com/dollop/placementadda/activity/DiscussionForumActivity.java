package com.dollop.placementadda.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.DiscussionAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.DiscussionForumModel;
import com.dollop.placementadda.model.TimeLineCommentModel;
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


public class DiscussionForumActivity extends BaseActivity {
    RecyclerView discussionUserForumRecyclerView;
    ArrayList<DiscussionForumModel> discussionForumModelslist = new ArrayList<>();
  //  EmojIconActions emojIcon;
    EditText emojiconEditText;
    ImageView emojiImageView, camera;
    View rootView;
    FloatingActionButton send;

    @Override
    protected int getContentResId() {
        return R.layout.activity_discussion_forum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(DiscussionForumActivity.this);
        setToolbarWithBackButton("Discussion Forum");
        discussionUserForumRecyclerView = (RecyclerView) findViewById(R.id.discussionUserForumRecyclerView);
        getPostOnDiscussion();

        emojiconEditText = (EditText) findViewById(R.id.editEmojicon);
        emojiImageView = (ImageView) findViewById(R.id.emojiIcon);
        camera = (ImageView) findViewById(R.id.camera);
        send = (FloatingActionButton) findViewById(R.id.send);
        rootView = findViewById(R.id.root_view);
      /*  emojIcon = new EmojIconActions(DiscussionForumActivity.this, rootView, emojiconEditText, emojiImageView);

        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.add_icon);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {

            }

            @Override
            public void onKeyboardClose() {

            }
        });*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emojiconEditText.getText().toString().length() == 0) {
                    S.T(DiscussionForumActivity.this, "Please Enter a value.");
                } else {
                    PostDiscussion();
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.I(DiscussionForumActivity.this, PostDiscussionActivity.class, null);
            }
        });
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(DiscussionForumActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    private void getPostOnDiscussion() {
        new JSONParser(DiscussionForumActivity.this).parseVollyStringRequest(Const.URL.getDiscussions, 1, getPostParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        discussionForumModelslist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            DiscussionForumModel contestlistModel = new DiscussionForumModel();
                            contestlistModel.setDiscussion_id(jsonObject2.getString("discussion_id"));
                            contestlistModel.setUser(jsonObject2.getString("user"));
                            contestlistModel.setProfile_pic(jsonObject2.getString("profile_pic"));
                            contestlistModel.setDiscussion_topic(jsonObject2.getString("discussion_topic"));
                            contestlistModel.setComment_count(jsonObject2.getString("comment_count"));
                            contestlistModel.setImage(jsonObject2.getString("image"));
                            contestlistModel.setCreated_date(jsonObject2.getString("created_date"));
                            contestlistModel.setIs_image(jsonObject2.getString("is_image"));
                            discussionForumModelslist.add(contestlistModel);
                        }
                        if (discussionForumModelslist.size() == 0) {
                            discussionUserForumRecyclerView.setVisibility(View.GONE);
                        } else {
                            discussionUserForumRecyclerView.setVisibility(View.VISIBLE);
                            DiscussionAdapter timeLineAdapter = new DiscussionAdapter(DiscussionForumActivity.this, discussionForumModelslist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            discussionUserForumRecyclerView.setLayoutManager(mLayoutManager);
                            discussionUserForumRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            discussionUserForumRecyclerView.setAdapter(timeLineAdapter);
                            timeLineAdapter.notifyDataSetChanged();
                        }


                    } else {

                        discussionUserForumRecyclerView.setVisibility(View.GONE);
                        S.T(DiscussionForumActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                }
            }/*167222*/
        });

    }

    protected Map<String, String> getPostParams() {
        Map<String, String> param = new HashMap<>();
        param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        return param;
    }


    private void PostDiscussion() {
        new JSONParser(DiscussionForumActivity.this).parseVollyStringRequest(Const.URL.submitDiscussion, 1, getCommentParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    ArrayList<TimeLineCommentModel> timeLineCommentModelslist = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        emojiconEditText.setText("");
                        ConfirmPopup();
                        /* getPostOnDiscussion();*/
                    } else {
                        S.T(DiscussionForumActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                }
            }/*167222*/
        });

    }

    protected Map<String, String> getCommentParams() {
        Map<String, String> param = new HashMap<>();
        param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        param.put("discussion_topic", emojiconEditText.getText().toString());
        return param;
    }

    public void ConfirmPopup() {
        final Dialog dialog = new Dialog(DiscussionForumActivity.this);
        dialog.setContentView(R.layout.confirm_alert_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        ImageView cancel_img, full_image;
        Button continue_btn;
        cancel_img = (ImageView) dialog.findViewById(R.id.cancel_img);
        continue_btn = (Button) dialog.findViewById(R.id.continue_btn);

        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getPostOnDiscussion();
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getPostOnDiscussion();
            }
        });

        dialog.show();
    }
}
