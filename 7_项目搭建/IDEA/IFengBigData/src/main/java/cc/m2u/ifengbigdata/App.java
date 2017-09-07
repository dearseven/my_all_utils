package cc.m2u.ifengbigdata;

import android.support.multidex.MultiDexApplication;
import cc.m2u.ifengbigdata.utils.CrashHandler;

/**
 * Created by Administrator on 2017/9/7.
 */
public class App extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler ch = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(ch);
    }
}
