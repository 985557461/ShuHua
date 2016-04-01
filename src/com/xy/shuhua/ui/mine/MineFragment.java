package com.xy.shuhua.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xy.shuhua.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 2016/3/30.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private View zuoPinLL;
    private View zuoPinLineView;
    private View zhanLanLL;
    private View zhanLanLineView;
    private View wenZhangLL;
    private View wenZhangLineView;
    private ViewPager viewPager;

    private List<View> views = new ArrayList<>();
    private MinePagerAdapter minePagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        zuoPinLL = view.findViewById(R.id.zuoPinLL);
        zuoPinLineView = view.findViewById(R.id.zuoPinLineView);
        zhanLanLL = view.findViewById(R.id.zhanLanLL);
        zhanLanLineView = view.findViewById(R.id.zhanLanLineView);
        wenZhangLL = view.findViewById(R.id.wenZhangLL);
        wenZhangLineView = view.findViewById(R.id.wenZhangLineView);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        zuoPinLL.setOnClickListener(this);
        zhanLanLL.setOnClickListener(this);
        wenZhangLL.setOnClickListener(this);

        for (int i = 0; i < 3; i++) {
            ZuoPinRecyclerView itemView = new ZuoPinRecyclerView(getContext());
            views.add(itemView);
        }

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
        switch (view.getId()){
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
