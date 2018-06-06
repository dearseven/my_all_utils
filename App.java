
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * App的整体生命周期由他的控制，每个Activity生命周期的事件可以传递给探针接口，不在这里写
 * Created by wx on 2017/4/4.
 */

public class App extends Application implements Application.ActivityLifecycleCallbacks {

    /**
     * APP是否在前台
     */
    public boolean isActive;

    /**
     * 当APP在后台的时候，会启动一个计时，到一定时间APP还在后台，则认为APP结束
     */
    public static final int EXPIRE_WHEN_BACK = 1;

    /**
     * app在后台自我结束时间15S
     */
    public static final int EXPIRE_TIME = 15;

    @Override
    public void onCreate() {
        super.onCreate();

        Debug.p(getClass(), "启动Activity");
        CrashHandler ch = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(ch);
        //
        registerActivityLifecycleCallbacks(this);
        //启动队列
        AsynQueue.getInstance();
    }

    private List<Activity> activities = new ArrayList<Activity>();

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        activities.add(activity);
        //不管怎么样尝试对探针服务进行启动

        //启动心跳，探针也在这里面
        Intent it = new Intent(this, ProbeHeartBeatServcie.class);
        startService(it);

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!isActive) {
            // app 从后台唤醒，进入前台
            isActive = true;
            Debug.p(getClass(), "~~~~~~~~~~~~~~~~App foreground");
            h.removeMessages(EXPIRE_WHEN_BACK);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (!isAppOnForeground()) {
            isActive = false; // 全局变量 记录当前已经进入后台 app 进入后台
            Debug.p(getClass(), "~~~~~~~~~~~~~~~~App back");
            h.sendEmptyMessageDelayed(EXPIRE_WHEN_BACK, EXPIRE_TIME * 1000);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activities.remove(activity);
        Debug.p(getClass(), "~~~~~~~~~~~Activities size~~~~~~~~~~~~:" + activities.size());

        if (activities.size() == 0) {//当没有一个activity的时候，结束应用,
            // 不管使用户手动back还是超时导致结束所有Activity，总之在这里判定应用退出

            //记录一个退出的事件
            AsynQueue.getInstance().add(ProbeBean.makeQuitBean(this));
            //检测播放事件队列
            PlayStatuList.Companion.getInstance().whenAppQuit();
            //调用探针上传来上传最后的数据
            Worker.start(this);
            //停止探针和心跳服务
            Intent it = new Intent(this, ProbeHeartBeatServcie.class);
            stopService(it);
        }
    }

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXPIRE_WHEN_BACK:
                    if (isActive == false) {
                        Debug.p(App.class,"app在后台超时，结束所有Activity 触发Worker，结束心跳Service");
                        //直接关掉所有Activity这样的话会触发onActivityDestroyed里面的if语句
                        Iterator<Activity> it = activities.iterator();
                        while (it.hasNext()) {
                            Activity activity = it.next();
                            activity.finish();
                            //activities.remove(activity);
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the device
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
	
	   /**
     * 关掉activity除了传过来的
     *
     * @param activity
     */
    public void closeAllActivityExceptMe(Activity activity) {
        Iterator<Map.Entry<String, AppCompatActivity>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            String key = it.next().getKey();
            if (!key.equals(activity.getClass().getSimpleName())) {
                Activity a = map.get(key);
                a.finish();
                it.remove();
            }
        }
    }
}
