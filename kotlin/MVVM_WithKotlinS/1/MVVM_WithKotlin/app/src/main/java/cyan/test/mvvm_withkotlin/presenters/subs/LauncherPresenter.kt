package cyan.test.mvvm_withkotlin.presenters.subs

import android.support.v7.app.AppCompatActivity
import cyan.test.mvvm_withkotlin.activities.subs.LauncherActivity
import cyan.test.mvvm_withkotlin.models.Welcome
import cyan.test.mvvm_withkotlin.presenters.IPresenter

/**
 * Created by wx on 2018/5/2.
 */
class LauncherPresenter(activity: AppCompatActivity) : IPresenter(activity) {
    override fun onCreateDone() {
        val ctx = getTheActivity<LauncherActivity>()!!
        //变化一次
        val welcomeData = Welcome();
        welcomeData.setWelcome("Hello~~");
        welcomeData.setName("cyan!");
        //把对象放到binding的变量上
        ctx.vdBing!!.setWelcome(welcomeData)
        //在调用一次activity
        ctx.some()
    }

}