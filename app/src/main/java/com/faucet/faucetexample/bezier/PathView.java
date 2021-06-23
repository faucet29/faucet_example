package com.faucet.faucetexample.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PathView extends View {
    private Paint mPaint;
    private Path mPath;
    private int radio = 0;

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        RectF roundRect = new RectF(700, 300, 900, 700);
        mPath.addRoundRect(roundRect, radio, radio, Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);
        mPath.close();
    }

    public void setRadio(int radio) {
        this.radio = radio;
        invalidate();
    }
}
