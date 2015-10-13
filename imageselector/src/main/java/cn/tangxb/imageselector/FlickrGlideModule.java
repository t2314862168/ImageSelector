package cn.tangxb.imageselector;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

import cn.tangxb.imageselector.model.PhotoModel;

/**
 * {@link GlideModule} for the Flickr sample app.
 */
public class FlickrGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Do nothing.
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(PhotoModel.class, InputStream.class, new FlickrModelLoader.Factory());
    }
}
