package com.teetaa.testwh.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by wx on 2017/4/21.
 */

public class CyanGridView extends GridView {
    public CyanGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CyanGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //-------------------------------
//    private boolean isScroll = false;
//    private int position = 0;
//    //  private int iCount;
//    private int iColumns;
//    private int iFirstView;
//    private int iLastView;
//    private int iselecte;
//    private int downOrUp = -1;//0 down 1 up
//    private boolean isFirst = true;
//    static int iPageNum = 0;
//
//    @SuppressLint("NewApi")
//    @Override
//    protected void layoutChildren() {
//        super.layoutChildren();
//
//        if (isFirst) {
//            iFirstView = getFirstVisiblePosition();
//            iLastView = getLastVisiblePosition();
//            iPageNum = iLastView - iFirstView + 1;
//            iColumns = getNumColumns();
//            isFirst = false;
//        }
//
//        // iCount = getCount();
//
//        iselecte = getSelectedItemPosition();
//
//
//        this.setOnKeyListener(new OnKeyListener() {
//
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
//                        && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (iselecte % iPageNum >= iPageNum - iColumns
//                            && iselecte % iPageNum < iPageNum) {  // 如果在某一页的完整最后一行，并且点击向下按钮
//                        position = iselecte + iColumns;
//                        isScroll = true;
//                        downOrUp = 0;
//                    }
//                }
//                if (keyCode == KeyEvent.KEYCODE_DPAD_UP
//                        && event.getAction() == KeyEvent.ACTION_DOWN) {// 如果在每一页的第一行并且点击向上按钮
//                    if (iselecte % iPageNum >= 0
//                            && iselecte % iPageNum < iColumns) {
//                        isScroll = true;
//                        position = ((iselecte - iPageNum) / iColumns)
//                                * iColumns;
//                        downOrUp = 1;
//                    }
//                }
//                return false;
//
//            }
//        });
//        if (!isScroll) {
//            return;
//        }
//        isScroll = false;
//        smoothScrollToPositionFromTop(position , 0, 250);
//        Debug.i(CyanGridView.class, iPageNum + "," + position);
//
//    }
}
