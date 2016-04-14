package com.xy.shuhua.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.common.ActivityBaseNoSliding;
import com.xy.shuhua.util.SharedPreferenceUtil;
import com.xy.shuhua.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu on 2016/4/13.
 */
public class ActivitySearch extends ActivityBaseNoSliding implements View.OnClickListener {
    private EditText searchEditText;
    private TextView cancelTV;
    private TextView fuguTV;
    private TextView zihuaTV;
    private TextView yishuTV;
    private View historyLL;
    private LinearLayout historyContainer;
    private TextView clearHistoryTV;

    /**
     * searchName*
     */
    private static final String SEARCH_STRINGS = "SEARCH_STRINGS";
    private static final String SEARCH_HISTORY = "SEARCH_HISTORY";

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivitySearch.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void getViews() {
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        cancelTV = (TextView) findViewById(R.id.cancelTV);
        fuguTV = (TextView) findViewById(R.id.fuguTV);
        zihuaTV = (TextView) findViewById(R.id.zihuaTV);
        yishuTV = (TextView) findViewById(R.id.yishuTV);
        historyLL = findViewById(R.id.historyLL);
        historyContainer = (LinearLayout) findViewById(R.id.historyContainer);
        clearHistoryTV = (TextView) findViewById(R.id.clearHistoryTV);
    }

    @Override
    protected void initViews() {
        String searchStr = SharedPreferenceUtil.getStringValueFromSP(SEARCH_STRINGS, SEARCH_HISTORY);
        if (!TextUtils.isEmpty(searchStr)) {
            String[] array = searchStr.split(",");
            for (int i = 0; i < array.length; i++) {
                SearchHistoryItemView itemView = new SearchHistoryItemView(this);
                itemView.setData(array[i]);
                historyContainer.addView(itemView);
            }
        }
    }

    private void tryToAddOne(String str) {
        String searchStr = SharedPreferenceUtil.getStringValueFromSP(SEARCH_STRINGS, SEARCH_HISTORY);
        if (!TextUtils.isEmpty(searchStr)) {
            String[] array = searchStr.split(",");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                list.add(array[i]);
            }
            if (list.contains(str)) {
                return;
            }
            list.add(str);
            if (list.size() > 4) {
                list = list.subList(list.size() - 4, list.size());
            }
            historyContainer.removeAllViews();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                SearchHistoryItemView itemView = new SearchHistoryItemView(this);
                itemView.setData(list.get(i));
                historyContainer.addView(itemView);
                stringBuilder.append(list.get(i));
                if (i != list.size() - 1) {
                    stringBuilder.append(",");
                }
            }
            SharedPreferenceUtil.setStringDataIntoSP(SEARCH_STRINGS, SEARCH_HISTORY, stringBuilder.toString());
        } else {
            SearchHistoryItemView itemView = new SearchHistoryItemView(this);
            itemView.setData(str);
            historyContainer.addView(itemView);
            SharedPreferenceUtil.setStringDataIntoSP(SEARCH_STRINGS, SEARCH_HISTORY, str);
        }
    }

    @Override
    protected void setListeners() {
        cancelTV.setOnClickListener(this);
        fuguTV.setOnClickListener(this);
        zihuaTV.setOnClickListener(this);
        yishuTV.setOnClickListener(this);
        clearHistoryTV.setOnClickListener(this);

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    tryToSearch();
                }
                return true;
            }
        });
    }

    private void tryToSearch() {
        String searchStr = searchEditText.getText().toString();
        if (TextUtils.isEmpty(searchStr)) {
            ToastUtil.makeShortText("ÇëÊäÈëËÑË÷ÄÚÈÝ");
            return;
        }
        ActivitySearchResult.open(this, searchStr);
        tryToAddOne(searchStr);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelTV:
                finish();
                break;
            case R.id.fuguTV:
                break;
            case R.id.zihuaTV:
                break;
            case R.id.yishuTV:
                break;
            case R.id.clearHistoryTV:
                clearHistory();
                break;
        }
    }

    private void clearHistory() {
        historyContainer.removeAllViews();
        SharedPreferenceUtil.deleteAllInSP(SEARCH_STRINGS);
    }
}
