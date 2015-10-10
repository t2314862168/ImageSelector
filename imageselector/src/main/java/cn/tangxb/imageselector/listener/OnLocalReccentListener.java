package cn.tangxb.imageselector.listener;

import java.util.List;

import cn.tangxb.imageselector.model.PhotoModel;


public interface OnLocalReccentListener {
	public void onPhotoLoaded(List<PhotoModel> photos);
}
