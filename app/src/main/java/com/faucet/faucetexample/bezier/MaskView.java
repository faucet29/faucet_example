package com.faucet.faucetexample.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class MaskView extends View {
    //矩形
    public static int TYPE_RECT = 1;
    //月亮
    public static int TYPE_MOON = 2;
    //爱心
    public static int TYPE_LOVE = 3;
    //五角星
    public static int TYPE_STAR = 4;

    private int type = TYPE_LOVE;
    //镂空边框
    private Paint border = new Paint(Paint.ANTI_ALIAS_FLAG);
    //镂空区域
    private Paint mask = new Paint(Paint.ANTI_ALIAS_FLAG);
    //背景
    private Paint background = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();
    private Path path2 = new Path();
    private int radio = 0;
    private int realRadio = 0;//最大半径
    private int currentX = 0;
    private int currentY = 0;
    private int rotate = 0;//旋转角度
    private float scale = 1; // 伸缩比例

    // 移动过程中临时变量
    private int actionX;
    private int actionY;
    private float spacing;
    private float degree;
    private int moveType; // 0=未选择，1=拖动，2=缩放
    private OnDrawDataChangeListener onDrawDataChangeListener;

    private int w = 500;//mask图案宽度

    public MaskView(Context context) {
        super(context);
        init();
    }

    public MaskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        // 硬件加速不支持，图层混合。
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // 取景框颜色、线宽
        border.setColor(Color.parseColor("#cfc961"));
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(3);

        mask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //中心点
        currentX = w/2;
        currentY = h/2;
    }

    private Matrix matrix = new Matrix();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        fillRectRound(0, 0, getWidth(), getHeight(), 0, 0);
        canvas.drawPath(path, background);

        if (type == TYPE_RECT) {
            drawRect(canvas);
        } else if (type == TYPE_MOON) {
            drawStar(canvas);
        } else if (type == TYPE_LOVE) {
            drawLove(canvas);
        } else if (type == TYPE_STAR) {
            drawMoon(canvas);
        }
    }

    /**
     * 设置要显示的mask形状
     * @param type MaskView.TYPE_RECT, MaskView.TYPE_MOON, TYPE_LOVE, TYPE_STAR
     */
    public void setMaskViewType(int type){
        this.type = type;
    }

    /**
     * 设置圆角
     * @param radio 1-100
     */
    public void setRadio(int radio) {
        this.radio = radio;
        invalidate();
    }

    /**
     * 设置旋转
     * @param rotate 0-360
     */
    public void setRotate(int rotate) {
        this.rotate = rotate;
        invalidate();
    }

    /**
     * 是否反转镂空效果
     * @param isReversal
     */
    public void reversalCutOut(boolean isReversal) {
        if (isReversal) {
            background.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mask.setXfermode(null);
        } else {
            background.setXfermode(null);
            mask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        invalidate();
    }

    /**
     * 回调绘制的path，旋转度等数据
     * @param onDrawDataChangeListener
     */
    public void setOnDrawDataChangeListener(OnDrawDataChangeListener onDrawDataChangeListener) {
        this.onDrawDataChangeListener = onDrawDataChangeListener;
    }

    public interface OnDrawDataChangeListener {
        /**
         * 回调数据
         * @param path 绘制path
         * @param rotate 旋转角度0-360
         * @param centerPoint 中心点
         * @param scale 缩放比例
         * @param width 宽
         * @param height 高
         */
        void onDrawDataChange(Path path, int rotate, Point centerPoint, float scale, int width, int height);
    }

    private void drawMoon(Canvas canvas) {
        int h = (int) (w * 1.064);
        int m = (int) (w * 0.04);
        int aX = (int) (- 1.5f * m);
        int aY = (int) (-(h/2) + 0.75f * m);
        int cX = w / 2;
        int cY = m;
        int bX = aX - m;
        int bY = cY + m;
        int dX = (int) (- w / 2 + 3.5 * m);
        int dY = (int) (cY + 7.5 * m);
        //矩阵重置
        matrix.reset();
        //矩阵移动
        matrix.postTranslate(currentX, currentY);
        //矩阵旋转
        matrix.postRotate(rotate, currentX, currentY);
        //矩阵缩放
        matrix.postScale(scale, scale, currentX, currentY);
        path.reset();
        path.moveTo(aX, aY);
        path.cubicTo( aX - 4.5f*m, bY - (bY - aY) / 4 * 3, aX - 4.5f * m, bY - (bY - aY) / 4, bX, bY);
        path.cubicTo(bX + (cX - bX) / 4, bY + 4*m, bX + (cX - bX) / 4 * 3, bY + 4 * m, cX, cY);
        path.cubicTo(cX - w * 0.1f, h / 2 + 2f * m, cX - w * 0.65f, h / 2 + m, dX, dY);
        path.cubicTo(aX - w * 0.5f - m / 2, 2.5f * m, aX - w * 0.5f - 0.5f * m, dX - 1.5f * m, aX, aY);
        if (onDrawDataChangeListener != null) {
            onDrawDataChangeListener.onDrawDataChange(path, rotate, new Point(currentX, currentY), scale, w, h);
        }
        path.transform(matrix);
        canvas.drawPath(path, mask);

        path.addCircle(currentX, currentY, 15, Path.Direction.CW);
        canvas.drawPath(path, border);
    }

    private void drawLove(Canvas canvas) {
        int h = (int) (w * 0.9);
        //爱心上角点到爱心最高点的垂直距离
        int pq = (int) (0.085 * h);
        //爱心上下两个角的点的坐标
        int qX = 0;
        int qY = - (h/2 - pq);
        int kX = 0;
        int kY = h/2;
        //两个控制点的坐标
        int yX = - (w/2 - pq);
        int yY = (int) - (h/2 + 2.4 * pq);
        int zX = (int) - (w/2 + 4.6 * pq);
        int zY = - pq;
        //矩阵重置
        matrix.reset();
        //矩阵移动
        matrix.postTranslate(currentX, currentY);
        //矩阵旋转
        matrix.postRotate(rotate, currentX, currentY);
        //矩阵缩放
        matrix.postScale(scale, scale, currentX, currentY);
        path.reset();
        path2.reset();
        Path maskPath = new Path();
        //缓存未add进maskPath的点
        Path tempPath = new Path();
        //上下两个临界点的当前值
        int criticalACurrent = (int) (12 * ((float)radio/(float)100));
        int criticalCCurrent = 100 - (int) (12 * ((float)radio/(float)100));
        int aX, aY, cX, cY;
        for (int i = 0; i <= 100; i++) {
            float t = (float) i / 100;
            //三阶贝塞尔
            int x = (int) (qX * Math.pow(1 - t, 3) + yX * 3 * Math.pow(1 - t, 2) * t + zX * 3 * (1 - t) * Math.pow(t, 2) + kX * Math.pow(t, 3));
            int y = (int) (qY * Math.pow(1 - t, 3) + yY * 3 * Math.pow(1 - t, 2) * t + zY * 3 * (1 - t) * Math.pow(t, 2) + kY * Math.pow(t, 3));
            if (i == criticalACurrent) {
                aX = x;
                aY = y;
                maskPath.moveTo(-aX, aY);
                tempPath.moveTo(-aX, aY);
                maskPath.quadTo(qX, qY, aX, aY);
            } else if (i > criticalACurrent && i < criticalCCurrent) {
                maskPath.lineTo(x, y);
                tempPath.lineTo(-x, y);
            } else if (i == criticalCCurrent) {
                cX = x;
                cY = y;
                maskPath.lineTo(cX, cY);
                tempPath.lineTo(-cX, cY);
                maskPath.quadTo(kX, kY, -cX, cY);
            }
        }
        path.addPath(maskPath);
        path.addPath(tempPath);
        if (onDrawDataChangeListener != null) {
            onDrawDataChangeListener.onDrawDataChange(path, rotate, new Point(currentX, currentY), scale, w, h);
        }
        path.transform(matrix);
        canvas.drawPath(path, mask);

        //绘制框和中心点
        path.addCircle(currentX, currentY, 15, Path.Direction.CW);
        canvas.drawPath(path, border);
    }

    private void drawStar(Canvas canvas){
        int starLeafHeight = w/2 * 20/33;//五角星叶子的高度，即顶点形成的等腰三角形的高度
        int starBellyHeight = w/2 * 13/33;//五角星中心点到上述等腰三角形底边的距离，此变量决定五角星肚子大小
        int criticalPointScale = 70;//临界点，1-100，划分starLeafHeight所述的等腰三角形的腰，从这个点叶子和根部的角的弧度

        //顶点坐标
        int vertexPointX = 0;
        int vertexPointY = -(starBellyHeight + starLeafHeight);

        int h3 = (int) (Math.tan(Math.PI/180*36) * starBellyHeight);
        double vertexAngle = Math.atan((double)h3/(double)starLeafHeight);
        int starRootRightPointX = h3;//五角星叶子根部点
        int starRootRightPointY = -starBellyHeight;
        //五角星叶子腰长
        int starLeafWaistL = (int)Math.sqrt(Math.pow(starLeafHeight, 2) + Math.pow(h3, 2));
        //顶点到临界点距离
        int vertexToCriticalPointL = (int) (starLeafWaistL * ((float) criticalPointScale / 100));
        //五角星根部点到临界点距离
        int rootToCriticalPointL = starLeafWaistL - vertexToCriticalPointL;
        //左边临界点坐标
        int criticalPointLeftX = (int) -(Math.sin(vertexAngle) * vertexToCriticalPointL);
        int criticalPointLeftY = (int) (-(starLeafHeight + starBellyHeight) + (Math.cos(vertexAngle) * vertexToCriticalPointL));
        //右边临界点坐标
        int criticalPointRightX = (int) -criticalPointLeftX;
        int criticalPointRightY = criticalPointLeftY;
        //计算贝塞尔曲线两个端点坐标
        int leafEndpointLeftX = (int) -(Math.sin(vertexAngle) * vertexToCriticalPointL * ((float)radio/(float)100));
        int leafEndpointLeftY = (int) (-(starLeafHeight + starBellyHeight) + (Math.cos(vertexAngle) * vertexToCriticalPointL * ((float)radio/(float)100)));
        int leafEndpointRightX = (int) -leafEndpointLeftX;
        int leafEndpointRightY = leafEndpointLeftY;
        //根部贝塞尔曲线端点
        int leafRootPointRightX = (int) (Math.sin(vertexAngle) * (starLeafWaistL - (rootToCriticalPointL * ((float)radio/(float)100))));
        int leafRootPointRightY = (int) (-(starLeafHeight + starBellyHeight) + (Math.cos(vertexAngle) * (starLeafWaistL - (rootToCriticalPointL * ((float)radio/(float)100)))));
        //计算第二个叶子的临界点坐标
        double a = Math.PI/180*72;
        int secondLeafCriticalPointLeftX = (int) ((criticalPointLeftX-0)*Math.cos(a) - (criticalPointLeftY-0)*Math.sin(a) + 0);
        int secondLeafCriticalPointLeftY = (int) ((criticalPointLeftY-0)*Math.cos(a) + (criticalPointLeftX-0)*Math.sin(a) + 0);
        //第二个叶子的左边根部端点
        int secondLeafRootPointLeftX = (int) ((secondLeafCriticalPointLeftX - starRootRightPointX) * ((float)radio/(float)100)) + starRootRightPointX;
        int secondLeafRootPointLeftY = (int) ((secondLeafCriticalPointLeftY - starRootRightPointY) * ((float)radio/(float)100)) + starRootRightPointY;
        //绘制
        Path maskPath = new Path();
        maskPath.moveTo(criticalPointLeftX, criticalPointLeftY);
        maskPath.lineTo(leafEndpointLeftX, leafEndpointLeftY);
        maskPath.quadTo(vertexPointX, vertexPointY, leafEndpointRightX, leafEndpointRightY);
        maskPath.lineTo(criticalPointRightX, criticalPointRightY);
        maskPath.lineTo(leafRootPointRightX, leafRootPointRightY);
        maskPath.quadTo(starRootRightPointX, starRootRightPointY, secondLeafRootPointLeftX, secondLeafRootPointLeftY);
        maskPath.lineTo(secondLeafCriticalPointLeftX, secondLeafCriticalPointLeftY);
        maskPath.lineTo(0, 0);
        maskPath.close();
        Path borderPath = new Path();
        borderPath.moveTo(criticalPointLeftX, criticalPointLeftY);
        borderPath.lineTo(leafEndpointLeftX, leafEndpointLeftY);
        borderPath.quadTo(vertexPointX, vertexPointY, leafEndpointRightX, leafEndpointRightY);
        borderPath.lineTo(criticalPointRightX, criticalPointRightY);
        borderPath.lineTo(leafRootPointRightX, leafRootPointRightY);
        borderPath.quadTo(starRootRightPointX, starRootRightPointY, secondLeafRootPointLeftX, secondLeafRootPointLeftY);
        borderPath.lineTo(secondLeafCriticalPointLeftX, secondLeafCriticalPointLeftY);
        //矩阵重置
        matrix.reset();
        //矩阵移动
        matrix.postTranslate(currentX, currentY);
        //矩阵旋转
        matrix.postRotate(rotate, currentX, currentY);
        //矩阵缩放
        matrix.postScale(scale, scale, currentX, currentY);

        path.reset();
        path2.reset();
        for (int i = 0; i < 5; i++) {
            if (i != 0) {
                matrix.postRotate(72, currentX, currentY);
            }
            path.addPath(maskPath);
            path2.addPath(borderPath);
        }
        path.close();
        if (onDrawDataChangeListener != null) {
            onDrawDataChangeListener.onDrawDataChange(path, rotate, new Point(currentX, currentY), scale, w, w);
        }
        path.transform(matrix);
        path2.transform(matrix);
        canvas.drawPath(path, mask);

        //绘制框和中心点
        path2.addCircle(currentX, currentY, 15, Path.Direction.CW);
        canvas.drawPath(path2, border);
    }

    private void drawRect(Canvas canvas) {
        realRadio = w/2;
        int h = w;
        int left = currentX - w/2;
        int top = currentY - h/2;
        int right = w/2 + currentX;
        int bottom = h/2 + currentY;
        //画遮罩图
        int radioRect = (int) (realRadio * ((float)radio / (float) 100));
        fillRectRound(left, top, right, bottom, radioRect, radioRect);
        //矩阵重置
        matrix.reset();
        //矩阵旋转
        matrix.postRotate(rotate, currentX, currentY);
        //矩阵缩放
        matrix.postScale(scale, scale, currentX, currentY);

        if (onDrawDataChangeListener != null) {
            onDrawDataChangeListener.onDrawDataChange(path, rotate, new Point(currentX, currentY), scale, w, h);
        }
        //用matrix改变当前path
        path.transform(matrix);
        //再绘制改变后的path
        canvas.drawPath(path, mask);
        //绘制框和中心点
        path.addCircle(currentX, currentY, 15, Path.Direction.CW);
        canvas.drawPath(path, border);
    }

    private void fillRectRound(float left, float top, float right, float bottom, float rx, float ry) {
        path.reset();
        RectF roundRect = new RectF(left, top, right, bottom);
        path.addRoundRect(roundRect, rx, ry, Path.Direction.CW);
        path.close();
    }

    // 触碰两点间距离
    private float getSpacing(MotionEvent event) {
        //通过三角函数得到两点间的距离
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // 取旋转角度
    private float getDegree(MotionEvent event) {
        //得到两个手指间的旋转角度
        double delta_x = event.getX(0) - event.getX(1);
        double delta_y = event.getY(0) - event.getY(1);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private float tempScale = 0;
    private int tempRotate = 0;
    private float lastFingerUpScale = 1;//上一次手指抬起来时的缩放比例
    private int lastFingerUpRotate = 0;//上一次手指抬起来时的旋转角度
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                moveType = 1;
                actionX = (int) event.getX();
                actionY = (int) event.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                moveType = 2;
                spacing = getSpacing(event);
                degree = getDegree(event);
                break;
            case MotionEvent.ACTION_MOVE:
                boolean isChange = false;
                if (moveType == 1) {
                    boolean a = false;
                    boolean b = false;
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    int currentXTemp = currentX + x - actionX;
                    int currentYTemp = currentY + y - actionY;
                    if ((currentXTemp > 0 && currentXTemp < getWidth())) {
                        currentX = currentXTemp;
                        a = x != actionX;
                        actionX = x;
                    }
                    if ((currentYTemp > 0 && currentYTemp < getHeight())) {
                        currentY = currentYTemp;
                        b = y != actionY;
                        actionY = y;
                    }
                    isChange = a || b;
                    if (isChange) {
                        invalidate();
                    }
                } else if (moveType == 2) {
                    scale = lastFingerUpScale * getSpacing(event) / spacing;
                    rotate = lastFingerUpRotate + (int) (getDegree(event) - degree);
                    if (rotate > 360) {
                        rotate = rotate - 360;
                    }
                    if (rotate < -360) {
                        rotate = rotate + 360;
                    }
                    isChange = scale != tempScale || tempRotate != rotate;
                    tempRotate = rotate;
                    tempScale = scale;
                    if (isChange) {
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                lastFingerUpScale = scale;
                lastFingerUpRotate = rotate;
                moveType = 0;
        }
        return super.onTouchEvent(event);
    }
}