package com.xy.shuhua.ui.PhotoChooser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.PhotoChooser.PhotoPickerActivity;
import com.xy.shuhua.ui.PhotoChooser.beans.Photo;
import com.xy.shuhua.ui.PhotoChooser.utils.OtherUtils;
import com.xy.shuhua.ui.PhotoChooser.widgets.PhotoItemView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Class: PhotoAdapter
 * @Description: 图片适配器
 * @author: lling(www.liuling123.com)
 * @Date: 2015/11/4
 */
public class PhotoAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_PHOTO = 1;

    private List<Photo> mDatas;
    //存放已选中的Photo数据
    private List<String> mSelectedPhotos;
    private Context mContext;
    private int mWidth;
    //是否显示相机，默认不显示
    private boolean mIsShowCamera = false;
    //照片选择模式，默认单选
    private int mSelectMode = PhotoPickerActivity.MODE_SINGLE;
    //图片选择数量
    private int mMaxNum = PhotoPickerActivity.DEFAULT_NUM;

    private View.OnClickListener mOnPhotoClick;
    private PhotoClickCallBack mCallBack;

    public PhotoAdapter(Context context, List<Photo> mDatas) {
        this.mDatas = mDatas;
        this.mContext = context;
        int screenWidth = OtherUtils.getWidthInPx(mContext);
        mWidth = (screenWidth - OtherUtils.dip2px(mContext, 4))/3;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 && mIsShowCamera) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PHOTO;
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Photo getItem(int position) {
        if(mIsShowCamera) {
            if(position == 0){
                return null;
            }
            return mDatas.get(position-1);
        }else{
            return mDatas.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return mDatas.get(position).getId();
    }

    public void setDatas(List<Photo> mDatas) {
        this.mDatas = mDatas;
    }

    public void setIsShowCamera(boolean isShowCamera) {
        this.mIsShowCamera = isShowCamera;
    }

    public boolean isShowCamera() {
        return mIsShowCamera;
    }

    public void setMaxNum(int maxNum) {
        this.mMaxNum = maxNum;
    }

    public void setPhotoClickCallBack(PhotoClickCallBack callback) {
        mCallBack = callback;
    }


    /**
     * 获取已选中相片
     * @return
     */
    public List<String> getmSelectedPhotos() {
        return mSelectedPhotos;
    }

    public void setSelectMode(int selectMode) {
        this.mSelectMode = selectMode;
        if(mSelectMode == PhotoPickerActivity.MODE_MULTI) {
            initMultiMode();
        }
    }

    /**
     * 初始化多选模式所需要的参数
     */
    private void initMultiMode() {
        mSelectedPhotos = new ArrayList<String>();
        mOnPhotoClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoItemView photoItemView = (PhotoItemView)v;
                    String path = ((PhotoItemView)v).photo.getPath();
                if(mSelectedPhotos.contains(path)) {
                    photoItemView.maskView.setVisibility(View.GONE);
                    photoItemView.selectView.setSelected(false);
                    mSelectedPhotos.remove(path);
                } else {
                    if(mSelectedPhotos.size() >= mMaxNum) {
                        Toast.makeText(mContext, R.string.msg_maxi_capacity,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mSelectedPhotos.add(path);
                    photoItemView.maskView.setVisibility(View.VISIBLE);
                    photoItemView.selectView.setSelected(true);
                }
                if(mCallBack != null) {
                    mCallBack.onPhotoClick();
                }
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItemViewType(position) == TYPE_CAMERA) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_camera_layout, null);
            convertView.setTag(null);
            //设置高度等于宽度
            GridView.LayoutParams lp = new GridView.LayoutParams(mWidth, mWidth);
            convertView.setLayoutParams(lp);
            return convertView;
        } else {
            PhotoItemView photoItemView = null;
            if(convertView != null && convertView instanceof PhotoItemView){
                photoItemView = (PhotoItemView)convertView;
            }else{
                photoItemView = new PhotoItemView(mContext);
            }
            Photo photo = getItem(position);
            photoItemView.photo = photo;
            if(mSelectMode == PhotoPickerActivity.MODE_MULTI) {
                photoItemView.setOnClickListener(mOnPhotoClick);
                photoItemView.selectView.setVisibility(View.VISIBLE);
                if(mSelectedPhotos != null && mSelectedPhotos.contains(photo.getPath())) {
                    photoItemView.selectView.setSelected(true);
                    photoItemView.maskView.setVisibility(View.VISIBLE);
                } else {
                    photoItemView.selectView.setSelected(false);
                    photoItemView.maskView.setVisibility(View.GONE);
                }
            } else {
                photoItemView.selectView.setVisibility(View.GONE);
            }
            Glide.with(mContext).load(new File(photo.getPath())).override(mWidth,mWidth).placeholder(R.drawable.ic_photo_loading).error(R.drawable.ic_photo_loading).into(photoItemView.photoImageView);
            return photoItemView;
        }
    }

    /**
     * 多选时，点击相片的回调接口
     */
    public interface PhotoClickCallBack {
        void onPhotoClick();
    }
}
