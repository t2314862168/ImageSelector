package cn.tangxb.imageselector.adapter;
/**
 * 
 * @author Aizaz AZ
 *
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.tangxb.imageselector.model.AlbumModel;
import cn.tangxb.imageselector.view.AlbumItemLayout;


public class AlbumAdapter extends MBaseAdapter<AlbumModel> {

	public AlbumAdapter(Context context, ArrayList<AlbumModel> models) {
		super(context, models);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AlbumItemLayout albumItem = null;
		if (convertView == null) {
			albumItem = new AlbumItemLayout(context);
			convertView = albumItem;
		} else
			albumItem = (AlbumItemLayout) convertView;
		albumItem.update(models.get(position));
		return convertView;
	}

}
