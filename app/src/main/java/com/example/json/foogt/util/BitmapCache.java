package com.example.json.foogt.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Mzz on 2016/1/5.
 */
public class BitmapCache implements ImageLoader.ImageCache {
    public static BitmapCache mInstance;
    static {
        mInstance = new BitmapCache();
    }

    public static BitmapCache getInstance(){
        return mInstance;
    }

    private LruCache<String, Bitmap> mCache;

    private BitmapCache() {
        int maxSize = 10 * 1024 * 1024;
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }

    public void clear() {
        mCache.evictAll();
    }

}