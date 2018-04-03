
/**
 * Created by wx on 2018/3/13.
 */
interface WeekCalendarOnChange {
    /**
     * @weekBean 当前的数据
     * @selectPageIndex 选中的页面编号，也就是选中的数据编号
     * @selectItemIndex 选中的数据编号中的日期的索引号
     * y,m.d年月日
     */
    abstract fun onChange(weekBean: WeekBean, selectPageIndex: Int, selectItemIndex: Int, y: Int, m: Int, d: Int)
}