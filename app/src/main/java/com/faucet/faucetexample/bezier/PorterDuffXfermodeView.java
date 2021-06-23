package com.faucet.faucetexample.bezier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.faucet.faucetexample.R;
import com.faucet.quickutils.utils.ScreenUtils;

import androidx.annotation.Nullable;

public class PorterDuffXfermodeView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PorterDuffXfermodeView(Context context) {
        super(context);
    }

    public PorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap src = BitmapFactory.decodeResource(getResources(), R.mipmap.bg, null);
        Bitmap dist = BitmapFactory.decodeResource(getResources(), R.mipmap.dist, null);

        /*
         * 将绘制操作保存到新的图层（更官方的说法应该是离屏缓存）
         */
        int sc = canvas.saveLayer(0, 0, src.getWidth(), src.getHeight(), null, Canvas.ALL_SAVE_FLAG);

        // 先绘制dis目标图
        canvas.drawBitmap(dist, 0, 0, paint);

        // 设置混合模式   （只在源图像和目标图像相交的地方绘制目标图像）
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        // 再绘制src源图
        canvas.drawBitmap(src, 0, 0, paint);

        // 还原混合模式
        paint.setXfermode(null);

        // 还原画布
        canvas.restoreToCount(sc);
    }
}
