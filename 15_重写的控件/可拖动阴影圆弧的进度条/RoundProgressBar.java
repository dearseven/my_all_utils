
import java.math.BigDecimal;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnTouchListener;

;

/**
 * 圆形拖动的进度条
 *
 * @author x
 */
public class RoundProgressBar extends android.support.v7.widget.AppCompatImageView implements OnTouchListener {

    /**
     * 最大的角度值，360度
     */
    public static final int MAX_ANGLE = 360;

    /**
     * 无法获取角度值时，返回该值
     */
    private static final int INVALID_ANGLE = -1;

    /**
     * 增加拖动的范围，改善拖动流畅性
     */
    private static final int IMPROVE_DIS = 30;

    /**
     * 防止触碰太过敏感，触碰距离在此值之内则不执行复杂操作（比如invalidate()）
     */
    private static final int FLING_DIS = 3;

    /**
     * 起始点相对系统坐标轴的的角度
     */
    private int mStartAngle;

    /**
     * 可拖动的角度范围
     */
    private int mEnabledDragAngleRange = MAX_ANGLE;

    /**
     * 用来拖动的drawable
     */
    private Drawable mDragDrawable;

    /**
     * mDragDrawable所在的角度（坐标轴对应的角度，而不是相对起始点的角度）
     */
    private int mAngle;

    /**
     * mDragDrawable拖动路径与RoundProgressBar（ImageView）边缘的距离（彼此是同心圆）。 往里算正，往外算负
     */
    private int mDragOffset;

    /**
     * 是否可以开始拖动
     */
    private boolean mStartDrag = true;

    /**
     * 是否允许继续拖动
     */
    private boolean mEnableDragContinue = true;

    /**
     * 上次触碰的x坐标
     */
    private int mLastTouchX;

    /**
     * 上次触碰的y坐标
     */
    private int mLastTouchY;

    /**
     * 拖动（角度）发生变化时的监听
     */
    private OnDragAngleChangedListener mListener;

    public RoundProgressBar(Context context) {
        super(context);
        init(context);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context Context
     */
    private void init(Context context) {
        mDragDrawable = context.getResources().getDrawable(
                R.drawable.round_progressbar_default_drag_drawable_selector);
        setOnTouchListener(this);
    }

    Paint mPaint;
    Paint p;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect bounds = getDragDrawableBounds(mAngle);
        if (bounds != null) {
            mDragDrawable.setBounds(bounds);
            canvas.save();
            //
            RectF rf = new RectF(getDrawable().getBounds());
            int dif = dip2px(getContext(), 18);
            rf.offsetTo(getDrawable().getBounds().left + dif, getDrawable()
                    .getBounds().top + dif);
            RectF _rf = new RectF(rf.left + 5, rf.top + 5, rf.right - 5,
                    rf.bottom - 5);
            //把画布旋转-90度 这样下面画圆弧的时候就不需要从-90度开始了
            canvas.rotate(-90, _rf.centerX(), _rf.centerY());

            //1-----------带阴影的背景圆环
            if (mPaint == null) {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);  // 关闭硬件加速
                //this.setWillNotDraw(false);                    // 调用此方法后，才会执行 onDraw(Canvas) 方法

                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(15f);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(Color.parseColor("#735D97"));
                mPaint.setShadowLayer(19f, 0f, 0f, Color.parseColor("#aaffffff"));
            }
            canvas.drawArc(_rf, 9, 360, false, mPaint);
            //2-------------画白色的圆环
            if (p == null) {
                p = new Paint();
                p.setStrokeWidth(11f);
                p.setStyle(Paint.Style.STROKE);
                p.setAntiAlias(true);
                //
                int[] colors = new int[]{// 渐变色数组 6
                        Color.parseColor("#eeFFFFFF"),//从这里开始!
                        Color.parseColor("#44fefefe"),
                        Color.parseColor("#11fdfdfd"),
                        Color.parseColor("#44fefefe"),
                        Color.parseColor("#eefdfdfd")};
                Shader shader = new SweepGradient(_rf.centerX(), _rf.centerY(), colors, null);
                p.setShader(shader);
            }
//            if ((mAngle * 1f) + 90 != 360) {
//                DebugLog.log(null, "mAngle=" + mAngle, getClass());
//                p.setARGB(200, 255, 255, 255);
//            } else {
//                p.setARGB(0, 255, 255, 255);
//            }
            //画圆弧
            //因为把画布旋转-90度了 这样画圆弧的时候就不需要从-90度开始了,而是从0开始
            canvas.drawArc(_rf, 0,
                    (mAngle * 1f) + 90 > 360 ? (mAngle * 1f) + 90 - 360
                            : (mAngle * 1f) + 90, false, p);
            canvas.restore();
            // cyan 画圆弧和圆结束
            //4---------------画圆弧和圆结束
            canvas.translate(getPaddingLeft(), getPaddingTop());
            //canvas.rotate(mAngle, bounds.exactCenterX(), bounds.exactCenterY());
            // cyan 画出那个按钮
            mDragDrawable.draw(canvas);
            //cyan --2018!!
            canvas.save();
            canvas.restore();
            //--------------------------------
            /*
            // 画一个白圆先
            Paint _p = new Paint();
//            Shader shader = new LinearGradient(0, 0, 10F, 10F,
//                    new int[]{Color.WHITE, Color.BLUE, Color.WHITE,},
//                    new float[]{0.0F, 0.1F, 0.9F}, Shader.TileMode.MIRROR);
//
//            _p.setShader(shader);
            _p.setStrokeWidth(10f);
            _p.setStyle(Paint.Style.STROKE);
            _p.setAntiAlias(true);
            // _p.setARGB(255, 255, 255, 255);
            canvas.drawArc(_rf, 9, 360, false, _p);


            // 画圆弧
            canvas.drawArc(_rf, -90,
                    (mAngle * 1f) + 90 > 360 ? (mAngle * 1f) + 90 - 360
                            : (mAngle * 1f) + 90, false, p);
            canvas.restore();
            // cyan 画圆弧和圆结束

            canvas.translate(getPaddingLeft(), getPaddingTop());
            //canvas.rotate(mAngle, bounds.exactCenterX(), bounds.exactCenterY());
            // cyan 画出那个按钮
            mDragDrawable.draw(canvas);
            //cyan --2018!!
            canvas.save();
            canvas.restore();
*/
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (SleepFragment.isSleepAlarmAreaOpen) {
            return true;
        }
        boolean ret = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mEnableDragContinue = true;
            case MotionEvent.ACTION_MOVE:
                boolean pressing = false;
                if (mStartDrag && mEnableDragContinue) {
                    boolean enableDragContinue = false;
                    int touchX = (int) event.getX() - getPaddingLeft();
                    int touchY = (int) event.getY() - getPaddingTop();
                    if (Math.abs(touchX - mLastTouchX) > FLING_DIS
                            || Math.abs(touchY - mLastTouchY) > FLING_DIS) {
                        mLastTouchX = touchX;
                        mLastTouchY = touchY;
                        int angle = touchpointToAngle(touchX, touchY);
                        if (isInDragArea(angle, touchX, touchY)) {
                            if (correctAngle(angle - mStartAngle) <= mEnabledDragAngleRange) {
                                int oldAngle = getDragRelativeAngle();
                                mAngle = angle;
                                // cyan 触发重绘
                                invalidate();
                                int newAngle = getDragRelativeAngle();
                                if (mListener != null) {
                                    mListener.onDragAngleChanged(oldAngle,
                                            newAngle, mEnabledDragAngleRange);
                                }
                                enableDragContinue = true;
                                pressing = true;
                            } else if (isInDragArea(mAngle, touchX, touchY)) {
                                enableDragContinue = true;
                                pressing = true;
                            }
                        }
                    } else {
                        enableDragContinue = true;
                        pressing = true;
                    }

                    mEnableDragContinue = enableDragContinue;
                }

                if (pressing) {
                    setPressStateToDragDrawable();
                } else {
                    setEmptyStateToDragDrawable();
                }

                ret = pressing;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastTouchX = 0;
                mLastTouchY = 0;
                mEnableDragContinue = true;
                setEmptyStateToDragDrawable();
                ret = true;
                break;

            default:
                break;
        }

        return ret;
    }

    /**
     * 将拖动的Drawable设为按下状态
     */
    private void setPressStateToDragDrawable() {
        if (mDragDrawable.isStateful()) {
            if (mDragDrawable
                    .setState(new int[]{android.R.attr.state_pressed})) {
                invalidate();
            }
        }
    }

    /**
     * 将拖动的Drawable设为原始状态
     */
    private void setEmptyStateToDragDrawable() {
        if (mDragDrawable.isStateful()) {
            if (mDragDrawable
                    .setState(new int[]{android.R.attr.state_empty})) {
                invalidate();
            }
        }
    }

    /**
     * 触摸点是否在可拖动的区域
     *
     * @param angle  角度，0~360
     * @param touchX x坐标
     * @param touchY y坐标
     * @return true or false
     */
    private boolean isInDragArea(int angle, int touchX, int touchY) {
        boolean ret = false;
        if (angle != INVALID_ANGLE) {
            Rect bounds = getDragDrawableBounds(angle);
            if (bounds != null) {
                bounds.inset(-IMPROVE_DIS, -IMPROVE_DIS);
                if (bounds.contains(touchX, touchY)) {
                    ret = true;
                }
            }
        }

        return ret;
    }

    /**
     * 触摸点相对中心点的角度
     *
     * @param touchX x坐标
     * @param touchY y坐标
     * @return 正常角度值（0~360）或者INVALID_ANGLE
     */
    private int touchpointToAngle(int touchX, int touchY) {
        Rect rect = getImageCoordinateRect();
        if (rect == null) {
            return -1;
        }

        int x = (int) touchX - rect.centerX();
        int y = (int) touchY - rect.centerY();
        if (x == 0 && y == 0) {
            return -1;
        }

        int angle;
        if (x == 0 && y > 0) {
            angle = 90;
        } else if (x == 0 && y < 0) {
            angle = 270;
        } else {
            angle = (int) (Math.atan(y / (double) x) * 180 / Math.PI);
            if (x < 0) {
                angle += 180;
            }
        }

        return correctAngle(angle);
    }

    /**
     * 获取对应角度上的拖动区域,调整触摸按钮的显示位置
     *
     * @param angle 坐标轴对应的角度（并非相对起始点的角度）
     * @return 拖动区域，无法获取时返回为null
     */
    private Rect getDragDrawableBounds(int angle) {
        Rect rect = getImageCoordinateRect();
        if (rect == null) {
            return null;
        }

        int centerX = rect.centerX();
        int centerY = rect.centerY();
        int a = rect.right - centerX;
        int b = rect.bottom - centerY;

        // 根据椭圆坐标系以及角度tan值计算轨迹
        int dragCenterX, dragCenterY;
        if (angle == 90) {
            dragCenterX = centerX;
            dragCenterY = centerY + a;
        } else if (angle == 270) {
            dragCenterX = centerX;
            dragCenterY = centerY - a;
        } else {
            double tan = Math.tan(angle * Math.PI / 180);
            double tan2 = tan * tan;
            double a2 = a * a;
            double b2 = b * b;
            double x = Math.sqrt((a2 * b2) / (tan2 * a2 + b2));
            if (angle > 90 && angle < 270) {
                x = -x;
            }
            double y = x * tan;

            dragCenterX = (int) (centerX + x);
            dragCenterY = (int) (centerY + y);
        }

        Rect bounds = new Rect();
        bounds.right = mDragDrawable.getIntrinsicWidth();
        bounds.bottom = mDragDrawable.getIntrinsicHeight();
        int dx = dragCenterX - (int)(bounds.centerX()*0.75F);
        int dy = dragCenterY - (int)(bounds.centerY()*0.75F);
        bounds.offset(dx, dy);
        return bounds;
    }

    /**
     * 获取Image修正后的区域，用来确定mDragDrawable的拖动轨迹（区域和角度）
     *
     * @return 矩形区域
     */
    private Rect getImageCoordinateRect() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }

        int viewCenterX = (getWidth() - getPaddingRight() - getPaddingLeft()) >>> 1;
        int viewCenterY = (getHeight() - getPaddingBottom() - getPaddingTop()) >>> 1;

        Rect rect = drawable.copyBounds();
        rect.inset(mDragOffset, mDragOffset); // 修正范围
        rect.offset(viewCenterX - rect.centerX(), viewCenterY - rect.centerY()); // 移到View的正中间

        return rect;
    }

    /**
     * 设置拖动点相对起始点的角度值，介于0~360度之内，且不能超过可拖动的角度范围
     *
     * @param angle 角度值
     */
    public void setDragRelativeAngle(int angle) {
        if (angle > mEnabledDragAngleRange) {
            angle = mEnabledDragAngleRange;
        }
        mAngle = correctAngle(angle + mStartAngle);
        invalidate();
    }

    /**
     * 获取拖动点相对起始点的度值，介于0~360度之内
     *
     * @return 角度值
     */
    public int getDragRelativeAngle() {
        return correctAngle(mAngle - mStartAngle);
    }

    /**
     * 设置起始点相对系统坐标轴的角度
     *
     * @param startAngle 相对角度，可为负值
     */
    public void setStartAngle(int startAngle) {
        mStartAngle = startAngle;
    }

    /**
     * 设置可拖动的角度范围值
     *
     * @param range 角度范围值，0~360以内
     */
    public void setEnabledDragAngleRange(int range) {
        if (range > MAX_ANGLE) {
            range = MAX_ANGLE;
        } else if (range < 0) {
            range = 0;
        }

        mEnabledDragAngleRange = range;
    }

    /**
     * 获取可拖动的角度范围值
     *
     * @return 角度范围值，0~360以内
     */
    public int getEnabledDragAngleRange() {
        return mEnabledDragAngleRange;
    }

    /**
     * 设置拖动路径与RoundProgressBar（ImageView）边缘的距离（彼此是同心圆）。
     * 往里算正，往外算负。最大不能超过RoundProgressBar自身的半径
     *
     * @param offset
     */
    public void setDragOffset(int offset) {
        mDragOffset = offset;
    }

    /**
     * 获取拖动路径与RoundProgressBar（ImageView）边缘的距离（彼此是同心圆）。
     *
     * @return 距离值
     */
    public int getDragOffset() {
        return mDragOffset;
    }

    /**
     * 开始拖动
     */
    public void startDrag() {
        mStartDrag = true;
    }

    /**
     * 停止拖动
     */
    public void endDrag() {
        mStartDrag = false;
    }

    /**
     * 设置用来拖动并控制进度的drawable，不能为空
     *
     * @param d Drawable
     */
    public void setDragDrawable(Drawable d) {
        if (d == null) {
            throw new NullPointerException("DragDrawable can't be null!");
        }

        mDragDrawable = d;
        invalidate();
    }

    /**
     * 获取用来拖动并控制进度的drawable
     *
     * @return Drawable
     */
    public Drawable getDragDrawble() {
        return mDragDrawable;
    }

    /**
     * 设置拖动角度改变的监听器
     *
     * @param listener OnDragAngleChangedListener
     */
    public void setOnDragAngleChangedListener(
            OnDragAngleChangedListener listener) {
        mListener = listener;
    }

    /**
     * 纠正角度值，使其在0~360之内
     *
     * @param angle 纠正前的角度值
     * @return 纠正后的角度值
     */
    public final int correctAngle(int angle) {
        angle %= MAX_ANGLE;
        if (angle < 0) {
            angle += MAX_ANGLE;
        }

        return angle;
    }

    /**
     * 监听拖动角度变化的Listener
     *
     * @author qumiao
     */
    public interface OnDragAngleChangedListener {

        /**
         * 拖动的角度发生变化
         *
         * @param oldAngle 旧角度值
         * @param newAngle 新角度值
         * @param maxAngle 可拖动的最大角度值
         */
        void onDragAngleChanged(int oldAngle, int newAngle, int maxAngle);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 设置大小，让这个view的高度和宽度相等为其中的小值
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();

        // 得到这个view应该的大小
        int r = Math.min(new BigDecimal(childWidthSize * 1).intValue(),
                new BigDecimal(childHeightSize * 1).intValue());

        // 宽高相等
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                new BigDecimal(r).intValue(), MeasureSpec.EXACTLY);

        // widthMeasureSpec = MeasureSpec.makeMeasureSpec(
        // new BigDecimal(r).intValue(), MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
