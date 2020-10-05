package com.dollop.placementadda.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.dollop.placementadda.R;
import com.dollop.placementadda.activity.basic.BaseActivity;
import com.dollop.placementadda.adapter.MoreCoinsAdapter;
import com.dollop.placementadda.adapter.TimeLineAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.TimeLineModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.Helper;
import com.dollop.placementadda.sohel.JSONParser;
import com.dollop.placementadda.sohel.S;
import com.dollop.placementadda.sohel.multipart.AppHelper;
import com.dollop.placementadda.sohel.multipart.VolleyMultipartRequest;
import com.dollop.placementadda.sohel.multipart.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.dollop.placementadda.sohel.Const.URL.POST_IMAGE_URL;

public class TimeLineActivity extends BaseActivity implements View.OnClickListener {
    public static final int PERMISSION_REQUEST_CODE = 1111;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int REQUEST = 1337;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int CAPTURE_REQUEST_CODE = 100;
    public static int SELECT_FROM_GALLERY = 2;
    public static int SELECT_VIDEO_FROM_GALLERY = 4;
    public static int CAMERA_PIC_REQUEST = 0;
    public static TimeLineActivity ActivityContext = null;
    public static int VIDEO_CAPTURED = 1;
    private static File mediaFile;
    RecyclerView timeLineRecyclerView;
    TimeLineAdapter timeLineAdapter;
    EditText bottomsheetShowPostEt, postOnTimeLineEditText, postOnTimeLineEditTextwithImage_tv;
    TextView bottomSheetUserName;
    LinearLayout bottomSheetHeading, bottomsheetPhotoLayoutBtn, image;
    RelativeLayout videoRelativeLayout;
    ImageView bottomsheetCLoseBtn, bottomSheetPostImageVIew;
    Uri mImageCaptureUri;
    VideoView documentvideo_videoview;
    Button post_btn, post_update_btn;
    CircleImageView user_profile_pic;
    private List<TimeLineModel> timeLineModelList = new ArrayList<>();
    private BottomSheetBehavior bottomSheetBehavior;
    private Uri videoFileUri;
    private ImageView image1;
    private ImageView thumbnall_imageview;
    private String filePath;
    private Bitmap bitmap;
    private byte[] uploadBytes;
    String postDetailImageCheck = "";
    private String PostIds = "";
    private String isImage = "";
    private String imagePath = "";

    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // Check that the SDCard is mounted getExternalStorageDirectory

        File folder = new File(Environment.getExternalStorageDirectory(), "Medon24");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
            S.E("folder is created ");
        } else {
            // Do something else on failure
            S.E("folder is not created ");
        }
        // Create the storage directory(MyCameraVideo) if it does not exist
        // For unique file name appending current timeStamp with file name
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());


        if (type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(folder, "VID_" + timeStamp + ".mp4");
            if (!mediaFile.exists()) {
                try {
                    mediaFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    S.E("checkVideoPAthError::" + mediaFile);
                }
            }
        } else {
            return null;
        }
        return mediaFile;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_time_line;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(TimeLineActivity.this);
        setToolbarWithBackButton("Timeline");
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        documentvideo_videoview = (VideoView) findViewById(R.id.documentvideo_videoview);
        timeLineRecyclerView = findViewById(R.id.timeLineRecyclerView);
        bottomSheetHeading = findViewById(R.id.bottomSheetHeading);
        postOnTimeLineEditText = findViewById(R.id.postOnTimeLineEditText);
        bottomsheetShowPostEt = findViewById(R.id.bottomsheetShowPostEt);
        bottomSheetUserName = findViewById(R.id.bottomSheetUserName);
        videoRelativeLayout = findViewById(R.id.videoRelativeLayout);
        bottomsheetCLoseBtn = (ImageView) findViewById(R.id.bottomsheetCLoseBtn);
        bottomSheetPostImageVIew = findViewById(R.id.bottomSheetPostImageVIew);
        image1 = (ImageView) findViewById(R.id.image1);
        thumbnall_imageview = (ImageView) findViewById(R.id.thumbnall_imageview);
        image = findViewById(R.id.image);
        bottomsheetPhotoLayoutBtn = findViewById(R.id.bottomsheetPhotoLayoutBtn);
        post_btn = (Button) findViewById(R.id.post_btn);
        post_update_btn = findViewById(R.id.post_btn);
        user_profile_pic = (CircleImageView) findViewById(R.id.user_profile_pic);
        postOnTimeLineEditTextwithImage_tv = (EditText) findViewById(R.id.postOnTimeLineEditTextwithImage_tv);

        try {
            String image = UserDataHelper.getInstance().getList().get(0).getProfile_pic();

            Picasso.with(TimeLineActivity.this).load(image).error(R.drawable.ic_user).into(user_profile_pic);
        } catch (Exception e) {
            e.getMessage();

        }

        bottomSheetUserName.setText(UserDataHelper.getInstance().getList().get(0).getUserName());
        initListners();
        getPostOnTimeLine();

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!postOnTimeLineEditText.getText().toString().equals("") || !postDetailImageCheck.equals("")) {
                    UploadPost_OnServer();
                } else {
                    S.T(TimeLineActivity.this, "Please Enter Field Or Choose Image !");
                }
            }
        });

        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(TimeLineActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));


        bottomsheetCLoseBtn.setImageResource(R.drawable.ic_close_btns);
        bottomsheetShowPostEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void initListners() {
        bottomSheetHeading.setOnClickListener(this);

        bottomsheetCLoseBtn.setOnClickListener(this);
        bottomsheetPhotoLayoutBtn.setOnClickListener(this);
        postOnTimeLineEditTextwithImage_tv.setOnClickListener(this);
        image1.setOnClickListener(this);
        image.setOnClickListener(this);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomsheetShowPostEt.setVisibility(View.GONE);
                    bottomSheetUserName.setVisibility(View.VISIBLE);
                    bottomsheetCLoseBtn.setVisibility(View.VISIBLE);

                    //bottomSheetHeading.setText(getString(R.string.text_collapse_me));
                } else {
                    bottomsheetShowPostEt.setVisibility(View.VISIBLE);
                    bottomSheetUserName.setVisibility(View.GONE);
                    bottomsheetCLoseBtn.setVisibility(View.GONE);
                    //  bottomSheetHeading.setText(getString(R.string.text_expand_me));
                }

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        postOnTimeLineEditText.setVisibility(View.VISIBLE);
                        bottomSheetPostImageVIew.setVisibility(View.GONE);
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;

                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        timeLineAdapter = new TimeLineAdapter(TimeLineActivity.this, timeLineModelList);
        try {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            timeLineRecyclerView.setLayoutManager(mLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        timeLineRecyclerView.setItemAnimator(new DefaultItemAnimator());
        timeLineRecyclerView.setAdapter(timeLineAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottomSheetHeading:
                // Expanding the bottom sheet
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                break;

            case R.id.postOnTimeLineEditTextwithImage_tv:
                // Expanding the bottom sheet
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case R.id.bottomsheetCLoseBtn:
                // Expanding the bottom sheet

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.bottomsheetPhotoLayoutBtn:
                selectImage();
                bottomSheetPostImageVIew.setVisibility(View.VISIBLE);
                postOnTimeLineEditTextwithImage_tv.setVisibility(View.VISIBLE);
                bottomsheetShowPostEt.setVisibility(View.GONE);
                videoRelativeLayout.setVisibility(View.GONE);
                postOnTimeLineEditText.setVisibility(View.GONE);
                break;
            case R.id.image:

                S.E("select image click method me ja rha h");
                selectVideoImages();
                documentvideo_videoview.setVisibility(View.GONE);
                videoRelativeLayout.setVisibility(View.VISIBLE);
                bottomSheetPostImageVIew.setVisibility(View.GONE);
                postOnTimeLineEditTextwithImage_tv.setVisibility(View.GONE);
                bottomsheetShowPostEt.setVisibility(View.GONE);
                videoRelativeLayout.setVisibility(View.VISIBLE);
                postOnTimeLineEditText.setVisibility(View.GONE);
                break;
            case R.id.image1:
                documentvideo_videoview.setVisibility(View.VISIBLE);
                image1.setVisibility(View.GONE);
                thumbnall_imageview.setVisibility(View.GONE);
                MediaController mediaController = new MediaController(TimeLineActivity.this);
                mediaController.setAnchorView(documentvideo_videoview);
                documentvideo_videoview.setMediaController(mediaController);
                documentvideo_videoview.setVideoURI(videoFileUri);
                documentvideo_videoview.requestFocus();
                documentvideo_videoview.start();
                break;
        }
    }

    private void selectVideoImages() {
        documentvideo_videoview.setVisibility(View.GONE);
        videoRelativeLayout.setVisibility(View.VISIBLE);
        bottomSheetPostImageVIew.setVisibility(View.GONE);
        postOnTimeLineEditTextwithImage_tv.setVisibility(View.GONE);
        bottomsheetShowPostEt.setVisibility(View.GONE);
        videoRelativeLayout.setVisibility(View.VISIBLE);
        final CharSequence[] options = {"From Camera", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TimeLineActivity.this);
        builder.setTitle("Please choose a Video");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkVideoCameraPermission()) {
                            videocameraIntent();
                        } else {
                            requestCameraPermissions();
                        }
                    } else videocameraIntent();
                }
                if (options[item].equals("From Gallery")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkVideoCameraPermission()) {
                            videogalleryIntent();
                        } else {
                            requestCameraPermissions();
                        }
                    } else videogalleryIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ContextCompat.checkSelfPermission(TimeLineActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TimeLineActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CAPTURE_REQUEST_CODE);


            } else {
                if (ContextCompat.checkSelfPermission(TimeLineActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TimeLineActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CAPTURE_REQUEST_CODE);
                }
            }
            if (ContextCompat.checkSelfPermission(TimeLineActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TimeLineActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAPTURE_REQUEST_CODE);
            } else {
                if (ContextCompat.checkSelfPermission(TimeLineActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TimeLineActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAPTURE_REQUEST_CODE);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(TimeLineActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    private void videogalleryIntent() {
        try {
            Intent intent = new Intent().setType("video/*").setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select video"), SELECT_VIDEO_FROM_GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(TimeLineActivity.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void videocameraIntent() {
        try {
            Intent captureVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult(captureVideoIntent, VIDEO_CAPTURED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkVideoCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(TimeLineActivity.this, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
            Uri cameraURI = data.getData();
            Bitmap productImageBitmap = (Bitmap) data.getExtras().get("data");
            bottomSheetPostImageVIew.setImageBitmap(productImageBitmap);
            postDetailImageCheck = "true";


        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            Uri galleryURI = data.getData();
            bottomSheetPostImageVIew.setImageURI(galleryURI);
            postDetailImageCheck = "true";


        } else if (resultCode == RESULT_OK && requestCode == VIDEO_CAPTURED) {
            videoFileUri = data.getData();

            filePath = videoFileUri.getPath();
            S.E("filePath" + filePath);

            try {
                bitmap = retriveVideoFrameFromVideo(filePath);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            if (bitmap != null) {
                bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);
                thumbnall_imageview.setImageBitmap(bitmap);
            }
            try {
                InputStream iStream = getContentResolver().openInputStream(videoFileUri);
                uploadBytes = getBytes(iStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            S.E("image one visibility" + image1);
            postDetailImageCheck = "true";
            image1.setVisibility(View.VISIBLE);
        } else if (requestCode == SELECT_VIDEO_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            videoFileUri = data.getData();
            filePath = videoFileUri.getPath();
            S.E("filePath" + filePath);
            postDetailImageCheck = "true";
            try {
                bitmap = retriveVideoFrameFromVideo(filePath);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            if (bitmap != null) {
                bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);
                thumbnall_imageview.setImageBitmap(bitmap);
            }
            try {
                InputStream iStream = getContentResolver().openInputStream(videoFileUri);
                uploadBytes = getBytes(iStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // image.setVisibility(View.GONE);
            image1.setVisibility(View.VISIBLE);
        }

        // After camera screen this code will excuted


    }

    private void selectImage() {
        final CharSequence[] options = {"From Camera", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please choose an Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkCameraPermission())
                            cameraIntent();
                        else requestPermission();
                    } else cameraIntent();
                } else if (options[item].equals("From Gallery")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkGalleryPermission()) galleryIntent();
                        else requestGalleryPermission();
                    } else galleryIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();
    }

    private void galleryIntent() {
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FROM_GALLERY);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(TimeLineActivity.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(TimeLineActivity.this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(TimeLineActivity.this, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(TimeLineActivity.this, READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void getPostOnTimeLine() {
        new JSONParser(TimeLineActivity.this).parseVollyStringRequest(Const.URL.getPosts, 1, getPostParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("getContest response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        timeLineModelList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            TimeLineModel contestlistModel = new TimeLineModel();
                            contestlistModel.setUserName(jsonObject2.getString("user"));
                            contestlistModel.setPost_id(jsonObject2.getString("post_id"));
                            contestlistModel.setUserProfilePic(jsonObject2.getString("profile_pic"));
                            contestlistModel.setImage(jsonObject2.getString("image"));
                            contestlistModel.setIs_image(jsonObject2.getString("is_image"));
                            contestlistModel.setStatus(jsonObject2.getString("status"));
                            contestlistModel.setPost(jsonObject2.getString("post"));
                            contestlistModel.setCreated_date(jsonObject2.getString("created_date"));
                            contestlistModel.setLike_count(jsonObject2.getString("like_count"));
                            contestlistModel.setComment_count(jsonObject2.getString("comment_count"));
                            contestlistModel.setIs_like(jsonObject2.getString("is_like"));
                            contestlistModel.setUser_id(jsonObject2.getString("user_id"));
                            timeLineModelList.add(contestlistModel);
                        }
                        S.E("list size" + timeLineModelList.size());
                        if (timeLineModelList.size() == 0) {
                            timeLineRecyclerView.setVisibility(View.GONE);
                        } else {
                            timeLineRecyclerView.setVisibility(View.VISIBLE);
                            timeLineAdapter.notifyDataSetChanged();
                        }
                    } else {
                        timeLineRecyclerView.setVisibility(View.GONE);
                        S.T(TimeLineActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    S.E("errror" + e);
                }
            }/*167222*/
        });
    }

    protected Map<String, String> getPostParams() {
        Map<String, String> param = new HashMap<>();
        param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());

        S.E("prams : : " + param);
        return param;
    }

    public void UploadPost_OnServer() {
        final Dialog dialog = S.initProgressDialog(this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.URL.submitPost, new Response.Listener<NetworkResponse>() {

            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                S.E(response.toString());
                Log.e("updateOn Serverresult", "" + resultResponse.toString());
                try {
                    JSONObject mainObject = new JSONObject(resultResponse);
                    if (mainObject.getString("message").equals("success")) {
                        dialog.dismiss();

                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        getPostOnTimeLine();
                        postOnTimeLineEditText.setText("");
                        timeLineAdapter.notifyDataSetChanged();

                        S.T(TimeLineActivity.this, mainObject.getString("message"));
                    } else {
                        dialog.dismiss();
                        S.T(TimeLineActivity.this, mainObject.getString("message"));
                        // S.showSnackBar(getActivity(),"Challege Add"+mainObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error", "" + e);
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String S = "Unknown error";
                dialog.dismiss();
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        S = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        S = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        Log.e("response Status", String.valueOf(response));
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("S Status", status);
                        Log.e("S Message", message);

                        if (networkResponse.statusCode == 404) {
                            S = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            S = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            S = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            S = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("nmetwork ", "c d" + e);
                    }
                }
                Log.i("S", S);
                error.printStackTrace();
            }
        }

        )
                /*name_et,email_et,dob_et,hobbies_et,bloodgroup_et,address_et,phone_et*/ {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());

                param.put("timeline_text", postOnTimeLineEditText.getText().toString());
                S.E("prams : : " + param);
                return param;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    Bitmap bitmap = ((BitmapDrawable) bottomSheetPostImageVIew.getDrawable()).getBitmap();
                    Log.e("Image_post ", "Image_post" + bitmap);
                    if (bitmap != null)
                        params.put("image", new DataPart(System.currentTimeMillis() + "user_image.png", AppHelper.getFileDataFromDrawable(bitmap), "image/png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(multipartRequest);
        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
    }

    public void getRefresh() {
        getPostOnTimeLine();
    }

    public void getPostlistRefersh() {
        getPostOnTimeLine();
    }

    public void deletPost(final String postId) {
        new JSONParser(TimeLineActivity.this).parseVollyStringRequest(Const.URL.DeletePost, 1, getDeleteParam(postId), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("delete Post" + response);
                S.E("Get Post" + getDeleteParam(postId));
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("success")) {
                        getPostlistRefersh();
                    }
                } catch (JSONException e) {
                    S.E("errror" + e);
                }
            }/*167222*/
        });
    }

    private Map<String, String> getDeleteParam(String postId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
        params.put("post_id", postId);
        return params;
    }


    public void editTest(TimeLineModel timeLineModel) {
        S.E("Edit--<");
        post_btn.setVisibility(View.GONE);
        post_update_btn.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        String userName = timeLineModel.getUserName();
        PostIds = timeLineModel.getPost_id();
        isImage = timeLineModel.getIs_image();
        if (isImage.equals("1")) {
            String imagePathstr = timeLineModel.getImage();
            String pathName[] = imagePathstr.split("/");
            imagePath = pathName[pathName.length - 1];
        }
        if (timeLineModel.getIs_image().equals("1"))
            S.E("userName" + userName);
        String dates = timeLineModel.getCreated_date();
        S.E("dates" + dates);
        S.E("postOnTimeLineEditTextwithImage_tv" + timeLineModel.getPost());
        String Image_in_string = timeLineModel.getImage();
        S.E("image in string" + Image_in_string);
        if (Image_in_string.equals("")) {

        } else {
            Picasso.with(TimeLineActivity.this)
                    .load(Image_in_string)
                    .into(bottomSheetPostImageVIew);

        }
        S.E("c " + Image_in_string);
        postOnTimeLineEditTextwithImage_tv.setText(timeLineModel.getPost());
        postOnTimeLineEditText.setText(timeLineModel.getPost());

        if (PostIds != null && !PostIds.equals("")) {
            if (!postOnTimeLineEditTextwithImage_tv.getText().toString().equals("")) {
                postOnTimeLineEditTextwithImage_tv.setVisibility(View.VISIBLE);
                bottomSheetPostImageVIew.setVisibility(View.GONE);
                // getPostEdit();
            } else if (postOnTimeLineEditTextwithImage_tv.getText().toString().equals("") && postOnTimeLineEditTextwithImage_tv == null) {
                bottomSheetPostImageVIew.setVisibility(View.VISIBLE);
                postOnTimeLineEditTextwithImage_tv.setVisibility(View.GONE);
                Picasso.with(TimeLineActivity.this)
                        .load(Image_in_string)
                        .into(bottomSheetPostImageVIew);

                // getPostEdit();
            } else {
                S.T(TimeLineActivity.this, "Please write or select post..");
            }

        } else {
            S.T(TimeLineActivity.this, "Please write your post..");
        }


        if (postOnTimeLineEditText.getText().toString().equals("")) {
            bottomSheetPostImageVIew.setVisibility(View.VISIBLE);
            postOnTimeLineEditTextwithImage_tv.setVisibility(View.GONE);

        } else {
            bottomSheetPostImageVIew.setVisibility(View.GONE);
            postOnTimeLineEditTextwithImage_tv.setVisibility(View.VISIBLE);
        }


        post_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPostEdit();
            }
        });
        if (isImage.equals("1")) {
            bottomSheetPostImageVIew.setVisibility(View.VISIBLE);
            postOnTimeLineEditTextwithImage_tv.setVisibility(View.GONE);
            postOnTimeLineEditText.setVisibility(View.GONE);
            Picasso.with(TimeLineActivity.this)
                    .load(Image_in_string)
                    .into(bottomSheetPostImageVIew);

        } else {
            bottomSheetPostImageVIew.setVisibility(View.GONE);
            postOnTimeLineEditTextwithImage_tv.setVisibility(View.VISIBLE);
        }

    }

    private void getPostEdit() {
        final Dialog dialog = S.initProgressDialog(this);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Const.URL.editPost, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                S.E(response.toString());
                Log.e("updateOn Serverresult", "" + resultResponse.toString());
                try {
                    JSONObject mainObject = new JSONObject(resultResponse);
                    if (mainObject.getString("message").equals("success")) {
                        dialog.dismiss();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        getPostOnTimeLine();
                        postOnTimeLineEditText.setText("");
                        S.T(TimeLineActivity.this, mainObject.getString("message"));
                    } else {
                        dialog.dismiss();
                        S.T(TimeLineActivity.this, mainObject.getString("message"));
                                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error", "" + e);
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String S = "Unknown error";
                dialog.dismiss();
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        S = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        S = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        Log.e("response Status", String.valueOf(response));
                        String status = response.getString("status");
                        String message = response.getString("message");
                        Log.e("S Status", status);
                        Log.e("S Message", message);
                        if (networkResponse.statusCode == 404) {
                            S = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            S = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            S = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            S = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("nmetwork ", "c d" + e);
                    }
                }
                Log.i("S", S);
                error.printStackTrace();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", UserDataHelper.getInstance().getList().get(0).getUserId());
                param.put("post_id", PostIds);
                param.put("is_remove_img", isImage);
                param.put("hidden_image", imagePath);
                param.put("timeline_text", postOnTimeLineEditText.getText().toString());

                S.E("prams : : " + param);
                return param;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    Bitmap bitmap = ((BitmapDrawable) bottomSheetPostImageVIew.getDrawable()).getBitmap();
                    Log.e("hidden_image ", "Image_post" + bitmap);
                    if (bitmap != null)
                        params.put("image", new DataPart(System.currentTimeMillis() + "user_image.png", AppHelper.getFileDataFromDrawable(bitmap), "image/png"));
                    // params.put("is_remove_img", new DataPart(System.currentTimeMillis() + "user_image.png", AppHelper.getFileDataFromDrawable(bitmap), "image/png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(multipartRequest);
        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
    }
}

