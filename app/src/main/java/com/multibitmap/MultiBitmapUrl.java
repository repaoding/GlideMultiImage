package com.multibitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * created by sfx on 2017/12/28.
 */

public class MultiBitmapUrl extends MultiUrl {

    private final static int parentWidth = 192;//父组件宽
    private final static int parentHeight = 192;//父组件高
    private final static int mGap = 8; //宫格间距
    private volatile static RequestOptions options;

    private final Drawable errorDrawable;

    public MultiBitmapUrl(Drawable errorDrawable, String... urls) {
        super(urls);
        this.errorDrawable = errorDrawable;
    }

    public InputStream getFaceInputStream(RequestManager requestManager) throws ExecutionException, InterruptedException, IOException {
        if (requestManager == null) return null;
        final Bitmap bitmap = multiBitmap(requestManager);
        final InputStream inputStream = convertBitmapToInputStream(bitmap);
        bitmap.recycle();
        return inputStream;
    }


    private static Bitmap loadBitmap(RequestManager requestManager, String url) {
        try {
            if (TextUtils.isEmpty(url)) return null;
            if (options == null) {
                options = new RequestOptions().skipMemoryCache(true);
            }
            return requestManager
                    .asBitmap()
                    .load(url)
                    .apply(options)
                    .submit(parentWidth, parentHeight).get();
        } catch (Exception e) {

        }
        return null;
    }


    private Bitmap multiBitmap(RequestManager requestManager) throws ExecutionException, InterruptedException {
        final int length = size();
        final int childrenCount = length <= 9 ? length : 9;
        final int mColumnCount = calculateColumn(childrenCount);
        final Bitmap multiBitmap = Bitmap.createBitmap(parentWidth, parentHeight, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(multiBitmap);
        canvas.drawColor(Color.parseColor("#EEEEEE"));
        for (int i = 0; i <= childrenCount - 1; i++) {
            int rowNum = i / mColumnCount;//当前行数
            int columnNum = i % mColumnCount;//当前列数
            int mImageSize = (parentWidth - (mColumnCount + 1) * mGap) / mColumnCount;//图片尺寸

            int t_center = (parentHeight + mGap) / 2;//中间位置以下的顶点（有宫格间距）
            int b_center = (parentHeight - mGap) / 2;//中间位置以上的底部（有宫格间距）
            int l_center = (parentWidth + mGap) / 2;//中间位置以右的左部（有宫格间距）
            int r_center = (parentWidth - mGap) / 2;//中间位置以左的右部（有宫格间距）
            int center = (parentHeight - mImageSize) / 2;//中间位置以上顶部（无宫格间距）

            int left = mImageSize * columnNum + mGap * (columnNum + 1);
            int top = mImageSize * rowNum + mGap * (rowNum + 1);
            int right = left + mImageSize;
            int bottom = top + mImageSize;
            final Bitmap bitmap = loadBitmap(requestManager, getSafeStringUrl(i));

            /*
             * 不同子view情况下的不同显示
             */
            if (childrenCount == 1) {
                drawBitmapAt(canvas, bitmap, left, top, right, bottom);
            } else if (childrenCount == 2) {
                drawBitmapAt(canvas, bitmap, left, center, right, center + mImageSize);
            } else if (childrenCount == 3) {
                if (i == 0) {
                    drawBitmapAt(canvas, bitmap, center, top, center + mImageSize, bottom);
                } else {
                    drawBitmapAt(canvas, bitmap, mGap * i + mImageSize * (i - 1), t_center, mGap * i + mImageSize * i, t_center + mImageSize);
                }
            } else if (childrenCount == 4) {
                drawBitmapAt(canvas, bitmap, left, top, right, bottom);
            } else if (childrenCount == 5) {
                if (i == 0) {
                    drawBitmapAt(canvas, bitmap, r_center - mImageSize, r_center - mImageSize, r_center, r_center);
                } else if (i == 1) {
                    drawBitmapAt(canvas, bitmap, l_center, r_center - mImageSize, l_center + mImageSize, r_center);
                } else {
                    drawBitmapAt(canvas, bitmap, mGap * (i - 1) + mImageSize * (i - 2), t_center, mGap * (i - 1) + mImageSize * (i - 1), t_center + mImageSize);
                }
            } else if (childrenCount == 6) {
                if (i < 3) {
                    drawBitmapAt(canvas, bitmap, mGap * (i + 1) + mImageSize * i, b_center - mImageSize, mGap * (i + 1) + mImageSize * (i + 1), b_center);
                } else {
                    drawBitmapAt(canvas, bitmap, mGap * (i - 2) + mImageSize * (i - 3), t_center, mGap * (i - 2) + mImageSize * (i - 2), t_center + mImageSize);
                }
            } else if (childrenCount == 7) {
                if (i == 0) {
                    drawBitmapAt(canvas, bitmap, center, mGap, center + mImageSize, mGap + mImageSize);
                } else if (i > 0 && i < 4) {
                    drawBitmapAt(canvas, bitmap, mGap * i + mImageSize * (i - 1), center, mGap * i + mImageSize * i, center + mImageSize);
                } else {
                    drawBitmapAt(canvas, bitmap, mGap * (i - 3) + mImageSize * (i - 4), t_center + mImageSize / 2, mGap * (i - 3) + mImageSize * (i - 3), t_center + mImageSize / 2 + mImageSize);
                }
            } else if (childrenCount == 8) {
                if (i == 0) {
                    drawBitmapAt(canvas, bitmap, r_center - mImageSize, mGap, r_center, mGap + mImageSize);
                } else if (i == 1) {
                    drawBitmapAt(canvas, bitmap, l_center, mGap, l_center + mImageSize, mGap + mImageSize);
                } else if (i > 1 && i < 5) {
                    drawBitmapAt(canvas, bitmap, mGap * (i - 1) + mImageSize * (i - 2), center, mGap * (i - 1) + mImageSize * (i - 1), center + mImageSize);
                } else {
                    drawBitmapAt(canvas, bitmap, mGap * (i - 4) + mImageSize * (i - 5), t_center + mImageSize / 2, mGap * (i - 4) + mImageSize * (i - 4), t_center + mImageSize / 2 + mImageSize);
                }
            } else if (childrenCount == 9) {
                drawBitmapAt(canvas, bitmap, left, top, right, bottom);
            }
        }
        return multiBitmap;
    }

    private void drawBitmapAt(Canvas canvas, Bitmap bitmap, int left, int top, int right, int bottom) {

        if (bitmap != null) {
            canvas.drawBitmap(bitmap, null, new Rect(left, top, right, bottom), null);
//            bitmap.recycle();
        } else if (errorDrawable != null) {
            errorDrawable.setBounds(left, top, right, bottom);
            errorDrawable.draw(canvas);
        }
    }

    private int calculateColumn(int imagesSize) {
        if (imagesSize < 3) {
            return imagesSize;
        } else if (imagesSize <= 4) {
            return 2;
        } else {
            return 3;
        }
    }

    private InputStream convertBitmapToInputStream(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
