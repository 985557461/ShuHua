package com.xy.shuhua.ui.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.art.model.ArtGoodsItemModel;

/**
 * Created by xiaoyu on 2016/3/31.
 */
public class WenZhangItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private View rootView;
    private ImageView imageView;
    private TextView title;
    private TextView content;

    public WenZhangItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        title = (TextView) rootView.findViewById(R.id.title);
        content = (TextView) rootView.findViewById(R.id.content);

        rootView.setOnClickListener(this);
    }

    public void setData() {
//        if (itemModel == null) {
//            return;
//        }
//        if (!TextUtils.isEmpty(itemModel.imageurl)) {
//            Glide.with(context).load(itemModel.imageurl).into(imageView);
//        } else {
//            Glide.with(context).load("").into(imageView);
//        }
//        if (!TextUtils.isEmpty(itemModel.name)) {
//            name.setText(itemModel.name);
//        } else {
//            name.setText("");
//        }
    }

    @Override
    public void onClick(View view) {
    }
}
