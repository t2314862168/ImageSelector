package cn.tangxb.imageselector.listener;


import java.util.List;

import cn.tangxb.imageselector.model.AlbumModel;

public interface OnLocalAlbumListener {
	public void onAlbumLoaded(List<AlbumModel> albums);
}
