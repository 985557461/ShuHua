package com.xy.shuhua.ui.mine;

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
import com.xy.shuhua.ui.art.model.ArtGoodsItemModel;

/**
 * Created by xiaoyu on 2016/3/31.
 */
public class ZuoPinItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private View rootView;
    private ImageView imageView;
    private TextView name;

    public ArtGoodsItemModel itemModel;

    public ZuoPinItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        name = (TextView) rootView.findViewById(R.id.name);

        rootView.setOnClickListener(this);
    }

    public void setData(ArtGoodsItemModel itemModel) {
        this.itemModel = itemModel;
        if (itemModel == null) {
            return;
        }
        if (!TextUtils.isEmpty(itemModel.imageurl)) {
            Glide.with(context).load(itemModel.imageurl).dontAnimate().placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(imageView);
        } else {
            Glide.with(context).load("").dontAnimate().placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(imageView);
        }
        if (!TextUtils.isEmpty(itemModel.name)) {
            name.setText(itemModel.name);
        } else {
            name.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        ActivityArtGoodsInfo.open((Activity) context, itemModel.id);
    }
}
