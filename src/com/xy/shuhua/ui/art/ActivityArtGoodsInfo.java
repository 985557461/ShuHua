package com.xy.shuhua.ui.art;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.CommonModel;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.CustomApplication;
import com.xy.shuhua.ui.art.model.ArtGoodsInfoModel;
import com.xy.shuhua.ui.art.model.ArtGoodsItemModel;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.ui.home.ActivityAuthorHomePage;
import com.xy.shuhua.ui.home.HomeFragment;
import com.xy.shuhua.ui.user.UserInfoModel;
import com.xy.shuhua.ui.view.HackyViewPager;
import com.xy.shuhua.util.DialogUtil;
import com.xy.shuhua.util.DisplayUtil;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.PrintHttpUrlUtil;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import com.xy.shuhua.util.photoview.PhotoView;
import io.rong.imkit.RongIM;
import okhttp3.Call;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/4/5.
 */
public class ActivityArtGoodsInfo extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private View picView;
    private View likeView;
    private View shareView;
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
    private TextView zuopinDesc;
    private View containerView;
    private LinearLayout zuopinContainer;

    //悬浮框相关
    private View backViewTwo;
    private TextView title;
    private ViewPager mViewPager;
    private View xuanfuContainer;
    private SamplePagerAdapter samplePagerAdapter;

    private static final String kGoodsId = "goods_id";
    private String goodsId;

    private ArtGoodsInfoModel artGoodsInfoModel;
    private ArrayList<String> imageUrls = new ArrayList<>();

    private String userName;
    private String userAvatar;
    private String introdece;

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
        shareView = findViewById(R.id.shareView);
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
        zuopinDesc = (TextView) findViewById(R.id.zuopinDesc);
        containerView = findViewById(R.id.containerView);
        zuopinContainer = (LinearLayout) findViewById(R.id.zuopinContainer);

        backViewTwo = findViewById(R.id.backViewTwo);
        title = (TextView) findViewById(R.id.title);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        xuanfuContainer = findViewById(R.id.xuanfuContainer);
    }

    @Override
    protected void initViews() {
        refreshData();

        mainIV.setOnClickListener(this);
        backViewTwo.setOnClickListener(this);
        samplePagerAdapter = new SamplePagerAdapter();
        mViewPager.setAdapter(samplePagerAdapter);
    }

    private class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(ActivityArtGoodsInfo.this).load(imageUrls.get(position)).into(photoView);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    protected void setListeners() {
        shareView.setOnClickListener(this);
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
            samplePagerAdapter.notifyDataSetChanged();
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.name)) {
                name.setText(artGoodsInfoModel.art.name);
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.artsize)) {
                guiGe.setText("尺寸: " + artGoodsInfoModel.art.artsize);
            }else{
                guiGe.setText("尺寸: 默认");
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.descri)) {
                zuopinDesc.setText(artGoodsInfoModel.art.descri);
            }else{
                zuopinDesc.setText("该作品目前还没有简介");
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.price)) {
                price.setText(artGoodsInfoModel.art.price + "元");
            }else{
                price.setText("暂无报价");
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.artnum)) {
                zuopinCount.setText(artGoodsInfoModel.art.artnum + "件作品");
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.newdate)) {
                time.setText(artGoodsInfoModel.art.newdate);
            } else {
                time.setText(new Date().toString());
            }
            if (!TextUtils.isEmpty(artGoodsInfoModel.art.id)) {
                bianhao.setText("默认艺术 " + artGoodsInfoModel.art.id);
            } else {
                bianhao.setText("默认艺术 0");
            }
        }
        if (artGoodsInfoModel.user != null) {
            if (!TextUtils.isEmpty(artGoodsInfoModel.user.imageurl)) {
                Glide.with(this).load(artGoodsInfoModel.user.imageurl).placeholder(R.drawable.me_avatar_boy).error(R.drawable.me_avatar_boy).into(authorAvatar);
            } else {
                authorAvatar.setImageResource(R.drawable.me_avatar_boy);
            }
            userName = getUserName(artGoodsInfoModel.user);
            userAvatar = artGoodsInfoModel.user.imageurl;
            introdece = artGoodsInfoModel.user.introduce;
            if (!TextUtils.isEmpty(userName)) {
                authorName.setText(artGoodsInfoModel.user.nickname);
            } else {
                authorName.setText("游客");
            }
        }
        if (artGoodsInfoModel.lists == null || artGoodsInfoModel.lists.size() == 0) {
            containerView.setVisibility(View.GONE);
        } else {
            containerView.setVisibility(View.VISIBLE);
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
        if (TextUtils.isEmpty(userName)) {
            if (artGoodsInfoModel != null && artGoodsInfoModel.user != null) {
                String userId = artGoodsInfoModel.user.userid;
                if (!TextUtils.isEmpty(userId)) {
                    getUserInfo(userId);
                }
            }
        }
    }

    //得到发布作品人的名字
    private String getUserName(UserInfoModel userInfoModel) {
        if (!TextUtils.isEmpty(userInfoModel.realname)) {
            return userInfoModel.realname;
        }
        if (!TextUtils.isEmpty(userInfoModel.nickname)) {
            return userInfoModel.nickname;
        }
        if (!TextUtils.isEmpty(userInfoModel.username)) {
            return userInfoModel.username;
        }
        return "";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backViewTwo:
                xuanfuContainer.setVisibility(View.INVISIBLE);
                break;
            case R.id.backView:
                finish();
                break;
            case R.id.likeView:
                praiseZuoPin();
                break;
            case R.id.picView:
            case R.id.mainIV:
                xuanfuContainer.setVisibility(View.VISIBLE);
                title.setText("1/" + imageUrls.size());
                mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        title.setText((position + 1) + "/" + imageUrls.size());
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {

                    }
                });
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

                    ActivityAuthorHomePage.open(this, userId, userAvatar, userName,introdece);
                }
                break;
            case R.id.shareView:
                showShare();
                break;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("某人艺术");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.wandoujia.com/apps/com.xy.shuhua");//豌豆荚的下载地址
        // text是分享文本，所有平台都需要这个字段
        oks.setText("某人艺术，专注书画作家的平台");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //下面这句话必须加上，不然微信分享就是一句话，不是一个连接
        oks.setImageUrl("http://img.wdjimg.com/mms/icon/v1/e/b3/1c0177a447f3db4d13ee8b3fa2351b3e_256_256.png");//豌豆荚上面的app图标
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.wandoujia.com/apps/com.xy.shuhua");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.wandoujia.com/apps/com.xy.shuhua");

        // 启动分享GUI
        oks.show(this);
    }

    private void tryToChat() {
        if (artGoodsInfoModel != null && artGoodsInfoModel.user != null) {
            String userId = artGoodsInfoModel.user.userid;
            if (TextUtils.isEmpty(userId)) {
                ToastUtil.makeShortText("作家不存在");
                return;
            }
            if (TextUtils.isEmpty(userName)) {
                RongIM.getInstance().startPrivateChat(this, userId, userId);
            } else {
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

    @Override
    public void onBackPressed() {
        if (xuanfuContainer.getVisibility() == View.VISIBLE) {
            xuanfuContainer.setVisibility(View.INVISIBLE);
            return;
        }
        super.onBackPressed();
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
                            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(CustomApplication.getInstance());
                            Intent intent = new Intent(HomeFragment.kRefresh);
                            manager.sendBroadcast(intent);
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
                        Log.d("xiaoyu", e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        UserModel userModel = GsonUtil.transModel(response, UserModel.class);
                        if (userModel != null) {
                            userName = userModel.nickname;
                            userAvatar = userModel.imagepath;
                        }
                    }
                });
    }

    class UserModel {
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
