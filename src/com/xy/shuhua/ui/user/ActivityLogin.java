package com.xy.shuhua.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import com.mob.tools.utils.UIHandler;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.Account;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.ActivityMain;
import com.xy.shuhua.ui.CustomApplication;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.util.DialogUtil;
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
public class ActivityLogin extends ActivityBaseNoSliding implements View.OnClickListener, PlatformActionListener, Handler.Callback {
    private View backView;
    private EditText phoneNumber;
    private EditText password;
    private TextView forgetPwd;
    private TextView register;
    private TextView login;
    private View weixinLoginView;
    private View qqLoginView;
    private View sinaLoginView;

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityLogin.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        Account account = CustomApplication.getInstance().getAccount();
        if (!TextUtils.isEmpty(account.userId)) {
            ActivityMain.open(this);
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        password = (EditText) findViewById(R.id.password);
        forgetPwd = (TextView) findViewById(R.id.forgetPwd);
        register = (TextView) findViewById(R.id.register);
        login = (TextView) findViewById(R.id.login);
        weixinLoginView = findViewById(R.id.weixinLoginView);
        qqLoginView = findViewById(R.id.qqLoginView);
        sinaLoginView = findViewById(R.id.sinaLoginView);
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
        weixinLoginView.setOnClickListener(this);
        qqLoginView.setOnClickListener(this);
        sinaLoginView.setOnClickListener(this);
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
            case R.id.qqLoginView:
                authorize(new QZone(this));
                break;
            case R.id.weixinLoginView:
                authorize(new Wechat(this));
                break;
            case R.id.sinaLoginView:
                authorize(new SinaWeibo(this));
                break;
        }
    }

    private void authorize(Platform plat) {
        if (plat.isValid()) {
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                login(plat.getName(), userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    private void login(String plat, String userId, HashMap<String, Object> userInfo) {
        Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    private void tryToLogin() {
        final String phoneStr = phoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneStr)) {
            ToastUtil.makeShortText(getString(R.string.input_phone_num));
            return;
        }
        final String pwdStr = password.getText().toString();
        if (TextUtils.isEmpty(pwdStr)) {
            ToastUtil.makeShortText(getString(R.string.input_pwd));
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
                        ToastUtil.makeShortText(getString(R.string.login_failed));
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
                        ToastUtil.makeShortText(getString(R.string.login_failed));
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("xiaoyu", response);
                        if (TextUtils.isEmpty(response)) {
                            ToastUtil.makeShortText(getString(R.string.login_failed));
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
                            ToastUtil.makeShortText(getString(R.string.login_failed));
                        }
                    }
                });
    }


    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(CustomApplication.getCurProcessName(getApplicationContext()))) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    ToastUtil.makeShortText(getString(R.string.login_failed));
                    Log.d("xiaoyu", "--onTokenIncorrect");
                }

                @Override
                public void onSuccess(String userid) {
                    ToastUtil.makeShortText(getString(R.string.login_success));
                    ActivityMain.open(ActivityLogin.this);
                    finish();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    ToastUtil.makeShortText(getString(R.string.login_failed));
                }
            });
        }
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
            login(platform.getName(), platform.getDb().getUserId(), res);
        }
        System.out.println(res);
        System.out.println("------User Name ---------" + platform.getDb().getUserName());
        System.out.println("------User ID ---------" + platform.getDb().getUserId());
        final String avatar = platform.getDb().getUserIcon();
        final String name = platform.getDb().getUserName();
        final String token = platform.getDb().getToken();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                thirdLogin(avatar, name, token);
            }
        });
    }

    //第三方登陆接口
    private void thirdLogin(String avatar, String name, String token) {
        Map<String, String> params = new HashMap<>();
        params.put("userType", "0");
        params.put("nickName", name);
        params.put("imagepath", avatar);
        params.put("ucode", token);
        params.put("flag", "1");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtil.getInstance().showLoading(ActivityLogin.this);
            }
        });
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.THIRD_LOGIN, params);
        OkHttpUtils.post()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.THIRD_LOGIN)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogUtil.getInstance().dismissLoading(ActivityLogin.this);
                                ToastUtil.makeShortText(getString(R.string.login_failed));
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("xiaoyu", "response:" + response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogUtil.getInstance().dismissLoading(ActivityLogin.this);
                            }
                        });
                        UserInfoModel userInfoModel = GsonUtil.transModel(response, UserInfoModel.class);
                        if (userInfoModel != null && "1".equals(userInfoModel.result)) {
                            Account account = CustomApplication.getInstance().getAccount();
                            account.userId = userInfoModel.userid;
                            account.userName = userInfoModel.nickname;
                            account.avatar = userInfoModel.imageurl;
                            account.saveMeInfoToPreference();
                            getChatToken();
                        } else {
                            ToastUtil.makeShortText(getString(R.string.login_failedd));
                        }
                    }
                });
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MSG_USERID_FOUND: {
            }
            break;
            case MSG_LOGIN: {
                Toast.makeText(this, getString(R.string.login), Toast.LENGTH_SHORT).show();
                System.out.println("---------------");

//				Builder builder = new Builder(this);
//				builder.setTitle(R.string.if_register_needed);
//				builder.setMessage(R.string.after_auth);
//				builder.setPositiveButton(R.string.ok, null);
//				builder.create().show();
            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, getString(R.string.shouquan_calcel), Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_CANCEL--------");
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, getString(R.string.shouquan_failed), Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_ERROR--------");
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this, getString(R.string.shouquan_success), Toast.LENGTH_SHORT).show();
                System.out.println("--------MSG_AUTH_COMPLETE-------");
            }
            break;
        }
        return false;
    }
}
