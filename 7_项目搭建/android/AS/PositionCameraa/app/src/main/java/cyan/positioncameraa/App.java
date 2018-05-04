package cyan.positioncameraa;
import android.app.Application;
import android.support.multidex.MultiDexApplication;

import cyan.positioncameraa.utils.CrashHandler;
import cyan.positioncameraa.utils.DeviceIdGetter;


/**
 * Created by wx on 2017/4/4.
 */

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler ch = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(ch);
        new DeviceIdGetter(getApplicationContext());
    }
}
