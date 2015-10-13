package cn.tangxb.imageselector.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.ArrayList;
import java.util.List;

import cn.tangxb.imageselector.R;
import cn.tangxb.imageselector.model.PhotoModel;
import cn.tangxb.imageselector.view.PhotoItemLayout;


/**
 * @author Aizaz AZ
 */


public class PhotoSelectorAdapter extends MBaseAdapter<PhotoModel> implements ListPreloader.PreloadModelProvider<PhotoModel> {

    private int itemWidth;
    private int horizentalNum = 3;
    private PhotoItemLayout.onPhotoItemCheckedListener listener;
    private LayoutParams itemLayoutParams;
    private PhotoItemLayout.onItemClickListener mCallback;
    private OnClickListener cameraListener;
    private ViewPreloadSizeProvider<PhotoModel> preloadSizeProvider;
    private DrawableRequestBuilder<PhotoModel> mGlideBuilder;

    private PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models) {
        super(context, models);
    }

    public PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models, int screenWidth, PhotoItemLayout.onPhotoItemCheckedListener listener, PhotoItemLayout.onItemClickListener mCallback,
                                OnClickListener cameraListener) {
        this(context, models);
        setItemWidth(screenWidth);
        this.listener = listener;
        this.mCallback = mCallback;
        this.cameraListener = cameraListener;

        mGlideBuilder = Glide.with(context).from(PhotoModel.class)
                .placeholder(R.drawable.bg_loading)
                .error(R.drawable.bg_load_failed)
                .crossFade()
                .centerCrop()
                .dontAnimate();
    }

    public void setPreloadSizeProvider(ViewPreloadSizeProvider<PhotoModel> preloadSizeProvider) {
        this.preloadSizeProvider = preloadSizeProvider;
    }

    /**
     * 设置每一个Item的宽高
     */
    public void setItemWidth(int screenWidth) {
        int horizentalSpace = context.getResources().getDimensionPixelSize(R.dimen.sticky_item_horizontalSpacing);
        this.itemWidth = (screenWidth - (horizentalSpace * (horizentalNum - 1))) / horizentalNum;
        this.itemLayoutParams = new LayoutParams(itemWidth, itemWidth);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoItemLayout item = null;
        if (convertView == null || !(convertView instanceof PhotoItemLayout)) {
            item = new PhotoItemLayout(context, listener);
            item.setLayoutParams(itemLayoutParams);
            ImageView imageView = (ImageView) item.findViewById(R.id.iv_photo_lpsi);
            preloadSizeProvider.setView(imageView);
            convertView = item;
        } else {
            item = (PhotoItemLayout) convertView;
        }
        item.setImageDrawable(mGlideBuilder, models.get(position));
        item.setSelected(models.get(position).isChecked());
        item.setOnClickListener(mCallback, position);
        return convertView;
    }

    @Override
    public List<PhotoModel> getPreloadItems(int position) {
        return models.subList(position, position + 1);
    }

    @Override
    public GenericRequestBuilder getPreloadRequestBuilder(PhotoModel model) {
        return mGlideBuilder;
    }
}
