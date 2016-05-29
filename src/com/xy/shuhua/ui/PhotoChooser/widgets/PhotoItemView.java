package com.xy.shuhua.ui.PhotoChooser.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.PhotoChooser.beans.Photo;

/**
 * Created by Administrator on 2016/5/19.
 */
public class PhotoItemView extends FrameLayout{
    public ImageView photoImageView;
    public ImageView selectView;
    public View maskView;
    public FrameLayout wrapLayout;

    public Photo photo;

    public PhotoItemView(Context context) {
        super(context);
        init(context);
    }

    public PhotoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PhotoItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.item_photo_layout,this,true);

        photoImageView = (ImageView) findViewById(R.id.imageview_photo);
        selectView = (ImageView) findViewById(R.id.checkmark);
        maskView = findViewById(R.id.mask);
        wrapLayout = (FrameLayout) findViewById(R.id.wrap_layout);
    }
}
