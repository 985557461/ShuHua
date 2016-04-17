package com.xy.shuhua.ui.home.photo_choose;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.xy.shuhua.R;
import com.xy.shuhua.ui.view.FixWidthFrameLayout;

import java.io.File;

/**
 * Created by xiaoyu on 15-11-30.
 */
public class SquarePhotoView extends FixWidthFrameLayout {
	private ImageView imageView;
	private ImageView delete;

	private String path;

	/**
	 * 删除按钮监听*
	 */
	private DeleteListener deleteListener;

	public interface DeleteListener {
		public void onDeleteClicked(String path);
	}

	public SquarePhotoView(Context context) {
		super(context);
		init(context);
	}

	public SquarePhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SquarePhotoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.square_photo_view, this, true);
		imageView = (ImageView) findViewById(R.id.imageView);
		delete = (ImageView) findViewById(R.id.delete);

		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (deleteListener != null) {
					deleteListener.onDeleteClicked(path);
				}
			}
		});
	}

	public void setDeleteListener(DeleteListener listener) {
		this.deleteListener = listener;
	}

	public void setData(String path) {
		this.path = path;
		Glide.with(getContext()).load(new File(path)).into(imageView);
	}
}
