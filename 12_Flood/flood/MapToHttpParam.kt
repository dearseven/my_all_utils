
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
            sb.append("_cts_=${System.currentTimeMillis()}")
            return sb.toString()
        }
		
		fun toParamString2(m: Map<String, Any>): String {
            // val s1 = Signature.getSignatureRequestPara(m, Signature.HTTP_METHOD_POST)
            // var sb = StringBuffer()
            var list = ArrayList<String>(m.size)
            m.mapKeys {
                //sb.append("$it&")
                list.add("$it")
            }
            list.add("_ts_=${System.currentTimeMillis()}")
            list.add("_dvctype_=${Configs.DEVICE_TYPE}")
            list.add("_dvcid_=${DeviceIdGetter.getUuidIfNullEmpty()}")
            //sb.append("_cts_=${System.currentTimeMillis()}")
            val map = HashMap<String, Any>()
            list.sortedBy {
                it
            }.forEach {
                val firstEq = it.indexOf("=")
                val k = it.substring(0, firstEq)
                val v = it.substring(firstEq + 1)
                //DLog.log(javaClass,"$k=$v")
                map.put(k, v)
            }
			val s2=toParamString(map)
            //val s2 = Signature.getSignatureRequestParameter(map, Signature.HTTP_METHOD_POST)
            return s2 //URLDecoder.decode(s2, "UTF-8")
        }
    }
}
