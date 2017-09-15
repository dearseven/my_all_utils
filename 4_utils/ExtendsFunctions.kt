
import android.view.View

/**
 * Created by wx on 2017/7/25.
 */
/**
 *SDK>=26
 * 扩展方法
 */
fun <T : View> View.findView(id: Int): T {
    return findViewById(id)
}


/* SDK 26以下
fun <T> View.findView(id: Int): T {
    return findViewById(id) as T
}
*/
