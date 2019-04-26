
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_summary_order_info.*
import kotlinx.android.synthetic.main.activity_summary_user_growth.*
import kotlinx.android.synthetic.main.base_toolbar.*
import org.json.JSONArray
import org.json.JSONObject

class SummaryOrderInfoActivity : BaseCompatActivity(), CWebView.CWebViewEventHandler {

    override fun getLayoutId(): Int = R.layout.activity_summary_order_info

    /**
     * webview的状态 1加载成功 -1加载失败 0初始化
     */
    var webView1Loaded = 0

    //--START CWEBVIEW CALLBACK
    override fun onPageFinshed(view: CWebView?, loadError: Boolean) {
        if (!loadError) {//没错
            webView1Loaded = 1
            asoi_stbt1.visibility = View.GONE
            asoi_user_new.setOnClickListener {
                asoi_user_new_line.visibility = View.VISIBLE
                asoi_user_active_line.visibility = View.INVISIBLE
                asoi_user_start_line.visibility = View.INVISIBLE
                jsCall(1)
            }
            asoi_user_active.setOnClickListener {
                asoi_user_new_line.visibility = View.INVISIBLE
                asoi_user_active_line.visibility = View.VISIBLE
                asoi_user_start_line.visibility = View.INVISIBLE
                jsCall(2)
            }
            asoi_user_start.setOnClickListener {
                asoi_user_new_line.visibility = View.INVISIBLE
                asoi_user_active_line.visibility = View.INVISIBLE
                asoi_user_start_line.visibility = View.VISIBLE
                jsCall(3)
            }
            jsCall(1)
        } else {
            asoi_stbt1.visibility = View.VISIBLE
            asoi_stbt1.setText(resources.getString(R.string.jiazaishibaileo))
            asoi_stbt1.setOnClickListener {
                webView1Loaded = 0
                asoi_stbt1.setText(resources.getString(R.string.zhengzaijiazai))
                view!!.loadUrl(view.currentUrl)
            }
        }
    }


    private fun jsCall(i: Int) {
        val info = AdminInfo.toInstance(this)
        val param = MapToHttpParam.toParamStringNotAddElse(
                mapOf(
                        "adminID" to info.adminId,
                        (if (daysMode == 0) "days" else "beginDay") to selectedDays,
                        "channels" to selectedChannelsId
                ))
        DLog.log(javaClass, "load function param:$param")
        asoi_wv1.loadUrl("javascript:appToJs($i,\"$param\")")
    }

    override fun onStartLoad(view: CWebView?, url: String?) {
    }

    override fun loadProgress(view: CWebView?, newProgress: Int) {
    }
//--end CWEBVIEW CALLBACK

  
}
