package com.xy.shuhua.ui.art.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 2016/6/14.
 */
public class AuthorModelWithZuoPin {
    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public String result;
    @SerializedName("list")
    public List<Data> list;

    public static class Data{
        @SerializedName("introduce")
        public String introduce;
        @SerializedName("realname")
        public String realname;
        @SerializedName("userid")
        public String userid;
        @SerializedName("imageurl")
        public String imageurl;
        @SerializedName("artlist")
        public List<ArtGoodsItemModel> artlist;
    }
}
