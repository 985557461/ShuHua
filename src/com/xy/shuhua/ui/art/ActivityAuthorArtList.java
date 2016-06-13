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
 * Created by xiaoyu on 2016/4/2.
 */
public class ActivityAuthorArtList extends ActivityBaseNoSliding{
    private View backView;
    private TabPageIndicator indicator;
    private ViewPager viewPager;
    private ArtPagerAdapter artPagerAdapter;
    private List<AuthorArtView> views = new ArrayList<>();

    private String[] titles = new String[]{"推荐","最热","最新"};
    public static final String kCategory = "category";//当代-1；书画-2；儿童画-3
    private String category;

    public static void open(Activity activity,String category){
        Intent intent = new Intent(activity,ActivityAuthorArtList.class);
        intent.putExtra(kCategory,category);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        category = getIntent().getStringExtra(kCategory);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_art_list);
    }

    @Override
    protected void getViews() {
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        backView = findViewById(R.id.backView);
    }

    @Override
    protected void initViews() {
        for(int i=0;i<titles.length;i++){
            views.add(new AuthorArtView(this,i,category));
        }

        artPagerAdapter = new ArtPagerAdapter();
        viewPager.setAdapter(artPagerAdapter);
        indicator.setViewPager(viewPager);
    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class ArtPagerAdapter extends PagerAdapter {

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
