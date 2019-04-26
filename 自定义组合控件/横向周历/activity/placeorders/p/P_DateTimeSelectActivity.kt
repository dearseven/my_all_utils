
import android.support.v4.view.ViewPager

import kotlinx.android.synthetic.main.activity_date_time_select.*
import java.lang.ref.WeakReference

/**
 * Created by wx on 2018/3/13.
 */
class P_DateTimeSelectActivity : WeekCalendarOnChange {
    override fun onChange(weekBean: WeekBean, selectPageIndex: Int, selectItemIndex: Int) {
        DLog.log(javaClass, "${weekBean} || ${selectPageIndex} || ${selectItemIndex}")
        //在这里控制界面显示
    }

    var ctx: WeakReference<DateTimeSelectActivity>? = null

    constructor(activity: DateTimeSelectActivity) {
        ctx = WeakReference(activity)
    }

    //周历适配器
    private var weekCalendarAdatepr: WeekCalenderAdapter? = null

    //初始控件
    fun initView() {
        //初始化上方周历
        initWeekCalendar()
    }

    /**
     * 对周历做初始化
     */
    private fun initWeekCalendar() {
        weekCalendarAdatepr = WeekCalenderAdapter(ctx!!.get()!!, this)
        ctx!!.get()!!.aDateTimeSelectWeekCalander.adapter = weekCalendarAdatepr
        weekCalendarAdatepr!!.notifyDataSetChanged()
        ctx!!.get()!!.aDateTimeSelectWeekCalander.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                weekCalendarAdatepr!!.whenPageSeleted(ctx!!.get()!!.aDateTimeSelectWeekCalander, position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        //ctx!!.get()!!.aDateTimeSelectWeekCalander.setCurrentItem(0)
        //注册按月上下跳转的事件
        ctx!!.get()!!.aDateTimeSelectPreMonth.setOnClickListener {
            weekCalendarAdatepr!!.preMonth(ctx!!.get()!!.aDateTimeSelectWeekCalander)
        }
        ctx!!.get()!!.aDateTimeSelectNextMonth.setOnClickListener {
            weekCalendarAdatepr!!.nextMonth(ctx!!.get()!!.aDateTimeSelectWeekCalander)
        }
        //注册上下周的事件
        ctx!!.get()!!.aDateTimeSelectLeftArrow2.setOnClickListener {
            weekCalendarAdatepr!!.preWeek(ctx!!.get()!!.aDateTimeSelectWeekCalander)
        }
        ctx!!.get()!!.aDateTimeSelectRightArrow2.setOnClickListener {
            weekCalendarAdatepr!!.nextWeek(ctx!!.get()!!.aDateTimeSelectWeekCalander)
        }
    }
}