package com.xy.shuhua.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.art.model.ArtGoodsItemModel;
import com.xy.shuhua.ui.art.model.ArtGoodsListModel;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.ui.mine.ZuoPinItemViewHolder;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.PrintHttpUrlUtil;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import com.xy.shuhua.util.recyclerview.AutoLoadMoreRecyclerView;
import com.xy.shuhua.util.recyclerview.DividerGridItemDecoration;
import com.xy.shuhua.util.recyclerview.LoadMoreInterface;
import com.xy.shuhua.util.ultra_pull_refresh.PtrClassicFrameLayout;
import com.xy.shuhua.util.ultra_pull_refresh.PtrFrameLayout;
import com.xy.shuhua.util.ultra_pull_refresh.PtrHandler;
import okhttp3.Call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/4/14.
 */
public class ActivitySearchResult extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private PtrClassicFrameLayout refreshContainer;
    private AutoLoadMoreRecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private List<ArtGoodsItemModel> itemModels = new ArrayList<>();

    private static final int limit = 20;
    private int start_num = 0;

    public static final String NAME = "name";
    private String name;

    public static void open(Activity activity, String name) {
        Intent intent = new Intent(activity, ActivitySearchResult.class);
        intent.putExtra(NAME, name);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getStringExtra(NAME);
        setContentView(R.layout.activity_search_result);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        refreshContainer = (PtrClassicFrameLayout) findViewById(R.id.refreshContainer);
        recyclerView = (AutoLoadMoreRecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    protected void initViews() {
        recyclerView.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.getRecyclerView().addItemDecoration(new DividerGridItemDecoration(this, R.drawable.goods_list_divider));

        refreshContainer.setLastUpdateTimeRelateObject(this);
        refreshContainer.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return false;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshData();
            }
        });
        refreshContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshContainer.autoRefresh();
            }
        }, 100);

        recyclerView.setLoadMoreInterface(new LoadMoreInterface() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        customAdapter = new CustomAdapter(this);
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
    }

    private void refreshData() {
        start_num = 1;
        Map<String, String> params = new HashMap<>();
        params.put("limit", limit + "");
        params.put("start_num", start_num + "");
        params.put("category","");
        params.put("status","");
        params.put("name", name);
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.SEARCH_ARTS, params);
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
                            customAdapter.notifyDataSetChanged();
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
        params.put("category","");
        params.put("status","");
        params.put("name", name);
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.SEARCH_ARTS)
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
                            customAdapter.notifyDataSetChanged();
                            if (goodsListModel.artlist.size() < 20) {//没有更多了
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
            case R.id.backView:
                finish();
                break;
        }
    }

    private class CustomAdapter extends RecyclerView.Adapter<ZuoPinItemViewHolder> {
        private Context context;
        private LayoutInflater inflater;

        public CustomAdapter(Context context) {
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
