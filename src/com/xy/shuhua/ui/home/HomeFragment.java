package com.xy.shuhua.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xy.shuhua.R;
import com.xy.shuhua.util.recyclerview.DividerGridItemDecoration;
import com.xy.shuhua.util.ultra_pull_refresh.PtrClassicFrameLayout;
import com.xy.shuhua.util.ultra_pull_refresh.PtrDefaultHandler;
import com.xy.shuhua.util.ultra_pull_refresh.PtrFrameLayout;
import com.xy.shuhua.util.ultra_pull_refresh.PtrHandler;

/**
 * Created by xiaoyu on 2016/3/22.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private PtrClassicFrameLayout refreshContainer;
    private RecyclerView recyclerView;
    private HomeHeaderView headerView;

    private GoodsAdapter goodsAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        refreshContainer = (PtrClassicFrameLayout) view.findViewById(R.id.refreshContainer);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getContext(), R.drawable.goods_list_divider));

        headerView = new HomeHeaderView(getContext());
        headerView.attachTo(recyclerView);
        headerView.setEventParent(refreshContainer);

        refreshContainer.setLastUpdateTimeRelateObject(this);
        refreshContainer.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshContainer.refreshComplete();
            }
        });
        refreshContainer.autoRefresh();

        goodsAdapter = new GoodsAdapter(getContext());
        recyclerView.setAdapter(goodsAdapter);
    }

    @Override
    public void onClick(View view) {

    }

    private class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int spaceViewType = 1;
        private final int fullViewType = 2;
        private Context context;
        private LayoutInflater inflater;

        public GoodsAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            if (viewType == spaceViewType) {
                View view = inflater.inflate(R.layout.space_item_view, viewGroup, false);
                SpaceItemViewHolder viewHolder = new SpaceItemViewHolder(context, view);
                return viewHolder;
            } else if (viewType == fullViewType) {
                View view = inflater.inflate(R.layout.goods_item_view, viewGroup, false);
                GoodsItemViewHolder viewHolder = new GoodsItemViewHolder(context, view);
                return viewHolder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof SpaceItemViewHolder) {
                ((SpaceItemViewHolder) viewHolder).setData();
            } else if (viewHolder instanceof GoodsItemViewHolder) {
                ((GoodsItemViewHolder) viewHolder).setData();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return spaceViewType;
            } else {
                return fullViewType;
            }
        }

        @Override
        public int getItemCount() {
            return 31;
        }
    }
}
