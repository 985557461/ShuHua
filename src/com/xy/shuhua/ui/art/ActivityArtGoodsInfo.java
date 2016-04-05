package com.xy.shuhua.ui.art;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;

/**
 * Created by xiaoyu on 2016/4/5.
 */
public class ActivityArtGoodsInfo extends ActivityBaseNoSliding {

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityArtGoodsInfo.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_goods_info);
    }

    @Override
    protected void getViews() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {

    }
}
