package single_instance


/**
 * Created by wx on 2017/6/1.
 */

class Lazy2ThreadSafe {
    companion object {

        private var instance: Lazy2ThreadSafe? =null

        @Synchronized
        fun get(): Lazy2ThreadSafe {
            if (instance == null) {
             instance= Lazy2ThreadSafe()
            }
            return instance!!
        }

    }
}
