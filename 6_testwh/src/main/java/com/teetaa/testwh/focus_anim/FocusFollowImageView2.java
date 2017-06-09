package com.teetaa.testwh.focus_anim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import org.greenrobot.eventbus.EventBus;

/**
 * 带焦点渐变动画的view
 * Created by wx on 2017/4/18.
 */

public class FocusFollowImageView2 extends android.support.v7.widget.AppCompatImageView {
    public FocusFollowImageView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            EventBus.getDefault().post(this);
        } else {

        }
    }

    private boolean anim = false;
    private Matrix mGradientMatrix=new Matrix();

    /**
     * .渐变动画,在fragment1被回调，（eventbus里面）
     */
    public void startGradient() {
        mPaint = new Paint();

        ValueAnimator va = ValueAnimator.ofFloat(0, getHeight());
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float frac = animation.getAnimatedFraction();
                if (frac == 0f) {
                    mLinearGradient = new LinearGradient(getWidth() * frac, getHeight() * frac, getWidth(), getHeight(),
                            new int[]{0x00ffffff, 0x33ffffff, 0x00ffffff},
                            new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
                } else if (frac == 1.0f) {
                    mLinearGradient = new LinearGradient(getWidth(), getHeight(), getWidth(), getHeight(),
                            new int[]{0x00ff0000, 0x0000ff00, 0x000000ff},
                            new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
                } else {
//                    mLinearGradient = new LinearGradient(getWidth() * frac, getHeight() * frac, getWidth(), getHeight(),
//                            new int[]{0x00ffffff, 0x33ffffff, 0x00ffffff},
//                            new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
                    mGradientMatrix.setTranslate(getWidth() * frac, getHeight() * frac);
                    mLinearGradient.setLocalMatrix(mGradientMatrix);
                }
                mPaint.setShader(mLinearGradient);
                postInvalidate();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                anim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                anim = false;
                mPaint = null;
                mLinearGradient = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                anim = false;
                mPaint = null;
                mLinearGradient = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
    }


    private LinearGradient mLinearGradient;
    //private Matrix mGradientMatrix;
    private Paint mPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (anim)
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }
}
