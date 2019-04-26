package cyan.positioncameraa.activities.subs

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import cyan.positioncameraa.R
import cyan.positioncameraa.activities.BaseParentActivity
import cyan.positioncameraa.databinding.ActivityMainBinding
import cyan.positioncameraa.models.Welcome
import cyan.positioncameraa.presenters.subs.LauncherPresenter
import cyan.positioncameraa.utils.MMap
import cyan.positioncameraa.utils.flood.APIClient2
import cyan.positioncameraa.utils.flood.WeakHandler
import cyan.positioncameraa.utils.httputils.ClientAPIUtil
import cyan.positioncameraa.widget.LoadingDiaglog
import kotlinx.android.synthetic.main.base_toolbar_show_back_as_text.*

import java.lang.ref.WeakReference

class MainActivity : BaseParentActivity<ActivityMainBinding, LauncherPresenter>() {
    override fun getMenuId(): Int = R.menu.main

    override fun whenOptionsItemSelected(item: MenuItem) {
    }

    override fun get_Presenter(): LauncherPresenter? {
        return LauncherPresenter(this)
    }

    override fun get_LayoutId(): Int = R.layout.activity_main

    var loading: LoadingDiaglog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar_subtitle.text = ""
        toolbar_title.text = "Main!"
    }

    fun some() {
        loading = LoadingDiaglog()

//        h!!.get()!!.postDelayed({
//            val welcomeData = Welcome();
//            welcomeData.setWelcome("Cyan!!!");
//            welcomeData.setName("Hello~~~");
//            //把对象放到binding的变量上
//            vdBing!!.setWelcome(welcomeData)
//        }, 3000)
        //
        loading!!.showDialog(this)
        //转1.5秒以后再显示~
        wh!!.postDelayed({
            doGet()
        }, 1500)

    }

    private fun doGet() {
        val client = APIClient2.getInstance()
        client.postForTest({
            ClientAPIUtil.getInstance().seeBaidu(it)
        }, MMap().put("wd", "2").get(), wh, { errTr, wh -> loading!!.hideDialog() }, { tr ->
            loading!!.hideDialog()
            val welcomeData = Welcome();
            welcomeData.setWelcome("Cyan!!!");
            welcomeData.setName(tr.data as String);
            //把对象放到binding的变量上
            vdBing!!.setWelcome(welcomeData)
        })
    }

    val h = object : WeakReference<Handler>(Handler()) {

    }

    val wh = object : WeakHandler(this@MainActivity) {}


}
