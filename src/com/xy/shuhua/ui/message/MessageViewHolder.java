package com.xy.shuhua.ui.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xiaoyu on 2016/3/31.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder{
    private Context context;
    private View rootView;

    public MessageViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;
    }
}
