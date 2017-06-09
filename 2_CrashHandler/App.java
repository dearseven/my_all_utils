package cc.m2u.lottery;

import android.app.Application;

import cc.m2u.lottery.utils.CrashHandler;


/**
 * Created by wx on 2017/4/4.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler ch = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(ch);
    }
}
