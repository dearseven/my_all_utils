
import org.json.JSONObject

/**
 * Created by Administrator on 2018/4/9.
 */
class CsonKt {
    private var js: JSONObject? = null
    private var rawJsonStr: String? = null

    fun getRawJsonStr(): String? {
        return rawJsonStr
    }

    constructor(jsonStr: String) {
        try {
            rawJsonStr = jsonStr
            js = JSONObject(jsonStr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    constructor(obj: JSONObject) {
        try {
            js = obj
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T> getObjectType(k: String): T? {
        try {
            return js!!.get(k) as T
        } catch (e: Exception) {
            return null
        }
    }


    fun <T> getSpecificType(k: String, clazz: Class<T>): T? {
        try {
            val o = js!!.get(k)
            return clazz.cast(o)
        } catch (e: Exception) {
            return null
        }

    }

    fun getInt(k: String): Int {
        try {
            return js!!.getInt(k)
        } catch (e: Exception) {
            return Integer.MIN_VALUE
        }
    }

    fun getDouble(k: String): Double? {
        try {
            return js!!.getDouble(k)
        } catch (e: Exception) {
            return java.lang.Double.MIN_VALUE
        }

    }

    fun getLong(k: String): Long? {
        try {
            return js!!.getLong(k)
        } catch (e: Exception) {
            return java.lang.Long.MIN_VALUE
        }

    }

    fun getBool(k: String): Boolean? {
        try {
            return js!!.getBoolean(k)
        } catch (e: Exception) {
            return false
        }

    }

}