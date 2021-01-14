
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by wx on 2017/7/6.
 */
class JSONArrToList {
//    fun to(arr: JSONArray): ArrayList<out Any> {
//        var list = ArrayList<Any>(arr.length())
//        for (i in 0..arr.length() - 1) {
//
//        }
//        return list
//    }

//    fun <T> to(arr: JSONArray): T {
//        var list = ArrayList<Any>(arr.length())
//        for (i in 0..arr.length() - 1) {
//            list.add(arr.get(i))
//        }
//        return list as T
//    }

    fun <T> to(arr: JSONArray, lash: (arrList: ArrayList<JSONObject>) -> T): T {
        var list = ArrayList<JSONObject>(arr.length())
        for (i in 0..arr.length() - 1) {
            list.add(arr.getJSONObject(i))
        }
        //return list as T
        var ret = lash(list);
        return ret as T
    }

    fun toForeach(arr: JSONArray, lash: (jsonObj: JSONObject) -> Unit): Unit {
        var list = ArrayList<JSONObject>(arr.length())
        for (i in 0..arr.length() - 1) {
            list.add(arr.getJSONObject(i))
        }
        //return list as T
        list.forEach {
            lash(it)
        }
    }
    companion object {
        fun toForeach(arr: JSONArray, lash: (jsonObj: JSONObject) -> Unit): Unit {
            (0..arr.length() - 1).forEach { lash((arr[it] as @kotlin.ParameterName(name = "jsonObj") JSONObject)) }
        }
    }
}