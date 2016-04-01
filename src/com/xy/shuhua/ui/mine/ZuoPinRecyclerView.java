package com.xy.shuhua.ui.mine;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.xy.shuhua.R;
import com.xy.shuhua.util.recyclerview.DividerGridItemDecoration;
import com.xy.shuhua.util.ultra_pull_refresh.PtrClassicFrameLayout;
import com.xy.shuhua.util.ultra_pull_refresh.PtrFrameLayout;
import com.xy.shuhua.util.ultra_pull_refresh.PtrHandler;

/**
 * Created by xiaoyu on 2016/4/1.
 */
public class ZuoPinRecyclerView extends FrameLayout{
    private PtrClassicFrameLayout refreshContainer;
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;

    public ZuoPinRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public ZuoPinRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZuoPinRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.zuopin_recycler_view,this,true);

        refreshContainer = (PtrClassicFrameLayout) findViewById(R.id.refreshContainer);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,GridLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getContext(), R.drawable.goods_list_divider));

        refreshContainer.setLastUpdateTimeRelateObject(this);
        refreshContainer.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return false;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshContainer.refreshComplete();
            }
        });
        refreshContainer.autoRefresh();

        customAdapter = new CustomAdapter(getContext());
        recyclerView.setAdapter(customAdapter);
    }

    private class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private LayoutInflater inflater;

        public CustomAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.zuopin_item_view, viewGroup, false);
            ZuoPinItemViewHolder viewHolder = new ZuoPinItemViewHolder(context, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }
}
