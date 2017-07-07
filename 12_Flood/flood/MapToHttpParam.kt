package cc.m2u.hidrogen.utils

/**
 * Created by wx on 2017/7/6.
 */
class MapToHttpParam {
    companion object {
        /**
         * map->a=va&b=vb&_cts_=12313123123
         */
        fun toParamString(m: Map<String, Any>): String {
            var sb = StringBuffer()
            m.mapKeys {
                sb.append("$it&")
            }
            sb.append("_cts_:${System.currentTimeMillis()}")
            return sb.toString()
        }
    }
}
