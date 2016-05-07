package com.xy.shuhua.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import com.google.gson.annotations.SerializedName;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.Account;
import com.xy.shuhua.common_background.CommonModel;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.CustomApplication;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.ui.home.photo_choose.PhotoSelectAdapter;
import com.xy.shuhua.ui.home.photo_choose.PhotoSelectView;
import com.xy.shuhua.ui.home.photo_choose.SquarePhotoView;
import com.xy.shuhua.util.DisplayUtil;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import com.xy.shuhua.util.photo.IntentUtils;
import com.xy.shuhua.util.photo.PhotoActivity;
import com.xy.shuhua.util.photo.PhotoAlbumActivity;
import okhttp3.Call;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/4/17.
 */
public class ActivityPublishZuoPin extends ActivityBaseNoSliding implements View.OnClickListener,
        PhotoSelectView.FooterClickListener, PhotoSelectView.PhotoDeleteListener {
    private View backView;
    private View rightView;
    /**
     * picture about
     * *
     */
    private PhotoSelectView photoSelectView;
    private PhotoSelectAdapter adapter;
    private static final int kActivitySettingSelectPicRequest = 101;
    private String avatarPath = "";
    private List<String> pathsList = new ArrayList<String>();
    private List<String> serverPaths = new ArrayList<String>();

    /**
     * 图片上传的索引*
     */
    private int pathIndex;


    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityPublishZuoPin.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_zuopin);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        rightView = findViewById(R.id.rightView);
        photoSelectView = (PhotoSelectView) findViewById(R.id.photoSelectView);
    }

    @Override
    protected void initViews() {
        adapter = new MyPhotoSelectAdapter();
        photoSelectView.setAdapter(adapter);
    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
        rightView.setOnClickListener(this);
        photoSelectView.setFooterClickListener(this);
        photoSelectView.setPhotoDeleteListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backView:
                finish();
                break;
            case R.id.rightView:
                /**先判断需不需要上传图片**/
                if (pathsList != null && pathsList.size() <= 0) {
                    ToastUtil.makeShortText("请选择图片");
                    return;
                }
                serverPaths.clear();
                pathIndex = 0;
                uploadImage(pathIndex);
                break;
        }
    }

    private void uploadImage(int index) {
        OkHttpUtils.post()
                .addFile("file", System.currentTimeMillis() + "", new File(pathsList.get(index)))
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
                            serverPaths.add(uploadImageModel.result);
                            pathIndex++;
                            if (pathIndex > pathsList.size() - 1) {
                                Log.d("xiaoyu", "图片上传完毕");
                                publishZuoPin();
                            } else {
                                uploadImage(pathIndex);
                            }
                            ToastUtil.makeShortText("图片上传成功");
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

    private void publishZuoPin() {
        StringBuilder stringBuilder = new StringBuilder();
        int realCount = serverPaths.size();
        int serverCount = 6;
        if (realCount < serverCount) {
            int addCount = serverCount - realCount;
            for (int i = 0; i < addCount; i++) {
                serverPaths.add("\"\"");
            }
        }
        for (int i = 0; i < serverCount; i++) {
            stringBuilder.append(serverPaths.get(i));
            if (i != serverPaths.size() - 1) {
                stringBuilder.append(",");
            }
        }
        Map<String, String> params = new HashMap<>();
        params.put("artname", "test");
        params.put("price", "100");
        params.put("category", "1");
        params.put("caizhi", "砂纸");
        params.put("artsize", "100");
        params.put("imageurl", stringBuilder.toString());
        Account account = CustomApplication.getInstance().getAccount();
        params.put("userid", account.userId);
        OkHttpUtils.post()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.PUBLISH_ZUOPIN)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.makeShortText("网络连接失败了");
                    }

                    @Override
                    public void onResponse(String response) {
                        CommonModel homeInfoModel = GsonUtil.transModel(response, CommonModel.class);
                        if (homeInfoModel == null || !"1".equals(homeInfoModel.result)) {
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        ToastUtil.makeShortText("发布成功");
                        finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == kActivitySettingSelectPicRequest && resultCode == RESULT_OK) {
            String[] paths = data.getStringArrayExtra(PhotoAlbumActivity.Key_SelectPaths);
            if (paths != null && paths.length <= 0) {
                return;
            }
            if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromAlbum)) {
                if (!TextUtils.isEmpty(paths[0])) {
                    avatarPath = paths[0];
                    pathsList.add(avatarPath);
                    photoSelectView.setAdapter(adapter);
                }
            } else if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromCamera)) {
                if (!TextUtils.isEmpty(paths[0])) {
                    avatarPath = paths[0];
                    pathsList.add(avatarPath);
                    photoSelectView.setAdapter(adapter);
                }
            }
        }
//        else if (requestCode == kPhotoCropImageRequest && resultCode == RESULT_OK) {
//            avatarPath = data.getStringExtra(ActivityCropImage.kCropImagePath);
//            pathsList.add(avatarPath);
//            photoSelectView.setAdapter(adapter);
//            return;
//        }
    }

    @Override
    public void onFooterClicked() {
        Intent intent = IntentUtils.goToAlbumIntent(new ArrayList<String>(), 1, getResources().getString(R.string.confirm), true, ActivityPublishZuoPin.this);
        startActivityForResult(intent, kActivitySettingSelectPicRequest);
    }

    @Override
    public void onPhotoDeleteClicked(String path) {
        pathsList.remove(path);
        photoSelectView.setAdapter(adapter);
    }

    private class MyPhotoSelectAdapter extends PhotoSelectAdapter<SquarePhotoView> {

        @Override
        public int getWidth() {
            WindowManager wm = (WindowManager) ActivityPublishZuoPin.this.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            return width - DisplayUtil.dip2px(ActivityPublishZuoPin.this, 14);
        }

        @Override
        public int getTotalCount() {
            return pathsList.size();
        }

        @Override
        public int getColCount() {
            return 4;
        }

        @Override
        public int getHorMargin() {
            return 0;
        }

        @Override
        public int getVerMargin() {
            return DisplayUtil.dip2px(ActivityPublishZuoPin.this, 10);
        }

        @Override
        public void setView(SquarePhotoView view, int position) {
            view.setData(pathsList.get(position));
        }
    }
}
