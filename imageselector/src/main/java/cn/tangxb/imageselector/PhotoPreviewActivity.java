package cn.tangxb.imageselector;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.tangxb.imageselector.domain.PhotoSelectorDomain;
import cn.tangxb.imageselector.listener.OnLocalRecentListener;
import cn.tangxb.imageselector.model.PhotoModel;
import cn.tangxb.imageselector.utils.GlideLoader;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2015/10/11.
 * 有一个不是bug的bug(ViewPager的setAdapter会直接调用instantiateItem:里面的position会从0到1,
 * 传递过来的position在调用 setCurrentItem 会导致调用destroyItem，从而会导致PhotoViewAttacher在0,1的时候的getImageView会返回null,
 * 从而出现ImageView no longer exists. You should not use this PhotoViewAttacher any more)
 */
public class PhotoPreviewActivity extends Activity {
    ViewPager mViewPager;
    PhotoPreviewAdapter previewAdapter;
    int position;
    ArrayList<PhotoModel> photoModelList;
    PhotoSelectorDomain photoSelectorDomain;
    String albumName;
    DrawableRequestBuilder<PhotoModel> mGlideBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopreview);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        photoModelList = new ArrayList<>();
        previewAdapter = new PhotoPreviewAdapter();
        mViewPager.setAdapter(previewAdapter);
        mGlideBuilder = Glide.with(this).from(PhotoModel.class)
                .placeholder(R.drawable.bg_loading)
                .error(R.drawable.bg_load_failed)
                .crossFade()
                .centerCrop()
                .dontAnimate();

        photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position", 0);
        albumName = bundle.getString("albumName");
        if (albumName != null) {
            if (albumName.equals(getResources().getString(R.string.recent_photos))) {
                photoSelectorDomain.getReccent(recentListener); // 更新最近照片
            } else {
                photoSelectorDomain.getAlbum(albumName, recentListener); // 获取选中相册的照片
            }
        } else {
            photoModelList = (ArrayList<PhotoModel>) bundle.getSerializable("selectedPhotos");
            previewAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(position);
        }
    }

    class PhotoPreviewAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return photoModelList != null ? photoModelList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FrameLayout frameLayout = new FrameLayout(container.getContext());
            frameLayout.setBackgroundColor(Color.BLACK);
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            // use url to load
            String imageUrl = "file://" + photoModelList.get(position).getOriginalPath();
            GlideLoader.loadImg(PhotoPreviewActivity.this, photoView, imageUrl);
            // use custom ModelLoader to load
//            mGlideBuilder.load(photoModelList.get(position)).into(photoView);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            frameLayout.addView(photoView, params);
            container.addView(frameLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return frameLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private OnLocalRecentListener recentListener = new OnLocalRecentListener() {
        @Override
        public void onPhotoLoaded(List<PhotoModel> photos) {
            photoModelList.clear();
            for (PhotoModel model : photos) {
                photoModelList.add(model);
            }
            previewAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(position, false);
        }
    };
}
