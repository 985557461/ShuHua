package com.xy.shuhua.ui.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoyu on 2016/4/9.
 */
public class UserInfoModel {
    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public String result;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("user_id")
    public String user_id;
    @SerializedName("imageurl")
    public String imageurl;
}
