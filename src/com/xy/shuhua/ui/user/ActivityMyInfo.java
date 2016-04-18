package com.xy.shuhua.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;

/**
 * Created by xiaoyu on 2016/4/18.
 */
public class ActivityMyInfo extends ActivityBaseNoSliding implements View.OnClickListener{
    private View backView;
    private View modifyAvatar;
    private ImageView avatarImage;
    private View modifyNam;
    private TextView nickName;

    public static void open(Activity activity){
        Intent intent = new Intent(activity,ActivityMyInfo.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        modifyAvatar = findViewById(R.id.modifyAvatar);
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        modifyNam = findViewById(R.id.modifyNam);
        nickName = (TextView) findViewById(R.id.nickName);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
        modifyAvatar.setOnClickListener(this);
        modifyNam.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backView:
                finish();
                break;
        }
    }
}
