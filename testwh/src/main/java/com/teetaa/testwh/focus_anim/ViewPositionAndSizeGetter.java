package com.teetaa.testwh.focus_anim;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by wx on 2017/4/18.
 */

public class ViewPositionAndSizeGetter {
    /**
     * 计算后，获得对象的 x y w h
     *
     * @param v
     * @param ctx
     * @return
     */
    public Result calculate(View v, Context ctx, Activity activity) {
        Result r = new Result();
        r.h = v.getHeight();
        r.w = v.getWidth();

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        r.x = location[0];
        r.y = location[1];
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();

        if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {

        } else {
            r.y = r.y - result;//减去状态栏
        }
        return r;
    }

    /**
     * 结果
     */
    public static class Result {
        private int x, y, w, h;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getW() {
            return w;
        }

        public int getH() {
            return h;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "x=" + x +
                    ", y=" + y +
                    ", w=" + w +
                    ", h=" + h +
                    '}';
        }
    }
}
