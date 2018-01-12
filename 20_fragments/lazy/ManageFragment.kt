package fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


/**
 * 功能管理的Fragments
 */
class ManageFragment : BaseLazyFragment() {
    //顶部的我的
    var rvTopMine: RecyclerView? = null

    companion object {
        val TAG = ManageFragment::class.java.name
    }

//    var mview: View? = null
//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        mview = inflater!!.inflate(R.layout.apps_manage_fragment_layout, container, false)
//        return mview
//    }

    //---
    override fun initViews(vararg args: Any?) {
        rvTopMine = selfView.findViewById(R.id.appMoreMangeRv)
        //
        initMine()
    }

    /**
     * 初始化顶部小区域的我的应用
     */
    private fun initMine() {
        var apps = Apps.getApps(Apps.AppsType.goodLife);
        var adapter = GridRecyclerViewAdapter(activity.applicationContext, R.layout.app_more_itemview_layout_mine, object : GridRecyclerViewAdapter.CallBackFunc {
            override fun viewCreate(holder: ViewKeeper?, position: Int) {
                var img = holder!!._itemView!!.findViewById<ImageView>(R.id.app_more_itemview_layout_mine_icon)
                img.setImageResource(apps[position].icon)
            }
        }, 5, rvTopMine!!);
        adapter.addDatas(apps, false);
        rvTopMine!!.adapter = adapter
    }

    override fun intoFragment(vararg args: Any?) {
        if (!hasInited) {
            hasInited = true;
            initViews();
        }
    }


    override fun onBackFilter(obj: Any?): Boolean {
        return super.onBackFilter(obj)
    }

    override fun getLayoutId(): Int = R.layout.apps_manage_fragment_layout;
    //
}