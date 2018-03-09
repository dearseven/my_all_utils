
import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * app_more_itemview_layout_mine.xml
 */
class ListRecyclerViewAdapter : RecyclerView.Adapter<ViewKeeper> {
    interface CallBackFunc {
        abstract fun viewCreate(holder: ViewKeeper?, position: Int, datas: ArrayList<Any>)
    }

    var callBack: CallBackFunc? = null;
    var ctx: Context? = null
    var resLayout: Int? = null
    var inflater: LayoutInflater? = null

    override fun getItemCount(): Int = datas!!.size

    override fun onBindViewHolder(holder: ViewKeeper?, position: Int) {
        callBack!!.viewCreate(holder, position, datas!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewKeeper {
        return ViewKeeper(inflater!!.inflate(resLayout!!, parent, false))
    }

    var datas: ArrayList<Any>? = null

//    /**
//     * 添加数据
//     */
//    fun addDatas(datas: ArrayList<out Any>, isAppend: Boolean) {
//        if (!isAppend) {
//            this.datas.clear()
//        }
//        datas.forEach {
//            this.datas.add(it)
//        }
//    }

    fun remove(int: Int) {
        this.datas!!.remove(this.datas!![int])
        notifyItemRemoved(int)
        notifyItemRangeChanged(0, this.datas!!.size);
    }

    /**
     * @param callBackFunc 在adapter的onBindViewHolder里回调
     * @param 列数
     */
    constructor(ctx: Context, resLayout: Int, callBackFunc: CallBackFunc, view: RecyclerView) {
        this.ctx = ctx;
        this.resLayout = resLayout;
        this.inflater = LayoutInflater.from(ctx)
        this.callBack = callBackFunc

        //
        val linearLayoutManager = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
        view.setLayoutManager(linearLayoutManager)
        view.addItemDecoration(DividerGridItemDecoration(ctx))
        view.itemAnimator = DefaultItemAnimator()
    }
}