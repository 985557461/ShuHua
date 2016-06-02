package com.xy.shuhua.ui.art;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.art.model.HomeArtItemModel;

/**
 * Created by xiaoyu on 2016/3/31.
 */
public class ArtItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private View rootView;
    private ImageView coverImageView;
    private TextView title;
    private TextView count;

    private HomeArtItemModel homeArtItemModel;

    public ArtItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;
        coverImageView = (ImageView) rootView.findViewById(R.id.coverImageView);
        title = (TextView) rootView.findViewById(R.id.title);
        count = (TextView) rootView.findViewById(R.id.count);
        rootView.setOnClickListener(this);
    }

    public void setData(HomeArtItemModel homeArtItemModel) {
        this.homeArtItemModel = homeArtItemModel;
        if (homeArtItemModel == null) {
            return;
        }
        if ("艺术名家".equals(homeArtItemModel.name)) {
            Glide.with(context).load(R.drawable.icon_art_cover_image).into(coverImageView);
        } else if("当代艺术品".equals(homeArtItemModel.name)){
            Glide.with(context).load(R.drawable.dangdai_art_pic).into(coverImageView);
        }else if("书画作品".equals(homeArtItemModel.name)){
            Glide.with(context).load(R.drawable.shuhua_art_pic).into(coverImageView);
        }else if("儿童书画作品".equals(homeArtItemModel.name)){
            Glide.with(context).load(R.drawable.ertong_art_pic).into(coverImageView);
        }
        if (!TextUtils.isEmpty(homeArtItemModel.name)) {
            title.setText(homeArtItemModel.name);
        }
        if (!TextUtils.isEmpty(homeArtItemModel.count)) {
            count.setText("共" + homeArtItemModel.count + "个" + homeArtItemModel.name);
        }
    }

    @Override
    public void onClick(View view) {
        if ("艺术名家".equals(homeArtItemModel.name)) {
            ActivityArtTypeInfo.open((Activity) context);
        } else if("当代艺术品".equals(homeArtItemModel.name)){
            ActivitySearchArtByType.open((Activity) context,"0","当代艺术品");
        }else if("书画作品".equals(homeArtItemModel.name)){
            ActivitySearchArtByType.open((Activity) context,"1","书画作品");
        }else if("儿童书画作品".equals(homeArtItemModel.name)){
            ActivitySearchArtByType.open((Activity) context,"2","儿童书画作品");
        }
    }
}
