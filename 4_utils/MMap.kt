package *

class MMap {
    private val _this: MutableMap<Any, Any> = mutableMapOf()
    fun put(k: Any, v: Any): MMap {
        _this.put(k, v)
        return this
    }

    fun <T> get(k: Any, clz: Class<T>): T? {
        try {
            val o = _this.get(k)
            return clz.cast(o)
        } catch (e: Exception) {
            return null
        }
    }
}

