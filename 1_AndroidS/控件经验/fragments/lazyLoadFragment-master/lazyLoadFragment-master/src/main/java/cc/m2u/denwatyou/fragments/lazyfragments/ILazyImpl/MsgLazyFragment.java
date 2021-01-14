package cc.m2u.denwatyou.fragments.lazyfragments.ILazyImpl;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.m2u.denwatyou.fragments.lazyfragments.ILazy.ILazyLoadFragment;

/**
 * 短信
 */
public class MsgLazyFragment extends ILazyLoadFragment {
    protected static final String FRAGMENT_INDEX = "fragment_index";
    protected View mFragmentView;

    protected int mCurIndex = -1;
    protected int layoutId = -1;

    /**
     * 标志位，标志已经初始化完成
     */
    protected boolean isPrepared = false;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    protected boolean mHasLoadedOnce = false;

    @Override
    public void onLazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        lazyload();
        mHasLoadedOnce = true;
    }

    @Override
    public void onHide() {

    }

    @Override
    public int getFragmentIndex() {
        return mCurIndex;
    }

    @Override
    public String getFragmentName() {
        return SampleLazyFragmentImpl.class.getName();
    }

    @Override
    public void setFragmentIndex(int index) {
        mCurIndex = index;
    }

    @Override
    public void setLayoutId(int index) {
        layoutId = index;
    }

    /**
     * 创建新实例
     *
     * @param index
     * @param bundle
     * @param c,要被创建的fragment的类
     * @return
     */
    public static ILazyLoadFragment newInstance(int index, Bundle bundle, Class c, int layoutId) {
        bundle.putInt(FRAGMENT_INDEX, index);
        ILazyLoadFragment fragment = null;
        try {
            fragment = (ILazyLoadFragment) c.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        fragment.setLayoutId(layoutId);
        fragment.setFragmentIndex(index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mFragmentView == null) {
            mFragmentView = (View) inflater.inflate(
                    layoutId, container, false);
            // 获得索引值
            Bundle bundle = getArguments();
            if (bundle != null) {
                mCurIndex = bundle.getInt(FRAGMENT_INDEX);
            }
            isPrepared = true;
            onLazyLoad();
        }
//        // 因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
//        if (parent != null) {
//            parent.removeView(mFragmentView);
//        }
        return mFragmentView;
    }

    //-----------下面是业务方法----------
    private void lazyload() {
        //init_views
        //reg_allthings....
        //logic.....

        Log.i("denwatyou","msg lazyload");

    }


}
