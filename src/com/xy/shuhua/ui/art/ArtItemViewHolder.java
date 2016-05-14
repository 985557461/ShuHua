package com.xy.shuhua.ui.art;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.art.model.HomeArtItemModel;

/**
 * Created by xiaoyu on 2016/3/31.
 */
public class ArtItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private View rootView;
    private TextView title;
    private TextView count;

    private HomeArtItemModel homeArtItemModel;

    public ArtItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;
        title = (TextView) rootView.findViewById(R.id.title);
        count = (TextView) rootView.findViewById(R.id.count);
        rootView.setOnClickListener(this);
    }

    public void setData(HomeArtItemModel homeArtItemModel) {
        this.homeArtItemModel = homeArtItemModel;
        if (homeArtItemModel == null) {
            return;
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
        } else {

        }
    }
}
