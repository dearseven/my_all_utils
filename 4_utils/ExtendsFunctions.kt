
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView

/**
 * Created by wx on 2017/7/25.
 */
/**
 *SDK>=26
 * 扩展方法

fun <T : View> View.findView(id: Int): T {
return findViewById(id)
}
 */

/* SDK 26以下
fun <T> View.findView(id: Int): T {
    return findViewById(id) as T
}
*/


fun TextView.setTextViewColor(/*tv: TextView,*/ colorId: Int, ctx: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextColor(ctx.resources.getColor(colorId, ctx.theme))
    } else {
        this.setTextColor(ctx.resources.getColor(colorId))
    }
}