package com.xy.shuhua.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;

/**
 * Created by xiaoyu on 2016/5/15.
 */
public class ActivityIdentifyShowTips extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private View nextStepView;

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityIdentifyShowTips.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_show_tips);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        nextStepView = findViewById(R.id.nextStepView);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
        nextStepView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.nextStepView) {
            ActivityIdentifyInfo.open(this);
            finish();
        } else if (view.getId() == R.id.backView) {
            finish();
        }
    }
}
