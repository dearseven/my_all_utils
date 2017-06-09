package com.teetaa.testwh.eventbus_param;

import android.view.View;

/**
 * Created by wx on 2017/4/18.
 */

public class MainViewPageItenSelected {
    public int index;
    public View focusLayer;
    public boolean needAnim = true;
    //在接受eventbus的代码里，要把对象requestFocus，一般不用拉,在这里主要是当用户在焦点在viewpager的最左侧item时，点击了左，上按钮切回到左侧导航使用
    public boolean forcedHasFocus=false;
}
