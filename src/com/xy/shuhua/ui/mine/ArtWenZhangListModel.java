package com.xy.shuhua.ui.mine;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 2016/4/4.
 */
public class ArtWenZhangListModel {
    @SerializedName("result")
    public String result;
    @SerializedName("message")
    public String message;
    @SerializedName("artlist")
    public List<ArtWenZhangItemModel> artlist;
}
