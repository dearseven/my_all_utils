
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cc.m2u.ifengbigdata.R
import cc.m2u.ifengbigdata.fragments.BaseLazyFragment
import cc.m2u.ifengbigdata.utils.DLog

class MainFragmentType1 : BaseLazyFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        DLog.log(javaClass,"123")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initViews(vararg args: Any?) {
        val m: Map<String, Object> = (args[0] as Array<Any>)[0] as Map<String, Object>
        val tv1 = selfView.findViewById<TextView>(R.id.fmt1_tv)
        tv1.text = m.get("display") as String
    }

    override fun intoFragment(vararg args: Any?) {
        if (hasInited == false) {
            hasInited = true;
            initViews(args)
        }
    }

    override fun onBackFilter(obj: Any?): Boolean {
        return super.onBackFilter(obj)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_type1
    }

}