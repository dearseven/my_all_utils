
import android.util.Log
import android.view.View
import android.widget.Toast
import com.teetaa.outsourcing.carinspection.R

/**
 * 把按钮事件绑定在这里可以通过统一设置tag等进行拦截，id进行拦截行为
 */
class ClickEventHandler {
    companion object {
        val instance = Holder.ClickEventHandler
    }

    private constructor()

    private object Holder {
        val ClickEventHandler = ClickEventHandler()
    }


    fun beforeClick(view: View): Boolean {
        var next = true//设置为false就拦截啊
        //可以拦截
        if (view.id == R.id.MainTxt1) {
            Toast.makeText(view.context, "setClickEventHandler--MainTxt1", Toast.LENGTH_SHORT)
                .show()
            //next = true
        } else if (view.id == R.id.MainTxt3) {
            Toast.makeText(view.context, "setClickEventHandler--MainTxt3", Toast.LENGTH_SHORT)
                .show()
            //next = false
        }
        //
        return next
    }

    fun endClick(view: View) {
        Log.i("ClickEventHandler", "${view} endClick")
    }
}

/**
MainTxt1.setClickEventHandler { v ->
startFrontServiceAndLocate()
}
//这可以用 it访问View对象
MainTxt3.setClickEventHandler {
openOtherMap()
}
 */
fun View.setClickEventHandler(clicked: (View) -> Unit) {
    this.setOnClickListener {
        if (ClickEventHandler.instance.beforeClick(this)) {
            clicked(this)
        }
        ClickEventHandler.instance.endClick(this)
    }
}