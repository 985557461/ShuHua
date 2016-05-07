package com.xy.shuhua.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.Account;
import com.xy.shuhua.common_background.CommonModel;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.CustomApplication;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import okhttp3.Call;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyu on 2015/6/14.
 */
public class ActivityModifyPassword extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private View rightView;
    private EditText oldPwd;
    private EditText newPwd;
    private LinearLayout showPwdLL;
    private ImageView showPwd;

    private boolean isShowPwd = false;

    /**
     * account
     * *
     */
    private Account account;

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityModifyPassword.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
    }

    @Override
    protected void getViews() {
        account = CustomApplication.getInstance().getAccount();
        backView = findViewById(R.id.backView);
        rightView = findViewById(R.id.rightView);
        oldPwd = (EditText) findViewById(R.id.oldPwd);
        newPwd = (EditText) findViewById(R.id.newPwd);
        showPwdLL = (LinearLayout) findViewById(R.id.showPwdLL);
        showPwd = (ImageView) findViewById(R.id.showPwd);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
        rightView.setOnClickListener(this);
        showPwdLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backView:
                finish();
                break;
            case R.id.rightView:
                tryToCommit();
                break;
            case R.id.showPwdLL:
                showPwd();
                break;
        }
    }

    private void showPwd() {
        if (!isShowPwd) {
            showPwd.setBackgroundColor(Color.parseColor("#ff0000"));
            oldPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            newPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isShowPwd = true;
        } else {
            showPwd.setBackgroundColor(Color.parseColor("#00ff00"));
            oldPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            newPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isShowPwd = false;
        }
    }

    private void tryToCommit() {
        String oldPwdStr = oldPwd.getText().toString();
        if (TextUtils.isEmpty(oldPwdStr)) {
            ToastUtil.makeShortText("«Î ‰»Îæ…√‹¬Î");
            return;
        }
        final String newPwdStr = newPwd.getText().toString();
        if (TextUtils.isEmpty(newPwdStr)) {
            ToastUtil.makeShortText("«Î ‰»Î–¬√‹¬Î");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("phoneNum", account.phoneNumber);
        params.put("oldPassword", oldPwdStr);
        params.put("newPassword", newPwdStr);
        OkHttpUtils.post()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.MODIFY_PWD)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.makeShortText("Õ¯¬Á¡¨Ω” ß∞‹¡À");
                    }

                    @Override
                    public void onResponse(String response) {
                        CommonModel commonModel = GsonUtil.transModel(response,CommonModel.class);
                        if (commonModel != null && "1".equals(commonModel.result)) {
                            account.password = newPwdStr;
                            account.saveMeInfoToPreference();
                            ToastUtil.makeShortText("–ﬁ∏ƒ≥…π¶");
                            finish();
                        } else {
                            ToastUtil.makeShortText("–ﬁ∏ƒ ß∞‹");
                        }
                    }
                });
    }
}
