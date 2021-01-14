
import android.os.Bundle
import android.view.MenuItem


import kotlinx.android.synthetic.main.base_toolbar_for_index.*

/**
 * 下单的时候预约时间
 * 上方的itemview
 * date_select_item_layout.xml
 * 下方的itemview
 * select_time_item_layout.xml
 */
class DateTimeSelectActivity : BaseCompatActivity(), V_DateTimeSelectActivity {
    override fun getLayoutId(): Int = R.layout.activity_date_time_select

    override fun getMenuId(): Int = 0

    override fun whenOptionsItemSelected(item: MenuItem?) {
    }

    private var __p: P_DateTimeSelectActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar_subtitle.text = ""
        __p=P_DateTimeSelectActivity(this@DateTimeSelectActivity)

        __p!!.initView()
    }

}
