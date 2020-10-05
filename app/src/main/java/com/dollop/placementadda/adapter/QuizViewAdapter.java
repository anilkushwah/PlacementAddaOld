package com.dollop.placementadda.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dollop.placementadda.R;
import com.dollop.placementadda.model.BannerModel;
import com.dollop.placementadda.sohel.Const;
import com.dollop.placementadda.sohel.S;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kasim on 18-01-2018.
 */

public class QuizViewAdapter extends PagerAdapter {

    private ArrayList<BannerModel> images;
    private LayoutInflater inflater;
    private Context context;

    public QuizViewAdapter(Context context, ArrayList<BannerModel> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.imageSlide);

        Picasso.with(context).load(Const.URL.Banner_Image + images.get(position).getBannerImage()).into(myImage);


        S.E("Aniruddha data"+Const.URL.Banner_Image + images.get(position).getBannerImage());
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}