package cyan.positioncameraa.utils.flood

import cyan.positioncameraa.utils.SimpleHttp
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * 淘汰了Flood，可以实现链式操作
 * 配合TotalAsynRun实现完全的后台操作，如果用了TotalAsynRun其实就不要runThread，除非是一定要再某段事件内给予响应
 * <br/>
 * 关于使用http的正确用法

TotalAsynRun.getInstance()._run(Flood2()) {
it.runSimpleHttp(Configs.RUN_AT_THREAD_TIMEOUT) {
doWithServer(ctx, id);
}.runNext(Configs.RUN_AT_THREAD_TIMEOUT, null) {
flag = check(ctx, id, it._get());
flag
}.run {
if (it.isRetNotNull()) {
listenr.loginStatusCallback(it._get())
}
true
}
}
1 用runSimpleHttp返回SimpleHttp.Result
2 用runNext处理返回的SimpleHttp.Result
3 再用run或者runUI更新数据或者UI
 * Created by wx on 2017/6/20.
 */
class Flood2 {


    var data: Any? = null

    /**
     * 返回任意泛型
     */
    fun <T> _get(): T {
        return data as T
    }

    /**
     * 可以判断data是不是为null
     */
    fun isRetNotNull(): Boolean {
        return data != null
    }

    fun _set(value: Any) {
        data = value
    }

    companion object {
        //并不是单例模式
        fun getInstance() = Flood2()
    }

    fun clean() {
        data = null
    }

    fun run(lash: (arg: Flood2) -> Any): Flood2 {
        this.data = lash(this)
        return this
    }

    /**
     * 把jsonarr转成一个ArrayList<JSONObject>然后再处理
     */
    fun runWithJSONArr(lash: (jsonList: ArrayList<JSONObject>, arg: Flood2) -> Any): Flood2 {
        val arr = this._get<JSONArray>()
        JSONArrToList().to(arr) {
            this.data = lash(it, this)
        }
        return this
    }

    /**
     * 因为用了get的wait模式，其实还是可能阻塞ui ，但是不会说在主线程反问网络等问题
     * 如果需要完全不阻塞ui，可以代码都跑在Thread中,如果调用这个代码，
     * !!!!!!!一定要返回SimpleHttp.Result
     * 不然有时候会抛出错，如果要一个一个的调用，请runNext
     */
    fun runSimpleHttp(timeout: Long, lash: (objectArg: Flood2) -> Any): Flood2 {
        val es = java.util.concurrent.Executors.newSingleThreadExecutor()
        var f: java.util.concurrent.Future<Any>? = null
        //
        try {
            val task = java.util.concurrent.Callable<Any> {
                lash(this);
            }
            // 提交任务
            f = es.submit<Any>(task)
            this.data = f!!.get(timeout, java.util.concurrent.TimeUnit.MILLISECONDS)
        } catch (eex: java.util.concurrent.ExecutionException) {
            eex.printStackTrace()
            f!!.cancel(true)
            var rs = SimpleHttp.Result();
            rs.errorMsg = SimpleHttp.Result.ERROR_EXCEPTION
            rs.returnCode = SimpleHttp.Result.CODE_EXCEPTION
            rs.retString = null
            this.data = rs
        } catch (tex: java.util.concurrent.TimeoutException) {
            var rs = SimpleHttp.Result();
            rs.errorMsg = SimpleHttp.Result.ERROR_TIME_OUT
            rs.returnCode = SimpleHttp.Result.CODE_TIME_OUT
            rs.retString = null
            this.data = rs
            tex.printStackTrace()
        } catch (e: Exception) {
            var rs = SimpleHttp.Result();
            rs.errorMsg = SimpleHttp.Result.ERROR_EXCEPTION
            rs.returnCode = SimpleHttp.Result.CODE_EXCEPTION
            rs.retString = null
            this.data = rs
            e.printStackTrace()
        } finally {
            es.shutdown()
        }
        //
        return this
    }


    /**
     *可以用于一步一步的执行任务
     * wrongflag用于错误的默认值，建议设置和返回值同类型但是是是错误的情况,或者直接给一个null也可以吧
     * 其实我觉得用null最好了，用isRetNull来判定就好了，不过也直接实例化一个TempResult
     */
    fun runNext(timeout: Long, wrongFlg: Any?, lash: (objectArg: Flood2) -> Any?): Flood2 {
        val es = java.util.concurrent.Executors.newSingleThreadExecutor()
        var f: java.util.concurrent.Future<Any>? = null
        //
        try {
            val task = java.util.concurrent.Callable<Any> {
                lash(this);
            }
            // 提交任务
            f = es.submit<Any>(task)
            this.data = f!!.get(timeout, java.util.concurrent.TimeUnit.MILLISECONDS)
        } catch (eex: java.util.concurrent.ExecutionException) {
            eex.printStackTrace()
            f!!.cancel(true)
            this.data = wrongFlg
        } catch (tex: java.util.concurrent.TimeoutException) {
            this.data = wrongFlg
            tex.printStackTrace()
        } catch (e: Exception) {
            this.data = wrongFlg
            e.printStackTrace()
        } finally {
            es.shutdown()
        }
        //
        return this
    }


    fun runAtUI(h: android.os.Handler, lash: (arg: Flood2) -> Unit): Flood2 {
        h.post {
            lash(this)
        }
        return this
    }

//    fun runTotalOnBack(lash: (objectArg: Flood2) -> Any): Flood2 {
//        Thread(Runnable {
//            lash(this)
//        })
//        return this;
//    }


    /**
     * 方便传结果，其实可以配合SimpleHttp的结果和api的msgCode（我们习惯用这个来表示api的返回状态）来设置flag
     * what用于也许还要传递个其他的什么东西,data在正确的时候肯定是结果，但是如果是错误的时候，也许是一个错误字符串或者是null
     * 这个要看业务逻辑
     * 不管怎么样，建议
     * httpcode放simplehttp的交互返回标示,比如SimpleHttp.Result.CODE_TIME_OUT
     * flag放接口的msgCode
     * 这样就可以实现两个状态位都有
     *
     */
    class TempResult() {
        var httpcode = SimpleHttp.Result.CODE_TIME_OUT
        var flag = -1;
        var data: Any? = null
        var what: Any? = null
        //可以原始的参数一个一个的放进来，然后带出去，这个是配合APIClient使用
        var rawParam: ArrayList<Any>? = null
        var paramMap: Map<String, Any>? = null

        constructor(flag: Int, Data: Any?, what: Any?) : this() {

        }
    }

}


fun main(args: Array<String>) {
    //一般操作,
    Flood2.getInstance().run() { "wangxu" }.runSimpleHttp(3000) {
        println("myname is ${it._get<String>()}")
        Date()
    }.run { println("new is ${it._get<Date>()}") }

    //实现完全后台
    TotalAsynRun.getInstance()._run(Flood2.getInstance()) {
        it.run() { "wangxu" }.runSimpleHttp(3000) {
            println("myname is ${it._get<String>()}")
            Date()//其实这里应该返回SimpleHttp.Result
        }.run { println("new is ${it._get<Date>()}") }
    }
}