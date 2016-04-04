package com.xy.shuhua.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.xy.shuhua.R;

/**
 * Created by xiaoyu on 2016/3/24.
 */
public class SpaceItemViewHolder extends RecyclerView.ViewHolder{
    private Context context;
    private View rootView;
    private View lineLeft;
    private View lineRight;

    public SpaceItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;
        lineLeft = rootView.findViewById(R.id.lineLeft);
        lineRight = rootView.findViewById(R.id.lineRight);
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

    public void setData(){

    }
}
