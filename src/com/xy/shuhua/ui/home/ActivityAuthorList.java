package com.xy.shuhua.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.ui.home.model.ArtUserListModel;
import com.xy.shuhua.ui.home.model.ArtUserModel;
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
 * Created by xiaoyu on 2016/5/14.
 */
public class ActivityAuthorList extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private PtrClassicFrameLayout refreshContainer;
    private AutoLoadMoreRecyclerView recyclerView;

    private AuthorAdapter authorAdapter;
    private List<ArtUserModel> artUserModels = new ArrayList<>();

    private static final int limit = 20;
    private int start_num = 0;

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityAuthorList.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_list);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        refreshContainer = (PtrClassicFrameLayout) findViewById(R.id.refreshContainer);
        recyclerView = (AutoLoadMoreRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.getRecyclerView().addItemDecoration(new DividerListItemDecoration(this, DividerListItemDecoration.VERTICAL_LIST, R.drawable.goods_list_divider));

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

        authorAdapter = new AuthorAdapter(this);
        recyclerView.setAdapter(authorAdapter);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backView) {
            finish();
        }
    }

    private void refreshData() {
        start_num = 0;
        Map<String, String> params = new HashMap<>();
        params.put("limit", limit + "");
        params.put("start_num", (start_num * limit) + "");
        params.put("status", "1");
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.ALL_AUTHOR)
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
                        ArtUserListModel artUserListModel = GsonUtil.transModel(response, ArtUserListModel.class);
                        if (artUserListModel == null || !"1".equals(artUserListModel.result)) {
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        if (artUserListModel.list != null) {
                            artUserModels.clear();
                            artUserModels.addAll(artUserListModel.list);
                            authorAdapter.notifyDataSetChanged();
                            if (artUserListModel.list.size() < 20) {//没有更多了
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
        params.put("status", "1");
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.ALL_AUTHOR)
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
                        ArtUserListModel artUserListModel = GsonUtil.transModel(response, ArtUserListModel.class);
                        if (artUserListModel == null || !"1".equals(artUserListModel.result)) {
                            start_num--;
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        if (artUserListModel.list != null) {
                            artUserModels.addAll(artUserListModel.list);
                            authorAdapter.notifyDataSetChanged();
                            if (artUserListModel.list.size() < 20) {//没有更多了
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

    private class AuthorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private LayoutInflater inflater;

        public AuthorAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.author_item_view, viewGroup, false);
            AuthorItemViewHolder viewHolder = new AuthorItemViewHolder(context, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ((AuthorItemViewHolder) viewHolder).setData(artUserModels.get(i));
        }

        @Override
        public int getItemCount() {
            return artUserModels.size();
        }
    }
}
