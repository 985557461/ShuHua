package com.xy.shuhua.ui.art.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 2016/4/4.
 */
public class HomeArtListModel {
    @SerializedName("result")
    public String result;
    @SerializedName("message")
    public String message;
    @SerializedName("list")
    public List<HomeArtItemModel> list;
}
