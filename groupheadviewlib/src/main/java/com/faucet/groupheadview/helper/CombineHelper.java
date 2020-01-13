package com.faucet.groupheadview.helper;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.faucet.groupheadview.listener.OnHandlerListener;

import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CombineHelper {
    public static CombineHelper init() {
        return SingletonHolder.instance;
    }


    private CombineHelper(){

    }

    private static class SingletonHolder {
        private static final CombineHelper instance = new CombineHelper();
    }

    /**
     * 通过url加载
     *
     * @param builder
     */
    private void loadByUrls(final Builder builder) {
        int subSize = builder.subSize;
        Bitmap defaultBitmap = null;
        if (builder.placeholder != 0) {
            defaultBitmap = CompressHelper.getInstance()
                    .compressResource(builder.context.getResources(), builder.placeholder, subSize, subSize);
        }
        final ProgressHandler handler = new ProgressHandler(defaultBitmap, builder.count, new OnHandlerListener() {
            @Override
            public void onComplete(Bitmap[] bitmaps) {
                setBitmap(builder, bitmaps);
            }
        });
        for (int i = 0; i < builder.count; i++) {
            final int finalI = i;
            GlideApp.with(builder.context)
                    .asBitmap()
                    .placeholder(builder.placeholder)
                    .load(builder.urls[i])
                    .into(new SimpleTarget<Bitmap>(subSize, subSize) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (resource != null) {
                                handler.obtainMessage(1, finalI, -1, resource).sendToTarget();
                            } else {
                                handler.obtainMessage(2, finalI, -1, null).sendToTarget();
                            }
                        }
                    });
        }
    }

    /**
     * 通过图片的资源id、bitmap加载
     *
     * @param builder
     */
    private void loadByResBitmaps(Builder builder) {
        int subSize = builder.subSize;
        Bitmap[] compressedBitmaps = new Bitmap[builder.count];
        for (int i = 0; i < builder.count; i++) {
            if (builder.resourceIds != null) {
                compressedBitmaps[i] = CompressHelper.getInstance()
                        .compressResource(builder.context.getResources(), builder.resourceIds[i], subSize, subSize);
            } else if (builder.bitmaps != null) {
                compressedBitmaps[i] = CompressHelper.getInstance()
                        .compressResource(builder.bitmaps[i], subSize, subSize);
            }
        }
        setBitmap(builder, compressedBitmaps);
    }

    public void load(Builder builder) {
        if (builder.progressListener != null) {
            builder.progressListener.onStart();
        }

        if (builder.urls != null) {
            loadByUrls(builder);
        } else {
            loadByResBitmaps(builder);
        }
    }

    private void setBitmap(final Builder b, Bitmap[] bitmaps) {
        Bitmap result = b.layoutManager.combineBitmap(b.size, b.subSize, b.gap, b.gapColor, bitmaps);

        // 返回最终的组合Bitmap
        if (b.progressListener != null) {
            b.progressListener.onComplete(result);
        }

        // 给ImageView设置最终的组合Bitmap
        if (b.imageView != null) {
            b.imageView.setImageBitmap(result);
        }
    }
}
