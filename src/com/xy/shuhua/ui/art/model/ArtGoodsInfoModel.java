package com.xy.shuhua.ui.art.model;

import com.google.gson.annotations.SerializedName;
import com.xy.shuhua.ui.user.UserInfoModel;

import java.util.List;

/**
 * Created by xiaoyu on 2016/5/14.
 */
public class ArtGoodsInfoModel {
    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public String result;
    @SerializedName("art")
    public Art art;
    @SerializedName("user")
    public UserInfoModel user;
    @SerializedName("lists")
    public List<ArtGoodsItemModel> lists;

    public class Art{
        @SerializedName("artsize")
        public String artsize;//尺寸
        @SerializedName("praisenum")
        public String praisenum;//赞
        @SerializedName("userid")
        public String userid;
        @SerializedName("type")
        public String type;
        @SerializedName("id")
        public String id;
        @SerializedName("category")
        public String category;//类别
        @SerializedName("username")
        public String username;
        @SerializedName("price")
        public String price;//价格
        @SerializedName("caizhi")
        public String caizhi;//材质
        @SerializedName("name")
        public String name;//作品名称
        @SerializedName("artnum")
        public String artnum;//作品数量（后台没有返回）
        @SerializedName("newdate")
        public String newdate;
        @SerializedName("imageurl")
        public String imageurl;
        @SerializedName("imageurl1")
        public String imageurl1;
        @SerializedName("imageurl2")
        public String imageurl2;
        @SerializedName("imageurl3")
        public String imageurl3;
        @SerializedName("imageurl4")
        public String imageurl4;
        @SerializedName("imageurl5")
        public String imageurl5;
    }
}
