
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

import kotlinx.android.synthetic.main.base_toolbar_for_index.*
import android.view.MotionEvent


/**
 * 下单的第二步
 * in_order_step2_see_order_layout.xml用于popupwindow
 */
class OrderStep2ChooseDriverActivity : BaseCompatActivity() {
    private var isFirst = true
    private var __p: P_OrderStep2ChooseDriverActivity? = null

    override fun getLayoutId(): Int = R.layout.activity_order_step2_choose_driver

    override fun getMenuId(): Int = 0

    override fun whenOptionsItemSelected(item: MenuItem?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar_subtitle.text = ""
        //
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (isFirst) {
            isFirst = false
            __p = P_OrderStep2ChooseDriverActivity(this@OrderStep2ChooseDriverActivity)
            __p!!.initView()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return if (__p!!.pop != null && __p!!.pop!!.isShowing()) {
            __p!!.pop!!.dismiss()
            false
        } else super.dispatchTouchEvent(event)
    }
}
