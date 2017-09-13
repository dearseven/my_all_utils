package cyan.view.cyan_autosize.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import cyan.view.cyan_autosize.R;
import cyan.view.cyan_autosize.configs.AutoSizeConfigs;

import java.math.BigDecimal;

/**
 * Created by Cyan on 2017/8/6.
 */

public class ASToolbar extends Toolbar {

    public ASToolbar(Context context) {
        super(context);
    }

    public ASToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private final String T = "auto-size";
    float w = -1f;
    float h = -1f;
    private Context ctx = null;

    public ASToolbar(Context context, @Nullable AttributeSet attrs) {
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
            setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
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
        float factor = ((sw * 1.0f / AutoSizeConfigs.STANDHARD_W) + (sh * 1.0f / AutoSizeConfigs.STANDHARD_H)) / 2;
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
        float factor = ((sw * 1.0f / AutoSizeConfigs.STANDHARD_W) + (sh * 1.0f / AutoSizeConfigs.STANDHARD_H)) / 2;
        //在计算出应该得h
        int shouldH = new BigDecimal(h * factor).intValue();
        result=MeasureSpec.makeMeasureSpec(shouldH,MeasureSpec.EXACTLY);
        return result;
    }





}
