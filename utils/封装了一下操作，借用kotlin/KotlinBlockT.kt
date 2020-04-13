
class KotlinBlockT<T> {
//    companion object {
//        val instance = SingletonHolder.holder
//    }
//
//    private object SingletonHolder {
//        val holder = KotlinBlockFunction(T,R)
//    }

    companion object {
    }

    fun apply(any: T, func: (param: T) -> T): T {
        return any.apply { func(this) }

    }

}