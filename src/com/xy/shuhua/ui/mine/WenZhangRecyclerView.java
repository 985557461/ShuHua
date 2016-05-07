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
import com.xy.shuhua.common_background.Account;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.CustomApplication;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.PrintHttpUrlUtil;
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
 * Created by xiaoyu on 2016/4/1.
 */
public class WenZhangRecyclerView extends FrameLayout {
    private PtrClassicFrameLayout refreshContainer;
    private AutoLoadMoreRecyclerView recyclerView;
    private ArtAdapter artAdapter;
    private List<ArtWenZhangItemModel> itemModels = new ArrayList<>();

    private static final int limit = 20;
    private int start_num = 0;
    private Account account;
    private int type;//0 展览 1 文章

    public WenZhangRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public WenZhangRecyclerView(Context context, int type) {
        super(context);
        this.type = type;
        init(context);
    }

    public WenZhangRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WenZhangRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        account = CustomApplication.getInstance().getAccount();
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.zuopin_recycler_view, this, true);

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
        params.put("userid", account.userId);
        params.put("type", type + "");
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.MY_WENZHANG, params);
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.MY_ZUOPIN)
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
                        ArtWenZhangListModel goodsListModel = GsonUtil.transModel(response, ArtWenZhangListModel.class);
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
        params.put("userid", account.userId);
        params.put("type", type + "");
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.MY_WENZHANG)
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
                        ArtWenZhangListModel goodsListModel = GsonUtil.transModel(response, ArtWenZhangListModel.class);
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

    private class ArtAdapter extends RecyclerView.Adapter<WenZhangItemViewHolder> {
        private Context context;
        private LayoutInflater inflater;

        public ArtAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public WenZhangItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.wenzhang_item_view, viewGroup, false);
            WenZhangItemViewHolder viewHolder = new WenZhangItemViewHolder(context, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(WenZhangItemViewHolder viewHolder, int i) {
            viewHolder.setData();
        }

        @Override
        public int getItemCount() {
            return itemModels.size();
        }
    }
}
