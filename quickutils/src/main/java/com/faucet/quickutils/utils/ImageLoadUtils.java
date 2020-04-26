package com.faucet.quickutils.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.fragment.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.faucet.quickutils.core.manager.GlideApp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 16/9/9.
 */
public abstract class ImageLoadUtils {

    protected abstract int setPlaceHolder ();
    private static Map<String, CircleRoundDrawable> circleRoundDrawableMap = new HashMap<>();

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
     * @param corner 单位dp
     */
    public void displayRoundedCornersImage (Context context, ImageView iv, String url, int corner) {
        displayRoundedCornersImage(context, iv, url, corner, 0);
    }

    /**
     * 当PlaceHolder图非圆角时使用
     * @param context
     * @param iv
     * @param url
     * @param corner 单位px
     * @param cornerPercent 圆角占placeholder图宽度百分比 0f-1f
     */
    public void displayRoundedCornersImage (Context context, ImageView iv, String url, int corner, float cornerPercent) {
        if (!isContextError(context)) {
            CircleRoundDrawable circleRoundDrawable = circleRoundDrawableMap.get(setPlaceHolder() + "");
            if (circleRoundDrawable == null) {
                circleRoundDrawable = new CircleRoundDrawable(context, setPlaceHolder());
                circleRoundDrawableMap.put(setPlaceHolder() + "", circleRoundDrawable);
            }
            circleRoundDrawable.setRoundAngle(cornerPercent);
            circleRoundDrawable.setType(1);
            GlideApp.with(context)
                    .load(url + "?x-oss-process=image/resize,p_100")
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(corner)))
                    .thumbnail(GlideApp.with(context).load(url + "?x-oss-process=image/resize,p_30").apply(RequestOptions.bitmapTransform(new RoundedCorners(corner))))
                    .placeholder(circleRoundDrawable)
                    .error(circleRoundDrawable)
                    .into(iv);
        }
    }

    /**
     * 当iv显示为centercrop,PlaceHolder会因为裁剪而失去圆角，故传入宽高压缩至适应iv宽高
     * @param context
     * @param iv
     * @param url
     * @param corner 单位dp
     * @param cornerPercent 圆角占placeholder图宽度百分比 0f-1f
     * @param w 设置placeholder压缩的宽度
     * @param h 设置placeholder压缩的高度
     */
    public void displayRoundedCornersImage (Context context, ImageView iv, String url, int corner, float cornerPercent, int w, int h) {
        if (!isContextError(context)) {
            CircleRoundDrawable circleRoundDrawable = circleRoundDrawableMap.get(setPlaceHolder() + "," + w);
            if (circleRoundDrawable == null) {
                circleRoundDrawable = new CircleRoundDrawable(context, setPlaceHolder(), w, h);
                circleRoundDrawableMap.put(setPlaceHolder() + "," + w, circleRoundDrawable);
            }
            circleRoundDrawable.setRoundAngle(cornerPercent);
            circleRoundDrawable.setType(1);
            GlideApp.with(context)
                    .load(url + "?x-oss-process=image/resize,p_100")
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(corner)))
                    .thumbnail(GlideApp.with(context).load(url + "?x-oss-process=image/resize,p_30").apply(RequestOptions.bitmapTransform(new RoundedCorners(corner))))
                    .placeholder(circleRoundDrawable)
                    .error(circleRoundDrawable)
                    .into(iv);
        }
    }

    /**
     * 加载半圆角图
     * @param context
     * @param iv
     * @param url
     * @param corner 单位dp
     */
    public void displayHalfRoundedCornersImage (Context context, ImageView iv, String url, int corner) {
        displayHalfRoundedCornersImage(context, iv, url, corner, 0);
    }

    /**
     * 当PlaceHolder图非圆角时使用
     * @param context
     * @param iv
     * @param url
     * @param corner 单位dp
     * @param cornerPercent 圆角占placeholder图宽度百分比 0f-1f
     */
    public void displayHalfRoundedCornersImage (Context context, ImageView iv, String url, int corner, float cornerPercent) {
        if (!isContextError(context)){
            CircleRoundDrawable circleRoundDrawable = circleRoundDrawableMap.get(setPlaceHolder() + "");
            if (circleRoundDrawable == null) {
                circleRoundDrawable = new CircleRoundDrawable(context, setPlaceHolder());
                circleRoundDrawableMap.put(setPlaceHolder() + "", circleRoundDrawable);
            }
            circleRoundDrawable.setRoundAngle(cornerPercent);
            circleRoundDrawable.setType(1);
            GlideHalfRoundTransform glideHalfRoundTransform = new GlideHalfRoundTransform(context, corner);
            glideHalfRoundTransform.setExceptCorner(false, false, true, true);
            RequestOptions options = new RequestOptions().transform(glideHalfRoundTransform);
            GlideApp.with(context)
                    .load(url + "?x-oss-process=image/resize,p_100")
                    .apply(options)
                    .thumbnail(GlideApp.with(context).load(url + "?x-oss-process=image/resize,p_30").apply(options))
                    .placeholder(circleRoundDrawable)
                    .error(circleRoundDrawable)
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
            CircleRoundDrawable circleRoundDrawable = circleRoundDrawableMap.get(setPlaceHolder() + "");
            if (circleRoundDrawable == null) {
                circleRoundDrawable = new CircleRoundDrawable(context, setPlaceHolder());
                circleRoundDrawableMap.put(setPlaceHolder() + "", circleRoundDrawable);
            }
            circleRoundDrawable.setType(2);
            GlideApp.with(context)
                    .load(url)
                    .thumbnail(GlideApp.with(context).load(url + "?x-oss-process=image/resize,p_30").apply(RequestOptions.bitmapTransform(new CircleCrop())))
                    .placeholder(circleRoundDrawable)
                    .error(circleRoundDrawable)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
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
            CircleRoundDrawable circleRoundDrawable = circleRoundDrawableMap.get(setPlaceHolder() + "");
            if (circleRoundDrawable == null) {
                circleRoundDrawable = new CircleRoundDrawable(context, setPlaceHolder());
                circleRoundDrawableMap.put(setPlaceHolder() + "", circleRoundDrawable);
            }
            circleRoundDrawable.setType(2);
            GlideApp.with(context)
                    .load(url)
                    .thumbnail(GlideApp.with(context).load(url + "?x-oss-process=image/resize,p_30").apply(RequestOptions.bitmapTransform(new CircleCrop())))
                    .placeholder(circleRoundDrawable)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
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
