package com.xy.shuhua.ui.art;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.art.model.ArtGoodsItemModel;
import com.xy.shuhua.ui.art.model.ArtGoodsListModel;
import com.xy.shuhua.ui.mine.ZuoPinItemViewHolder;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import com.xy.shuhua.util.recyclerview.AutoLoadMoreRecyclerView;
import com.xy.shuhua.util.recyclerview.DividerGridItemDecoration;
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
 * Created by xiaoyu on 2016/4/2.
 */
public class AuthorArtView extends FrameLayout {
    private PtrClassicFrameLayout refreshContainer;
    private AutoLoadMoreRecyclerView recyclerView;

    private ArtAdapter artAdapter;
    private List<ArtGoodsItemModel> itemModels = new ArrayList<>();

    //status 0 推荐 1 最热 2 最新
    private static final int limit = 20;
    private int start_num = 0;
    private int status = 0;

    public AuthorArtView(Context context) {
        super(context);
        init(context);
    }

    public AuthorArtView(Context context,int status) {
        super(context);
        this.status = status;
        init(context);
    }

    public AuthorArtView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public AuthorArtView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.author_art_view, this, true);

        refreshContainer = (PtrClassicFrameLayout) findViewById(R.id.refreshContainer);
        recyclerView = (AutoLoadMoreRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.getRecyclerView().setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.getRecyclerView().addItemDecoration(new DividerGridItemDecoration(getContext(), R.drawable.goods_list_divider));

        refreshContainer.setLastUpdateTimeRelateObject(this);
        refreshContainer.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
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

        artAdapter = new ArtAdapter(getContext());
        recyclerView.setAdapter(artAdapter);
    }

    private void refreshData() {
        start_num = 1;
        Map<String, String> params = new HashMap<>();
        params.put("limit", limit + "");
        params.put("start_num", start_num + "");
        params.put("status", status + "");
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.QUERY_ARTS)
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
                        ArtGoodsListModel goodsListModel = GsonUtil.transModel(response, ArtGoodsListModel.class);
                        if (goodsListModel == null || !"1".equals(goodsListModel.result)) {
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        if (goodsListModel.artlist != null) {
                            itemModels.clear();
                            itemModels.addAll(goodsListModel.artlist);
                            artAdapter.notifyDataSetChanged();
                            if (goodsListModel.artlist.size() < 20) {//没有更多了
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
        params.put("status", status + "");
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.QUERY_ARTS)
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
                        ArtGoodsListModel goodsListModel = GsonUtil.transModel(response, ArtGoodsListModel.class);
                        if (goodsListModel == null || !"1".equals(goodsListModel.result)) {
                            start_num--;
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        if (goodsListModel.artlist != null) {
                            itemModels.addAll(goodsListModel.artlist);
                            artAdapter.notifyDataSetChanged();
                            if (goodsListModel.artlist.size() < 20) {//没有更多了
                                recyclerView.hasMore(false);
                            } else {//也许还有更多
                                recyclerView.hasMore(true);
                            }
                        }
                    }
                });
    }

    private class ArtAdapter extends RecyclerView.Adapter<ZuoPinItemViewHolder> {
        private Context context;
        private LayoutInflater inflater;

        public ArtAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ZuoPinItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.zuopin_item_view, viewGroup, false);
            ZuoPinItemViewHolder viewHolder = new ZuoPinItemViewHolder(context, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ZuoPinItemViewHolder viewHolder, int i) {
            viewHolder.setData(itemModels.get(i));
        }

        @Override
        public int getItemCount() {
            return itemModels.size();
        }
    }
}
