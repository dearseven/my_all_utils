package cc.m2u.denwatyou.fragments.lazyfragments.ILazy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cc.m2u.denwatyou.R;

/**
 * 延迟加载的fragment的抽象父类
 */
public abstract class ILazyLoadFragment extends Fragment {


    /** Fragment当前状态是否可见 */
    public boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    public void onVisible() {
        onLazyLoad();
    }

    /**
     * 不可见
     */
    public void onInvisible() {
        onHide();
    }

    /**
     * 延迟加载 子类必须重写此方法
     */
    public abstract void onLazyLoad();

    /**
     * invisible，用于做一些判断
     */
    public abstract void onHide();

    /**
     * 返回index的id
     */
    public abstract int getFragmentIndex();

    /**
     * 返回index的name
     */
    public abstract String getFragmentName();


    /**
     * 设置index的id
     */
    public abstract void setFragmentIndex(int index);


    /**
     * layoutid setting
     */
    public abstract void setLayoutId(int index);

}
