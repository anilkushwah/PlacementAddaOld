package com.dollop.placementadda.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dollop.placementadda.R;
import com.dollop.placementadda.adapter.DiscussionAdapter;
import com.dollop.placementadda.adapter.LeaderBoardAdapter;
import com.dollop.placementadda.database.datahelper.UserDataHelper;
import com.dollop.placementadda.model.LeaderBoardModel;
import com.dollop.placementadda.model.MainFragModel;
import com.dollop.placementadda.notification.Config;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import com.dollop.placementadda.activity.basic.BaseActivity;


public class PostDiscussionActivity extends BaseActivity implements SurfaceHolder.Callback {
    Camera camera;
    private static final int REQUEST = 1337;
    public static final int PERMISSION_REQUEST_CODE = 1111;
    private SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    ImageView image, cancel_imgview;
    boolean previewing = false;
    private Bitmap correctBmp;
    CircleImageView comment_user_circullerimage;
    TextView name_et;

    @Override
    protected int getContentResId() {
        return R.layout.activity_post_discussion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNetworkAvailability(PostDiscussionActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        image = (ImageView) findViewById(R.id.post_discussion_image_btn);
        cancel_imgview = (ImageView) findViewById(R.id.cancel_imgview);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        comment_user_circullerimage = (CircleImageView) findViewById(R.id.comment_user_circullerimage);

        surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
        name_et = (TextView) findViewById(R.id.name_et);

        try {
            String image = UserDataHelper.getInstance().getList().get(0).getProfile_pic();
            Picasso.with(PostDiscussionActivity.this).load(image).error(R.drawable.ic_user).into(comment_user_circullerimage);
            name_et.setText(UserDataHelper.getInstance().getList().get(0).getUserName());

        } catch (Exception e) {
            e.getMessage();

        }
        cancel_imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkCameraPermission()) {
                if (!previewing) {
                    camera = Camera.open();
                    if (camera != null) {
                        try {
                            camera.setDisplayOrientation(90);
                            camera.setPreviewDisplay(surfaceHolder);
                            camera.startPreview();
                            previewing = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                requestPermission();
            }
        } else {
            if (!previewing) {
                camera = Camera.open();
                if (camera != null) {
                    try {
                        camera.setDisplayOrientation(90);
                        camera.setPreviewDisplay(surfaceHolder);
                        camera.startPreview();
                        previewing = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camera != null) {
                    try {
                        camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);
                    } catch (Exception e) {
                    }

                }
            }
        });
        RecyclerView gallery = (RecyclerView) findViewById(R.id.Galleryimage_recycleview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkGalleryPermission()) {
                gallery.setAdapter(new ImageAdapter(this));
                gallery.setLayoutManager(new LinearLayoutManager(PostDiscussionActivity.this, LinearLayoutManager.HORIZONTAL, false));
                gallery.setAdapter(new ImageAdapter(this));
            } else {
                requestGalleryPermission();
            }
            if (checkExternalPermission()) {
            } else {
                requestExternalPermission();
            }
        }
        BroadcastReceiver broadcastReceiver = S.LocalBroadcastReciver(PostDiscussionActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.QuizRequest));
    }

    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {

        public void onShutter() {
        }
    };


    Camera.PictureCallback myPictureCallback_RAW = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] arg0, Camera arg1) {

        }
    };


    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            try {
                Dialog dialog = S.initProgressDialog(PostDiscussionActivity.this);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
                Bitmap correctBmp1 = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), matrix, true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                correctBmp1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                correctBmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                File folder = new File(Environment.getExternalStorageDirectory(), "PlacementAdda");
                if (!folder.exists()) {
                    folder.mkdir();
                }
                Date date = new Date();
                File fileName = new File(folder, date + "disscussionfouram.jpeg");

                if (!fileName.exists()) {
                    try {
                        fileName.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                FileOutputStream out = null;

                try {
                    out = new FileOutputStream(fileName);
                    correctBmp1.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //main_imageview.setImageBitmap(correctBmp);

                try {
                    Intent i = new Intent(PostDiscussionActivity.this, FinalPostDiscussionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("image", fileName.getPath()); // photo is an byte array you already stored data in it
                    bundle.putString("Check", "Camera");
                    i.putExtras(bundle);
                    startActivity(i);
                    dialog.dismiss();
                } catch (RuntimeException e) {

                }
                previewing = true;

            } catch (Exception e) {
            }
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (previewing) {
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(PostDiscussionActivity.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(PostDiscussionActivity.this, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(PostDiscussionActivity.this, READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkExternalPermission() {
        int result2 = ContextCompat.checkSelfPermission(PostDiscussionActivity.this, WRITE_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(PostDiscussionActivity.this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private void requestExternalPermission() {
        ActivityCompat.requestPermissions(PostDiscussionActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST);
    }


    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
        private int postionForselected = 0;
        ArrayList<String> imageData = getAllShownImagesPath(PostDiscussionActivity.this);
        Context context;

        public ImageAdapter(Context context) {
            this.context = context;
            this.postionForselected = postionForselected;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.selectedimageadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final String imagePath = imageData.get(position);
            Glide.with(context).load(imagePath)
                    .placeholder(R.drawable.ic_cancel_music).centerCrop()
                    .into(holder.mainimage_img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putString("image", imageData.get(position));
                    bundle.putString("Check", "gallery");

                    S.I(PostDiscussionActivity.this, FinalPostDiscussionActivity.class, bundle);


                }
            });
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView mainimage_img;

            public MyViewHolder(View itemView) {
                super(itemView);
                mainimage_img = (ImageView) itemView.findViewById(R.id.mainimage_img);
            }
        }

        @Override
        public int getItemCount() {
            return imageData.size();
        }
    }


    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] arr = baos.toByteArray();
        String result = Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }
}

