package com.xy.shuhua.ui.user;

import android.app.Activity;
import android.content.Intent;
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
import com.xy.shuhua.util.DialogUtil;
import com.xy.shuhua.util.DisplayUtil;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.PrintHttpUrlUtil;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
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

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityLogin.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                ActivityForgetPwd.open(this);
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
            ToastUtil.makeShortText("ÇëÊäÈëµç»°ºÅÂë");
            return;
        }
        final String pwdStr = password.getText().toString();
        if (TextUtils.isEmpty(pwdStr)) {
            ToastUtil.makeShortText("ÊäÈëÃÜÂë");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("phoneNum", phoneStr);
        params.put("password", pwdStr);
        DialogUtil.getInstance().showLoading(this);
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.URL_LOGIN, params);
        OkHttpUtils.post()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.URL_LOGIN)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        DialogUtil.getInstance().dismissLoading(ActivityLogin.this);
                        ToastUtil.makeShortText("µÇÂ¼Ê§°Ü");
                    }

                    @Override
                    public void onResponse(String response) {
                        DialogUtil.getInstance().dismissLoading(ActivityLogin.this);
                        UserInfoModel userInfoModel = GsonUtil.transModel(response, UserInfoModel.class);
                        if ("1".equals(userInfoModel.result)) {
                            Account account = CustomApplication.getInstance().getAccount();
                            account.password = pwdStr;
                            account.phoneNumber = phoneStr;
                            account.userId = userInfoModel.user_id;
                            account.userName = userInfoModel.nickname;
                            account.address = userInfoModel.address;
                            account.age = userInfoModel.age;
                            account.introduce = userInfoModel.introduce;
                            account.userType = userInfoModel.usertype;
                            account.saveMeInfoToPreference();
                            getChatToken();
                        } else {
                            ToastUtil.makeShortText(userInfoModel.message);
                        }
                    }
                });
    }

    private void getChatToken() {
        Account account = CustomApplication.getInstance().getAccount();
        Map<String, String> params = new HashMap<>();
        params.put("userId", account.userId);
        params.put("userName", account.userName);
        params.put("portraitUri", account.avatar);
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.GET_CHAT_TOKEN, params);
        OkHttpUtils.post()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.GET_CHAT_TOKEN)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.makeShortText("µÇÂ¼Ê§°Ü");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("xiaoyu", response);
                        if (TextUtils.isEmpty(response)) {
                            ToastUtil.makeShortText("µÇÂ¼Ê§°Ü");
                            return;
                        }
                        response = response.replaceAll("\\\\", "");
                        response = response.substring(1, response.length());
                        response = response.substring(0, response.length() - 1);
                        TokenModel tokenModel = GsonUtil.transModel(response, TokenModel.class);
                        if (tokenModel != null && "200".equals(tokenModel.code)) {
                            Account account = CustomApplication.getInstance().getAccount();
                            account.chatToken = tokenModel.token;
                            account.saveMeInfoToPreference();
                            connect(tokenModel.token);
                        } else {
                            ToastUtil.makeShortText("µÇÂ¼Ê§°Ü");
                        }
                    }
                });
    }


    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(CustomApplication.getCurProcessName(getApplicationContext()))) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    ToastUtil.makeShortText("µÇÂ¼Ê§°Ü");
                    Log.d("xiaoyu", "--onTokenIncorrect");
                }

                @Override
                public void onSuccess(String userid) {
                    ToastUtil.makeShortText("µÇÂ½³É¹¦");
                    ActivityMain.open(ActivityLogin.this);
                    finish();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    ToastUtil.makeShortText("µÇÂ¼Ê§°Ü");
                }
            });
        }
    }
}
