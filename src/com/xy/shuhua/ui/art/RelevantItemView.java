package com.xy.shuhua.ui.art;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.art.model.ArtGoodsItemModel;
import com.xy.shuhua.util.ToastUtil;

/**
 * Created by xiaoyu on 2016/4/5.
 */
public class RelevantItemView extends FrameLayout {
    private ImageView imageView;
    private TextView name;

    private ArtGoodsItemModel itemModel;

    public RelevantItemView(Context context) {
        super(context);
        init(context);
    }

    public RelevantItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RelevantItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.zuopin_item_view, this, true);

        imageView = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.name);

        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (itemModel != null) {
                    if (!TextUtils.isEmpty(itemModel.id)) {
                        ActivityArtGoodsInfo.open((Activity) context, itemModel.id);
                    } else {
                        ToastUtil.makeShortText("作品不存在");
                    }
                }
            }
        });
    }

    public void setData(ArtGoodsItemModel itemModel) {
        this.itemModel = itemModel;
        if (itemModel == null) {
            return;
        }
        if (!TextUtils.isEmpty(itemModel.imageurl)) {
            Glide.with(getContext()).load(itemModel.imageurl).placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(imageView);
        }else{
            Glide.with(getContext()).load(R.drawable.icon_art_pressed).placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(imageView);
        }
        if (!TextUtils.isEmpty(itemModel.name)) {
            name.setText(itemModel.name);
        } else {
            name.setText("");
        }
    }
}
