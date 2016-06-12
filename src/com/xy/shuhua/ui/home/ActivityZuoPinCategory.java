package com.xy.shuhua.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;

/**
 * Created by xiaoyu on 2016/6/12.
 */
public class ActivityZuoPinCategory extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private View rightView;
    private CategoryCheckView dangdaiView;
    private CategoryCheckView shuHuaView;
    private CategoryCheckView shufaView;
    private CategoryCheckView huihuaView;
    private CategoryCheckView youhuaView;
    private CategoryCheckView ertonghuaView;

    public static final String kContent = "content";
    private String content;

    public static void openForResult(Activity activity,String content,int requestCode){
        Intent intent = new Intent(activity,ActivityZuoPinCategory.class);
        intent.putExtra(kContent,content);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        content = getIntent().getStringExtra(kContent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zuopin_category);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        rightView = findViewById(R.id.rightView);
        dangdaiView = (CategoryCheckView) findViewById(R.id.dangdaiView);
        shuHuaView = (CategoryCheckView) findViewById(R.id.shuHuaView);
        shufaView = (CategoryCheckView) findViewById(R.id.shufaView);
        huihuaView = (CategoryCheckView) findViewById(R.id.huihuaView);
        youhuaView = (CategoryCheckView) findViewById(R.id.youhuaView);
        ertonghuaView = (CategoryCheckView) findViewById(R.id.ertonghuaView);
    }

    @Override
    protected void initViews() {
        dangdaiView.name.setText("µ±´ú");
        shuHuaView.name.setText("Êé»­");
        shufaView.name.setText("Êé·¨");
        huihuaView.name.setText("»æ»­");
        youhuaView.name.setText("ÓÍ»­");
        ertonghuaView.name.setText("¶ùÍ¯»­");

        if("µ±´ú".equals(content)){
            dangdaiView.setCheck(true);
        }else if("Êé»­".equals(content)){
            shuHuaView.setCheck(true);
        }else if("Êé·¨".equals(content)){
            shufaView.setCheck(true);
        }else if("»æ»­".equals(content)){
            huihuaView.setCheck(true);
        }else if("ÓÍ»­".equals(content)){
            youhuaView.setCheck(true);
        }else if("¶ùÍ¯»­".equals(content)){
            ertonghuaView.setCheck(true);
        }
    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
        rightView.setOnClickListener(this);
        dangdaiView.setOnClickListener(this);
        shuHuaView.setOnClickListener(this);
        shufaView.setOnClickListener(this);
        huihuaView.setOnClickListener(this);
        youhuaView.setOnClickListener(this);
        ertonghuaView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backView:
                finish();
                break;
            case R.id.rightView:
                toComplete();
                break;
            case R.id.dangdaiView:
                dangdaiView.setCheck(true);
                shuHuaView.setCheck(false);
                shufaView.setCheck(false);
                huihuaView.setCheck(false);
                youhuaView.setCheck(false);
                ertonghuaView.setCheck(false);
                break;
            case R.id.shuHuaView:
                dangdaiView.setCheck(false);
                shuHuaView.setCheck(true);
                shufaView.setCheck(false);
                huihuaView.setCheck(false);
                youhuaView.setCheck(false);
                ertonghuaView.setCheck(false);
                break;
            case R.id.shufaView:
                dangdaiView.setCheck(false);
                shuHuaView.setCheck(false);
                shufaView.setCheck(true);
                huihuaView.setCheck(false);
                youhuaView.setCheck(false);
                ertonghuaView.setCheck(false);
                break;
            case R.id.huihuaView:
                dangdaiView.setCheck(false);
                shuHuaView.setCheck(false);
                shufaView.setCheck(false);
                huihuaView.setCheck(true);
                youhuaView.setCheck(false);
                ertonghuaView.setCheck(false);
                break;
            case R.id.youhuaView:
                dangdaiView.setCheck(false);
                shuHuaView.setCheck(false);
                shufaView.setCheck(false);
                huihuaView.setCheck(false);
                youhuaView.setCheck(true);
                ertonghuaView.setCheck(false);
                break;
            case R.id.ertonghuaView:
                dangdaiView.setCheck(false);
                shuHuaView.setCheck(false);
                shufaView.setCheck(false);
                huihuaView.setCheck(false);
                youhuaView.setCheck(false);
                ertonghuaView.setCheck(true);
                break;
        }
    }

    private void toComplete(){
        String categoryStr = "";
        if(dangdaiView.isChecked()){
            categoryStr = "µ±´ú";
        }
        if(shuHuaView.isChecked()){
            categoryStr = "Êé»­";
        }
        if(shufaView.isChecked()){
            categoryStr = "Êé·¨";
        }
        if(huihuaView.isChecked()){
            categoryStr = "»æ»­";
        }
        if(youhuaView.isChecked()){
            categoryStr = "ÓÍ»­";
        }
        if(ertonghuaView.isChecked()){
            categoryStr = "¶ùÍ¯»­";
        }
        Intent intent = new Intent();
        intent.putExtra(kContent,categoryStr);
        setResult(RESULT_OK,intent);
        finish();
    }
}
