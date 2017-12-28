package com.multibitmap;

import android.support.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.InputStream;

/**
 * created by sfx on 2017/12/28.
 */

public class MultiBitmapFetcher implements DataFetcher<InputStream> {

    private final MultiBitmapUrl faceUrlSet;
    private final RequestManager requestManager;

    public MultiBitmapFetcher(MultiBitmapUrl faceUrlSet, RequestManager requestManager) {
        this.faceUrlSet = faceUrlSet;
        this.requestManager = requestManager;
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        try {
            callback.onDataReady(faceUrlSet.getFaceInputStream(requestManager));
        } catch (Exception e) {
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void cancel() {

    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
