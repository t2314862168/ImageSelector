package cn.tangxb.imageselector;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.File;
import java.io.InputStream;

import cn.tangxb.imageselector.model.PhotoModel;

/**
 * An implementation of ModelStreamLoader that leverages the StreamOpener class and the ExecutorService backing the
 * Engine to download the image and resize it in memory before saving the resized version
 * directly to the disk cache.
 */
public class FlickrModelLoader implements StreamModelLoader<PhotoModel> {
    private final ModelLoader<File, InputStream> loader;

    public FlickrModelLoader(Context context, ModelLoader<File, InputStream> loader) {
        this.loader = loader;
    }

    protected String getFilePath(PhotoModel model) {
        return model.getOriginalPath();
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(PhotoModel model, int width, int height) {
        return loader.getResourceFetcher(new File(getFilePath(model)), width, height);
    }

    public static class Factory implements ModelLoaderFactory<PhotoModel, InputStream> {
        @Override
        public ModelLoader<PhotoModel, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new FlickrModelLoader(context, factories.buildModelLoader(File.class, InputStream.class));
        }

        @Override
        public void teardown() {

        }
    }
}
