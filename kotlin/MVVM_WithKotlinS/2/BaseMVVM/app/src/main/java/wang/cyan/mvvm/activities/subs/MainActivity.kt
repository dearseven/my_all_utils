package wang.cyan.mvvm.activities.subs;

import android.os.Bundle
import android.os.Handler
import wang.cyan.mvvm.R
import wang.cyan.mvvm.activities.BaseParentActivity
import wang.cyan.mvvm.databinding.ActivityMainBinding
import wang.cyan.mvvm.models.Welcome
import wang.cyan.mvvm.presenters.subs.LauncherPresenter
import java.lang.ref.WeakReference

class MainActivity : BaseParentActivity<ActivityMainBinding, LauncherPresenter>() {
    override fun get_Presenter(): LauncherPresenter? {
        return LauncherPresenter(this)
    }

    override fun get_LayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun some() {
        h!!.get()!!.postDelayed({
            val welcomeData = Welcome();
            welcomeData.setWelcome("Cyan!!!");
            welcomeData.setName("Hello~~~");
            //把对象放到binding的变量上
            vdBing!!.setWelcome(welcomeData)
        }, 3000)
    }

    val h = object : WeakReference<Handler>(Handler()) {

    }

}
