package com.dollop.placementadda.adapter;


        import android.content.Context;
        import android.support.v7.widget.CardView;
        import android.support.v7.widget.RecyclerView;
        import android.util.SparseBooleanArray;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.dollop.placementadda.R;
        import com.dollop.placementadda.model.QuizCategoryModel;

        import java.util.List;

/**
 * Created by Kasim on 22-01-2018.
 */

public class QuizCategorySubcategoryAdapter extends RecyclerView.Adapter<QuizCategorySubcategoryAdapter.MyViewHolder> {
    Context context;
    List<QuizCategoryModel> quizCategoryModelList;
    String userId;
    private SparseBooleanArray mSelectedItemsIds;

    public QuizCategorySubcategoryAdapter(Context context, List<QuizCategoryModel> quizCategoryModelList) {
        this.context = context;
        this.quizCategoryModelList = quizCategoryModelList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public QuizCategorySubcategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_category_subcategory, parent, false);
        QuizCategorySubcategoryAdapter.MyViewHolder viewHolder = new QuizCategorySubcategoryAdapter.MyViewHolder(v);
        return viewHolder;
    }

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    @Override
    public void onBindViewHolder(final QuizCategorySubcategoryAdapter.MyViewHolder holder, final int position) {
        final QuizCategoryModel quizCategoryModel = quizCategoryModelList.get(position);
       /* if (quizCategoryModel.getCategory_Image().equals("")) {
            holder.categoryLogoIv.setImageResource(R.drawable.sbi_logo);
        } else {
            Picasso.with(context).load(Const.URL.Categry_Image+quizCategoryModel.getCategory_Image()).into(holder.categoryLogoIv);

        }*/
        holder.categryNameTv.setText(quizCategoryModel.getCategoryName());

       /* ArrayList<QuizSubCategoryModel>quizSubCatgoryModels=quizCategoryModel.getQuizSubCatgoryModelsList();

        QuizCatSubcateAdapter quizCategoryAdapter = new QuizCatSubcateAdapter(context, quizSubCatgoryModels);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(quizCategoryAdapter);
        holder.rvSubcategoryLIstId.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
*/

     /*   holder.categryLayoutCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((QuizCategoryActivity) context).getSelected(quizCategoryModel.getCategoryId());
                }
                catch (Exception e){e.printStackTrace();}
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return (null != quizCategoryModelList ? quizCategoryModelList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryLogoIv,categoryViewsTv;
        TextView categryNameTv, categoryQuizzTv;
        CardView categryLayoutCardview;
        RecyclerView rvSubcategoryLIstId;

        public MyViewHolder(View itemView) {
            super(itemView);

         /*   categryLayoutCardview = (CardView) itemView.findViewById(R.id.categryLayoutCardview);
            categoryLogoIv = (ImageView) itemView.findViewById(R.id.categoryLogoIv);
            categryNameTv = (TextView) itemView.findViewById(R.id.categryNameTv);
            categoryQuizzTv = (TextView) itemView.findViewById(R.id.categoryQuizzTv);
            categoryViewsTv = (ImageView) itemView.findViewById(R.id.categoryViewsTv);*/
//                    rvSubcategoryLIstId = (RecyclerView) itemView.findViewById(R.id.rvSubcategoryLIstId);
            categryNameTv = (TextView) itemView.findViewById(R.id.tvCategoryId);

        }
    }
}