
/**
 * Created by wx on 2017/4/4.
 */

public class App extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {
    Map<String, AppCompatActivity> map = new HashMap<String, AppCompatActivity>();

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler ch = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(ch);
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        DLog.log(getClass(), activity.getClass().getSimpleName() + "," + getClass());
        if (map.containsKey(activity.getClass().getSimpleName())) {
            DLog.log(getClass(), "contains,need finish " + activity);
            map.get(activity.getClass().getSimpleName()).finish();
            map.remove(activity.getClass().getSimpleName());
        }
        map.put(activity.getClass().getSimpleName(), (AppCompatActivity) activity);
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
        map.remove(activity.getClass().getSimpleName());
    }
}
