
import android.support.v7.widget.RecyclerView
import android.view.View

class ViewKeeper(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var _itemView: View? = null

    init {
        _itemView = itemView
    }
}