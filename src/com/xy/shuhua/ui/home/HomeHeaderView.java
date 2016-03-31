package com.xy.shuhua.ui.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.util.recyclerview.RecyclerViewHeader;
import com.xy.shuhua.util.viewpager_indicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 2016/3/23.
 */
public class HomeHeaderView extends RecyclerViewHeader {
    private HomeBannerViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;

    private BannerAdapter bannerAdapter;
    private List<BannerImageView> bannerImageViews = new ArrayList<>();

    public HomeHeaderView(Context context) {
        super(context);
        init(context);
    }

    public HomeHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomeHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.home_header_view, this, true);

        viewPager = (HomeBannerViewPager) findViewById(R.id.viewPager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);

        setData();
    }

    public void setEventParent(ViewGroup viewGroup){
        viewPager.setNestParent(viewGroup);
    }

    public void setData() {
        for(int i=0;i<4;i++){
            BannerImageView imageView = new BannerImageView(getContext());
            bannerImageViews.add(imageView);
        }
        bannerAdapter = new BannerAdapter();
        viewPager.setAdapter(bannerAdapter);
        circlePageIndicator.setViewPager(viewPager);
    }

    private class BannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BannerImageView imageView = bannerImageViews.get(position);
            if (imageView.getParent() != null) {
                container.removeView(imageView);
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(bannerImageViews.get(position));
        }
    }

    private static class BannerImageView extends ImageView implements OnClickListener {
        public BannerImageView(Context context) {
            super(context);
            init(context);
        }

        public BannerImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init(context);
        }

        public BannerImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            setScaleType(ScaleType.CENTER_CROP);
            setLayoutParams(params);
            setOnClickListener(this);

            setData();
        }

        public void setData() {
            Glide.with(getContext()).load("http://auction.artron.net/getpic_new.php?type=b&host=auction.artron.net&hash=BB%2Ff%2FvdHh5%2BejxJL%2FLYhnA9s9Gfw").into(this);
        }

        @Override
        public void onClick(View view) {
        }
    }
}
