package com.xy.shuhua.ui.message;

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
import com.xy.shuhua.ui.art.ArtItemViewHolder;
import com.xy.shuhua.util.ultra_pull_refresh.PtrClassicFrameLayout;
import com.xy.shuhua.util.ultra_pull_refresh.PtrDefaultHandler;
import com.xy.shuhua.util.ultra_pull_refresh.PtrFrameLayout;
import com.xy.shuhua.util.ultra_pull_refresh.PtrHandler;

/**
 * Created by xiaoyu on 2016/3/29.
 */
public class MessageFragment extends Fragment implements View.OnClickListener{

    private PtrClassicFrameLayout refreshContainer;
    private RecyclerView recyclerView;
    private ArtAdapter artAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment, null);
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
                refreshContainer.refreshComplete();
            }
        });
        refreshContainer.autoRefresh();

        artAdapter = new ArtAdapter(getContext());
        recyclerView.setAdapter(artAdapter);
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
            View view = inflater.inflate(R.layout.message_item_view, viewGroup, false);
            MessageViewHolder viewHolder = new MessageViewHolder(context, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}
