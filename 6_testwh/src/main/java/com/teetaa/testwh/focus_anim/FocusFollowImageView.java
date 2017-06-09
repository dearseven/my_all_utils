package com.teetaa.testwh.focus_anim;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import org.greenrobot.eventbus.EventBus;

/**
 * 不带焦点渐变效果
 * Created by wx on 2017/4/18.
 */

public class FocusFollowImageView extends android.support.v7.widget.AppCompatImageView {
    public FocusFollowImageView(Context context, @Nullable AttributeSet attrs) {
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


}
