package com.xy.shuhua.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.ui.home.photo_choose.PhotoSelectAdapter;
import com.xy.shuhua.ui.home.photo_choose.PhotoSelectView;
import com.xy.shuhua.ui.home.photo_choose.SquarePhotoView;
import com.xy.shuhua.util.DisplayUtil;
import com.xy.shuhua.util.photo.IntentUtils;
import com.xy.shuhua.util.photo.PhotoActivity;
import com.xy.shuhua.util.photo.PhotoAlbumActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 2016/4/17.
 */
public class ActivityPublishZuoPin extends ActivityBaseNoSliding implements View.OnClickListener,
        PhotoSelectView.FooterClickListener, PhotoSelectView.PhotoDeleteListener {
    private View backView;
    /**
     * picture about
     * *
     */
    private PhotoSelectView photoSelectView;
    private PhotoSelectAdapter adapter;
    private static final int kActivitySettingSelectPicRequest = 101;
    private String avatarPath = "";
    private List<String> pathsList = new ArrayList<String>();

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityPublishZuoPin.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_zuopin);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        photoSelectView = (PhotoSelectView) findViewById(R.id.photoSelectView);
    }

    @Override
    protected void initViews() {
        adapter = new MyPhotoSelectAdapter();
        photoSelectView.setAdapter(adapter);
    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
        photoSelectView.setFooterClickListener(this);
        photoSelectView.setPhotoDeleteListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == kActivitySettingSelectPicRequest && resultCode == RESULT_OK) {
            String[] paths = data.getStringArrayExtra(PhotoAlbumActivity.Key_SelectPaths);
            if (paths != null && paths.length <= 0) {
                return;
            }
            if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromAlbum)) {
                if (!TextUtils.isEmpty(paths[0])) {
                    avatarPath = paths[0];
                    pathsList.add(avatarPath);
                    photoSelectView.setAdapter(adapter);
                }
            } else if (data.getStringExtra(PhotoActivity.kWhereFrom).equals(PhotoActivity.kFromCamera)) {
                if (!TextUtils.isEmpty(paths[0])) {
                    avatarPath = paths[0];
                    pathsList.add(avatarPath);
                    photoSelectView.setAdapter(adapter);
                }
            }
        }
//        else if (requestCode == kPhotoCropImageRequest && resultCode == RESULT_OK) {
//            avatarPath = data.getStringExtra(ActivityCropImage.kCropImagePath);
//            pathsList.add(avatarPath);
//            photoSelectView.setAdapter(adapter);
//            return;
//        }
    }

    @Override
    public void onFooterClicked() {
        Intent intent = IntentUtils.goToAlbumIntent(new ArrayList<String>(), 1, getResources().getString(R.string.confirm), true, ActivityPublishZuoPin.this);
        startActivityForResult(intent, kActivitySettingSelectPicRequest);
    }

    @Override
    public void onPhotoDeleteClicked(String path) {
        pathsList.remove(path);
        photoSelectView.setAdapter(adapter);
    }

    private class MyPhotoSelectAdapter extends PhotoSelectAdapter<SquarePhotoView> {

        @Override
        public int getWidth() {
            WindowManager wm = (WindowManager) ActivityPublishZuoPin.this.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            return width - DisplayUtil.dip2px(ActivityPublishZuoPin.this, 14);
        }

        @Override
        public int getTotalCount() {
            return pathsList.size();
        }

        @Override
        public int getColCount() {
            return 4;
        }

        @Override
        public int getHorMargin() {
            return 0;
        }

        @Override
        public int getVerMargin() {
            return DisplayUtil.dip2px(ActivityPublishZuoPin.this, 10);
        }

        @Override
        public void setView(SquarePhotoView view, int position) {
            view.setData(pathsList.get(position));
        }
    }
}
