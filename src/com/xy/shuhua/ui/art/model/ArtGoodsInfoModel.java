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
        public String artsize;//�ߴ�
        @SerializedName("praisenum")
        public String praisenum;//��
        @SerializedName("userid")
        public String userid;
        @SerializedName("type")
        public String type;
        @SerializedName("id")
        public String id;
        @SerializedName("category")
        public String category;//���
        @SerializedName("username")
        public String username;
        @SerializedName("price")
        public String price;//�۸�
        @SerializedName("caizhi")
        public String caizhi;//����
        @SerializedName("name")
        public String name;//��Ʒ����
        @SerializedName("artnum")
        public String artnum;//��Ʒ��������̨û�з��أ�
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
