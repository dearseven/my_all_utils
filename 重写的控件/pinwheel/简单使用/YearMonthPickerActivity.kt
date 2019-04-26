
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.MenuItem
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_year_month_picker.*
import java.util.*

class YearMonthPickerActivity : BaseCompatActivity(), OnWheelChangedListener, OnWheelScrollListener {
    var y = 0 //索引
    var m = 0 //索引
    override fun onScrollingFinished(wheel: WheelView?) {
        if (wheel!!.id == R.id.aymp_y) {
            DLog.log(YearMonthPickerActivity::class.java, "${startY+y} 年")
        } else if (wheel!!.id == R.id.aymp_m) {
            DLog.log(YearMonthPickerActivity::class.java, "${m+1} 月")
        }
    }

    override fun onScrollingStarted(wheel: WheelView?) {
    }

    override fun onChanged(wheel: WheelView?, oldValue: Int, newValue: Int) {
        //给的是索引
        if (wheel!!.id == R.id.aymp_y) {
            y = newValue
        } else if (wheel!!.id == R.id.aymp_m) {
            m = newValue
        }
    }

    val startY = 2000
    override fun getLayoutId(): Int {
        return R.layout.activity_year_month_picker
    }

    override fun getMenuId(): Int {
        return 0
    }

    override fun whenOptionsItemSelected(item: MenuItem?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //获得结束年
        val endY = Calendar.getInstance().get(Calendar.YEAR)

        aymp_y.setAdapter(NumbericWheelAdapter(startY, endY));
        aymp_y.setLabel("年");
        aymp_y.setCyclic(false);
        aymp_y.addChangingListener(this);
        aymp_y.addScrollingListener(this);

        aymp_m.setAdapter(NumbericWheelAdapter(1, 12));
        aymp_m.setLabel("月");
        aymp_m.setCyclic(false);
        aymp_m.addChangingListener(this);
        aymp_m.addScrollingListener(this);

        h.postDelayed({
            aymp_y.setCurrentItem(endY - startY, true)
            aymp_m.setCurrentItem(Calendar.getInstance().get(Calendar.MONTH), true)
        }, 300)

    }

    val h = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
        }
    }
}
