package cc.m2u.intelliv.activity.appstore.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cc.m2u.intelliv.activity.appstore.bean.Apps
import cc.m2u.intelliv.activity.appstore.radapters.GridRecyclerViewAdapter
import cc.m2u.intelliv.activity.appstore.radapters.ViewKeeper
import com.vrv.intelliv.R

/**
 * app_more_large_item_view
 */
class EditFragment : BaseLazyFragment {
    //顶部的我的
    var rvTopMine: RecyclerView? = null

    companion object {
        val TAG = EditFragment::class.java.name
    }

    constructor() {}
//    var mview: View? = null
//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        mview = inflater!!.inflate(R.layout.apps_edit_fragment_layout, container, false)
//        return mview
//    }

    override fun initViews(vararg args: Any?) {
        rvTopMine = selfView.findViewById(R.id.appMoreMangeEditRv)
        //
        initMine()
    }

    private fun initMine() {
        var apps = Apps.getApps(Apps.AppsType.assistant);
        var adapter = GridRecyclerViewAdapter(activity.applicationContext, R.layout.app_more_large_item_view, object : GridRecyclerViewAdapter.CallBackFunc {
            override fun viewCreate(holder: ViewKeeper?, position: Int, datas: ArrayList<Any>) {
                datas as ArrayList<Apps>
                var img = holder!!._itemView!!.findViewById<ImageView>(R.id.app_more_large_item_view_icon)
                img.setImageResource(datas[position].icon)
                var txt = holder!!._itemView!!.findViewById<TextView>(R.id.app_more_large_item_view_text)
                txt.text = datas[position].name
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

    override fun getLayoutId(): Int = R.layout.apps_edit_fragment_layout;
}