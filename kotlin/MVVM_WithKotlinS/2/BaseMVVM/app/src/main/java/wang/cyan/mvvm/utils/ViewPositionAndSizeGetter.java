package wang.cyan.mvvm.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;


/**
 * Created by wx on 2016/11/7.<br/>
 * 获取控件的大小和位置<br/>
 * 最好在onWindowFocusChanged之后调用，<br/>
 * 如果是fragment，可以用ViewTreeObserver(api 18 4.3) <br/>
 * 或者是activity告知当前的fragment
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
