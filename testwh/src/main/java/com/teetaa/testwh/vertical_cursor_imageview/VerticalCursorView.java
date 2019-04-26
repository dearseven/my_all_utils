package com.teetaa.testwh.vertical_cursor_imageview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.teetaa.testwh.R;

/**
 * 布局的是用x50宽度，高度随意<br><br/>
 * <p>
 * vcv.setCurIndex(1);//初始化的时候必须放到第一个，然后在跳到需要的位置,比如这里我是需要去2
 * vcv.setTotalPage(5);
 * vcv.reDrawCursor();
 * vcv.setCurIndex(2);
 * vcv.reDrawCursor();<br><br/>
 * Created by wx on 2017/4/19.
 */

public class VerticalCursorView extends android.support.v7.widget.AppCompatImageView {
    private float curIndex = 1;
    private int totalPage = 1;
    private float lastCurIndex = 1;

    private Path path;

    private Paint paintHonriLine;
    private Paint paintText;
    private Paint paintRect;

    public PaintFlagsDrawFilter mPaintFlagsDrawFilter;// 毛边过滤
    public String boardColr = "#888888";

    public float getCurIndex() {
        return curIndex;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setCurIndex(float curIndex) {
        lastCurIndex = this.curIndex;
        this.curIndex = curIndex;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public VerticalCursorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0,
                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        path = new Path();

        paintHonriLine = new Paint();
        paintHonriLine.setAntiAlias(true);
        paintHonriLine.setFilterBitmap(true);
        paintHonriLine.setColor(Color.LTGRAY);

        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setFilterBitmap(true);
        paintText.setColor(Color.RED);

        paintRect = new Paint();
        paintRect.setAntiAlias(true);
        paintRect.setFilterBitmap(true);
        paintRect.setColor(Color.LTGRAY);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();


        //画矩形
        float lineWidth = getResources().getDimension(R.dimen.x3);
        canvas.drawRect(getWidth() / 2.0f - lineWidth / 2.0f, 0f, getWidth() / 2.0f + lineWidth / 2.0f, getHeight(), paintHonriLine);
        //canvas.save();

        //准备文字
        String cs = ((int) curIndex) + "\n/\n" + totalPage;
        //文字大小
        float tw = getResources().getDimension(R.dimen.x12);
        paintText.setTextSize(tw);
        Paint.FontMetrics fm = paintText.getFontMetrics();
        //文字高度 我发现一个高度大概是2文字左右
        int lineH = (int) Math.ceil(fm.descent - fm.top) + 2;
        //方块的高度
        float rh = (lineH * (cs.length() - 2) / 2)* 1.25f;
        //Debug.i(VerticalCursorView.class, lineH + " lineH");

        //画一个方块1
        //canvas.restore();
        //方块的宽度
        float rw = getResources().getDimension(R.dimen.x25);
        //float rh =0;//= getResources().getDimension(R.dimen.y72);
        //继续处理方块
        //方块的y轴启示坐标
        float ry = (curIndex - 1) * ((getHeight() - rh) / (totalPage - 1));//(getHeight() - rh) / totalPage * curIndex;
        //Debug.i(VerticalCursorView.class, ry + " ~~ " + (ry + rh));
        //canvas.drawRect(getWidth() / 2.0f - rw / 2.0f, ry, getWidth() / 2.0f + rw / 2.0f, ry + rh , paintRect);
        RectF rf=new RectF(getWidth() / 2.0f - rw / 2.0f, ry, getWidth() / 2.0f + rw / 2.0f, ry + rh );
        canvas.drawRoundRect(rf,5f,5f,paintRect);

        //写字
        Path path = new Path();
        path.moveTo(getWidth() / 2.0f - tw / 2.0f, ry); //移动
        path.lineTo(getWidth() / 2.0f - tw / 2.0f, ry + rh);//沿着竖着写，所以这里x不变
        canvas.drawTextOnPath(cs, path, rh / cs.length() - 2, -getResources().getDimension(R.dimen.x1), paintText);
        //canvas.save();
    }

    public void reDrawCursor() {
        if (curIndex != lastCurIndex) {
            final float ci = curIndex;
            ValueAnimator va = ValueAnimator.ofFloat(lastCurIndex, ci);
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float f = animation.getAnimatedFraction();
                    curIndex = (ci - lastCurIndex) * f + lastCurIndex;
                    postInvalidate();
                }
            });
            va.start();
        } else {
            postInvalidate();
        }
    }
}
