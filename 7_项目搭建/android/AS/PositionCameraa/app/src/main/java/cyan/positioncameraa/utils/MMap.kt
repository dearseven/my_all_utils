package cyan.positioncameraa.utils

class MMap {
    //    private val _this: MutableMap<Any, Any> = mutableMapOf()
    private val _this: java.util.HashMap<String, Any> = HashMap()

    fun put(k: String, v: Any): MMap {
        _this.put(k, v)
        return this
    }

    fun <T> get(k: String, clz: Class<T>): T? {
        try {
            val o = _this.get(k)
            return clz.cast(o)
        } catch (e: Exception) {
            return null
        }
    }

    fun get(): HashMap<String, Any> {
        return _this
    }
}

