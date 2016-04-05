package com.xy.shuhua.ui.art;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.xy.shuhua.R;

/**
 * Created by xiaoyu on 2016/4/5.
 */
public class RelevantItemView extends FrameLayout {
    public RelevantItemView(Context context) {
        super(context);
        init(context);
    }

    public RelevantItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RelevantItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.zuopin_item_view, this, true);
    }
}
