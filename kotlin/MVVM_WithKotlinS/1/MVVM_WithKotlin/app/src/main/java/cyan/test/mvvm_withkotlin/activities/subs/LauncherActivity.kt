package cyan.test.mvvm_withkotlin.activities.subs

import android.os.Bundle
import android.os.Handler
import android.os.Message
import cyan.test.mvvm_withkotlin.R
import cyan.test.mvvm_withkotlin.activities.BaseParentActivity
import cyan.test.mvvm_withkotlin.databinding.ActivityLauncherBinding
import cyan.test.mvvm_withkotlin.models.Welcome
import cyan.test.mvvm_withkotlin.presenters.subs.LauncherPresenter
import java.lang.ref.WeakReference

class LauncherActivity : BaseParentActivity<ActivityLauncherBinding, LauncherPresenter>() {
    override fun get_Presenter(): LauncherPresenter? {
        return LauncherPresenter(this)
    }

    override fun get_LayoutId(): Int = R.layout.activity_launcher

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
