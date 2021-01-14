package single_instance

/**
 * Created by wx on 2017/6/1.
 */
class DoubleCheckInKotlin {
    //kotlin的双检查写法
    val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        DoubleCheckInstance()
    }
}