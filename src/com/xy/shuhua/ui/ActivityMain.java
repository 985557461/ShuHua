package com.xy.shuhua.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.art.ArtFragment;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.ui.home.HomeFragment;
import com.xy.shuhua.ui.message.MessageFragment;
import com.xy.shuhua.ui.mine.MineFragment;
import com.xy.shuhua.util.ActivityManagerUtil;
import com.xy.shuhua.util.ToastUtil;

public class ActivityMain extends ActivityBaseNoSliding implements View.OnClickListener {
    /**
     * fragments*
     */
    private Fragment mFragmentCurrent;

    private HomeFragment homeFragment;
    private MessageFragment messageFragment;
    private ArtFragment arttFragment;
    private MineFragment mineFragment;

    /**
     * views*
     */
    private View homeLL;
    private ImageView homeImageView;
    private TextView homeTextView;
    private View messageLL;
    private ImageView messageImageView;
    private TextView messageTextView;
    private View artLL;
    private ImageView artImageView;
    private TextView artTextView;
    private View myCenterLL;
    private ImageView myCenterImageView;
    private TextView myCenterTextView;

    /**
     * back finish*
     */
    private long lastTime;
    private static final long TIME_LONG = 3000000;

    public static void open(Activity activity){
        Intent intent = new Intent(activity,ActivityMain.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void getViews() {
        homeLL = findViewById(R.id.homeLL);
        homeImageView = (ImageView) findViewById(R.id.homeImageView);
        homeTextView = (TextView) findViewById(R.id.homeTextView);

        messageLL = findViewById(R.id.messageLL);
        messageImageView = (ImageView) findViewById(R.id.messageImageView);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        artLL = findViewById(R.id.artLL);
        artImageView = (ImageView) findViewById(R.id.artImageView);
        artTextView = (TextView) findViewById(R.id.artTextView);

        myCenterLL = findViewById(R.id.myCenterLL);
        myCenterImageView = (ImageView) findViewById(R.id.myCenterImageView);
        myCenterTextView = (TextView) findViewById(R.id.myCenterTextView);
    }

    @Override
    protected void initViews() {
        homeTextView.setSelected(true);
        homeImageView.setImageResource(R.drawable.icon_home_pressed);
        setDefaultFragment();
    }

    @Override
    protected void setListeners() {
        homeLL.setOnClickListener(this);
        messageLL.setOnClickListener(this);
        artLL.setOnClickListener(this);
        myCenterLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeLL:
                changeTab(0);
                break;
            case R.id.messageLL:
                changeTab(1);
                break;
            case R.id.artLL:
                changeTab(2);
                break;
            case R.id.myCenterLL:
                changeTab(3);
                break;
        }
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        homeFragment = new HomeFragment();
        transaction.add(R.id.fragementLayout, homeFragment);
        transaction.commit();
        mFragmentCurrent = homeFragment;
    }

    private void changeTab(int index) {
        switch (index) {
            case 0: {
                homeImageView.setImageResource(R.drawable.icon_home_pressed);
                messageImageView.setImageResource(R.drawable.icon_message_normal);
                artImageView.setImageResource(R.drawable.icon_art_normal);
                myCenterImageView.setImageResource(R.drawable.icon_mine_normal);

                homeTextView.setSelected(true);
                messageTextView.setSelected(false);
                artTextView.setSelected(false);
                myCenterTextView.setSelected(false);

                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                switchContent(mFragmentCurrent, homeFragment);
            }
            break;
            case 1: {
                homeImageView.setImageResource(R.drawable.icon_home_normal);
                messageImageView.setImageResource(R.drawable.icon_message_normal);
                artImageView.setImageResource(R.drawable.icon_art_normal);
                myCenterImageView.setImageResource(R.drawable.icon_mine_normal);

                homeTextView.setSelected(false);
                messageTextView.setSelected(true);
                artTextView.setSelected(false);
                myCenterTextView.setSelected(false);

                if(messageFragment == null){
                    messageFragment = new MessageFragment();
                }
                switchContent(mFragmentCurrent, messageFragment);
            }
            break;
            case 2: {
                homeImageView.setImageResource(R.drawable.icon_home_normal);
                messageImageView.setImageResource(R.drawable.icon_message_normal);
                artImageView.setImageResource(R.drawable.icon_art_pressed);
                myCenterImageView.setImageResource(R.drawable.icon_mine_normal);

                homeTextView.setSelected(false);
                messageTextView.setSelected(false);
                artTextView.setSelected(true);
                myCenterTextView.setSelected(false);

                if (arttFragment == null) {
                    arttFragment = new ArtFragment();
                }
                switchContent(mFragmentCurrent, arttFragment);
            }
            break;
            case 3: {
                homeImageView.setImageResource(R.drawable.icon_home_normal);
                messageImageView.setImageResource(R.drawable.icon_message_normal);
                artImageView.setImageResource(R.drawable.icon_art_normal);
                myCenterImageView.setImageResource(R.drawable.icon_mine_pressed);

                homeTextView.setSelected(false);
                messageTextView.setSelected(false);
                artTextView.setSelected(false);
                myCenterTextView.setSelected(true);

                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                switchContent(mFragmentCurrent, mineFragment);
            }
            break;
        }
    }

    public void switchContent(Fragment from, Fragment to) {
        if (from != to) {
            mFragmentCurrent = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // to.getLoaderManager().hasRunningLoaders();
            // 先判断是否被add过
            if (!to.isAdded()) {
                if (from != null && from.isAdded()) {
                    transaction.hide(from);
                }
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(R.id.fragementLayout, to).commitAllowingStateLoss();
            } else {
                // 隐藏当前的fragment，显示下一个
                transaction.hide(from).show(to).commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - lastTime < TIME_LONG) {
            ActivityManagerUtil.getInstance().killActivity();
        } else {
            ToastUtil.makeShortText("再按一次退出应用");
            lastTime = t;
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        ActivityManagerUtil.getInstance().killActivity();
        super.onDestroy();
    }
}
