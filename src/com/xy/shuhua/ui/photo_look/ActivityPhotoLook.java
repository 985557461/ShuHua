package com.xy.shuhua.ui.photo_look;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.util.photo.HackyViewPager;
import com.xy.shuhua.util.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class ActivityPhotoLook extends ActivityBaseNoSliding implements View.OnClickListener{
    private View backView;
    private TextView title;
    private ViewPager mViewPager;

    public static final String key_urls_list = "key_urls_list";
    private List<String> urls;

    public static void open(Activity activity, ArrayList<String> urls) {
        Intent intent = new Intent(activity, ActivityPhotoLook.class);
        intent.putStringArrayListExtra(key_urls_list, urls);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        urls = getIntent().getStringArrayListExtra(key_urls_list);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_look);

        backView = findViewById(R.id.backView);
        title = (TextView) findViewById(R.id.title);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);

        backView.setOnClickListener(this);
        title.setText("1/" + urls.size());
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText((position + 1) + "/" + urls.size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void getViews() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backView:
                finish();
                break;
        }
    }

    private class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(ActivityPhotoLook.this).load(urls.get(position)).into(photoView);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
