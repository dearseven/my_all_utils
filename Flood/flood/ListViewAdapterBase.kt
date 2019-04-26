
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * Created by wx on 2017/12/6.
 */
class ListViewAdapterBase : BaseAdapter {
    /**
     * 让DataBean实现这个接口
     */
    interface DataBeanType {}

    private var keeper: ItemViewKeeper? = null
//        set(value) {
//            keeper = value
//        }
//        get() = field

    var resId: Int = 0
    var ctx: Context? = null
    var inflater: LayoutInflater? = null

    constructor(resId: Int, kpr: ItemViewKeeper, ctx: Context) : super() {
        this.resId = resId
        this.keeper = kpr
        this.ctx = ctx
        inflater = LayoutInflater.from(ctx)
    }

    private constructor() {}

    /**
     * 创建View（getView）的时候会回调在方法，一定要实现这个接口啊
     */
    interface ItemViewKeeper {
        fun createView(i: Int, view: View, any: Any)
    }

    private val data by lazy {
        ArrayList< DataBeanType>()
    }

    /**
     * 添加数据
     */
    fun addDatas(datas: ArrayList<out DataBeanType>, isAppend: Boolean) {
        if (!isAppend)
            datas.clear()
        datas.forEach { data.add(it) }
    }

    override fun getView(i: Int, v: View?, vg: ViewGroup?): View {
        var view: View? = v
        if (view == null) {
            view = inflater!!.inflate(resId, vg, false)
        }
        if (keeper != null) {
            keeper!!.createView(i, view!!, data[i])
        }
        return view!!
    }

    override fun getItem(i: Int): DataBeanType = data[i]

    override fun getItemId(i: Int): Long = i.toLong()

    override fun getCount(): Int = data.size


}