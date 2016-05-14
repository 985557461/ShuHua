package com.xy.shuhua.ui.art;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xy.shuhua.R;
import com.xy.shuhua.common_background.ServerConfig;
import com.xy.shuhua.ui.art.model.HomeArtItemModel;
import com.xy.shuhua.ui.art.model.HomeArtListModel;
import com.xy.shuhua.util.GsonUtil;
import com.xy.shuhua.util.ToastUtil;
import com.xy.shuhua.util.okhttp.OkHttpUtils;
import com.xy.shuhua.util.okhttp.callback.StringCallback;
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
 * Created by xiaoyu on 2016/3/29.
 */
public class ArtFragment extends Fragment implements View.OnClickListener {
    private PtrClassicFrameLayout refreshContainer;
    private RecyclerView recyclerView;
    private ArtAdapter artAdapter;
    private List<HomeArtItemModel> homeArtItemModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.art_fragment, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        refreshContainer = (PtrClassicFrameLayout) view.findViewById(R.id.refreshContainer);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));

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

        artAdapter = new ArtAdapter(getContext());
        recyclerView.setAdapter(artAdapter);
    }

    private void refreshData() {
        Map<String, String> params = new HashMap<>();
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL + ServerConfig.GET_MOUREN)
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
                        HomeArtListModel homeArtListModel = GsonUtil.transModel(response, HomeArtListModel.class);
                        if (homeArtListModel == null || !"1".equals(homeArtListModel.result)) {
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        if (homeArtListModel.list != null) {
                            homeArtItemModels.clear();
                            homeArtItemModels.addAll(homeArtListModel.list);
                            artAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
    }

    private class ArtAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private LayoutInflater inflater;

        public ArtAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.art_item_view, viewGroup, false);
            ArtItemViewHolder viewHolder = new ArtItemViewHolder(context, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ((ArtItemViewHolder)viewHolder).setData(homeArtItemModels.get(i));
        }

        @Override
        public int getItemCount() {
            return homeArtItemModels.size();
        }
    }
}
