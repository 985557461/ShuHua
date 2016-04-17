package com.xy.shuhua.ui;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import com.xy.shuhua.common_background.Account;
import io.rong.imkit.RongIM;

/**
 * Created by xiaoyu on 2016/3/19.
 */
public class CustomApplication extends Application {
    private static CustomApplication instance;
    private Account account;

    public static boolean android_show_filter = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        /**
         * OnCreate �ᱻ����������룬��α������룬ȷ��ֻ������Ҫʹ�� RongIM �Ľ��̺� Push ����ִ���� init��
         * io.rong.push Ϊ���� push �������ƣ������޸ġ�
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK���õ�һ�� ��ʼ��
             */
            RongIM.init(this);
        }
    }

    /**
     * ��õ�ǰ���̵�����
     *
     * @param context
     * @return ���̺�
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static CustomApplication getInstance() {
        return instance;
    }

    public Account getAccount() {
        if (account == null) {
            account = Account.loadAccount();
        }
        return account;
    }
}
