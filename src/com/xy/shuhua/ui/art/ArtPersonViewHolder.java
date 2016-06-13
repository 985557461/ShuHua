package com.xy.shuhua.ui.art;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.art.model.ArtGoodsItemModel;
import com.xy.shuhua.ui.art.model.AuthorModelWithZuoPin;
import com.xy.shuhua.ui.home.ActivityAuthorHomePage;

/**
 * Created by xiaoyu on 2016/3/31.
 */
public class ArtPersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private View rootView;
    private ImageView avatarIV;
    private TextView desc;
    private TextView authorName;
    private ImageView imageOne;
    private ImageView imageTwo;
    private ImageView imageThree;

    public AuthorModelWithZuoPin.Data itemModel;
    public int size =0;

    public ArtPersonViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;
        avatarIV = (ImageView) rootView.findViewById(R.id.avatarIV);
        desc = (TextView) rootView.findViewById(R.id.desc);
        authorName = (TextView) rootView.findViewById(R.id.authorName);
        imageOne = (ImageView) rootView.findViewById(R.id.imageOne);
        imageTwo = (ImageView) rootView.findViewById(R.id.imageTwo);
        imageThree = (ImageView) rootView.findViewById(R.id.imageThree);

        imageOne.setOnClickListener(this);
        imageTwo.setOnClickListener(this);
        imageThree.setOnClickListener(this);
        rootView.setOnClickListener(this);
    }

    public void setData(AuthorModelWithZuoPin.Data itemModel) {
        this.itemModel = itemModel;
        if (itemModel == null) {
            return;
        }
        if (!TextUtils.isEmpty(itemModel.imageurl)) {
            Glide.with(context).load(itemModel.imageurl).placeholder(R.drawable.me_avatar_boy).error(R.drawable.me_avatar_boy).into(avatarIV);
        } else {
            Glide.with(context).load(R.drawable.me_avatar_boy).into(avatarIV);
        }
        if (!TextUtils.isEmpty(itemModel.realname)) {
            authorName.setText(itemModel.realname);
        } else {
            authorName.setText("");
        }
        if (!TextUtils.isEmpty(itemModel.introduce)) {
            desc.setText(itemModel.introduce);
        } else {
            desc.setText("");
        }
        size = itemModel.artlist == null ? 0 : itemModel.artlist.size();
        if(size == 0){
            imageOne.setVisibility(View.GONE);
            imageTwo.setVisibility(View.GONE);
            imageThree.setVisibility(View.GONE);
        }else{
            if(size >= 3){
                imageOne.setVisibility(View.VISIBLE);
                imageTwo.setVisibility(View.VISIBLE);
                imageThree.setVisibility(View.VISIBLE);

                ArtGoodsItemModel itemModel1 = itemModel.artlist.get(0);
                ArtGoodsItemModel itemModel2 = itemModel.artlist.get(1);
                ArtGoodsItemModel itemModel3 = itemModel.artlist.get(2);

                if (!TextUtils.isEmpty(itemModel1.imageurl)) {
                    Glide.with(context).load(itemModel1.imageurl).dontAnimate().placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(imageOne);
                } else {
                    Glide.with(context).load(R.drawable.me_avatar_boy).into(imageOne);
                }

                if (!TextUtils.isEmpty(itemModel2.imageurl)) {
                    Glide.with(context).load(itemModel2.imageurl).dontAnimate().placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(imageTwo);
                } else {
                    Glide.with(context).load(R.drawable.me_avatar_boy).into(imageOne);
                }

                if (!TextUtils.isEmpty(itemModel3.imageurl)) {
                    Glide.with(context).load(itemModel3.imageurl).dontAnimate().placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(imageThree);
                } else {
                    Glide.with(context).load(R.drawable.me_avatar_boy).into(imageOne);
                }
            }else if(size >= 2){
                imageOne.setVisibility(View.VISIBLE);
                imageTwo.setVisibility(View.VISIBLE);
                imageThree.setVisibility(View.INVISIBLE);

                ArtGoodsItemModel itemModel1 = itemModel.artlist.get(0);
                ArtGoodsItemModel itemModel2 = itemModel.artlist.get(1);

                if (!TextUtils.isEmpty(itemModel1.imageurl)) {
                    Glide.with(context).load(itemModel1.imageurl).dontAnimate().placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(imageOne);
                } else {
                    Glide.with(context).load(R.drawable.me_avatar_boy).into(imageOne);
                }

                if (!TextUtils.isEmpty(itemModel2.imageurl)) {
                    Glide.with(context).load(itemModel2.imageurl).dontAnimate().placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(imageTwo);
                } else {
                    Glide.with(context).load(R.drawable.me_avatar_boy).into(imageOne);
                }

            }else if(size >= 1){
                imageOne.setVisibility(View.VISIBLE);
                imageTwo.setVisibility(View.INVISIBLE);
                imageThree.setVisibility(View.INVISIBLE);

                ArtGoodsItemModel itemModel1 = itemModel.artlist.get(0);

                if (!TextUtils.isEmpty(itemModel1.imageurl)) {
                    Glide.with(context).load(itemModel1.imageurl).dontAnimate().placeholder(R.drawable.icon_art_pressed).error(R.drawable.icon_art_pressed).into(imageOne);
                } else {
                    Glide.with(context).load(R.drawable.me_avatar_boy).into(imageOne);
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageOne:
                if(size !=0){
                    ActivityArtGoodsInfo.open((Activity) context,itemModel.artlist.get(0).id);
                }
                break;
            case R.id.imageTwo:
                if(size !=0){
                    ActivityArtGoodsInfo.open((Activity) context,itemModel.artlist.get(1).id);
                }
                break;
            case R.id.imageThree:
                if(size !=0){
                    ActivityArtGoodsInfo.open((Activity) context,itemModel.artlist.get(2).id);
                }
                break;
            default:
                ActivityAuthorHomePage.open((Activity) context,itemModel.userid,itemModel.imageurl,itemModel.realname,itemModel.introduce);
                break;
        }
    }
}
