package com.xy.shuhua.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
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
    private View nameView;
    private TextView nameTv;
    private View priceView;
    private TextView priceTv;
    private View leiBieView;
    private TextView leiBieTv;
    private View caiZhiView;
    private TextView caiZhiTv;
    private View chiCunView;
    private TextView chiCunTv;
    private View createTimeView;
    private TextView createTimeTv;
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

    /**
     * requestcode*
     */
    private static final int request_name = 1001;
    private static final int request_price = 1002;
    private static final int request_leibie = 1003;
    private static final int request_caizhi = 1004;
    private static final int request_cicun = 1005;

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
        nameView = findViewById(R.id.nameView);
        nameTv = (TextView) findViewById(R.id.nameTv);
        priceView = findViewById(R.id.priceView);
        priceTv = (TextView) findViewById(R.id.priceTv);
        leiBieView = findViewById(R.id.leiBieView);
        leiBieTv = (TextView) findViewById(R.id.leiBieTv);
        caiZhiView = findViewById(R.id.caiZhiView);
        caiZhiTv = (TextView) findViewById(R.id.caiZhiTv);
        chiCunView = findViewById(R.id.chiCunView);
        chiCunTv = (TextView) findViewById(R.id.chiCunTv);
        createTimeView = findViewById(R.id.createTimeView);
        createTimeTv = (TextView) findViewById(R.id.createTimeTv);
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
        nameView.setOnClickListener(this);
        priceView.setOnClickListener(this);
        leiBieView.setOnClickListener(this);
        caiZhiView.setOnClickListener(this);
        chiCunView.setOnClickListener(this);
        createTimeView.setOnClickListener(this);
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
                tryToUpload();
                break;
            case R.id.nameView:
                ActivityInputContent.openForResult(this, request_name, "作品名称", nameTv.getText().toString());
                break;
            case R.id.priceView:
                ActivityInputContent.openForResult(this, request_price, "价格", priceTv.getText().toString());
                break;
            case R.id.leiBieView:
                ActivityInputContent.openForResult(this, request_leibie, "类别", leiBieTv.getText().toString());
                break;
            case R.id.caiZhiView:
                ActivityInputContent.openForResult(this, request_caizhi, "材质", caiZhiTv.getText().toString());
                break;
            case R.id.chiCunView:
                ActivityInputContent.openForResult(this, request_cicun, "尺寸", chiCunTv.getText().toString());
                break;
            case R.id.createTimeView:

                break;
        }
    }

    private void tryToUpload() {
        /**先判断需不需要上传图片**/
        if (pathsList != null && pathsList.size() <= 0) {
            ToastUtil.makeShortText("请选择图片");
            return;
        }
        String nameStr = nameTv.getText().toString();
        if (TextUtils.isEmpty(nameStr)) {
            ToastUtil.makeShortText("请输入名称");
            return;
        }
        String priceStr = priceTv.getText().toString();
        if (TextUtils.isEmpty(priceStr)) {
            ToastUtil.makeShortText("请输入价格");
            return;
        }
        String leibieStr = leiBieTv.getText().toString();
        if (TextUtils.isEmpty(leibieStr)) {
            ToastUtil.makeShortText("请输入类别");
            return;
        }
        String caizhiStr = caiZhiTv.getText().toString();
        if (TextUtils.isEmpty(caizhiStr)) {
            ToastUtil.makeShortText("请输入材质");
            return;
        }
        String chicunStr = chiCunTv.getText().toString();
        if (TextUtils.isEmpty(chicunStr)) {
            ToastUtil.makeShortText("请输入尺寸");
            return;
        }
        serverPaths.clear();
        pathIndex = 0;
        uploadImage(pathIndex);
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
        for (int i = 0; i < serverPaths.size(); i++) {
            stringBuilder.append(serverPaths.get(i));
            if (i != serverPaths.size() - 1) {
                stringBuilder.append(",");
            }
        }
        String nameStr = nameTv.getText().toString();
        String priceStr = priceTv.getText().toString();
        String leibieStr = leiBieTv.getText().toString();
        String caizhiStr = caiZhiTv.getText().toString();
        String chicunStr = chiCunTv.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("artname", nameStr);
        params.put("price", priceStr);
        params.put("category", leibieStr);
        params.put("caizhi",caizhiStr);
        params.put("artsize", chicunStr);
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
        if (requestCode == request_name && resultCode == RESULT_OK) {
            nameTv.setText(data.getStringExtra(ActivityInputContent.kcontent));
        } else if (requestCode == request_price && resultCode == RESULT_OK) {
            priceTv.setText(data.getStringExtra(ActivityInputContent.kcontent));
        } else if (requestCode == request_leibie && resultCode == RESULT_OK) {
            leiBieTv.setText(data.getStringExtra(ActivityInputContent.kcontent));
        } else if (requestCode == request_caizhi && resultCode == RESULT_OK) {
            caiZhiTv.setText(data.getStringExtra(ActivityInputContent.kcontent));
        } else if (requestCode == request_cicun && resultCode == RESULT_OK) {
            chiCunTv.setText(data.getStringExtra(ActivityInputContent.kcontent));
        } else if (requestCode == kActivitySettingSelectPicRequest && resultCode == RESULT_OK) {
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
