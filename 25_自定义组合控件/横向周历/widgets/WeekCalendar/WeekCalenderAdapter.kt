
import android.graphics.Color
import android.os.Build
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import kotlinx.android.synthetic.main.date_select_item_layout.view.*
import java.lang.ref.WeakReference
import java.util.*

/**
 * 上方那个日期滚动的Viewpager的适配器
 * Created by wx on 2018/3/12.
 */
class WeekCalenderAdapter : PagerAdapter {
    private val TOTAL_WEEKS = 52 * 3 //初始化数据的总周数，也是总页面数
    //当前所在的页面
    private var currentPage = 0
    //所中的索引所在的页面
    private var selectedPage = -1
    //选中的索引
    private var selectedIndex = -1

    private val today = Calendar.getInstance()
    //下一次循环开始的date
    private var beginDay = today
    //
    private var ctx: WeakReference<BaseCompatActivity>? = null
    //
    private var inflate: LayoutInflater? = null
    //
    private var weekCalendarChangeListener: WeekCalendarOnChange? = null

    //数据
    private val dates by lazy {
        ArrayList<WeekBean>()
    }

    fun getAdapterDates(): ArrayList<WeekBean> {
        return dates
    }

    constructor(activity: BaseCompatActivity, weekCalendarChangeListener: WeekCalendarOnChange) {
        ctx = WeakReference(activity)
        this.weekCalendarChangeListener = weekCalendarChangeListener
        inflate = LayoutInflater.from(ctx!!.get())
        initCalaner()
        weekCalendarChangeListener.onChange(dates[0], -1, -1)
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean = view == `object`

    override fun getCount(): Int = dates.size

    override fun destroyItem(container: ViewGroup?, position: Int, _object: Any?) {
        container!!.removeView(_object as View)
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val view = inflate!!.inflate(R.layout.date_select_item_layout, null)
        container!!.addView(view)
        val wb = dates[position]
        //
        val tv1 = view.findViewById<TextView>(R.id.dateSelectItemTV1)
        tv1.text = wb.days[0].toString()
        val tv2 = view.findViewById<TextView>(R.id.dateSelectItemTV2)
        tv2.text = wb.days[1].toString()
        val tv3 = view.findViewById<TextView>(R.id.dateSelectItemTV3)
        tv3.text = wb.days[2].toString()
        val tv4 = view.findViewById<TextView>(R.id.dateSelectItemTV4)
        tv4.text = wb.days[3].toString()
        val tv5 = view.findViewById<TextView>(R.id.dateSelectItemTV5)
        tv5.text = wb.days[4].toString()
        val tv6 = view.findViewById<TextView>(R.id.dateSelectItemTV6)
        tv6.text = wb.days[5].toString()
        val tv7 = view.findViewById<TextView>(R.id.dateSelectItemTV7)
        tv7.text = wb.days[6].toString()
        //
        val iv1 = view.findViewById<ImageView>(R.id.dateSelectItemIV1)
        val iv2 = view.findViewById<ImageView>(R.id.dateSelectItemIV2)
        val iv3 = view.findViewById<ImageView>(R.id.dateSelectItemIV3)
        val iv4 = view.findViewById<ImageView>(R.id.dateSelectItemIV4)
        val iv5 = view.findViewById<ImageView>(R.id.dateSelectItemIV5)
        val iv6 = view.findViewById<ImageView>(R.id.dateSelectItemIV6)
        val iv7 = view.findViewById<ImageView>(R.id.dateSelectItemIV7)
        //
        if (wb.days[0] != 0) {
            iv1.setOnClickListener {
                selectedPage = position
                selectedIndex = 0
                showSelect(0, view)
                weekCalendarChangeListener!!.onChange(wb, selectedPage, selectedIndex)
            }
        } else {
            tv1.text = ""
            iv1.setImageResource(R.drawable.circle_date_select_bg_disable)
        }
        if (wb.days[1] != 0) {
            iv2.setOnClickListener {
                selectedPage = position
                selectedIndex = 1
                showSelect(1, view)
                weekCalendarChangeListener!!.onChange(wb, selectedPage, selectedIndex)
            }
        } else {
            tv2.text = ""
            iv2.setImageResource(R.drawable.circle_date_select_bg_disable)
        }
        if (wb.days[2] != 0) {
            iv3.setOnClickListener {
                selectedPage = position
                selectedIndex = 2
                showSelect(2, view)
                weekCalendarChangeListener!!.onChange(wb, selectedPage, selectedIndex)
            }
        } else {
            tv3.text = ""
            iv3.setImageResource(R.drawable.circle_date_select_bg_disable)
        }
        if (wb.days[3] != 0) {
            iv4.setOnClickListener {
                selectedPage = position
                selectedIndex = 3
                showSelect(3, view)
                weekCalendarChangeListener!!.onChange(wb, selectedPage, selectedIndex)
            }
        } else {
            tv4.text = ""
            iv4.setImageResource(R.drawable.circle_date_select_bg_disable)
        }
        if (wb.days[4] != 0) {
            iv5.setOnClickListener {
                selectedPage = position
                selectedIndex = 4
                showSelect(4, view)
                weekCalendarChangeListener!!.onChange(wb, selectedPage, selectedIndex)
            }
        } else {
            tv5.text = ""
            iv5.setImageResource(R.drawable.circle_date_select_bg_disable)
        }
        if (wb.days[5] != 0) {
            iv6.setOnClickListener {
                selectedPage = position
                selectedIndex = 5
                showSelect(5, view)
                weekCalendarChangeListener!!.onChange(wb, selectedPage, selectedIndex)
            }
        } else {
            tv6.text = ""
            iv6.setImageResource(R.drawable.circle_date_select_bg_disable)
        }
        if (wb.days[6] != 0) {
            iv7.setOnClickListener {
                selectedPage = position
                selectedIndex = 6
                showSelect(6, view)
                weekCalendarChangeListener!!.onChange(wb, selectedPage, selectedIndex)
            }
        } else {
            tv7.text = ""
            iv7.setImageResource(R.drawable.circle_date_select_bg_disable)
        }
        //因为控件会回收，这里用一个position也就是页面号（数据索引号）来标识
        view.setTag(R.id.view_content_id, position)
        return view
    }

    /**
     * 展示显示效果啊
     */
    private fun showSelect(i: Int, view: View) {
        val tv1 = view.findViewById<TextView>(R.id.dateSelectItemTV1)
        val tv2 = view.findViewById<TextView>(R.id.dateSelectItemTV2)
        val tv3 = view.findViewById<TextView>(R.id.dateSelectItemTV3)
        val tv4 = view.findViewById<TextView>(R.id.dateSelectItemTV4)
        val tv5 = view.findViewById<TextView>(R.id.dateSelectItemTV5)
        val tv6 = view.findViewById<TextView>(R.id.dateSelectItemTV6)
        val tv7 = view.findViewById<TextView>(R.id.dateSelectItemTV7)
        //
        val iv1 = view.findViewById<ImageView>(R.id.dateSelectItemIV1)
        val iv2 = view.findViewById<ImageView>(R.id.dateSelectItemIV2)
        val iv3 = view.findViewById<ImageView>(R.id.dateSelectItemIV3)
        val iv4 = view.findViewById<ImageView>(R.id.dateSelectItemIV4)
        val iv5 = view.findViewById<ImageView>(R.id.dateSelectItemIV5)
        val iv6 = view.findViewById<ImageView>(R.id.dateSelectItemIV6)
        val iv7 = view.findViewById<ImageView>(R.id.dateSelectItemIV7)
        //
        val wb = dates[currentPage]
        if (wb.days[0] != 0)
            iv1.setImageResource(R.drawable.circle_date_select_bg)
        if (wb.days[1] != 0)
            iv2.setImageResource(R.drawable.circle_date_select_bg)
        if (wb.days[2] != 0)
            iv3.setImageResource(R.drawable.circle_date_select_bg)
        if (wb.days[3] != 0)
            iv4.setImageResource(R.drawable.circle_date_select_bg)
        if (wb.days[4] != 0)
            iv5.setImageResource(R.drawable.circle_date_select_bg)
        if (wb.days[5] != 0)
            iv6.setImageResource(R.drawable.circle_date_select_bg)
        if (wb.days[6] != 0)
            iv7.setImageResource(R.drawable.circle_date_select_bg)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (wb.days[0] != 0)
                tv1.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black, ctx!!.get()!!.theme))
            if (wb.days[1] != 0)
                tv2.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black, ctx!!.get()!!.theme))
            if (wb.days[2] != 0)
                tv3.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black, ctx!!.get()!!.theme))
            if (wb.days[3] != 0)
                tv4.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black, ctx!!.get()!!.theme))
            if (wb.days[4] != 0)
                tv5.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black, ctx!!.get()!!.theme))
            if (wb.days[5] != 0)
                tv6.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black, ctx!!.get()!!.theme))
            if (wb.days[6] != 0)
                tv7.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black, ctx!!.get()!!.theme))
        } else {
            if (wb.days[0] != 0)
                tv1.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black))
            if (wb.days[1] != 0)
                tv2.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black))
            if (wb.days[2] != 0)
                tv3.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black))
            if (wb.days[3] != 0)
                tv4.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black))
            if (wb.days[4] != 0)
                tv5.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black))
            if (wb.days[5] != 0)
                tv6.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black))
            if (wb.days[6] != 0)
                tv7.setTextColor(ctx!!.get()!!.resources.getColor(R.color.middle_black))
        }
        when (i) {
            0 -> {
                iv1.setImageResource(R.drawable.circle_date_select_bg_selected)
                tv1.setTextColor(Color.parseColor("#FFFFFF"))
            }
            1 -> {
                iv2.setImageResource(R.drawable.circle_date_select_bg_selected)
                tv2.setTextColor(Color.parseColor("#FFFFFF"))
            }
            2 -> {
                iv3.setImageResource(R.drawable.circle_date_select_bg_selected)
                tv3.setTextColor(Color.parseColor("#FFFFFF"))
            }
            3 -> {
                iv4.setImageResource(R.drawable.circle_date_select_bg_selected)
                tv4.setTextColor(Color.parseColor("#FFFFFF"))
            }
            4 -> {
                iv5.setImageResource(R.drawable.circle_date_select_bg_selected)
                tv5.setTextColor(Color.parseColor("#FFFFFF"))
            }
            5 -> {
                iv6.setImageResource(R.drawable.circle_date_select_bg_selected)
                tv6.setTextColor(Color.parseColor("#FFFFFF"))
            }
            6 -> {
                iv7.setImageResource(R.drawable.circle_date_select_bg_selected)
                tv7.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }
    }

    /**
     * 这个方法被viewpaper监听OnPageChangeListener调用
     * ,
     * 主要是找到正确的显示view，然后处理显示
     */
    fun whenPageSeleted(vp: ViewPager, pageIndex: Int) {
        currentPage = pageIndex
        (0..vp.childCount - 1).forEach {
            val view = vp.getChildAt(it)
            val tag = view.getTag(R.id.view_content_id) as Int
            //上面取tag是因为涉及到了控件回收，所以其实在创建的时候就给每一个view一个独立的tag（页面的编号）
            //这里通过这个来识别view
            if (tag == pageIndex) {
                if (pageIndex == selectedPage) {
                    showSelect(selectedIndex, view)
                } else {
                    //一个都没有选中呗
                    showSelect(-1, view)
                }
            }
        }
        weekCalendarChangeListener!!.onChange(dates[currentPage], selectedPage, selectedIndex)
    }

    /**
     * 如果可能，跳到上一个月
     */
    fun preMonth(vp: ViewPager) {
        val wb = dates[currentPage]
        val m = wb.m
        for (i in 0..currentPage - 1) {
            val _wb = dates[i]
            val _m = _wb.m
            if (m == 1 && _m == 12 && wb.y - _wb.y == 1) {//当前月是1的话，那么前置月是12
                vp.setCurrentItem(i, true)
                break
            } else {
                if (m - _m == 1&& wb.y == _wb.y ) {//普通情况就是比当前月小1
                    vp.setCurrentItem(i, true)
                    break
                }
            }
        }
    }

    /**
     * 如果可能跳到下一个月
     */
    fun nextMonth(vp: ViewPager) {
        val wb = dates[currentPage]
        val m = wb.m //找到第一个比这个大1的就行了
        //var index = -1
        for (i in currentPage..dates.size - 1) {
            val _wb = dates[i]
            val _m = _wb.m
            if (m == 12 && _m == 1) {//当前月是12的话，那么后置月是1
                vp.setCurrentItem(i, true)
                break
            } else {
                if (_m - m == 1) {//普通情况就是比当前月大1
                    vp.setCurrentItem(i, true)
                    break
                }
            }
        }
    }

    /**
     * 如果可能，上一周
     */
    fun preWeek(vp: ViewPager) {
        if (currentPage > 0) {
            vp.setCurrentItem(currentPage - 1, true)
        }
    }

    /**
     * 如果可能，下一周
     */
    fun nextWeek(vp: ViewPager) {
        if (currentPage < dates.size - 1) {
            vp.setCurrentItem(currentPage + 1, true)
        }
    }
    //-----------------------------适配器的工具方法-----------------------------------------------------
    /**
     * 初始化数据
     */
    private fun initCalaner() {
        (1..TOTAL_WEEKS).forEach {
            if (it == 1) {
                //第一周
                calculateDates()
            } else {
                calculateDates()
            }
        }
        //循环数据
    }

    /**
     * 生成数据
     */
    private fun calculateDates() {
        //初始化数据bean
        val wb = WeekBean()
        // 通过beginDay获取年月
        //val c = Calendar.getInstance()
        //DLog.log(javaClass,"begin day:${beginDay}")
        wb.y = beginDay.get(Calendar.YEAR)
        wb.m = beginDay.get(Calendar.MONTH) + 1
        //获取第一天是星期几//其实一般都是周一，但是如果是第一次循环，则不一定是星期一
        val weekday = getWeekDay(beginDay.get(Calendar.DAY_OF_WEEK))
        //循环7次
        (0..6).forEach {
            if (it < weekday.ordinal) {//小于今天的过滤掉，只有外层大循环（第一次调用calculateDates方法）第一次循环有这种情况
                wb.setDays(it, 0)
            } else if (it == weekday.ordinal) {
                wb.setDays(it, beginDay.get(Calendar.DAY_OF_MONTH))
            } else {
                //正式填入数据
                //DLog.log(javaClass,"${it - weekday.ordinal}")
                beginDay.add(Calendar.DAY_OF_YEAR, 1)//it - weekday.ordinal)
                if (beginDay.get(Calendar.MONTH) == wb.m - 1) {
                    wb.setDays(it, beginDay.get(Calendar.DAY_OF_MONTH))
                } else {
                    beginDay.add(Calendar.DAY_OF_YEAR, -1)
                }
            }
        }
        dates.add(wb)
        //循环外，date跳到下周去
        beginDay.add(Calendar.DATE, 1)
    }


    enum class WeekDay {
        mon, tues, weds, thur, fri, sat, sun
    }

    /**
     * 通过日历返回的day of week 获取自己习惯的顺序
     */
    private fun getWeekDay(calendarDayOfWeek: Int): WeekDay {
        if (calendarDayOfWeek == 1) {
            return WeekDay.sun
        } else if (calendarDayOfWeek == 2) {
            return WeekDay.mon
        } else if (calendarDayOfWeek == 3) {
            return WeekDay.tues
        } else if (calendarDayOfWeek == 4) {
            return WeekDay.weds
        } else if (calendarDayOfWeek == 5) {
            return WeekDay.thur
        } else if (calendarDayOfWeek == 6) {
            return WeekDay.fri
        } else {
            return WeekDay.sat
        }
    }
}