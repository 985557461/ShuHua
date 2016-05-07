package com.xy.shuhua.ui.art;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.ui.mine.ArtWenZhangListModel;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.PrintHttpUrlUtil;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import okhttp3.Call;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/4/5.
 */
public class ActivityArtGoodsInfo extends ActivityBaseNoSliding {
    private static final String kGoodsId = "goods_id";
    private String goodsId;

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
            ToastUtil.makeShortText("商品id为空");
            finish();
            return;
        }
        setContentView(R.layout.activity_art_goods_info);
    }

    @Override
    protected void getViews() {

    }

    @Override
    protected void initViews() {
        refreshData();
    }

    @Override
    protected void setListeners() {

    }

    private void refreshData() {
        Map<String, String> params = new HashMap<>();
        params.put("id", goodsId);
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.ZUOPIN_INFO, params);
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.ZUOPIN_INFO)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.makeShortText("网络连接失败了");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("xiaoyu",response);
                    }
                });
    }
}
