package com.xy.shuhua.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.Account;
import com.xy.shuhua.common_background.CommonModel;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.CustomApplication;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.util.CommonUtil;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.PrintHttpUrlUtil;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import com.xy.shuhua.util.photo.IntentUtils;
import com.xy.shuhua.util.photo.PhotoActivity;
import com.xy.shuhua.util.photo.PhotoAlbumActivity;
import okhttp3.Call;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/5/15.
 */
public class ActivityIdentifyInfo extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private View sureView;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private View phontoView;
    private ImageView cardShow;

    private static final int request_avatar = 1001;
    private String avatarPath = "";
    private String serverUrl = "";

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityIdentifyInfo.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_info);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        sureView = findViewById(R.id.sureView);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        phontoView = findViewById(R.id.phontoView);
        cardShow = (ImageView) findViewById(R.id.cardShow);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
        sureView.setOnClickListener(this);
        phontoView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sureView) {
            if (TextUtils.isEmpty(avatarPath)) {
                ToastUtil.makeShortText("请选择身份证照片");
                return;
            }
            String nameStr = nameEditText.getText().toString();
            if (TextUtils.isEmpty(nameStr)) {
                ToastUtil.makeShortText("请输入姓名");
                return;
            }
            String emailStr = emailEditText.getText().toString();
            if (TextUtils.isEmpty(emailStr)) {
                ToastUtil.makeShortText("请输入邮箱");
                return;
            }
            if (!CommonUtil.isEmail(emailStr)) {
                ToastUtil.makeShortText("邮箱不合法");
                return;
            }
            String phoneStr = phoneEditText.getText().toString();
            if (TextUtils.isEmpty(phoneStr)) {
                ToastUtil.makeShortText("请输入手机号码");
                return;
            }
            if (!CommonUtil.isPhoneNumberValid(phoneStr)) {
                ToastUtil.makeShortText("手机号码不合法");
                return;
            }
            String addressStr = addressEditText.getText().toString();
            if (TextUtils.isEmpty(addressStr)) {
                ToastUtil.makeShortText("请输入地址");
                return;
            }
            uploadCardFile();
        } else if (view.getId() == R.id.backView) {
            finish();
        } else if (view.getId() == R.id.phontoView) {
            Intent intent = IntentUtils.goToAlbumIntent(new ArrayList<String>(), 1, getResources().getString(R.string.confirm), true, this);
            startActivityForResult(intent, request_avatar);
        }
    }

    private void uploadCardFile() {
        Map<String, String> params = new HashMap<>();
        OkHttpUtils.post()
                .params(params)
                .addFile("file", System.currentTimeMillis() + "", new File(avatarPath))
                .url(ServerConfig.BASE_URL + ServerConfig.UPLOAD_FILE)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.makeShortText("网络连接失败了");
                    }

                    @Override
                    public void onResponse(String response) {
                        UploadImageModel uploadImageModel = GsonUtil.transModel(response, UploadImageModel.class);
                        if (uploadImageModel != null && !TextUtils.isEmpty(uploadImageModel.result)) {
                            serverUrl = uploadImageModel.result;
                            tryToIdentify();
                        } else {
                            ToastUtil.makeShortText("图片上传失败");
                        }
                    }
                });
    }

    class UploadImageModel {
        @SerializedName("message")
        public String message;
        @SerializedName("result")
        public String result;
    }

    private void tryToIdentify() {
        if (TextUtils.isEmpty(avatarPath)) {
            ToastUtil.makeShortText("请选择身份证照片");
            return;
        }
        String nameStr = nameEditText.getText().toString();
        if (TextUtils.isEmpty(nameStr)) {
            ToastUtil.makeShortText("请输入姓名");
            return;
        }
        String emailStr = emailEditText.getText().toString();
        if (TextUtils.isEmpty(emailStr)) {
            ToastUtil.makeShortText("请输入邮箱");
            return;
        }
        if (!CommonUtil.isEmail(emailStr)) {
            ToastUtil.makeShortText("邮箱不合法");
            return;
        }
        String phoneStr = phoneEditText.getText().toString();
        if (TextUtils.isEmpty(phoneStr)) {
            ToastUtil.makeShortText("请输入手机号码");
            return;
        }
        if (!CommonUtil.isPhoneNumberValid(phoneStr)) {
            ToastUtil.makeShortText("手机号码不合法");
            return;
        }
        String addressStr = addressEditText.getText().toString();
        if (TextUtils.isEmpty(addressStr)) {
            ToastUtil.makeShortText("请输入地址");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("realname", nameStr);
        params.put("email", emailStr);
        params.put("mobile", phoneStr);
        params.put("address", addressStr);
        params.put("cardurl", serverUrl);
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.IDENTIFY_INFO, params);
        OkHttpUtils.post()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.IDENTIFY_INFO)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.makeShortText("认证失败");
                    }

                    @Override
                    public void onResponse(String response) {
                        CommonModel commonModel = GsonUtil.transModel(response, CommonModel.class);
                        if ("1".equals(commonModel.result)) {
                            ToastUtil.makeShortText("认证成功");
                            Account account = CustomApplication.getInstance().getAccount();
                            account.userType = "1";
                            finish();
                        } else {
                            ToastUtil.makeShortText("认证失败");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_avatar && resultCode == RESULT_OK) {
            String[] paths = data.getStringArrayExtra(PhotoAlbumActivity.Key_SelectPaths);
            if (paths != null && paths.length <= 0) {
                return;
            }
            if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromAlbum)) {
                if (!TextUtils.isEmpty(paths[0])) {
                    avatarPath = paths[0];
                    Glide.with(this).load(new File(avatarPath)).error(R.drawable.me_avatar_boy).into(cardShow);
                }
            } else if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromCamera)) {
                if (!TextUtils.isEmpty(paths[0])) {
                    avatarPath = paths[0];
                    Glide.with(this).load(new File(avatarPath)).error(R.drawable.me_avatar_boy).into(cardShow);
                }
            }
        }
    }
}
