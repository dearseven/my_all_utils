
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View



/**
 * Created by wx on 2018/3/14.
 */
class  SampleLazyFragmentImpl : ILazyLoadFragment() {
    protected val FRAGMENT_INDEX = "fragment_index"
    protected var mFragmentView: View? = null

    protected var mCurIndex = -1
    protected var _layoutId = -1

    /**
     * 标志位，标志已经初始化完成
     */
    protected var isPrepared = false
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    protected var mHasLoadedOnce = false

    override fun onLazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return
        }
        lazyload()
        mHasLoadedOnce = true
    }

    override fun onHide() {

    }

    override fun getFragmentIndex(): Int {
        return mCurIndex
    }

    override fun getFragmentName(): String {
        return SampleLazyFragmentImpl::class.java.name
    }

    override fun setFragmentIndex(index: Int) {
        mCurIndex = index
    }

    override fun setLayoutId(index: Int) {
        _layoutId = index
    }

    /**
     * 创建新实例
     *
     * @param index
     * @param bundle
     * @param c,要被创建的fragment的类
     * @return
     */
    fun newInstance(index: Int, bundle: Bundle, c: Class<*>, layoutId: Int): ILazyLoadFragment {
        bundle.putInt(FRAGMENT_INDEX, index)
        var fragment: ILazyLoadFragment? = null
        try {
            fragment = c.newInstance() as ILazyLoadFragment
        } catch (e: java.lang.InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        fragment!!.setLayoutId(layoutId)
        fragment.fragmentIndex = index
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (mFragmentView == null) {
            mFragmentView = inflater!!.inflate(
                    _layoutId, container, false) as View
            // 获得索引值
            val bundle = arguments
            if (bundle != null) {
                mCurIndex = bundle.getInt(FRAGMENT_INDEX)
            }
            isPrepared = true
            onLazyLoad()
        }
        //        // 因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        //        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        //        if (parent != null) {
        //            parent.removeView(mFragmentView);
        //        }
        return mFragmentView
    }

    //-----------下面是业务方法----------
    private fun lazyload() {
        //init_views
        //reg_allthings....
        //logic.....
    }

}
