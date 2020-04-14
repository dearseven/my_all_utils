
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

    var myT: T? = null;
    var myR: R? = null;
    fun applySelf(any: T, func: (param: T) -> R): KotlinBlockTR<T, R> {
        return this.apply {
            myT = any;myR = any.let { func(it); }
        }
    }

    fun doSomeOnR(func: (param: R) -> R): KotlinBlockTR<T, R> {
        myR = func(myR!!);
        return this;
    }
}