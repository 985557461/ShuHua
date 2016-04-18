package com.xy.shuhua.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.Account;
import com.xy.shuhua.ui.CustomApplication;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.ui.user.ActivityLogin;
import com.xy.shuhua.ui.user.ActivityModifyPassword;
import com.xy.shuhua.ui.user.ActivityMyInfo;

/**
 * Created by xiaoyu on 2016/4/14.
 */
public class ActivitySetting extends ActivityBaseNoSliding implements View.OnClickListener{
    private View backView;
    private View modifyPwd;
    private View modifyInfo;
    private View aboutUs;
    private View clearHuanCun;
    private View logout;

    public static void open(Activity activity){
        Intent intent = new Intent(activity,ActivitySetting.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        modifyPwd = findViewById(R.id.modifyPwd);
        modifyInfo = findViewById(R.id.modifyInfo);
        aboutUs = findViewById(R.id.aboutUs);
        clearHuanCun = findViewById(R.id.clearHuanCun);
        logout = findViewById(R.id.logout);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
        modifyInfo.setOnClickListener(this);
        modifyPwd.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        clearHuanCun.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backView:
                finish();
                break;
            case R.id.modifyPwd:
                ActivityModifyPassword.open(this);
                break;
            case R.id.modifyInfo:
                ActivityMyInfo.open(this);
                break;
            case R.id.logout:
                Account account = CustomApplication.getInstance().getAccount();
                account.clearMeInfo();
                account.saveMeInfoToPreference();
                ActivityLogin.open(this);
                break;
        }
    }
}
