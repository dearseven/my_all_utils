package com.teetaa.testwh.focus_anim;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.teetaa.testwh.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wx on 2017/4/18.
 */

public class FocusBiggerImageView extends android.support.v7.widget.AppCompatImageView {
    public FocusBiggerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAnimation();
    }

    private Animation scaleToLargeAnimation = null;
    private Animation scaleToResetAnimation = null;
    private int animationDuration = 100;
    private int animationDelay = 1;

    private void initAnimation() {
        scaleToLargeAnimation = AnimationUtils.loadAnimation(this.getContext(),
                R.anim.focus_bigger);
        scaleToLargeAnimation.setFillEnabled(true);
        scaleToLargeAnimation.setFillAfter(true);

        scaleToResetAnimation = AnimationUtils.loadAnimation(this.getContext(),
                R.anim.lost_focus_reset_size);
        scaleToResetAnimation.setFillEnabled(true);
        scaleToResetAnimation.setFillAfter(true);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            EventBus.getDefault().post(this);
            scaleToLarge();
        } else {
            scaleToReset();
            //this.clearAnimation();
        }
    }

    public void scaleToLarge() {
        if (animationDuration > 0) {
            clearAnimation();
            scaleToLargeAnimation.setStartTime(AnimationUtils
                    .currentAnimationTimeMillis() + animationDelay);
            setAnimation(scaleToLargeAnimation);
        }
    }

    public void scaleToReset() {
        if (animationDuration > 0) {
            clearAnimation();
            scaleToResetAnimation.setStartTime(AnimationUtils
                    .currentAnimationTimeMillis() + animationDelay);
            setAnimation(scaleToResetAnimation);
        }
    }
}
