package cyan.positioncameraa.utils.flood

import cyan.positioncameraa.utils.CSON
import cyan.positioncameraa.utils.Debug
import cyan.positioncameraa.utils.SimpleHttp
import cyan.positioncameraa.widget.AskDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.util.*

/**
 * Created by wx on 2018/5/4.
 */
class APIClient2 {
    object Holder {
        val instance = APIClient2()
    }

    companion object {
        fun getInstance(): APIClient2 {
            return Holder.instance
        }
    }

    /**
     * 只是测试的时候用了一下
     */
    fun postForTest(requestFun: (map: HashMap<String, Any>) -> Observable<ResponseBody>, paramMap: HashMap<String, Any>, h: WeakHandler, errFuc: (result: Flood2.TempResult, h: WeakHandler) -> Unit, sucFuc: (result: Flood2.TempResult) -> Unit) {
        val key = UUID.randomUUID()
        var tr = Flood2.TempResult()
        val o: Observable<ResponseBody> = requestFun(paramMap)
        o.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe({
            //Debug.log(APIClient2::class.java, "onAccept:\n${it.string()}")
            tr.httpcode = SimpleHttp.Result.CODE_SUCCESS
            tr.paramMap = paramMap
            tr.data = it.string()
            //判定返回的msgCode
            sucFuc(tr)
        }, {
            // Debug.log(APIClient2::class.java, "onError:\n${it.message}")
            //如果是error就是，默认一些小错误
            tr.httpcode = SimpleHttp.Result.CODE_EXCEPTION
            tr.paramMap = paramMap
            tr.data = CSON("{}")
            errFuc(tr, h)
        }, {
            // Debug.log(APIClient2::class.java, "on complete")
        })
    }


    fun post(requestFun: (map: HashMap<String, Any>) -> Observable<ResponseBody>, paramMap: HashMap<String, Any>, h: WeakHandler, errFuc: (result: Flood2.TempResult, h: WeakHandler) -> Unit, sucFuc: (result: Flood2.TempResult) -> Unit) {
        val key = UUID.randomUUID()
        var tr = Flood2.TempResult()
        val o: Observable<ResponseBody> = requestFun(paramMap)
        o.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe({
            // Debug.log(APIClient2::class.java, "onAccept:\n${it.string()}")
            tr.httpcode = SimpleHttp.Result.CODE_SUCCESS
            tr.paramMap = paramMap
            val cson = CSON(it.string())
            tr.data = cson//如果返回JSON String的情况下
            //判定返回的msgCode
            val msgCode = cson.getInt("msgCode")
            if (msgCode == 0) {
                sucFuc(tr)
            } else {
                errFuc(tr, h)
            }
        }, {
            // Debug.log(APIClient2::class.java, "onError:\n${it.message}")
            //如果是error就是，默认一些小错误
            tr.httpcode = SimpleHttp.Result.CODE_EXCEPTION
            tr.paramMap = paramMap
            tr.data = CSON("{}")
            errFuc(tr, h)
        }, {
            // Debug.log(APIClient2::class.java, "on complete")
        })
    }

    fun standardErrProcessWithTr(tr: Flood2.TempResult, h: WeakHandler, yes: (tr: Flood2.TempResult) -> Unit, no: (tr: Flood2.TempResult) -> Unit) {
        if (tr.httpcode != 0) {
            AskDialog().showConnectFailed(h.wr.get()!!, {
                if (it) {
                    yes(tr)
                } else {
                    no(tr)
                }
            })
        } else if (tr.flag != 0) {
            //这一段应后台设计的不同，会有不同的处理，因为这里就是显示错误嘛
            var errMsg: String? = (tr.data as CSON).getSpecificType("errMsg", String::class.java)
            if (errMsg == null) {
                //后台没有带过来错误消息文本，从其他配置文件里去
                errMsg = "未知异常"//errMsgMap[(tr.data as CSON).getSpecificType("msgCode", String::class.java)]
            }
            //
            AskDialog().showAPIFailed(if (errMsg == null) "" else errMsg, h.wr.get()!!, {
                if (it) {
                    yes(tr)
                } else {
                    no(tr)
                }
            })
        }
    }

}