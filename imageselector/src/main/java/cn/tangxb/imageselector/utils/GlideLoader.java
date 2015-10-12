package cn.tangxb.imageselector.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.tangxb.imageselector.R;

public class GlideLoader {
	public static void loadImg(Context context, ImageView imageView, String url) {
		Glide.with(context).load(url).placeholder(R.drawable.bg_loading)
				.error(R.drawable.bg_load_failed)
				.crossFade()
				.centerCrop()
				.dontAnimate()
				.into(imageView);
	}

	public static void loadImg(Activity activity, ImageView imageView,
			String url) {
		Glide.with(activity).load(url).placeholder(R.drawable.bg_loading)
				.error(R.drawable.bg_load_failed).crossFade().into(imageView);
	}

}
