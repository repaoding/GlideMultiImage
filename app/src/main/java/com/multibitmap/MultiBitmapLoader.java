package com.multibitmap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

/**
 * created by sfx on 2017/12/28.
 */

public class MultiBitmapLoader implements ModelLoader<MultiBitmapUrl, InputStream> {
    @Nullable
    private final ModelCache<MultiBitmapUrl, MultiBitmapUrl> modelCache;
    private final Context context;

    MultiBitmapLoader(Context requestManager) {
        this(null, requestManager);
    }

    MultiBitmapLoader(ModelCache<MultiBitmapUrl, MultiBitmapUrl> modelCache, Context context) {
        this.modelCache = modelCache;
        this.context = context;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(MultiBitmapUrl model, int width, int height, Options options) {
        MultiBitmapUrl faceUrlSet = model;
        if (modelCache != null) {
            faceUrlSet = modelCache.get(model, 0, 0);
            if (faceUrlSet == null) {
                modelCache.put(model, 0, 0, model);
                faceUrlSet = model;
            }
        }

        return new LoadData<>(faceUrlSet, new MultiBitmapFetcher(faceUrlSet, Glide.with(context)));
    }

    @Override
    public boolean handles(MultiBitmapUrl FaceUrlSet) {
        return true;
    }

    /**
     * The default factory for {@link MultiBitmapLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<MultiBitmapUrl, InputStream> {
        private final ModelCache<MultiBitmapUrl, MultiBitmapUrl> modelCache = new ModelCache<>(500);
        private final Context context;

        public Factory(Context context) {
            this.context = context.getApplicationContext();
        }

        @NonNull
        @Override
        public ModelLoader<MultiBitmapUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new MultiBitmapLoader(null, context);
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }
}
