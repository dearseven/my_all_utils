

/**
 * 配合Flood2实现完全后台
 * Created by wx on 2017/6/21.
 */
class TotalAsynRun {
    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        //这里不能用inner class这个写法 因为inner class不能用 companion
        val instance = TotalAsynRun()
    }

    /**
     * 可以完全在Flood2跑在子线程，
     * 但是如果要更新ui，记得获取数据以后，用runAtUI来更新界面
     */
    fun _run(_fd: Flood2, lash: (fd: Flood2) -> Any): TotalAsynRun {
        Thread(Runnable {
            lash(_fd)
        }).start()
        return this
    }
}
