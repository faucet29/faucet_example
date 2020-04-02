package com.faucet.quickutils.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CircleRoundDrawable extends Drawable {

    private Paint paint;//画笔
    private int mWidth;//图片宽与高的最小值
    private RectF rectF;//矩形
    private int radius;//半径
    private int roundAngle = 30;//默认圆角
    private Bitmap bitmap;//位图
    private int type=1;//默认为圆形
    private int width = 0;
    private int height = 0;

    public static final int TYPE_Round = 1;
    public static final int Type_Circle = 2;

    public CircleRoundDrawable(Context context, int resID) {
        init(BitmapFactory.decodeResource(context.getResources(), resID));
    }

    public CircleRoundDrawable(Bitmap oldbmp) {
        init(oldbmp);
    }

    public CircleRoundDrawable(Context context, int resID, int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
        init(BitmapFactory.decodeResource(context.getResources(), resID));
    }

    public CircleRoundDrawable(Bitmap oldbmp, int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
        init(oldbmp);
    }

    private void init(Bitmap oldbmp) {
        if (width != 0) {
            Matrix matrix = new Matrix();
            float scaleWidth = ((float) width / oldbmp.getWidth());
            float scaleHeight = ((float) height / oldbmp.getHeight());
            matrix.postScale(scaleWidth, scaleHeight);
            this.bitmap = Bitmap.createBitmap(oldbmp, 0, 0, oldbmp.getWidth(), oldbmp.getHeight(),
                    matrix, true);
        } else {
            this.bitmap = oldbmp;
        }
        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//抖动,不同屏幕尺的使用保证图片质量

        ///位图渲染器
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
        mWidth = Math.min(bitmap.getWidth(), bitmap.getHeight());
        //初始化半径
        radius = mWidth / 2;
    }

    /***
     * 设置圆角
     * @param roundAngle px
     */
    public void setRoundAngle(int roundAngle) {
        this.roundAngle = roundAngle;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * drawable将被绘制在画布上的区域
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        //绘制区域
        rectF = new RectF(left, top, right, bottom);
    }

    /**
     * 核心方法
     *
     * @param canvas
     */
    @Override
    public void draw(@NonNull Canvas canvas) {
        if (type ==Type_Circle) {
            canvas.drawCircle(mWidth / 2, mWidth / 2, radius, paint);
        } else{
            canvas.drawRoundRect(rectF, roundAngle, roundAngle, paint);
        }

    }

    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
        invalidateSelf();//更新设置

    }

    @Override
    public int getIntrinsicHeight() {
        if (type == Type_Circle) {
            return mWidth;
        }
        return bitmap.getHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        if (type == Type_Circle) {
            return mWidth;
        }
        return bitmap.getWidth();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        invalidateSelf();//更行设置

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}