package com.xy.shuhua.ui.art;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.art.model.ArtGoodsItemModel;

/**
 * Created by xiaoyu on 2016/4/5.
 */
public class RelevantRowView extends FrameLayout {
    private RelevantItemView itemOne;
    private RelevantItemView itemTwo;

    private ArtGoodsItemModel modelOne;
    private ArtGoodsItemModel modelTwo;

    public RelevantRowView(Context context) {
        super(context);
        init(context);
    }

    public RelevantRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RelevantRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.zuopin_row_view, this, true);

        itemOne = (RelevantItemView) findViewById(R.id.itemOne);
        itemTwo = (RelevantItemView) findViewById(R.id.itemTwo);
    }

    public void setData(ArtGoodsItemModel modelOne,ArtGoodsItemModel modelTwo){
        this.modelOne = modelOne;
        this.modelTwo = modelTwo;
        if(modelOne != null){
            itemOne.setVisibility(View.VISIBLE);
            itemOne.setData(modelOne);
        }else{
            itemOne.setVisibility(View.INVISIBLE);
        }
        if(modelTwo != null){
            itemTwo.setVisibility(View.VISIBLE);
            itemTwo.setData(modelTwo);
        }else{
            itemTwo.setVisibility(View.INVISIBLE);
        }
    }
}
