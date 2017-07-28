
import android.app.Activity;
import android.app.Application;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by wx on 2017/4/4.
 */

public class App extends Application implements Application.ActivityLifecycleCallbacks{
    List<Activity> activities = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler ch = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(ch);
        registerActivityLifecycleCallbacks(this);
    }

    public void finishActivities(List<String> clzSimpleNames) {
        Iterator<Activity> it = activities.iterator();
        DLog.log(getClass(),"before finishActivities:"+clzSimpleNames.size());
        while (it.hasNext()) {
            Activity a = it.next();
            DLog.log(getClass(),a.getClass().getSimpleName()+","+clzSimpleNames.contains(a.getClass().getSimpleName()));
            if (clzSimpleNames.contains(a.getClass().getSimpleName())) {
                a.finish();
            }
        }
        DLog.log(getClass(),"before finishActivities:"+clzSimpleNames.size());
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activities.remove(activity);
    }
}
