
import kotlin.properties.Delegates

class KeepWatchVar<T> {
    private var watcherMap = HashMap<String, VarWatcher<T>>()
    private var _var: T? = null
    private var observableVar by Delegates.observable(_var) { p, o, n ->
        if (o != n) {
            _var = n
            watcherMap.forEach {
                it.value?.also {
                    it.KeepWatchVarChanged(_var)
                }
            }
        }
    }

    constructor(_var: T) {
        this._var = _var
    }

    fun setVarAndNotify(newValue: T) {
        observableVar = newValue
    }

    fun getValue(): T? {
        return _var
    }

    fun addWatcher(key: String, w: VarWatcher<T>) {
        watcherMap.put(key, w)
    }

    fun removeWatcher(key: String) {
        watcherMap.remove(key)
    }

    fun removeWatchers() {
        watcherMap.clear()
    }

//    fun setVar(newValue: T) {
//        _var = newValue
//    }
//
//    fun getVar() {
//        return VAL
//    }

    interface VarWatcher<T> {
        fun KeepWatchVarChanged(_var: T?)
    }
}

