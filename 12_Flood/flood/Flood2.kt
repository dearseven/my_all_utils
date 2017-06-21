

import java.util.*

/**
 * 淘汰了Flood，可以实现链式操作
 * 配合TotalAsynRun实现完全的后台操作，如果用了TotalAsynRun其实就不要runThread，除非是一定要再某段事件内给予响应
 * Created by wx on 2017/6/20.
 */
class Flood2 {
    var data: Any? = null

    fun <T> _get(): T {
        return data as T
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
     * 因为用了get的wait模式，其实还是可能阻塞ui ，但是不会说在主线程反问网络等问题
     * 如果需要完全不阻塞ui，可以代码都跑在Thread中
     */
    fun runThread(timeout: Long, lash: (objectArg: Flood2) -> Any): Flood2 {
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
        } catch (tex: java.util.concurrent.TimeoutException) {
            tex.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            es.shutdown()
        }
        //
        return this
    }

    fun runAtUI(h: android.os.Handler, lash: (arg: Flood2) -> Any): Flood2 {
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
    Flood2.getInstance().run() { "wangxu" }.runThread(3000) {
        println("myname is ${it._get<String>()}")
        Date()
    }.run { println("new is ${it._get<Date>()}") }

    //实现完全后台
    TotalAsynRun.getInstance()._run(Flood2.getInstance()) {
        it.run() { "wangxu" }.runThread(3000) {
            println("myname is ${it._get<String>()}")
            Date()
        }.run { println("new is ${it._get<Date>()}") }
    }
}