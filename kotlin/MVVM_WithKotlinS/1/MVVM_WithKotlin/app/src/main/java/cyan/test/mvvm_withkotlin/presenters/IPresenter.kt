package cyan.test.mvvm_withkotlin.presenters

import android.support.v7.app.AppCompatActivity

/**
 * Created by Administrator on 2018/5/2.
 */
abstract class IPresenter {
    constructor() {
    }
    constructor(activity: AppCompatActivity) {
        this.activity = activity
    }
    var activity: AppCompatActivity? = null
//    fun <T : AppCompatActivity> getTheActivity(clz: Class<in AppCompatActivity>): Any? {
//        try {
//            return clz.cast(activity)
//        } catch (e: Exception) {
//            return null
//        }
//    }
    fun <T : AppCompatActivity> getTheActivity(): T? {
        try {
            return activity as T
        } catch (e: Exception) {
            return null
        }
    }

    abstract fun onCreateDone()
}