package com.faucet.quickutils.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.faucet.quickutils.core.manager.GlideApp;

/**
 * Created by mac on 16/9/9.
 */
public abstract class ImageUtils {

    protected abstract int setPlaceHolder ();

    /**
     * 正常加载图片
     *
     * @param context
     * @param iv       ImageView
     * @param url      图片地址
     */
    public void displayImage(Context context, ImageView iv, String url) {
        if (!isContextError(context))
            GlideApp.with(context)
                    .load(url)
                    .centerCrop()
                    .placeholder(setPlaceHolder())
                    .into(iv);
    }

    public void displayImageNocut(Context context, ImageView iv, String url) {
        if (!isContextError(context))
            GlideApp.with(context)
                    .load(url)
                    .placeholder(setPlaceHolder())
                    .into(iv);
    }

    public void displayBitmapImage (Context context, ImageView iv, String url, BitmapImageViewTarget bitmapImageViewTarget) {
        if (!isContextError(context))
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .into(bitmapImageViewTarget);
    }

    /**
     * 加载列表图片
     *
     * @param context
     * @param iv       ImageView
     * @param url      图片地址
     */
    public void displayImageForList(Context context, ImageView iv, String url) {
        if (!isContextError(context))
            GlideApp.with(context)
                .load(url+"?x-oss-process=image/resize,p_50")
                .centerCrop()
                .placeholder(setPlaceHolder())
                .into(iv);
    }

    public void displayImageIcon(Context context, ImageView iv, String url) {
        if (!isContextError(context))
            GlideApp.with(context)
                    .load(url+"?x-oss-process=image/resize,w_200,h_200")
                    .centerCrop()
                    .placeholder(setPlaceHolder())
                    .into(iv);
    }

    public void displayImageCenterCropWithAnim(Context context, ImageView iv, String url) {
        if (!(context == null || iv == null))
            GlideApp.with(context)
                    .load(url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(iv);
    }

    public void displayImageResourceWithAnim(Context context, ImageView iv, int draw) {
        if (!(context == null || iv == null))
            GlideApp.with(context)
                    .load(draw)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(iv);
    }

    /**
     * 加载资源文件
     * @param context
     * @param iv
     * @param draw
     */
    public void displayImageResource(Context context, ImageView iv, int draw) {
        if (!(context == null || iv == null))
            GlideApp.with(context)
                    .load(draw)
                    .centerCrop()
                    .placeholder(setPlaceHolder())
                    .into(iv);
    }

    /**
     * 加载圆角图
     * @param context
     * @param iv
     * @param url
     * @param corner
     */
    public void displayRoundedCornersImage (Context context, ImageView iv, String url, int corner) {
        if (!isContextError(context)) {
            RequestOptions options = new RequestOptions().transform(new GlideRoundTransform(context, corner));
            GlideApp.with(context)
                    .load(url + "?x-oss-process=image/resize,p_70")
                    .apply(options)
                    .thumbnail(GlideApp.with(context).load(setPlaceHolder()).apply(options))
//                    .placeholder(setPlaceHolder())
                    .into(iv);
        }
    }

    /**
     * 加载半圆角图
     * @param context
     * @param iv
     * @param url
     * @param corner
     */
    public void displayHalfRoundedCornersImage (Context context, ImageView iv, String url, int corner) {
        if (!isContextError(context)){
            GlideHalfRoundTransform glideHalfRoundTransform = new GlideHalfRoundTransform(context, corner);
            glideHalfRoundTransform.setExceptCorner(false, false, true, true);
            RequestOptions options = new RequestOptions().transform(glideHalfRoundTransform);
            GlideApp.with(context)
                    .load(url + "?x-oss-process=image/resize,p_70")
                    .apply(options)
                    .thumbnail(GlideApp.with(context).load(setPlaceHolder()).apply(options))
//                    .placeholder(setPlaceHolder())
                    .into(iv);
        }
    }

    /**
     * 加载圆形图
     * @param context
     * @param iv
     * @param url
     */
    public void displayCircleImage (Context context, ImageView iv, String url) {
        if (!isContextError(context)) {
            RequestOptions options = new RequestOptions().transform(new GlideCircleTransform(context));
            GlideApp.with(context)
                    .load(url)
                    .thumbnail(GlideApp.with(context).load(setPlaceHolder()).apply(options))
//                    .placeholder(setPlaceHolder())
                    .apply(options)
                    .into(iv);
        }
    }

    /**
     * 加载圆形图
     * @param context
     * @param iv
     * @param url
     */
    public void displayCircleImageWithBorder (Context context, ImageView iv, String url, int borderWidth, int borderColor) {
        if (!isContextError(context)) {
            RequestOptions options = new RequestOptions().transform(new GlideCircleTransform(context, borderWidth, borderColor));
            GlideApp.with(context)
                    .load(url)
                    .thumbnail(GlideApp.with(context).load(setPlaceHolder()).apply(options))
//                    .placeholder(setPlaceHolder())
                    .apply(options)
                    .into(iv);
        }
    }

    public void getImageBitmap (Context context, String url, int width, int height, OnHandlerListener onHandlerListener) {
        if (!isContextError(context))
            GlideApp.with(context)
                .asBitmap().load(url)
                .into(new SimpleTarget<Bitmap>(width, height) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        onHandlerListener.onComplete(resource);
                    }
                });
    }

    private boolean isContextError (Context context) {
        if (context != null) {
            if (context instanceof FragmentActivity) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (((FragmentActivity) context).isDestroyed()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else if (context instanceof Activity) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (((Activity) context).isDestroyed()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        } else {
            return true;
        }
    }
}
