
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * 淘汰了Flood，可以实现链式操作
 * 配合TotalAsynRun实现完全的后台操作，如果用了TotalAsynRun其实就不要runThread，除非是一定要再某段事件内给予响应
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
     * 其实我觉得用null最好了，用isRetNull来判定就好了
     */
    fun runNext(timeout: Long, wrongFlg: Any?, lash: (objectArg: Flood2) -> Any): Flood2 {
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
            Date()
        }.run { println("new is ${it._get<Date>()}") }
    }
}