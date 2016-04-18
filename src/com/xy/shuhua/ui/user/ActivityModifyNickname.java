package com.xy.shuhua.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.Account;
import com.xy.shuhua.ui.CustomApplication;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;

/**
 * Created by xiaoyu on 2015/7/11.
 */
public class ActivityModifyNickname extends ActivityBaseNoSliding implements View.OnClickListener {
    private View  backView;
    private EditText nickName;
    private View rightView;

    private Account account;

    public static void open(Activity activity,int requestCode){
        Intent intent = new Intent(activity,ActivityModifyNickname.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        account = CustomApplication.getInstance().getAccount();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nickname);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        rightView = findViewById(R.id.rightView);
        nickName = (EditText) findViewById(R.id.nickName);
    }

    @Override
    protected void initViews() {
        if (!TextUtils.isEmpty(account.userName)) {
            nickName.setText(account.userName);
            nickName.setSelection(account.userName.length());
        }
    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
        rightView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backView) {
            finish();
        } else if (view.getId() == R.id.rightView) {
            tryToCommit();
        }
    }

    private void tryToCommit() {
//        final String nickNameStr = nickName.getText().toString();
//        if (TextUtils.isEmpty(nickNameStr)) {
//            ToastUtil.makeShortText(getString(R.string.please_input_nickname));
//            return;
//        }
//        showDialog();
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("nickname", nickNameStr));
//        nameValuePairs.add(new BasicNameValuePair("userid", account.userId));
//        Request.doRequest(this, nameValuePairs, ServerConfig.URL_MODIFY_NICKNAME, Request.POST, new Request.RequestListener() {
//            @Override
//            public void onException(Request.RequestException e) {
//                dismissDialog();
//                ToastUtil.showErrorMessage(e, "");
//            }
//
//            @Override
//            public void onComplete(String response) {
//                dismissDialog();
//                CommonModel commonModel = HCApplicaton.getInstance().getGson().fromJsonWithNoException(response, CommonModel.class);
//                if (commonModel != null && commonModel.result == 1) {
//                    account.userName = nickNameStr;
//                    account.saveMeInfoToPreference();
//                    ToastUtil.makeShortText(getString(R.string.modifySuccessful));
//                    ActivityModifyNickname.this.setResult(RESULT_OK);
//                    finish();
//                } else {
//                    ToastUtil.makeShortText(getString(R.string.modifyPwdField));
//                }
//            }
//        });
    }
}
