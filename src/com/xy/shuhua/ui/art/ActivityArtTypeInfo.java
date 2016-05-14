package com.xy.shuhua.ui.art;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.util.viewpager_indicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 2016/4/1.
 */
public class ActivityArtTypeInfo extends ActivityBaseNoSliding implements View.OnClickListener{
    private View backView;
    private TabPageIndicator indicator;
    private ViewPager viewPager;
    private ArtPagerAdapter artPagerAdapter;
    private List<ArtTypeView> views = new ArrayList<>();

    private String[] titles = new String[]{"µ±´ú","Êé»­"};

    public static void open(Activity activity){
        Intent intent = new Intent(activity,ActivityArtTypeInfo.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_type_info);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    @Override
    protected void initViews() {
        for(int i=0;i<titles.length;i++){
            views.add(new ArtTypeView(this));
        }

        artPagerAdapter = new ArtPagerAdapter();
        viewPager.setAdapter(artPagerAdapter);
        indicator.setViewPager(viewPager);
    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.backView){
            finish();
        }
    }

    private class ArtPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
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
