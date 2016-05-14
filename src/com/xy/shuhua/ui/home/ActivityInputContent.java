package com.xy.shuhua.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.util.CommonUtil;
import com.xy.shuhua.util.ToastUtil;

/**
 * Created by xiaoyu on 2016/5/14.
 */
public class ActivityInputContent extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private TextView title;
    private View rightView;
    private EditText contentEditText;

    public static final String ktitle = "key_title";
    public static final String kcontent = "key_content";

    private String titleStr;
    private String contentStr;

    public static void openForResult(Activity activity, int requestCode, String titleStr, String contentStr) {
        Intent intent = new Intent(activity, ActivityInputContent.class);
        intent.putExtra(ktitle, titleStr);
        intent.putExtra(kcontent, contentStr);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        titleStr = getIntent().getStringExtra(ktitle);
        contentStr = getIntent().getStringExtra(kcontent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_content);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        title = (TextView) findViewById(R.id.title);
        rightView = findViewById(R.id.rightView);
        contentEditText = (EditText) findViewById(R.id.contentEditText);
    }

    @Override
    protected void initViews() {
        if (!TextUtils.isEmpty(titleStr)) {
            title.setText(titleStr);
        }
        if (!TextUtils.isEmpty(contentStr)) {
            contentEditText.setText(contentStr);
            contentEditText.setSelection(contentStr.length());
        }
    }

    @Override
    protected void setListeners() {
        rightView.setOnClickListener(this);
        backView.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        CommonUtil.hideSoftWindow(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backView:
                finish();
                break;
            case R.id.rightView:
                setResultFinish();
                break;
        }
    }

    private void setResultFinish() {
        String contentStr = contentEditText.getText().toString();
        if (TextUtils.isEmpty(contentStr)) {
            ToastUtil.makeShortText("«Î ‰»Îƒ⁄»›");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(kcontent, contentStr);
        setResult(RESULT_OK, intent);
        finish();
    }
}
