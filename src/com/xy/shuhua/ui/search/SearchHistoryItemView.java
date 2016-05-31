package com.xy.shuhua.ui.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.xy.shuhua.R;

/**
 * Created by xiaoyu on 2016/4/13.
 */
public class SearchHistoryItemView extends FrameLayout{
    public TextView contentTV;

    public SearchHistoryItemView(Context context) {
        super(context);
        init(context);
    }

    public SearchHistoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchHistoryItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.search_history_item_view, this, true);

        contentTV = (TextView) findViewById(R.id.contentTV);

    }

    public void setData(String str) {
        contentTV.setText(str);
    }
}
