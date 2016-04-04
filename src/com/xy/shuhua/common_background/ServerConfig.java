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

    public static final String QUERY_ARTS = "shuhua/api/artService/queryArts.do";
    public static final String HOME_QUERY_ART = "shuhua/api/artService/queryArt.do";
}
