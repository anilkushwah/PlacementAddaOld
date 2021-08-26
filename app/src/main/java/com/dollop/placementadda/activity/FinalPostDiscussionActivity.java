package com.dollop.placementadda.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dollop.placementadda.R;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.multipart.AppHelper;
import com.dollop.placementadda.sohel.multipart.VolleyMultipartRequest;
import com.dollop.placementadda.sohel.multipart.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


import com.dollop.placementadda.activity.basic.BaseActivity;


public class FinalPostDiscussionActivity extends BaseActivity {
    ImageView main_image, cancel_imgview;
    String FImage;
  //  EmojIconActions emojIcon;
    EditText emojiconEditText;
    ImageView emojiImageView;
    FloatingActionButton send;
    View rootView;
    CircleImageView comment_user_circullerimage;
    String Check;
    TextView name_et;

    @Override
    protected int getContentResId() {
        return R.layout.activity_final_post_discussion;
    }

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(FinalPostDiscussionActivity.this);
        main_image = (ImageView) findViewById(R.id.main_image);
        cancel_imgview = (ImageView) findViewById(R.id.cancel_imgview);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        comment_user_circullerimage = (CircleImageView) findViewById(R.id.comment_user_circullerimage);
        name_et = (TextView) findViewById(R.id.name_et);

        try {
            String image = UserDataHelper.getInstance().getList().get(0).getProfile_pic();
            Picasso.with(FinalPostDiscussionActivity.this).load(image).error(R.drawable.ic_user).into(comment_user_circullerimage);
            name_et.setText(UserDataHelper.getInstance().getList().get(0).getUserName());

        } catch (Exception e) {
            e.getMessage();

        }

        Bundle bundle = getIntent().getExtras();
        FImage = bundle.getString("image");
        Check = bundle.getString("Check");
        if (Check.equals("Camera")) {
            Bitmap myBitmap = BitmapFactory.decodeFile(FImage);
            main_image.setImageBitmap(myBitmap);
        } else if (Check.equals("gallery")) {
            File imgFile = new File(FImage);
            Bitmap myBitmap1 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            // Compress code
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myBitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Bitmap myBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            main_image.setImageBitmap(myBitmap);
        }
        emojiconEditText = (EditText) findViewById(R.id.editEmojicon);
        emojiImageView = (ImageView) findViewById(R.id.emojiIcon);

        send = (FloatingActionButton) findViewById(R.id.send);
        rootView = findViewById(R.id.root_view);
      /*  emojIcon = new EmojIconActions(FinalPostDiscussionActivity.this, rootView, emojiconEditText, emojiImageView);

        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
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
                UploadPost();
            }
        });

        cancel_imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.I_clear(FinalPostDiscussionActivity.this, DiscussionForumActivity.class, null);
            }
        });
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(FinalPostDiscussionActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    public void UploadPost() {
        final Dialog dialog = S.initProgressDialog(FinalPostDiscussionActivity.this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.URL.submitDiscussion, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    dialog.dismiss();
                    JSONObject jsonObject1 = new JSONObject(resultResponse);
                    if (jsonObject1.getString("status").equals("200")) {
                        ConfirmPopup();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                dialog.dismiss();
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");
                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
                params.put("discussion_topic", emojiconEditText.getText().toString());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    Bitmap bitmap = ((BitmapDrawable) main_image.getDrawable()).getBitmap();
                    if (bitmap != null)
                        params.put("image", new DataPart(System.currentTimeMillis() + "Image_discussion.png", AppHelper.getFileDataFromDrawable(bitmap), "image/png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);

    }

    public void ConfirmPopup() {
        final Dialog dialog = new Dialog(FinalPostDiscussionActivity.this);
        dialog.setContentView(R.layout.confirm_alert_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        ImageView cancel_img;
        Button continue_btn;
        cancel_img = (ImageView) dialog.findViewById(R.id.cancel_img);
        continue_btn = (Button) dialog.findViewById(R.id.continue_btn);

        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                S.I_clear(FinalPostDiscussionActivity.this, DiscussionForumActivity.class, null);
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                S.I_clear(FinalPostDiscussionActivity.this, DiscussionForumActivity.class, null);
            }
        });

        dialog.show();
    }
}
