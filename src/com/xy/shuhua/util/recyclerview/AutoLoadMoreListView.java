package com.xy.shuhua.util.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.xy.shuhua.R;

/**
 * Created by liangyu on 2016/3/28.
 */
public class AutoLoadMoreListView extends ListView implements ListView.OnScrollListener {
    private LoadMoreInterface loadMoreInterface;
    private int lastItem;
    private View footView;
    private boolean hasMore = false;
    private boolean isLoadingMore = false;

    public AutoLoadMoreListView(Context context) {
        super(context);
        init(context);
    }

    public AutoLoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoLoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footView = inflater.inflate(R.layout.load_more_listview_footer, null);
        addFooterView(footView);
        footView.setVisibility(GONE);
        setOnScrollListener(this);
    }

    public void setLoadMoreInterface(LoadMoreInterface loadMoreInterface) {
        this.loadMoreInterface = loadMoreInterface;
    }

    public void loadMoreCompleted() {
        isLoadingMore = false;
        footView.setVisibility(View.GONE);
    }

    public void hasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        ListAdapter adapter = getAdapter();
        if (adapter != null && lastItem == adapter.getCount() && hasMore && !isLoadingMore && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (loadMoreInterface != null) {
                footView.setVisibility(View.VISIBLE);
                loadMoreInterface.loadMore();
                isLoadingMore = true;
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItem = firstVisibleItem + visibleItemCount - 1;
    }
}
