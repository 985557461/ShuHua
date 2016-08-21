package com.xy.shuhua.ui.art;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.art.model.AuthorModelWithZuoPin;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
import com.xy.shuhua.util.recyclerview.AutoLoadMoreRecyclerView;
import com.xy.shuhua.util.recyclerview.DividerListItemDecoration;
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
public class ArtTypeView extends FrameLayout {
    private PtrClassicFrameLayout refreshContainer;
    private AutoLoadMoreRecyclerView recyclerView;

    private ArtAdapter artAdapter;

    private static final int limit = 10;
    private int start_num = 0;
    private String category;

    private List<AuthorModelWithZuoPin.Data> itemModels = new ArrayList<>();

    public ArtTypeView(Context context) {
        super(context);
        init(context);
    }

    public ArtTypeView(Context context,String category) {
        super(context);
        this.category = category;
        init(context);
    }

    public ArtTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ArtTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.art_type_view, this, true);

        refreshContainer = (PtrClassicFrameLayout) findViewById(R.id.refreshContainer);
        recyclerView = (AutoLoadMoreRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.getRecyclerView().addItemDecoration(new DividerListItemDecoration(getContext(), DividerListItemDecoration.VERTICAL_LIST, R.drawable.goods_list_divider));

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

        artAdapter = new ArtAdapter(getContext());
        recyclerView.setAdapter(artAdapter);
    }

    private void refreshData() {
        start_num = 0;
        Map<String, String> params = new HashMap<>();
        params.put("limit", limit + "");
        params.put("start_num", (start_num * limit) + "");
        params.put("categary", category);
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.AUTHOR_LIST)
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
                        AuthorModelWithZuoPin goodsListModel = GsonUtil.transModel(response, AuthorModelWithZuoPin.class);
                        if (goodsListModel == null || !"1".equals(goodsListModel.result)) {
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        if (goodsListModel.list != null) {
                            itemModels.clear();
                            itemModels.addAll(goodsListModel.list);
                            artAdapter.notifyDataSetChanged();
                            if (goodsListModel.list.size() < 20) {//没有更多了
                                recyclerView.hasMore(false);
                            } else {//也许还有更多
                                recyclerView.hasMore(true);
                            }
                        }else{
                            recyclerView.hasMore(false);
                        }
                    }
                });
    }

    private void loadMoreData() {
        start_num++;
        Map<String, String> params = new HashMap<>();
        params.put("limit", limit + "");
        params.put("start_num", (start_num * limit) + "");
        params.put("categary", category);
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.AUTHOR_LIST)
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
                        AuthorModelWithZuoPin goodsListModel = GsonUtil.transModel(response, AuthorModelWithZuoPin.class);
                        if (goodsListModel == null || !"1".equals(goodsListModel.result)) {
                            start_num--;
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        if (goodsListModel.list != null) {
                            itemModels.addAll(goodsListModel.list);
                            artAdapter.notifyDataSetChanged();
                            if (goodsListModel.list.size() < 20) {//没有更多了
                                recyclerView.hasMore(false);
                            } else {//也许还有更多
                                recyclerView.hasMore(true);
                            }
                        }else{
                            recyclerView.hasMore(false);
                        }
                    }
                });
    }

    private class ArtAdapter extends RecyclerView.Adapter<ArtPersonViewHolder> {
        private Context context;
        private LayoutInflater inflater;

        public ArtAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ArtPersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.art_person_item_view, viewGroup, false);
            ArtPersonViewHolder viewHolder = new ArtPersonViewHolder(context, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ArtPersonViewHolder viewHolder, int i) {
                viewHolder.setData(itemModels.get(i));
        }

        @Override
        public int getItemCount() {
            return itemModels.size();
        }
    }
}
