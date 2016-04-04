package com.xy.shuhua.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 2016/4/4.
 */
public class HomeInfoModel {
    @SerializedName("result")
    public String result;
    @SerializedName("message")
    public String message;
    @SerializedName("userlist")
    public List<ArtUserModel> userlist;
    @SerializedName("artlist")
    public List<HomeArtGoodsModel> artlist;
    @SerializedName("banner_posts")
    public List<BannerModel> banner_posts;
}
