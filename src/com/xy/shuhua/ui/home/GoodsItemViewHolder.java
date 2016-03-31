package com.xy.shuhua.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xiaoyu on 2016/3/24.
 */
public class GoodsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private Context context;
    private View rootView;

    public GoodsItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;
    }

    public void setData(){

    }

    @Override
    public void onClick(View view) {
    }
}
