package com.xy.shuhua.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.home.model.ArtUserModel;
import com.xy.shuhua.ui.home.model.BannerModel;
import com.xy.shuhua.ui.home.model.HomeArtGoodsModel;
import com.xy.shuhua.ui.home.model.HomeInfoModel;
import com.xy.shuhua.ui.search.ActivitySearch;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import com.xy.shuhua.util.recyclerview.AutoLoadMoreRecyclerView;
import com.xy.shuhua.util.recyclerview.LoadMoreInterface;
import com.xy.shuhua.util.ultra_pull_refresh.PtrClassicFrameLayout;
import com.xy.shuhua.util.ultra_pull_refresh.PtrDefaultHandler;
import com.xy.shuhua.util.ultra_pull_refresh.PtrFrameLayout;
import com.xy.shuhua.util.ultra_pull_refresh.PtrHandler;
import okhttp3.Call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/3/22.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private View searchView;
    private View upload;
    private PtrClassicFrameLayout refreshContainer;
    private AutoLoadMoreRecyclerView recyclerView;

    private GoodsAdapter goodsAdapter;
    private List<HomeArtGoodsModel> artGoodsModels = new ArrayList<>();

    private static final int limit = 20;
    private int start_num = 0;

    public List<ArtUserModel> userlist;
    public List<BannerModel> banner_posts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        searchView = view.findViewById(R.id.searchView);
        upload = view.findViewById(R.id.upload);
        refreshContainer = (PtrClassicFrameLayout) view.findViewById(R.id.refreshContainer);
        recyclerView = (AutoLoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.getRecyclerView().setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        searchView.setOnClickListener(this);
        upload.setOnClickListener(this);
        refreshContainer.setLastUpdateTimeRelateObject(this);
        refreshContainer.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView.getRecyclerView(), header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshData();
            }
        });
        refreshContainer.autoRefresh();

        recyclerView.setLoadMoreInterface(new LoadMoreInterface() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        goodsAdapter = new GoodsAdapter(getContext());
        recyclerView.setAdapter(goodsAdapter);
    }

    private void refreshData() {
        start_num = 0;
        Map<String, String> params = new HashMap<>();
        params.put("limit", limit + "");
        params.put("start_num", start_num + "");
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.HOME_QUERY_ART)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        refreshContainer.refreshComplete();
                        ToastUtil.makeShortText("网络连接失败了");
                    }

                    @Override
                    public void onResponse(String response) {
                        refreshContainer.refreshComplete();
                        HomeInfoModel homeInfoModel = GsonUtil.transModel(response, HomeInfoModel.class);
                        if (homeInfoModel == null || !"1".equals(homeInfoModel.result)) {
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        userlist = homeInfoModel.userlist;
                        banner_posts = homeInfoModel.banner_posts;
                        if (homeInfoModel.artlist != null) {
                            artGoodsModels.clear();
                            artGoodsModels.addAll(homeInfoModel.artlist);
                            Log.d("xiaoyu", "size--:" + artGoodsModels.size());
                            goodsAdapter.notifyDataSetChanged();
                            if (homeInfoModel.artlist.size() < 20) {//没有更多了
                                recyclerView.hasMore(false);
                            } else {//也许还有更多
                                recyclerView.hasMore(true);
                            }
                        }
                    }
                });
    }

    private void loadMoreData() {
        start_num++;
        Map<String, String> params = new HashMap<>();
        params.put("limit", limit + "");
        params.put("start_num", start_num + "");
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.HOME_QUERY_ART)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        start_num--;
                        recyclerView.loadMoreCompleted();
                        ToastUtil.makeShortText("网络连接失败了");
                    }

                    @Override
                    public void onResponse(String response) {
                        recyclerView.loadMoreCompleted();
                        HomeInfoModel homeInfoModel = GsonUtil.transModel(response, HomeInfoModel.class);
                        if (homeInfoModel == null || !"1".equals(homeInfoModel.result)) {
                            start_num--;
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        if (homeInfoModel.artlist != null) {
                            artGoodsModels.addAll(homeInfoModel.artlist);
                            goodsAdapter.notifyDataSetChanged();
                            if (homeInfoModel.artlist.size() < 20) {//没有更多了
                                recyclerView.hasMore(false);
                            } else {//也许还有更多
                                recyclerView.hasMore(true);
                            }
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchView:
                ActivitySearch.open(getActivity());
                break;
            case R.id.upload:
                ActivityPublishZuoPin.open(getActivity());
                break;
        }
    }

    private class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int headerViewType = 3;
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
            } else if (viewType == headerViewType) {
                View view = inflater.inflate(R.layout.home_header_view, viewGroup, false);
                HomeHeaderViewViewHolder viewHolder = new HomeHeaderViewViewHolder(context, view);
                return viewHolder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof SpaceItemViewHolder) {
                ((SpaceItemViewHolder) viewHolder).setData();
                ((SpaceItemViewHolder) viewHolder).showLine(i);
            } else if (viewHolder instanceof HomeHeaderViewViewHolder) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                params.setFullSpan(true);
                ((HomeHeaderViewViewHolder) viewHolder).setData(banner_posts, userlist);
                ((HomeHeaderViewViewHolder) viewHolder).setEventParent(refreshContainer);
            } else if (viewHolder instanceof GoodsItemViewHolder) {
                ((GoodsItemViewHolder) viewHolder).setData(artGoodsModels.get(i - 2));
                ((GoodsItemViewHolder) viewHolder).showLine(i);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return headerViewType;
            } else if (position == 1) {
                return spaceViewType;
            } else {
                return fullViewType;
            }
        }

        @Override
        public int getItemCount() {
            return artGoodsModels.size() + 2;
        }
    }
}
