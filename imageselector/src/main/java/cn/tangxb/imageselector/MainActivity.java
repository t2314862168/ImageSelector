package cn.tangxb.imageselector;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.tangxb.imageselector.model.AlbumModel;
import cn.tangxb.imageselector.model.PhotoModel;


public class MainActivity extends AppCompatActivity {
    TextView mTextView;
    LoaderManager mLoaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv);
//        XXXHelper.getInstance(this).setRetainedTextView(mTextView);
//        mLoaderManager = getSupportLoaderManager();
//        mLoaderManager.initLoader(1000, null, this);
        AlbumTask albumTask = new AlbumTask(this);
        albumTask.execute();
    }

    class AlbumTask extends AsyncTask<Void, Void, ArrayList<AlbumModel>> {
        private Context context;
        private ContentResolver resolver;

        public AlbumTask(Context context) {
            this.context = context;
            this.resolver = context.getContentResolver();
        }

        @Override
        protected ArrayList<AlbumModel> doInBackground(Void... params) {
            ArrayList<AlbumModel> albums = new ArrayList<AlbumModel>();
            Map<String, AlbumModel> map = new HashMap<String, AlbumModel>();
            Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.SIZE}, null, null, null);
            if (cursor == null || !cursor.moveToNext())
                return new ArrayList<AlbumModel>();
            cursor.moveToLast();
            String recentPhoto = context.getResources().getString(R.string.recent_photos);
            AlbumModel current = new AlbumModel(recentPhoto, 0, cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)), true); // "最近照片"相册
            albums.add(current);
            do {
                if (cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)) < 1024 * 10)
                    continue;

                current.increaseCount();
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                if (map.keySet().contains(name))
                    map.get(name).increaseCount();
                else {
                    AlbumModel album = new AlbumModel(name, 1, cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)));
                    map.put(name, album);
                    albums.add(album);
                }
            } while (cursor.moveToPrevious());
            cursor.close();
            return albums;
        }

        @Override
        protected void onPostExecute(ArrayList<AlbumModel> albumModels) {
            System.out.println();
        }
    }

    class PhotoTask extends AsyncTask<String, Void, ArrayList<PhotoModel>> {
        @Override
        protected ArrayList<PhotoModel> doInBackground(String... params) {
            String selection = null;
            String[] selectionArgs = null;
            if (params != null) {
                selection = "bucket_display_name = ?";
                selectionArgs = params;
            }
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_ADDED,
                            MediaStore.Images.ImageColumns.SIZE}, selection,
                    selectionArgs, MediaStore.Images.ImageColumns.DATE_ADDED);
            if (cursor == null || !cursor.moveToNext())
                return new ArrayList<PhotoModel>();
            ArrayList<PhotoModel> photos = new ArrayList<PhotoModel>();
            cursor.moveToLast();
            do {
                if (cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)) > 1024 * 10) {
                    PhotoModel photoModel = new PhotoModel();
                    photoModel.setOriginalPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)));
                    photos.add(photoModel);
                }
            } while (cursor.moveToPrevious());
            return photos;
        }

        @Override
        protected void onPostExecute(ArrayList<PhotoModel> data) {

        }
    }

//    @Override
//    public Loader<ArrayList<String>> onCreateLoader(int id, Bundle args) {
//        PhotoModelTaskLoader imageTaskLoader = new PhotoModelTaskLoader(this, Uri.parse("content://com.android.contacts/contacts"), null, null, null, null);
//        return imageTaskLoader;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> data) {
//        System.out.println();
//    }
//
//    @Override
//    public void onLoaderReset(Loader<ArrayList<String>> loader) {
//
//    }
}
