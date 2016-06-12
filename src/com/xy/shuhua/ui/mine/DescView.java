package com.xy.shuhua.ui.mine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.xy.shuhua.R;

/**
 * Created by xiaoyu on 2016/6/12.
 */
public class DescView extends FrameLayout{
    public TextView desc;

    public DescView(Context context) {
        super(context);
        init(context);
    }

    public DescView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DescView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.desc_view,this,true);

        desc = (TextView) findViewById(R.id.desc);
    }
}
