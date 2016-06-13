package com.xy.shuhua.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.Account;
import com.xy.shuhua.common_background.CommonModel;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.ActivityQuadrilateralCrop;
import com.xy.shuhua.ui.CustomApplication;
import com.xy.shuhua.ui.PhotoChooser.PhotoPickerActivity;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.ui.home.ActivityInputContent;
import com.xy.shuhua.util.CompressUtil;
import com.xy.shuhua.util.DialogUtil;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import okhttp3.Call;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/4/18.
 */
public class ActivityMyInfo extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private View modifyAvatar;
    private ImageView avatarImage;
    private View modifyName;
    private TextView nickName;
    private View areaView;
    private View ageView;
    private View jieShaoView;
    private View saveView;

    private Account account;

    private static final int PICK_PHOTO = 101;
    private static final int Crop_Photo = 102;
    private static final int request_name = 1002;
    private static final int request_area = 1003;
    private static final int request_age = 1004;
    private static final int request_introduce = 1005;

    private String avatarPath = "";
    private String nameStr = "";
    private String areaStr = "";
    private String ageStr = "";
    private String introduceStr = "";

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityMyInfo.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
    }

    @Override
    protected void getViews() {
        account = CustomApplication.getInstance().getAccount();
        nameStr = account.userName;
        areaStr = account.address;
        ageStr = account.age;
        introduceStr = account.introduce;
        backView = findViewById(R.id.backView);
        modifyAvatar = findViewById(R.id.modifyAvatar);
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        modifyName = findViewById(R.id.modifyName);
        nickName = (TextView) findViewById(R.id.nickName);
        areaView = findViewById(R.id.areaView);
        ageView = findViewById(R.id.ageView);
        jieShaoView = findViewById(R.id.jieShaoView);
        saveView = findViewById(R.id.saveView);
    }

    @Override
    protected void initViews() {
        if (!TextUtils.isEmpty(account.avatar)) {
            Glide.with(this).load(account.avatar).placeholder(R.drawable.me_avatar_boy).error(R.drawable.me_avatar_boy).into(avatarImage);
        } else {
            avatarImage.setImageResource(R.drawable.me_avatar_boy);
        }

        if (!TextUtils.isEmpty(account.userName)) {
            nickName.setText(account.userName);
        } else {
            nickName.setText("");
        }
    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
        modifyAvatar.setOnClickListener(this);
        modifyName.setOnClickListener(this);
        areaView.setOnClickListener(this);
        ageView.setOnClickListener(this);
        jieShaoView.setOnClickListener(this);
        saveView.setOnClickListener(this);
    }

    private void modifyAvatar() {
        DialogUtil.getInstance().showLoading(this);
        Map<String, String> params = new HashMap<>();
        params.put("userid", account.userId);
        OkHttpUtils.post()
                .params(params)
                .addFile("file", System.currentTimeMillis() + "", new File(avatarPath))
                .url(ServerConfig.BASE_URL + ServerConfig.MODIFY_AVATAR)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        DialogUtil.getInstance().dismissLoading(ActivityMyInfo.this);
                        ToastUtil.makeShortText("网络连接失败了");
                    }

                    @Override
                    public void onResponse(String response) {
                        UploadImageModel uploadImageModel = GsonUtil.transModel(response, UploadImageModel.class);
                        if (uploadImageModel != null && !TextUtils.isEmpty(uploadImageModel.result)) {
                            account.avatar = uploadImageModel.result;
                            modifyMyInfo();
                        } else {
                            DialogUtil.getInstance().dismissLoading(ActivityMyInfo.this);
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

    private void modifyMyInfo() {
        if(TextUtils.isEmpty(nameStr)){
            ToastUtil.makeShortText("请填写昵称");
            return;
        }
        if(TextUtils.isEmpty(areaStr)){
            ToastUtil.makeShortText("请填写地区");
            return;
        }
        if(TextUtils.isEmpty(ageStr)){
            ToastUtil.makeShortText("请填写年龄");
            return;
        }
        if(TextUtils.isEmpty(introduceStr)){
            ToastUtil.makeShortText("请填写个人介绍");
            return;
        }
        DialogUtil.getInstance().showLoading(this);
        Map<String, String> params = new HashMap<>();
        params.put("userid", account.userId);
        params.put("nickname", nameStr);
        params.put("address", areaStr);
        params.put("age", ageStr);
        params.put("introduce", introduceStr);
        OkHttpUtils.post()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.MODIFY_OTHER_INFO)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        DialogUtil.getInstance().dismissLoading(ActivityMyInfo.this);
                        ToastUtil.makeShortText("网络连接失败了");
                    }

                    @Override
                    public void onResponse(String response) {
                        DialogUtil.getInstance().dismissLoading(ActivityMyInfo.this);
                        CommonModel homeInfoModel = GsonUtil.transModel(response, CommonModel.class);
                        if (homeInfoModel == null || !"1".equals(homeInfoModel.result)) {
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        account.userName = nameStr;
                        account.address = areaStr;
                        account.age = ageStr;
                        account.introduce = introduceStr;
                        account.saveMeInfoToPreference();
                        ToastUtil.makeShortText("修改成功");
                        finish();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backView:
                finish();
                break;
            case R.id.modifyAvatar:
                Intent intent = new Intent(ActivityMyInfo.this, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
                startActivityForResult(intent, PICK_PHOTO);
                break;
            case R.id.modifyName:
                ActivityInputContent.openForResult(this, request_name, "昵称", account.userName);
                break;
            case R.id.areaView:
                ActivityInputContent.openForResult(this, request_area, "地区", account.address);
                break;
            case R.id.ageView:
                ActivityInputContent.openForResult(this, request_age, "年龄", account.age);
                break;
            case R.id.jieShaoView:
                ActivityInputContent.openForResult(this, request_introduce, "个人介绍", account.introduce);
                break;
            case R.id.saveView:
                tryToModify();
                break;
        }
    }

    private void tryToModify() {
        if (!TextUtils.isEmpty(avatarPath)) {
            modifyAvatar();
        } else {
            modifyMyInfo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_name && resultCode == RESULT_OK) {
            nameStr = data.getStringExtra(ActivityInputContent.kcontent);
            nickName.setText(nameStr);
        } else if (requestCode == request_area && resultCode == RESULT_OK) {
            areaStr = data.getStringExtra(ActivityInputContent.kcontent);
        } else if (requestCode == request_age && resultCode == RESULT_OK) {
            ageStr = data.getStringExtra(ActivityInputContent.kcontent);
        } else if (requestCode == request_introduce && resultCode == RESULT_OK) {
            introduceStr = data.getStringExtra(ActivityInputContent.kcontent);
        } else if (requestCode == PICK_PHOTO && resultCode == RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
            if (result != null && result.size() <= 0) {
                return;
            }
            if (!TextUtils.isEmpty(result.get(0))) {
                ActivityQuadrilateralCrop.openForResult(ActivityMyInfo.this, result.get(0), Crop_Photo);
            }
        }else if(requestCode == Crop_Photo && resultCode == RESULT_OK){
            avatarPath = data.getStringExtra(ActivityQuadrilateralCrop.kSavePath);
            avatarPath = CompressUtil.getCompressBmp(avatarPath);
            Glide.with(this).load(new File(avatarPath)).placeholder(R.drawable.me_avatar_boy).error(R.drawable.me_avatar_boy).into(avatarImage);
        }
    }
}
