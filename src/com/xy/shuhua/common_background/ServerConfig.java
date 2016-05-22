package com.xy.shuhua.common_background;

/**
 * Created by xiaoyu on 2016/3/19.
 */
public class ServerConfig {
    // ���Ի���
    public static String BASE_URL_TEST = "http://182.92.67.140:80/";
    // ��ʽ����
    public static String BASE_URL_OFFICAL = "http://182.92.67.140:80/";

    public static String BASE_URL = BASE_URL_OFFICAL;

    public static void initUrl(boolean boo) {
        if (boo) {
            ServerConfig.BASE_URL = ServerConfig.BASE_URL_TEST;
        } else {
            ServerConfig.BASE_URL = ServerConfig.BASE_URL_OFFICAL;
        }
    }

    public static final String URL_GET_VER_CODE = "shuhua/api/accounts/register.do";//�õ���֤��
    public static final String URL_REGISTER = "shuhua/api/accounts/activate.do";//ע��
    public static final String URL_LOGIN = "shuhua/api/accounts/login.do";//��¼
    public static final String QUERY_ARTS = "shuhua/api/artService/queryArts.do";
    public static final String HOME_QUERY_ART = "shuhua/api/artService/queryArt.do";//��ҳ�����ݽӿ�
    public static final String GET_CHAT_TOKEN = "shuhua/api/accounts/getRmToken.do";//�õ�����token�Ľӿ�
    public static final String SEARCH_ARTS = "shuhua/api/artService/queryArtsByName.do";//������Ʒ�ӿ�
    public static final String MODIFY_PWD = "shuhua/api/accounts/change_password.do";//�޸�����
    public static final String UPLOAD_FILE = "shuhua/api/export/upload.do";//POST�ϴ��ļ�  ����file
    public static final String MY_ZUOPIN = "shuhua/api/artService/getArtByUserid.do";//�����û�id��ѯ��Ʒ�б�
    public static final String MY_WENZHANG = "shuhua/api/activity/queryActivitys.do";//�����û�id��ѯ�ҵ����»���չ��
    public static final String ZUOPIN_INFO = "shuhua/api/artService/getArtDeatil.do";//��Ʒ��ϸ��Ϣ
    public static final String PUBLISH_ZUOPIN = "shuhua/api/art/saveArt.do";//������Ʒ�Ľӿ�
    public static final String ALL_AUTHOR = "shuhua/api/accounts/getArtist.do";//���������ҽӿ�
    public static final String GET_MOUREN = "shuhua/api/artService/getMouren.do";//��ҳ��ĳ�������ӿ�
    public static final String MODIFY_AVATAR = "shuhua/api/accounts/setImage2.do";//�޸�ͷ��
    public static final String MODIFY_OTHER_INFO = "shuhua/api/accounts/setUserinfo.do";//�޸��û�������Ϣ
    public static final String FIND_PWD = "shuhua/api/accounts/zhaohui.do";//�һ�����
    public static final String PRAISE_ZUOPIN = "shuhua/api/artService/parise.do";//����Ʒ
    public static final String IDENTIFY_INFO = "shuhua/api/accounts/authentication.do";//�����֤
    public static final String GET_ART_BY_TYPE  = "shuhua/api/artService/getArtByType.do";//��ҳĳ���������棬�������������Ʒ�������黭��Ʒ��,"��ͯ�黭��Ʒ"������Э��
}
