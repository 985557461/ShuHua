package com.xy.shuhua.common_background;

/**
 * Created by xiaoyu on 2016/3/19.
 */
public class ServerConfig {
    // 测试环境
    public static String BASE_URL_TEST = "http://101.201.72.28:8080/";
    // 正式环境
    public static String BASE_URL_OFFICAL = "http://101.201.72.28:8080/";

    public static String BASE_URL = BASE_URL_OFFICAL;

    public static void initUrl(boolean boo) {
        if (boo) {
            ServerConfig.BASE_URL = ServerConfig.BASE_URL_TEST;
        } else {
            ServerConfig.BASE_URL = ServerConfig.BASE_URL_OFFICAL;
        }
    }

    public static final String URL_GET_VER_CODE = "shuhua/api/accounts/register.do";
    public static final String URL_REGISTER = "shuhua/api/accounts/activate.do";
    public static final String URL_LOGIN = "shuhua/api/accounts/login.do";
    public static final String QUERY_ARTS = "shuhua/api/artService/queryArts.do";
    public static final String HOME_QUERY_ART = "shuhua/api/artService/queryArt.do";
    public static final String GET_CHAT_TOKEN = "shuhua/api/accounts/getRmToken.do";
    public static final String SEARCH_ARTS = "shuhua/api/artService/queryArtsByName.do";
    public static final String MODIFY_PWD = "shuhua/api/accounts/change_password.do";
    public static final String UPLOAD_FILE = "shuhua/api/export/upload.do";//POST  参数file
}
