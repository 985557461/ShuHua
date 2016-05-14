package com.xy.shuhua.ui.home;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.home.model.ArtUserModel;

/**
 * Created by xiaoyu on 2016/3/24.
 */
public class AuthorItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private View rootView;
    private ImageView avatar;
    private TextView name;
    private TextView desc;

    private ArtUserModel artUserModel;

    public AuthorItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;
        avatar = (ImageView) rootView.findViewById(R.id.avatar);
        desc = (TextView) rootView.findViewById(R.id.desc);
        name = (TextView) rootView.findViewById(R.id.name);
        rootView.setOnClickListener(this);
    }

    public void setData(ArtUserModel artUserModel) {
        this.artUserModel = artUserModel;
        if (artUserModel == null) {
            return;
        }
        if (!TextUtils.isEmpty(artUserModel.imageurl)) {
            Glide.with(context).load(artUserModel.imageurl).into(avatar);
        } else {
            Glide.with(context).load("").into(avatar);
        }
        if (!TextUtils.isEmpty(artUserModel.realname)) {
            name.setText(artUserModel.realname);
        } else {
            if (!TextUtils.isEmpty(artUserModel.nickname)) {
                name.setText(artUserModel.nickname);
            } else {
                if (!TextUtils.isEmpty(artUserModel.username)) {
                    name.setText(artUserModel.username);
                } else {
                    name.setText("");
                }
            }
        }
        if (!TextUtils.isEmpty(artUserModel.introduce)) {
            desc.setText(artUserModel.introduce);
        } else {
            desc.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        String avatar = artUserModel.imageurl;
        String nameStr = name.getText().toString();
        ActivityAuthorHomePage.open((Activity) context, artUserModel.userid, avatar, nameStr);
    }
}
