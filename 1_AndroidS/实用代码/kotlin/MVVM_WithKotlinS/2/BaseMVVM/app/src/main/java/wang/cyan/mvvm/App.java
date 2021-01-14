package wang.cyan.mvvm;
import android.app.Application;

import wang.cyan.mvvm.utils.CrashHandler;


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
