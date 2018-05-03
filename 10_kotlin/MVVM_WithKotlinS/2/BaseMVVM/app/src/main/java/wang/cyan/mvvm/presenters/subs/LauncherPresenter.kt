package wang.cyan.mvvm.presenters.subs
import android.support.v7.app.AppCompatActivity
import wang.cyan.mvvm.activities.subs.MainActivity
import wang.cyan.mvvm.models.Welcome
import wang.cyan.mvvm.presenters.IPresenter

/**
 * Created by wx on 2018/5/2.
 */
class LauncherPresenter(activity: AppCompatActivity) : IPresenter(activity) {
    override fun onCreateDone() {
        val ctx = getTheActivity<MainActivity>()!!
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