package com.xy.shuhua.ui.home;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.home.model.ArtUserModel;
import com.xy.shuhua.ui.home.model.BannerModel;
import com.xy.shuhua.util.DisplayUtil;
import com.xy.shuhua.util.viewpager_indicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 2016/3/23.
 */
public class HomeHeaderViewViewHolder extends RecyclerView.ViewHolder {
    private HomeBannerViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;
    private LinearLayout userContainer;
    private List<BannerModel> bannerModels = new ArrayList<>();
    private BannerAdapter bannerAdapter;
    private List<BannerImageView> bannerImageViews = new ArrayList<>();
    private List<ArtUserModel> artUserModels = new ArrayList<>();

    private Context context;
    private View rootView;
    private int itemWidth = 0;
    private int itemMargin = 0;

    public HomeHeaderViewViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;

        viewPager = (HomeBannerViewPager) rootView.findViewById(R.id.viewPager);
        circlePageIndicator = (CirclePageIndicator) rootView.findViewById(R.id.circlePageIndicator);
        userContainer = (LinearLayout) rootView.findViewById(R.id.userContainer);

        itemMargin = DisplayUtil.dip2px(context, 10);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        itemWidth = (int) ((width - itemMargin * 5 - DisplayUtil.dip2px(context, 24)) * 1.0f / 6);
    }

    public void setEventParent(ViewGroup viewGroup) {
        viewPager.setNestParent(viewGroup);
    }

    public void setData(List<BannerModel> bannerModels, List<ArtUserModel> artUserModels) {
        if (bannerModels != null && bannerModels.size() > 0) {
            this.bannerModels.clear();
            this.bannerImageViews.clear();
            this.bannerModels.addAll(bannerModels);
            bannerAdapter = new BannerAdapter();
            for (int i = 0; i < bannerModels.size(); i++) {
                BannerImageView imageView = new BannerImageView(context);
                imageView.setData(bannerModels.get(i));
                bannerImageViews.add(imageView);
            }
            viewPager.setAdapter(bannerAdapter);
            circlePageIndicator.setViewPager(viewPager);
        }
        /**艺术家**/
        if (artUserModels != null) {
            this.artUserModels.clear();
            this.artUserModels.addAll(artUserModels);
            int count = 0;
            if (artUserModels.size() > 5) {
                count = 5;
            } else {
                count = artUserModels.size();
            }
            userContainer.removeAllViews();
            userContainer.setVisibility(View.VISIBLE);
            for (int i = 0; i < count; i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.rightMargin = DisplayUtil.dip2px(context, 10);
                HomeUserItemView homeUserItemView = new HomeUserItemView(context);
                homeUserItemView.setData(artUserModels.get(i), false);
                userContainer.addView(homeUserItemView, params);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            HomeUserItemView homeUserItemView = new HomeUserItemView(context);
            homeUserItemView.setData(null, true);
            userContainer.addView(homeUserItemView, params);
        } else {
            userContainer.setVisibility(View.GONE);
        }
    }

    private class BannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return bannerImageViews.size();
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

    private static class BannerImageView extends ImageView {
        private BannerModel bannerModel;

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
            setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                }
            });
        }

        public void setData(BannerModel bannerModel) {
            this.bannerModel = bannerModel;
            if (bannerModel == null) {
                return;
            }
            if (!TextUtils.isEmpty(bannerModel.imageurl)) {
                Glide.with(getContext()).load(bannerModel.imageurl).placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(this);
            } else {
                Glide.with(getContext()).load("").placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(this);
            }
        }
    }

    private class HomeUserItemView extends FrameLayout {
        private ImageView avatar;
        private TextView name;
        public boolean isMore;
        public ArtUserModel artUserModel;

        public HomeUserItemView(Context context) {
            super(context);
            init(context);
        }

        public HomeUserItemView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public HomeUserItemView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init(context);
        }

        public void setData(ArtUserModel artUserModel, boolean isMore) {
            this.artUserModel = artUserModel;
            this.isMore = isMore;
            if (isMore) {
                avatar.setImageResource(R.drawable.icon_more);
                name.setText("更多");
            } else {
                if (!TextUtils.isEmpty(artUserModel.imageurl)) {
                    Glide.with(getContext()).load(artUserModel.imageurl).placeholder(R.drawable.me_avatar_boy).error(R.drawable.me_avatar_boy).into(avatar);
                } else {
                    avatar.setImageResource(R.drawable.me_avatar_boy);
                }
                if (!TextUtils.isEmpty(artUserModel.realname)) {
                    name.setText(artUserModel.realname);
                } else {
                    if (!TextUtils.isEmpty(artUserModel.nickname)) {
                        name.setText(artUserModel.nickname);
                    } else {
                        if (!TextUtils.isEmpty(artUserModel.username)) {
                            name.setText(artUserModel.username);
                        } else {
                            name.setText("");
                        }
                    }
                }
            }
        }

        private void init(final Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.home_user_item_view, this, true);
            avatar = (ImageView) findViewById(R.id.avatar);
            name = (TextView) findViewById(R.id.name);

            setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(isMore){
                        ActivityAuthorList.open((Activity) context);
                    }else{
                        String avatar = artUserModel.imageurl;
                        String nameStr = name.getText().toString();
                        ActivityAuthorHomePage.open((Activity) context,artUserModel.userid,avatar,nameStr);
                    }
                }
            });
        }
    }
}
