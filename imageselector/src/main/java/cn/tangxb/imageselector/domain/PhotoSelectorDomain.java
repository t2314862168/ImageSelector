package cn.tangxb.imageselector.domain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.List;

import cn.tangxb.imageselector.controller.AlbumController;
import cn.tangxb.imageselector.listener.OnLocalAlbumListener;
import cn.tangxb.imageselector.listener.OnLocalRecentListener;
import cn.tangxb.imageselector.model.AlbumModel;
import cn.tangxb.imageselector.model.PhotoModel;


@SuppressLint("HandlerLeak")
public class PhotoSelectorDomain {

	private AlbumController albumController;

	public PhotoSelectorDomain(Context context) {
		albumController = new AlbumController(context);
	}

	public void getReccent(final OnLocalRecentListener listener) {
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				listener.onPhotoLoaded((List<PhotoModel>) msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<PhotoModel> photos = albumController.getCurrent();
				Message msg = new Message();
				msg.obj = photos;
				handler.sendMessage(msg);
			}
		}).start();
	}

	/** 获取相册列表 */
	public void updateAlbum(final OnLocalAlbumListener listener) {
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				listener.onAlbumLoaded((List<AlbumModel>) msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<AlbumModel> albums = albumController.getAlbums();
				Message msg = new Message();
				msg.obj = albums;
				handler.sendMessage(msg);
			}
		}).start();
	}

	/** 获取单个相册下的所有照片信息 */
	public void getAlbum(final String name, final OnLocalRecentListener listener) {
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				listener.onPhotoLoaded((List<PhotoModel>) msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<PhotoModel> photos = albumController.getAlbum(name);
				Message msg = new Message();
				msg.obj = photos;
				handler.sendMessage(msg);
			}
		}).start();
	}

}
