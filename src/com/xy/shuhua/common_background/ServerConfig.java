package com.xy.shuhua.common_background;

/**
 * Created by xiaoyu on 2016/3/19.
 */
public class ServerConfig {
    // ���Ի���
    public static String BASE_URL_TEST = "http://182.92.227.113/";
    // ��ʽ����
    public static String BASE_URL_OFFICAL = "http://182.92.227.113/";

    public static String BASE_URL = BASE_URL_OFFICAL;

    public static void initUrl(boolean boo) {
        if (boo) {
            ServerConfig.BASE_URL = ServerConfig.BASE_URL_TEST;
        } else {
            ServerConfig.BASE_URL = ServerConfig.BASE_URL_OFFICAL;
        }
    }

    public static final String URL_GET_VER_CODE = "meifor/api/accounts/register.do";
    public static final String URL_REGISTER = "meifor/api/accounts/activate.do";
    public static final String URL_LOGIN = "meifor/api/accounts/login.do";
    public static final String URL_HOME_INFO = "meifor/api/product/queryType.do";
}
