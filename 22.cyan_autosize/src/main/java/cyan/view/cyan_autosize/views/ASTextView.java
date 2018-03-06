package com.teetaa.autosize.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import com.teetaa.autosize.R;
import com.teetaa.autosize.configs.AutoSizeConfigs;

import java.math.BigDecimal;


/**
 * 实现自定义控件适应高宽得一个例子，控件原本的layout-width和layout-height当w或者y在-1的情况下还是会分别起效的<br><br/>
 * 否则，则会按照比例来自适应，当然这个比例有一个基础值，设定在AutoSizeConfigs里面
 */
public class ASTextView extends android.support.v7.widget.AppCompatTextView {
    private final String T = "auto-size";
    float w = -1f;
    float h = -1f;
    private Context ctx = null;

    public ASTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ASTextView(Context context) {
        super(context);
    }

    public ASTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.au2size);
        w = ta.getFloat(R.styleable.au2size_w, -1.0f);
        h = ta.getFloat(R.styleable.au2size_h, -1.0f);
        Log.i(T, "w=" + w + ",h=" + h);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (w != -1f && h != -1f) {//==-1就是表示不重新计算大小
           // setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
            super.onMeasure(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        } else if (w == -1f && h != -1f) {
            //setMeasuredDimension(widthMeasureSpec, measureHeight(heightMeasureSpec));
            super.onMeasure(widthMeasureSpec, measureHeight(heightMeasureSpec));
        } else if (h == -1f && w != -1f) {
            //setMeasuredDimension(measureWidth(widthMeasureSpec), heightMeasureSpec);
            super.onMeasure(measureWidth(widthMeasureSpec), heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    private int measureWidth(int measureSpec) {
        int result = 0;
        // int specMode = MeasureSpec.getMode(measureSpec);
        //int specSize = MeasureSpec.getSize(measureSpec);

        // 获取屏幕宽度
        int sw = ctx.getResources().getDisplayMetrics().widthPixels;
        int sh = ctx.getResources().getDisplayMetrics().heightPixels;
        //获取和基准参数得比值w
        //获取和基准参数得比值
        AutoSizeConfigs.resetStandHardWH(ctx);
        float factor = ((sw * 1.0f / AutoSizeConfigs.STANDHARD_W) /*+ (sh * 1.0f / AutoSizeConfigs.STANDHARD_H)*/) /*/ 2*/;
        //在计算出应该得w
        int shouldW = new BigDecimal( w* factor).intValue();
        result=MeasureSpec.makeMeasureSpec(shouldW,MeasureSpec.EXACTLY);
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        // int specMode = MeasureSpec.getMode(measureSpec);
        //int specSize = MeasureSpec.getSize(measureSpec);

        // 获取屏幕宽度
        int sw = ctx.getResources().getDisplayMetrics().widthPixels;
        int sh = ctx.getResources().getDisplayMetrics().heightPixels;
        //获取和基准参数得比值w
        //获取和基准参数得比值
        AutoSizeConfigs.resetStandHardWH(ctx);
        float factor = (/*(sw * 1.0f / AutoSizeConfigs.STANDHARD_W) + */(sh * 1.0f / AutoSizeConfigs.STANDHARD_H))/* / 2*/;
        //在计算出应该得h
        int shouldH = new BigDecimal(h * factor).intValue();
        result=MeasureSpec.makeMeasureSpec(shouldH,MeasureSpec.EXACTLY);
        return result;
    }



    //获得导航栏的高度
    public int getNavigationBarHeight() {
        Resources resources = ctx.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android"); //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

//    //获取状态栏的高度
//    public int getStatusBarHeight() {
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;  //屏幕宽
//        int height = dm.heightPixels;  //屏幕高
//        Rect frame = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;  //状态栏高
//        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
//        int titleBarHeight = contentTop - statusBarHeight; //标题栏高
//        return titleBarHeight;
//    }




}
