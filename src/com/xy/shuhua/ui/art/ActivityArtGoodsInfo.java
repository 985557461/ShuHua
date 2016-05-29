package com.xy.shuhua.ui.art;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.CommonModel;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.art.model.ArtGoodsInfoModel;
import com.xy.shuhua.ui.art.model.ArtGoodsItemModel;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.ui.home.ActivityAuthorHomePage;
import com.xy.shuhua.ui.photo_look.ActivityPhotoLook;
import com.xy.shuhua.util.DialogUtil;
import com.xy.shuhua.util.DisplayUtil;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.PrintHttpUrlUtil;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import io.rong.imkit.RongIM;
import okhttp3.Call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/4/5.
 */
public class ActivityArtGoodsInfo extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private View picView;
    private View likeView;
    private ImageView mainIV;
    private TextView name;
    private TextView guiGe;
    private TextView price;
    private View userInfoView;
    private ImageView authorAvatar;
    private TextView authorName;
    private TextView zuopinCount;
    private View chatView;
    private TextView time;
    private TextView bianhao;
    private View otherZuoPinView;
    private LinearLayout zuopinContainer;

    private static final String kGoodsId = "goods_id";
    private String goodsId;

    private ArtGoodsInfoModel artGoodsInfoModel;
    private ArrayList<String> imageUrls = new ArrayList<>();

    private String userName;
    private String userAvatar;

    public static void open(Activity activity, String goodsId) {
        Intent intent = new Intent(activity, ActivityArtGoodsInfo.class);
        intent.putExtra(kGoodsId, goodsId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsId = getIntent().getStringExtra(kGoodsId);
        if (TextUtils.isEmpty(goodsId)) {
            ToastUtil.makeShortText("商品不存在");
        }
        setContentView(R.layout.activity_art_goods_info);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        picView = findViewById(R.id.picView);
        likeView = findViewById(R.id.likeView);
        mainIV = (ImageView) findViewById(R.id.mainIV);
        name = (TextView) findViewById(R.id.name);
        guiGe = (TextView) findViewById(R.id.guiGe);
        userInfoView = findViewById(R.id.userInfoView);
        price = (TextView) findViewById(R.id.price);
        authorAvatar = (ImageView) findViewById(R.id.authorAvatar);
        authorName = (TextView) findViewById(R.id.authorName);
        zuopinCount = (TextView) findViewById(R.id.zuopinCount);
        chatView = findViewById(R.id.chatView);
        time = (TextView) findViewById(R.id.time);
        bianhao = (TextView) findViewById(R.id.bianhao);
        otherZuoPinView = findViewById(R.id.otherZuoPinView);
        zuopinContainer = (LinearLayout) findViewById(R.id.zuopinContainer);
    }

    @Override
    protected void initViews() {
        refreshData();
    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
        picView.setOnClickListener(this);
        likeView.setOnClickListener(this);
        userInfoView.setOnClickListener(this);
        chatView.setOnClickListener(this);
    }

    private void updateUI() {
        if (artGoodsInfoModel == null) {
            return;
        }
        if (artGoodsInfoModel.art != null) {
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.imageurl)) {
                imageUrls.add(artGoodsInfoModel.art.imageurl);
                Glide.with(this).load(artGoodsInfoModel.art.imageurl).into(mainIV);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.imageurl1)) {
                imageUrls.add(artGoodsInfoModel.art.imageurl1);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.imageurl2)) {
                imageUrls.add(artGoodsInfoModel.art.imageurl2);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.imageurl3)) {
                imageUrls.add(artGoodsInfoModel.art.imageurl3);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.imageurl4)) {
                imageUrls.add(artGoodsInfoModel.art.imageurl4);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.imageurl5)) {
                imageUrls.add(artGoodsInfoModel.art.imageurl5);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.name)) {
                name.setText(artGoodsInfoModel.art.name);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.artsize)) {
                guiGe.setText(artGoodsInfoModel.art.artsize);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.price)) {
                price.setText(artGoodsInfoModel.art.price);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.artnum)) {
                zuopinCount.setText(artGoodsInfoModel.art.artnum + "件作品");
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.newdate)) {
                time.setText(artGoodsInfoModel.art.newdate);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.id)) {
                bianhao.setText("默认艺术 " + artGoodsInfoModel.art.id);
            }
        }
        if (artGoodsInfoModel.user != null) {
            if (!TextUtils.isEmpty(artGoodsInfoModel.user.imageurl)) {
                Glide.with(this).load(artGoodsInfoModel.user.imageurl).error(R.drawable.me_avatar_boy).into(authorAvatar);
            } else {
                authorAvatar.setImageResource(R.drawable.me_avatar_boy);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.user.nickname)) {
                authorName.setText(artGoodsInfoModel.user.nickname);
            }
        }
        if (artGoodsInfoModel.lists == null || artGoodsInfoModel.lists.size() == 0) {
            otherZuoPinView.setVisibility(View.GONE);
        } else {
            otherZuoPinView.setVisibility(View.VISIBLE);
            zuopinContainer.removeAllViews();
            int count = artGoodsInfoModel.lists.size();
            int row = count / 2 + (count % 2 == 0 ? 0 : 1);
            for (int i = 0; i < row; i++) {
                RelevantRowView rowView = new RelevantRowView(this);
                int indexOne = i * 2;
                int indexTwo = i * 2 + 1;
                ArtGoodsItemModel modelOne = null;
                ArtGoodsItemModel modelTwo = null;
                if (indexOne < count) {
                    modelOne = artGoodsInfoModel.lists.get(indexOne);
                }
                if (indexTwo < count) {
                    modelTwo = artGoodsInfoModel.lists.get(indexTwo);
                }
                rowView.setData(modelOne, modelTwo);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (i != row - 1) {
                    params.bottomMargin = DisplayUtil.dip2px(this, 12);
                }
                zuopinContainer.addView(rowView, params);
            }
        }

        //根据id查询用户具体信息
        if (artGoodsInfoModel != null && artGoodsInfoModel.user != null) {
            String userId = artGoodsInfoModel.user.userid;
            if (!TextUtils.isEmpty(userId)) {
                getUserInfo(userId);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backView:
                finish();
                break;
            case R.id.likeView:
                praiseZuoPin();
                break;
            case R.id.picView:
                ActivityPhotoLook.open(this,imageUrls);
                break;
            case R.id.chatView:
                tryToChat();
                break;
            case R.id.userInfoView:
                if (artGoodsInfoModel != null && artGoodsInfoModel.user != null) {
                    String userId = artGoodsInfoModel.user.userid;
                    if (TextUtils.isEmpty(userId)) {
                        ToastUtil.makeShortText("作家不存在");
                        return;
                    }

                    ActivityAuthorHomePage.open(this, userId, userAvatar, userName);
                }
                break;
        }
    }

    private void tryToChat() {
        if (artGoodsInfoModel != null && artGoodsInfoModel.user != null) {
            String userId = artGoodsInfoModel.user.userid;
            if (TextUtils.isEmpty(userId)) {
                ToastUtil.makeShortText("作家不存在");
                return;
            }
            if(TextUtils.isEmpty(userName)){
                RongIM.getInstance().startPrivateChat(this, userId, userId);
            }else{
                RongIM.getInstance().startPrivateChat(this, userId, userName);
            }
        }
    }

    private void refreshData() {
        Map<String, String> params = new HashMap<>();
        params.put("id", goodsId);
        DialogUtil.getInstance().showLoading(this);
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.ZUOPIN_INFO, params);
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.ZUOPIN_INFO)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        DialogUtil.getInstance().dismissLoading(ActivityArtGoodsInfo.this);
                        ToastUtil.makeShortText("网络连接失败了");
                    }

                    @Override
                    public void onResponse(String response) {
                        DialogUtil.getInstance().dismissLoading(ActivityArtGoodsInfo.this);
                        ArtGoodsInfoModel model = GsonUtil.transModel(response, ArtGoodsInfoModel.class);
                        if (model != null && "1".equals(model.result)) {
                            artGoodsInfoModel = model;
                            updateUI();
                        } else {
                            ToastUtil.makeShortText("网络连接失败了");
                        }
                    }
                });
    }

    private void praiseZuoPin() {
        Map<String, String> params = new HashMap<>();
        params.put("id", artGoodsInfoModel.art.id);
        DialogUtil.getInstance().showLoading(this);
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.PRAISE_ZUOPIN, params);
        OkHttpUtils.post()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.PRAISE_ZUOPIN)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        DialogUtil.getInstance().dismissLoading(ActivityArtGoodsInfo.this);
                        ToastUtil.makeShortText("网络连接失败");
                    }

                    @Override
                    public void onResponse(String response) {
                        DialogUtil.getInstance().dismissLoading(ActivityArtGoodsInfo.this);
                        CommonModel commonModel = GsonUtil.transModel(response, CommonModel.class);
                        if (commonModel != null && "1".equals(commonModel.result)) {
                            ToastUtil.makeShortText("点赞成功");
                        } else {
                            ToastUtil.makeShortText("网络连接失败");
                        }
                    }
                });
    }

    private void getUserInfo(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", userId);
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.GET_USER_INFO, params);
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.GET_USER_INFO)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("xiaoyu",e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        UserModel userModel = GsonUtil.transModel(response,UserModel.class);
                        if(userModel != null){
                            userName = userModel.nickname;
                            userAvatar = userModel.imagepath;
                        }
                    }
                });
    }

    class UserModel{
        @SerializedName("message")
        public String message;
        @SerializedName("result")
        public String result;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("imagepath")
        public String imagepath;
        @SerializedName("userid")
        public String userid;
    }
}
