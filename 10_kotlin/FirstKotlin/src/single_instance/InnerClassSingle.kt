package single_instance

/**
 * Created by wx on 2017/6/1.
 */
class InnerClassSingle {
    companion object{
        fun getInstance()=Holder.instance
    }

   private object Holder{//这里不能用inner class这个写法 因为inner class不能用 companion
        val instance=InnerClassSingle()
    }
}