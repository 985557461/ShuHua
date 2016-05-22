package com.xy.shuhua.common_background;

/**
 * Created by xiaoyu on 2016/3/19.
 */
public class ServerConfig {
    // 测试环境
    public static String BASE_URL_TEST = "http://182.92.67.140:80/";
    // 正式环境
    public static String BASE_URL_OFFICAL = "http://182.92.67.140:80/";

    public static String BASE_URL = BASE_URL_OFFICAL;

    public static void initUrl(boolean boo) {
        if (boo) {
            ServerConfig.BASE_URL = ServerConfig.BASE_URL_TEST;
        } else {
            ServerConfig.BASE_URL = ServerConfig.BASE_URL_OFFICAL;
        }
    }

    public static final String URL_GET_VER_CODE = "shuhua/api/accounts/register.do";//得到验证码
    public static final String URL_REGISTER = "shuhua/api/accounts/activate.do";//注册
    public static final String URL_LOGIN = "shuhua/api/accounts/login.do";//登录
    public static final String QUERY_ARTS = "shuhua/api/artService/queryArts.do";
    public static final String HOME_QUERY_ART = "shuhua/api/artService/queryArt.do";//首页的数据接口
    public static final String GET_CHAT_TOKEN = "shuhua/api/accounts/getRmToken.do";//得到聊天token的接口
    public static final String SEARCH_ARTS = "shuhua/api/artService/queryArtsByName.do";//搜索作品接口
    public static final String MODIFY_PWD = "shuhua/api/accounts/change_password.do";//修改密码
    public static final String UPLOAD_FILE = "shuhua/api/export/upload.do";//POST上传文件  参数file
    public static final String MY_ZUOPIN = "shuhua/api/artService/getArtByUserid.do";//根据用户id查询作品列表
    public static final String MY_WENZHANG = "shuhua/api/activity/queryActivitys.do";//根据用户id查询我的文章或者展览
    public static final String ZUOPIN_INFO = "shuhua/api/artService/getArtDeatil.do";//作品详细信息
    public static final String PUBLISH_ZUOPIN = "shuhua/api/art/saveArt.do";//发布作品的接口
    public static final String ALL_AUTHOR = "shuhua/api/accounts/getArtist.do";//所有艺术家接口
    public static final String GET_MOUREN = "shuhua/api/artService/getMouren.do";//主页面某人艺术接口
    public static final String MODIFY_AVATAR = "shuhua/api/accounts/setImage2.do";//修改头像
    public static final String MODIFY_OTHER_INFO = "shuhua/api/accounts/setUserinfo.do";//修改用户其他信息
    public static final String FIND_PWD = "shuhua/api/accounts/zhaohui.do";//找回密码
    public static final String PRAISE_ZUOPIN = "shuhua/api/artService/parise.do";//赞作品
    public static final String IDENTIFY_INFO = "shuhua/api/accounts/authentication.do";//身份认证
    public static final String GET_ART_BY_TYPE  = "shuhua/api/artService/getArtByType.do";//主页某人艺术界面，点击“当代艺术品”，“书画作品”,"儿童书画作品"搜索的协议
}
