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

import java.util.ArrayList;
import java.util.List;

import cn.tangxb.imageselector.domain.PhotoSelectorDomain;
import cn.tangxb.imageselector.listener.OnLocalReccentListener;
import cn.tangxb.imageselector.model.PhotoModel;
import cn.tangxb.imageselector.utils.GlideLoader;

/**
 * Created by Administrator on 2015/10/11.
 */
public class PhotoPreviewActivity extends Activity {
    ViewPager mViewPager;
    PhotoPreviewAdapter previewAdapter;
    int position;
    ArrayList<PhotoModel> photoModelList;
    PhotoSelectorDomain photoSelectorDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopreview);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());
        photoSelectorDomain.getReccent(recentListener); // 更新最近照片
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position", 0);
        photoModelList = new ArrayList<>();
        previewAdapter = new PhotoPreviewAdapter();
        mViewPager.setAdapter(previewAdapter);
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
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            String imageUrl = "file://" + photoModelList.get(position).getOriginalPath();
            GlideLoader.loadImg(PhotoPreviewActivity.this, imageView, imageUrl);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            frameLayout.addView(imageView, params);
            container.addView(frameLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return frameLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private OnLocalReccentListener recentListener = new OnLocalReccentListener() {
        @Override
        public void onPhotoLoaded(List<PhotoModel> photos) {
            photoModelList.clear();
            for (PhotoModel model : photos) {
                photoModelList.add(model);
            }
            previewAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(30, false);
        }
    };
}
