package com.xy.shuhua.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.CommonModel;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.util.CommonUtil;
import com.xy.shuhua.util.DialogUtil;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.PrintHttpUrlUtil;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import okhttp3.Call;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/5/14.
 */
public class ActivityForgetPwd extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private TextView title;
    private View rightView;
    private EditText contentEditText;
    private TextView message;

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityForgetPwd.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        title = (TextView) findViewById(R.id.title);
        rightView = findViewById(R.id.rightView);
        contentEditText = (EditText) findViewById(R.id.contentEditText);
        message = (TextView) findViewById(R.id.message);
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void setListeners() {
        rightView.setOnClickListener(this);
        backView.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        CommonUtil.hideSoftWindow(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backView:
                finish();
                break;
            case R.id.rightView:
                findPwd();
                break;
        }
    }

    private void findPwd() {
        final String phoneStr = contentEditText.getText().toString();
        if (TextUtils.isEmpty(phoneStr)) {
            ToastUtil.makeShortText("请输入电话号码");
            return;
        }
        if (!CommonUtil.isPhoneNumberValid(phoneStr)) {
            ToastUtil.makeShortText("电话号码不合法");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneStr);
        DialogUtil.getInstance().showLoading(this);
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.FIND_PWD, params);
        OkHttpUtils.post()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.FIND_PWD)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        DialogUtil.getInstance().dismissLoading(ActivityForgetPwd.this);
                        ToastUtil.makeShortText("网络连接失败");
                    }

                    @Override
                    public void onResponse(String response) {
                        DialogUtil.getInstance().dismissLoading(ActivityForgetPwd.this);
                        CommonModel commonModel = GsonUtil.transModel(response, CommonModel.class);
                        if (commonModel != null && "1".equals(commonModel.result)) {
                            ToastUtil.makeLongText("您的密码为：" + commonModel.message);
                            finish();
                        } else {
                            ToastUtil.makeShortText("网络连接失败");
                        }
                    }
                });
    }
}
