package com.xy.shuhua.ui.mine;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.art.model.ArtGoodsItemModel;
import com.xy.shuhua.ui.art.model.ArtGoodsListModel;
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
public class ZuoPinRecyclerView extends FrameLayout {
    private PtrClassicFrameLayout refreshContainer;
    private AutoLoadMoreRecyclerView recyclerView;
    private TextView emptyTips;
    private ArtAdapter artAdapter;
    private List<ArtGoodsItemModel> itemModels = new ArrayList<>();

    private static final int limit = 20;
    private int start_num = 0;

    private String userId;

    public ZuoPinRecyclerView(Context context,String userId) {
        super(context);
        this.userId = userId;
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

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.zuopin_recycler_view, this, true);

        emptyTips = (TextView) findViewById(R.id.emptyTips);
        refreshContainer = (PtrClassicFrameLayout) findViewById(R.id.refreshContainer);
        recyclerView = (AutoLoadMoreRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.getRecyclerView().setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.getRecyclerView().addItemDecoration(new DividerGridItemDecoration(getContext(), R.drawable.goods_list_divider));

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

    private void notifyDataChanged(){
        if(itemModels.size() > 0){
            refreshContainer.setVisibility(View.VISIBLE);
            emptyTips.setVisibility(View.GONE);
            artAdapter.notifyDataSetChanged();
        }else{
            refreshContainer.setVisibility(View.GONE);
            emptyTips.setVisibility(View.VISIBLE);
            emptyTips.setText("��û���ϴ���Ʒ��");
        }
    }

    public void refreshData() {
        start_num = 0;
        Map<String, String> params = new HashMap<>();
        params.put("limit", limit + "");
        params.put("start_num", (start_num * limit) + "");
        params.put("userid", userId);
        PrintHttpUrlUtil.printUrl(ServerConfig.BASE_URL + ServerConfig.MY_ZUOPIN, params);
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.MY_ZUOPIN)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        refreshContainer.refreshComplete();
                        ToastUtil.makeShortText("��������ʧ����");
                    }

                    @Override
                    public void onResponse(String response) {
                        refreshContainer.refreshComplete();
                        ArtGoodsListModel goodsListModel = GsonUtil.transModel(response, ArtGoodsListModel.class);
                        if (goodsListModel == null || !"1".equals(goodsListModel.result)) {
                            ToastUtil.makeShortText("��������ʧ����");
                            return;
                        }
                        if (goodsListModel.artlist != null) {
                            itemModels.clear();
                            itemModels.addAll(goodsListModel.artlist);
                            notifyDataChanged();
                            if (goodsListModel.artlist.size() < 20) {//û�и�����
                                recyclerView.hasMore(false);
                            } else {//Ҳ���и���
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
        params.put("userid", userId);
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.MY_ZUOPIN)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        start_num--;
                        recyclerView.loadMoreCompleted();
                        ToastUtil.makeShortText("��������ʧ����");
                    }

                    @Override
                    public void onResponse(String response) {
                        recyclerView.loadMoreCompleted();
                        ArtGoodsListModel goodsListModel = GsonUtil.transModel(response, ArtGoodsListModel.class);
                        if (goodsListModel == null || !"1".equals(goodsListModel.result)) {
                            start_num--;
                            ToastUtil.makeShortText("��������ʧ����");
                            return;
                        }
                        if (goodsListModel.artlist != null) {
                            itemModels.addAll(goodsListModel.artlist);
                            notifyDataChanged();
                            if (goodsListModel.artlist.size() < 20) {//û�и�����
                                recyclerView.hasMore(false);
                            } else {//Ҳ���и���
                                recyclerView.hasMore(true);
                            }
                        }else{
                            recyclerView.hasMore(false);
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
