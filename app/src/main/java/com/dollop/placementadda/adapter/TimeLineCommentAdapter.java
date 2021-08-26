package com.dollop.placementadda.adapter;

import android.app.Dialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.model.TimeLineCommentModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimeLineCommentAdapter extends RecyclerView.Adapter<TimeLineCommentAdapter.MyViewHolder> {
    Context context;
    List<TimeLineCommentModel> quizCategoryModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public TimeLineCommentAdapter(Context context, List<TimeLineCommentModel> quizCategoryModelList) {
        this.context = context;
        this.quizCategoryModelList = quizCategoryModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public TimeLineCommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_comment_user_forum, parent, false);
        TimeLineCommentAdapter.MyViewHolder viewHolder = new TimeLineCommentAdapter.MyViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final TimeLineCommentAdapter.MyViewHolder holder, final int position) {
        final TimeLineCommentModel quizCategoryModel = quizCategoryModelList.get(position);

       holder.txtViewCommentUserName.setText(quizCategoryModel.getUser_name());
       holder.Comment_time_tv.setText(quizCategoryModel.getCreated_date());
       holder.txtViewSingleComment.setText(quizCategoryModel.getComment());
       Picasso.with(context).load(quizCategoryModel.getProfile_pic()).into(holder.comment_user_circullerimage);
   holder.comment_user_circullerimage.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           ConfirmPopup(quizCategoryModel.getProfile_pic());
       }
   });
    }

    @Override
    public int getItemCount() {
        return (null != quizCategoryModelList ? quizCategoryModelList.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtViewCommentUserName, Comment_time_tv,txtViewSingleComment;
        CircleImageView comment_user_circullerimage;

        public MyViewHolder(View itemView) {

            super(itemView);
            txtViewCommentUserName = (TextView) itemView.findViewById(R.id.txtViewCommentUserName);
            Comment_time_tv = (TextView) itemView.findViewById(R.id.Comment_time_tv);
            txtViewSingleComment = (TextView) itemView.findViewById(R.id.txtViewSingleComment);
            comment_user_circullerimage = (CircleImageView) itemView.findViewById(R.id.comment_user_circullerimage);

        }
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
}
