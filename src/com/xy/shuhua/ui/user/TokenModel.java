package com.xy.shuhua.ui.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoyu on 2016/4/14.
 */
public class TokenModel {
    @SerializedName("code")
    public String code;
    @SerializedName("userId")
    public String userId;
    @SerializedName("token")
    public String token;
}
