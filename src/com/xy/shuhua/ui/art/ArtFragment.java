package com.xy.shuhua.ui.art;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xy.shuhua.R;

/**
 * Created by xiaoyu on 2016/3/29.
 */
public class ArtFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.art_fragment, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
    }

    @Override
    public void onClick(View view) {
    }
}
