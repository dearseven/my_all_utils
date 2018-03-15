
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow

import java.lang.ref.WeakReference

/**
 * Created by WX on 2018/3/15.
 */
class P_OrderStep2ChooseDriverActivity {
    var pop: PopupWindow? = null
    //
    var inflate: LayoutInflater? = null
    //
    var ctx: WeakReference<OrderStep2ChooseDriverActivity>? = null

    constructor(activity: OrderStep2ChooseDriverActivity) {
        ctx = WeakReference(activity)
    }

    /**
     * 初始化views
     *
     */
    fun initView() {
        inflate = LayoutInflater.from(ctx!!.get()!!)
        initPopupWindow()

    }

    /**
     * 初始化查看订单的popupwindow
     */
    private fun initPopupWindow() {
        val contentView = inflate!!.inflate(R.layout.in_order_step2_see_order_layout, null, false)
        pop = PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop!!.isFocusable = true
        pop!!.isTouchable = false
        pop!!.isOutsideTouchable = true
        //
        val cal = ViewPositionAndSizeGetter().calculate(ctx!!.get()!!.aOrderStep2ShowOrder, ctx!!.get(), ctx!!.get())
        ctx!!.get()!!.aOrderStep2ShowOrder.setOnClickListener {
            pop!!.showAsDropDown(ctx!!.get()!!.aOrderStep2ShowOrder, 0, -cal.h)
        }
        //
        pop!!.setOnDismissListener {
        }
    }
}