package cyan.positioncameraa.utils.flood

import android.os.Handler
import cyan.positioncameraa.utils.CSON
import cyan.positioncameraa.utils.SimpleHttp
import cyan.positioncameraa.widget.AskDialog
import java.util.*


/**
 * Created by wx on 2017/11/30.
 */
@Deprecated("换成retrifit和rxjava的")
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
     * {"msgCode":"??","errMsg":"..."}俊哥做的api就没有errMsg
     * 执行方法
     * 调用示例：
     *
    APIClient.getInstance().post(APIs.getInstance().getAPI(APIs.USER_DEVICE_SETTING), MapToHttpParam.toParamString(mapOf(
    "adminId" to adminId, "equipmentId" to equipId, "lang" to lang
    )), ui!!.h!!,arrayListOf(adminId,equipId,lang), { tr, h -> APIClient.getInstance().standhardErrProcess(tr, h) { getInfo() } }) { tr ->
    //成功执行的方法
        var _adminId=it.rawParam!!.get(0) as Int
    }
    这个getInfo()其实是处理和api交互的方法，然后errFuc会回调这个方法
	可以配合loadingView的那个Dialog,
	rawParam可以存放原始参数，然后这样便于回调的时候处理
     */
    fun post(url: String, param: String, h: WeakHandler,rawParam: ArrayList<Any>?, errFuc: (result: Flood2.TempResult, h: WeakHandler) -> Unit, sucFuc: (result: Flood2.TempResult) -> Unit) {
        val key = UUID.randomUUID()
        TotalAsynRun.getInstance()._run(Flood2()) {
            it.runSimpleHttp(Configs.RUN_AT_THREAD_TIMEOUT) {
				Thread.sleep(750) //这里如果有配合loadingview做一个延迟比较好~让动画转一下
                DLog.log(h.wr.get()!!.javaClass, "${key}请求:$url?$param")
                SimpleHttp.post(url, param)
            }.runNext(Configs.RUN_AT_THREAD_TIMEOUT, Flood2.TempResult()) {
                val temp = Flood2.TempResult();
                val rs = it._get<SimpleHttp.Result>()
                DLog.log(h.wr.get()!!.javaClass, "${key}返回:$rs")
                temp.httpcode = rs.returnCode
                if (rs.returnCode == SimpleHttp.Result.CODE_SUCCESS) {
                    val cson = CSON(rs.retString);
                    val msgCode = cson.getSpecificType("msgCode", String::class.java).toInt()
                    temp.flag = msgCode
                    temp.data = cson
                }
                temp
            }.runAtUI(h) {
                val tr = it._get<Flood2.TempResult>()
				tr.rawParam = rawParam
                if (tr.httpcode != 0 || tr.flag != 0) {
                    errFuc(tr, h)
                } else {
                    sucFuc(tr)
                }
            }
        }
    }

    fun standhardErrProcess(tr: Flood2.TempResult, h: WeakHandler, func: () -> Unit) {
        if (tr.httpcode != 0) {
            AskDialog().showConnectFailed(h.wr.get()!!, {
                if (it) {
                    func()
                }
            })
        } else if (tr.flag != 0) {
            //这一段应后台设计的不同，会有不同的处理，因为这里就是显示错误嘛
            var errMsg:String? = (tr.data as CSON).getSpecificType("errMsg", String::class.java)
            if(errMsg==null){
                //后台没有带过来错误消息文本，从其他配置文件里去
            }
            //
            AskDialog().showAPIFailed(if (errMsg == null) "" else errMsg, h.wr.get()!!, {
                if (it) {
                    func()
                }
            })
        }
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