
import android.os.Handler
import cc.m2u.hidrogen.utils.CSON
import cc.m2u.hidrogen.utils.Configs
import cc.m2u.hidrogen.utils.SimpleHttp
import cc.m2u.hidrogen.utils.WeakHandler

/**
 * Created by wx on 2017/11/30.
 */
class APIClient {
    object Holder {
        val instance = APIClient()
    }

    companion object {
        fun getInstance(): APIClient {
            return Holder.instance
        }
    }

    /**
     * 执行方法
     */
    fun post(url: String, param: String, h: Handler, func: (result: Flood2.TempResult) -> Unit) {
        TotalAsynRun.getInstance()._run(Flood2()) {
            it.runSimpleHttp(Configs.RUN_AT_THREAD_TIMEOUT) {
                SimpleHttp.post(url, param)
            }.runNext(Configs.RUN_AT_THREAD_TIMEOUT, Flood2.TempResult()) {
                val temp = Flood2.TempResult();
                val rs = it._get<SimpleHttp.Result>()
                temp.httpcode = rs.returnCode
                if (rs.returnCode == SimpleHttp.Result.CODE_SUCCESS) {
                    val cson = CSON(rs.retString);
                    val msgCode = cson.getSpecificType("msgCode", String::class.java).toInt()
                    temp.flag = msgCode
                    temp.data = cson
                }
                temp
            }.runAtUI(h) {
                func(it._get())
            }
        }
    }
}