
class KotlinBlockTR<T, R> {
//    companion object {
//        val instance = SingletonHolder.holder
//    }
//
//    private object SingletonHolder {
//        val holder = KotlinBlockFunction(T,R)
//    }

    companion object {
    }

    fun apply(any: T, func: (param: T) -> R): R {
        return any.let { func(it); }
    }

}