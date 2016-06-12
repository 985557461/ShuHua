package com.xy.shuhua.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.xy.shuhua.R;

/**
 * Created by xiaoyu on 2016/6/12.
 */
public class CategoryCheckView extends FrameLayout {
    public TextView name;
    public ImageView checkView;

    public boolean check;

    public CategoryCheckView(Context context) {
        super(context);
        init(context);
    }

    public CategoryCheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoryCheckView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.category_check_view, this, true);
        name = (TextView) findViewById(R.id.name);
        checkView = (ImageView) findViewById(R.id.checkView);
    }

    public void setCheck(boolean check) {
        this.check = check;
        checkView.setSelected(check);
    }

    public boolean isChecked(){
        return check;
    }
}
