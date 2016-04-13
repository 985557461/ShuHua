package com.xy.shuhua.ui.chat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class MyActivity extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (RongIM.getInstance() != null)
//                    RongIM.getInstance().startPrivateChat(MyActivity.this, "12346", "xiaoyutwo");
//            }
//        });
//
//        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (RongIM.getInstance() != null)
//                    RongIM.getInstance().startConversationList(MyActivity.this);
//            }
//        });
//
//        connect("+JsuMDMueZ2L6VNQBin3Po+1uW/cu1Mwg/eS9JxW7kQafpL//OzQNllIY+4x6etoH4kKLxCdznv7/PoZUNPgxQ==");
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
//    private void connect(String token) {
//        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {
//            /**
//             * IMKit SDK调用第二步,建立与服务器的连接
//             */
//            RongIM.connect(token, new RongIMClient.ConnectCallback() {
//                /**
//                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
//                 */
//                @Override
//                public void onTokenIncorrect() {
//                    Log.d("xiaoyu", "--onTokenIncorrect");
//                }
//
//                /**
//                 * 连接融云成功
//                 * @param userid 当前 token
//                 */
//                @Override
//                public void onSuccess(String userid) {
//                    Log.d("xiaoyu", "--onSuccess" + userid);
//                }
//
//                /**
//                 * 连接融云失败
//                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
//                 */
//                @Override
//                public void onError(RongIMClient.ErrorCode errorCode) {
//                    Log.d("xiaoyu", "--onError" + errorCode);
//                }
//            });
//        }
//    }

}
