package cn.tangxb.imageselector.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.tangxb.imageselector.R;
import cn.tangxb.imageselector.model.PhotoModel;
import cn.tangxb.imageselector.utils.GlideLoader;


/**
 * @author Aizaz AZ
 *
 */

public class PhotoItemLayout extends LinearLayout implements OnCheckedChangeListener,
		View.OnClickListener {

	private ImageView ivPhoto;
	private CheckBox cbPhoto;
	private onPhotoItemCheckedListener listener;
	private PhotoModel photo;
	private boolean isCheckAll;
	private onItemClickListener l;
	private int position;	

	private PhotoItemLayout(Context context) {
		super(context);
	}

	public PhotoItemLayout(Context context, onPhotoItemCheckedListener listener) {
		this(context);
		LayoutInflater.from(context).inflate(R.layout.layout_photoitem, this,
				true);
		this.listener = listener;

		setOnClickListener(this);

		ivPhoto = (ImageView) findViewById(R.id.iv_photo_lpsi);
		cbPhoto = (CheckBox) findViewById(R.id.cb_photo_lpsi);

		cbPhoto.setOnCheckedChangeListener(this); // CheckBox选中状态改变监听器
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (!isCheckAll) {
			listener.onCheckedChanged(photo, buttonView, isChecked); // 调用主界面回调函数
		}
		// 让图片变暗或者变亮
		if (isChecked) {
			setDrawingable();
			ivPhoto.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
		} else {
			ivPhoto.clearColorFilter();
		}
		photo.setChecked(isChecked);
	}

	/** 设置路径下的图片对应的缩略图 */
	public void setImageDrawable(final PhotoModel photo) {
		this.photo = photo;
		String url = "file://" + photo.getOriginalPath();
		GlideLoader.loadImg(getContext(), ivPhoto, url);
	}

	private void setDrawingable() {
		ivPhoto.setDrawingCacheEnabled(true);
		ivPhoto.buildDrawingCache();
	}

	@Override
	public void setSelected(boolean selected) {
		if (photo == null) {
			return;
		}
		isCheckAll = true;
		cbPhoto.setChecked(selected);
		isCheckAll = false;
	}

	public void setOnClickListener(onItemClickListener l, int position) {
		this.l = l;
		this.position = position;
	}

	/** 图片Item选中事件监听器 */
	public static interface onPhotoItemCheckedListener {
		public void onCheckedChanged(PhotoModel photoModel,
									 CompoundButton buttonView, boolean isChecked);
	}

	/** 图片点击事件 */
	public interface onItemClickListener {
		public void onItemClick(int position);
	}

	@Override
	public void onClick(View v) {
		if (l != null)
			l.onItemClick(position);
	}
}
