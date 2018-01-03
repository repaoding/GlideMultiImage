package com.multibitmap;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

@GlideModule
public class MultiGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
                                   @NonNull Registry registry) {
        //缓存大小为10M
        final int cacheSize = 5 * 1024 * 1024;
        //创建缓存对象
        final Cache cache = new Cache(getDiskCacheDir(context, "http"), cacheSize);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(cache);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client.build()));
        registry.append(MultiBitmapUrl.class, InputStream.class, new MultiBitmapLoader.Factory(context));
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    static class CacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originResponse = chain.proceed(chain.request());
            //设置缓存时间为，并移除了pragma消息头，移除它的原因是因为pragma也是控制缓存的一个消息头属性
            return originResponse.newBuilder().removeHeader("pragma")
                    .header("Cache-Control", "max-age=10")//设置10秒
                    .header("Cache-Control", "max-stale=30").build();
        }
    }

    private static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            final File cacheFile = context.getExternalCacheDir();
            if (cacheFile != null) {
                cachePath = cacheFile.getPath();
            }
        }
        if (TextUtils.isEmpty(cachePath)) {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
}
