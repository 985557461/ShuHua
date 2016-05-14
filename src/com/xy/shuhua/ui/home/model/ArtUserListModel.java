package com.xy.shuhua.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 2016/4/4.
 */
public class ArtUserListModel {
    @SerializedName("result")
    public String result;
    @SerializedName("message")
    public String message;
    @SerializedName("list")
    public List<ArtUserModel> list;
}
