package single_instance

/**
 * Created by wx on 2017/6/1.
 */
class DoubleCheckInstance {
    //直接翻译java的双检查
    companion object {
        private @Volatile var instance: DoubleCheckInstance? = null;
        fun get(): DoubleCheckInstance {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = DoubleCheckInstance()
                    }
                }
            }
            return instance!!
        }
    }

}