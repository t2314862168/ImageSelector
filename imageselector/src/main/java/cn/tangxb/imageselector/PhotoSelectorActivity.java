package cn.tangxb.imageselector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.ArrayList;
import java.util.List;

import cn.tangxb.imageselector.adapter.AlbumAdapter;
import cn.tangxb.imageselector.adapter.PhotoSelectorAdapter;
import cn.tangxb.imageselector.domain.PhotoSelectorDomain;
import cn.tangxb.imageselector.listener.OnLocalAlbumListener;
import cn.tangxb.imageselector.listener.OnLocalRecentListener;
import cn.tangxb.imageselector.model.AlbumModel;
import cn.tangxb.imageselector.model.PhotoModel;
import cn.tangxb.imageselector.utils.AnimationUtil;
import cn.tangxb.imageselector.view.PhotoItemLayout;


public class PhotoSelectorActivity extends Activity implements
        PhotoItemLayout.onItemClickListener, PhotoItemLayout.onPhotoItemCheckedListener, OnItemClickListener,
        OnClickListener {
    public static final int SINGLE_IMAGE = 1;
    public static final String KEY_MAX = "key_max";
    private int MAX_IMAGE;

    public static final int REQUEST_PHOTO = 0;
    private static final int REQUEST_CAMERA = 1;

    public static String RECCENT_PHOTO = null;

    private GridView gvPhotos;
    private ListView lvAblum;
    private Button btnOk;
    private TextView tvAlbum, tvPreview, tvTitle;
    private PhotoSelectorDomain photoSelectorDomain;
    private PhotoSelectorAdapter photoAdapter;
    private AlbumAdapter albumAdapter;
    private RelativeLayout layoutAlbum;
    private ArrayList<PhotoModel> selected;
    private TextView tvNumber;

    private static final int PRELOAD_AHEAD_ITEMS = 5;
    private ViewPreloadSizeProvider<PhotoModel> preloadSizeProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photoselector);

        RECCENT_PHOTO = getResources().getString(R.string.recent_photos);
        photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());

        selected = new ArrayList<PhotoModel>();

        tvTitle = (TextView) findViewById(R.id.tv_title_lh);
        gvPhotos = (GridView) findViewById(R.id.gv_photos_ar);
        lvAblum = (ListView) findViewById(R.id.lv_ablum_ar);
        btnOk = (Button) findViewById(R.id.btn_right_lh);
        tvAlbum = (TextView) findViewById(R.id.tv_album_ar);
        tvPreview = (TextView) findViewById(R.id.tv_preview_ar);
        layoutAlbum = (RelativeLayout) findViewById(R.id.layout_album_ar);
        tvNumber = (TextView) findViewById(R.id.tv_number);

        btnOk.setOnClickListener(this);
        tvAlbum.setOnClickListener(this);
        tvPreview.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        photoAdapter = new PhotoSelectorAdapter(getApplicationContext(),
                new ArrayList<PhotoModel>(), dm.widthPixels, this, this, this);
        gvPhotos.setAdapter(photoAdapter);

        // use ListPreloader to observer list scroll
        preloadSizeProvider = new ViewPreloadSizeProvider<>();
        photoAdapter.setPreloadSizeProvider(preloadSizeProvider);
        ListPreloader<PhotoModel> preLoader = new ListPreloader<>(photoAdapter, preloadSizeProvider, PRELOAD_AHEAD_ITEMS);
        gvPhotos.setOnScrollListener(preLoader);

        albumAdapter = new AlbumAdapter(getApplicationContext(),
                new ArrayList<AlbumModel>());
        lvAblum.setAdapter(albumAdapter);
        lvAblum.setOnItemClickListener(this);

        findViewById(R.id.bv_back_lh).setOnClickListener(this); // 返回

        photoSelectorDomain.getReccent(recentListener); // 更新最近照片
        photoSelectorDomain.updateAlbum(albumListener); // 跟新相册信息
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_right_lh) {

        } else if (v.getId() == R.id.tv_album_ar) {
            album();
        } else if (v.getId() == R.id.tv_preview_ar) {
            preview();
        }
    }

    /**
     * 预览照片
     */
    private void preview() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedPhotos", selected);
        Intent intent = new Intent(PhotoSelectorActivity.this, PhotoPreviewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void album() {
        if (layoutAlbum.getVisibility() == View.GONE) {
            popAlbum();
        } else {
            hideAlbum();
        }
    }

    /**
     * 弹出相册列表
     */
    private void popAlbum() {
        layoutAlbum.setVisibility(View.VISIBLE);
        new AnimationUtil(getApplicationContext(), R.anim.translate_up_current)
                .setLinearInterpolator().startAnimation(layoutAlbum);
    }

    /**
     * 隐藏相册列表
     */
    private void hideAlbum() {
        new AnimationUtil(getApplicationContext(), R.anim.translate_down)
                .setLinearInterpolator().startAnimation(layoutAlbum);
        layoutAlbum.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (layoutAlbum.getVisibility() == View.VISIBLE) {
            hideAlbum();
        } else
            super.onBackPressed();
    }

    @Override
    /** 相册列表点击事件 */
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        AlbumModel current = (AlbumModel) parent.getItemAtPosition(position);
        for (int i = 0; i < parent.getCount(); i++) {
            AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
            if (i == position)
                album.setCheck(true);
            else
                album.setCheck(false);
        }
        albumAdapter.notifyDataSetChanged();
        hideAlbum();
        tvAlbum.setText(current.getName());
        tvTitle.setText(current.getName());

        // 更新照片列表
        if (current.getName().equals(RECCENT_PHOTO))
            photoSelectorDomain.getReccent(recentListener);
        else
            photoSelectorDomain.getAlbum(current.getName(), recentListener); // 获取选中相册的照片
    }

    @Override
    /** 照片选中状态改变之后 */
    public void onCheckedChanged(PhotoModel photoModel, CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!selected.contains(photoModel))
                selected.add(photoModel);
            tvPreview.setEnabled(true);
        } else {
            selected.remove(photoModel);
        }
        tvNumber.setText("(" + selected.size() + ")");

        if (selected.isEmpty()) {
            tvPreview.setEnabled(false);
            tvPreview.setText(getString(R.string.preview));
        }
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("position", position);
        bundle.putString("albumName", tvAlbum.getText().toString());
        Intent intent = new Intent(PhotoSelectorActivity.this, PhotoPreviewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private OnLocalAlbumListener albumListener = new OnLocalAlbumListener() {
        @Override
        public void onAlbumLoaded(List<AlbumModel> albums) {
            albumAdapter.update(albums);
        }
    };

    private OnLocalRecentListener recentListener = new OnLocalRecentListener() {
        @Override
        public void onPhotoLoaded(List<PhotoModel> photos) {
            for (PhotoModel model : photos) {
                if (selected.contains(model)) {
                    model.setChecked(true);
                }
            }
            photoAdapter.update(photos);
            gvPhotos.smoothScrollToPosition(0); // 滚动到顶端
            // reset(); //--keep selected photos

        }
    };
}
