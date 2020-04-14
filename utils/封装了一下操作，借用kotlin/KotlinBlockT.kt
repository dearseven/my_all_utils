
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

    var myT: T? = null;
    fun applySelf(any: T, func: (param: T) -> T): KotlinBlockT<T> {
        return this.apply {
            myT = any.apply { func(this) };
        }
    }

    fun doSomeOnT(func: (param: T) -> T): KotlinBlockT<T> {
        myT = func(myT!!);
        return this;
    }
}