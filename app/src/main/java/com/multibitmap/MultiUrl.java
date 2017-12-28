package com.multibitmap;


import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;

import java.security.MessageDigest;

/**
 * created by sfx on 2017/12/27.
 */

public class MultiUrl implements Key {
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    private String[] urls;
    @Nullable
    private volatile byte[] cacheKeyBytes;
    private int hashCode;
    @Nullable
    private String[] safeUrls;

    public MultiUrl(String... urls) {
        this.urls = urls;

    }

    public int size() {
        return urls == null ? 0 : urls.length;
    }

    public String getSafeStringUrl(int i) {
        if (i >= size()) return null;
        if (i < 0) return null;
        if (safeUrls == null) {
            this.safeUrls = new String[size()];
        }
        if (TextUtils.isEmpty(safeUrls[i])) {
            // customer base url
            final String url = urls[i];
            String unsafeStringUrl = Preconditions.checkNotEmpty(url);
            if (TextUtils.isEmpty(unsafeStringUrl)) {
                unsafeStringUrl = url;
            }
            safeUrls[i] = Uri.encode(unsafeStringUrl, ALLOWED_URI_CHARS);
        }
        return safeUrls[i];
    }

    public String getCacheKey() {
        if (urls == null) return null;
        int size = urls.length;
        StringBuilder builder = new StringBuilder();
        String temp;
        for (int i = 0; i < size; i++) {
            temp = urls[i];
            builder.append(i).append(TextUtils.isEmpty(temp) ? "" : temp);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return getCacheKey();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(getCacheKeyBytes());
    }

    private byte[] getCacheKeyBytes() {
        if (cacheKeyBytes == null) {
            cacheKeyBytes = getCacheKey().getBytes(CHARSET);
        }
        return cacheKeyBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MultiUrl) {
            MultiUrl other = (MultiUrl) o;
            return getCacheKey().equals(other.getCacheKey());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = getCacheKey().hashCode();
            hashCode = 31 * hashCode;
        }
        return hashCode;
    }
}
