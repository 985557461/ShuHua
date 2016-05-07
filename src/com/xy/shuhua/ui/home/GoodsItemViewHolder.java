package com.xy.shuhua.ui.home;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.art.ActivityArtGoodsInfo;
import com.xy.shuhua.ui.home.model.HomeArtGoodsModel;

/**
 * Created by xiaoyu on 2016/3/24.
 */
public class GoodsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private View rootView;
    private ImageView imageView;
    private TextView name;
    private TextView price;
    private TextView likeNumber;
    private View lineLeft;
    private View lineRight;

    private HomeArtGoodsModel homeArtGoodsModel;

    public GoodsItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        name = (TextView) rootView.findViewById(R.id.name);
        price = (TextView) rootView.findViewById(R.id.price);
        likeNumber = (TextView) rootView.findViewById(R.id.likeNumber);
        lineLeft = rootView.findViewById(R.id.lineLeft);
        lineRight = rootView.findViewById(R.id.lineRight);

        rootView.setOnClickListener(this);
    }

    public void setData(HomeArtGoodsModel homeArtGoodsModel) {
        this.homeArtGoodsModel = homeArtGoodsModel;
        if (homeArtGoodsModel == null) {
            return;
        }
        if (!TextUtils.isEmpty(homeArtGoodsModel.imageurl)) {
            Glide.with(context).load(homeArtGoodsModel.imageurl).into(imageView);
        } else {
            Glide.with(context).load("").into(imageView);
        }
        if (!TextUtils.isEmpty(homeArtGoodsModel.name)) {
            name.setText(homeArtGoodsModel.name);
        } else {
            name.setText(homeArtGoodsModel.name);
        }
        if (!TextUtils.isEmpty(homeArtGoodsModel.price)) {
            price.setText(homeArtGoodsModel.price);
        } else {
            price.setText("");
        }
        if (!TextUtils.isEmpty(homeArtGoodsModel.praisenum)) {
            likeNumber.setText(homeArtGoodsModel.praisenum);
        } else {
            likeNumber.setText("0");
        }
    }

    public void showLine(int position) {
        int part = position % 2;
        if (part == 0) {//Å¼Êý
            lineLeft.setVisibility(View.GONE);
            lineRight.setVisibility(View.VISIBLE);
        } else {//»ùÊý
            lineLeft.setVisibility(View.VISIBLE);
            lineRight.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        ActivityArtGoodsInfo.open((Activity) context,homeArtGoodsModel.id);
    }
}
