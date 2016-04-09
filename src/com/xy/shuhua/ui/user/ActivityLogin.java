package com.xy.shuhua.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.Account;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.ActivityMain;
import com.xy.shuhua.ui.CustomApplication;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.PrintHttpUrlUtil;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import okhttp3.Call;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/3/15.
 */
public class ActivityLogin extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private EditText phoneNumber;
    private EditText password;
    private TextView forgetPwd;
    private TextView register;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**≈–∂œ”√ªß «∑Òµ«¬º**/
        Account account = CustomApplication.getInstance().getAccount();
        if (!TextUtils.isEmpty(account.userId)) {
            ActivityMain.open(this);
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        password = (EditText) findViewById(R.id.password);
        forgetPwd = (TextView) findViewById(R.id.forgetPwd);
        register = (TextView) findViewById(R.id.register);
        login = (TextView) findViewById(R.id.login);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        forgetPwd.setOnClickListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        backView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgetPwd:
                break;
            case R.id.register:
                ActivityRegister.open(this);
                break;
            case R.id.login:
                tryToLogin();
                break;
            case R.id.backView:
                finish();
                break;
        }
    }

    private void tryToLogin() {
        final String phoneStr = phoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneStr)) {
            ToastUtil.makeShortText(" ‰»ÎµÁª∞∫≈¬Î");
            return;
        }
        final String pwdStr = password.getText().toString();
        if (TextUtils.isEmpty(pwdStr)) {
            ToastUtil.makeShortText(" ‰»Î√‹¬Î");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("phoneNum", phoneStr);
        params.put("password", pwdStr);
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.URL_LOGIN, params);
        OkHttpUtils.post()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.URL_LOGIN)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("xiaoyu", e.toString());
                        ToastUtil.makeShortText("µ«¬º ß∞‹");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("xiaoyu", response);
                        UserInfoModel userInfoModel = GsonUtil.transModel(response, UserInfoModel.class);
                        if ("1".equals(userInfoModel.result)) {
                            Account account = CustomApplication.getInstance().getAccount();
                            account.password = pwdStr;
                            account.phoneNumber = phoneStr;
                            account.userId = userInfoModel.user_id;
                            account.saveMeInfoToPreference();
                            ToastUtil.makeShortText("µ«¬Ω≥…π¶");
                            ActivityMain.open(ActivityLogin.this);
                            finish();
                        } else {
                            ToastUtil.makeShortText(userInfoModel.message);
                        }
                    }
                });
    }
}
