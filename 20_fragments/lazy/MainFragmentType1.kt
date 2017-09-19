
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView


class MainFragmentType1 : BaseLazyFragment() {
    var webView1: WebView? = null
    /**
     * 第一个webview是否load完毕
     */
    var webView1Loaded = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initViews(vararg args: Any?) {
        val m: Map<String, Object> = (args[0] as Array<Any>)[0] as Map<String, Object>
//        val tv1 = selfView.findViewById<TextView>(R.id.fmt1_tv)
//        tv1.text = m.get("display") as String
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