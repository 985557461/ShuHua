package com.xy.shuhua.ui.art;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xiaoyu on 2016/3/31.
 */
public class ArtPersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private Context context;
    private View rootView;

    public ArtPersonViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;

        rootView.setOnClickListener(this);
    }

    public void setData(){

    }

    @Override
    public void onClick(View view) {
        ActivityAuthorArtList.open((Activity) context);
    }
}
