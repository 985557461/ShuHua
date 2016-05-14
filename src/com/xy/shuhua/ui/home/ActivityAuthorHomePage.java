package com.xy.shuhua.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.ui.mine.ActivityAllZuoPin;
import com.xy.shuhua.ui.mine.WenZhangRecyclerView;
import com.xy.shuhua.ui.mine.ZuoPinRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 2016/5/14.
 */
//其他艺术家主页
public class ActivityAuthorHomePage extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private ImageView avatar;
    private TextView name;
    private View allZuoPin;
    private View zuoPinLL;
    private View zuoPinLineView;
    private View zhanLanLL;
    private View zhanLanLineView;
    private View wenZhangLL;
    private View wenZhangLineView;
    private ViewPager viewPager;

    private List<View> views = new ArrayList<>();
    private MinePagerAdapter minePagerAdapter;

    public static final String kUserid = "key_user_id";
    public static final String kUserAvatar = "key_user_avatar";
    public static final String kUserName = "key_user_name";
    private String userid;
    private String useravatar;
    private String username;

    public static void open(Activity activity, String userid, String useravatar, String username) {
        Intent intent = new Intent(activity, ActivityAuthorHomePage.class);
        intent.putExtra(kUserid, userid);
        intent.putExtra(kUserAvatar, useravatar);
        intent.putExtra(kUserName, username);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userid = getIntent().getStringExtra(kUserid);
        useravatar = getIntent().getStringExtra(kUserAvatar);
        username = getIntent().getStringExtra(kUserName);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_home_page);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        avatar = (ImageView) findViewById(R.id.avatar);
        name = (TextView) findViewById(R.id.name);
        allZuoPin = findViewById(R.id.allZuoPin);
        zuoPinLL = findViewById(R.id.zuoPinLL);
        zuoPinLineView = findViewById(R.id.zuoPinLineView);
        zhanLanLL = findViewById(R.id.zhanLanLL);
        zhanLanLineView = findViewById(R.id.zhanLanLineView);
        wenZhangLL = findViewById(R.id.wenZhangLL);
        wenZhangLineView = findViewById(R.id.wenZhangLineView);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        backView.setOnClickListener(this);
        allZuoPin.setOnClickListener(this);
        zuoPinLL.setOnClickListener(this);
        zhanLanLL.setOnClickListener(this);
        wenZhangLL.setOnClickListener(this);


        ZuoPinRecyclerView itemView1 = new ZuoPinRecyclerView(this, userid);
        views.add(itemView1);
        WenZhangRecyclerView itemView2 = new WenZhangRecyclerView(this, 0, userid);
        views.add(itemView2);
        WenZhangRecyclerView itemView3 = new WenZhangRecyclerView(this, 1, userid);
        views.add(itemView3);

        minePagerAdapter = new MinePagerAdapter();
        viewPager.setAdapter(minePagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                changedSelectedState(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        initUserData();
    }

    private void initUserData() {
        /**初始化数据**/
        if (!TextUtils.isEmpty(useravatar)) {
            Glide.with(this).load(useravatar).error(R.drawable.me_avatar_boy).into(avatar);
        } else {
            avatar.setImageResource(R.drawable.me_avatar_boy);
        }

        if (!TextUtils.isEmpty(username)) {
            name.setText(username);
        } else {
            name.setText("去设置");
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {

    }

    private void changedSelectedState(int index) {
        switch (index) {
            case 0:
                zuoPinLineView.setVisibility(View.VISIBLE);
                zhanLanLineView.setVisibility(View.GONE);
                wenZhangLineView.setVisibility(View.GONE);
                break;
            case 1:
                zuoPinLineView.setVisibility(View.GONE);
                zhanLanLineView.setVisibility(View.VISIBLE);
                wenZhangLineView.setVisibility(View.GONE);
                break;
            case 2:
                zuoPinLineView.setVisibility(View.GONE);
                zhanLanLineView.setVisibility(View.GONE);
                wenZhangLineView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backView:
                finish();
                break;
            case R.id.allZuoPin:
                ActivityAllZuoPin.open(this,userid);
                break;
            case R.id.zuoPinLL:
                changedSelectedState(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.zhanLanLL:
                changedSelectedState(1);
                viewPager.setCurrentItem(1);
                break;
            case R.id.wenZhangLL:
                changedSelectedState(2);
                viewPager.setCurrentItem(2);
                break;
        }
    }

    private class MinePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
