package com.xy.shuhua.ui.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.xy.shuhua.R;

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
    }

    @Override
    public void onClick(View view) {
    }
}
