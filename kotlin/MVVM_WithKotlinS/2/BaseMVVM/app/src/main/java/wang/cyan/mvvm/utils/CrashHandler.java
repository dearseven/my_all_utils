package wang.cyan.mvvm.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;

import uex.InsertUncatchedException;
import wang.cyan.mvvm.App;
import wang.cyan.mvvm.activity.LaunchActivity;


/**
 * Created by wx on 2016/6/15.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler, Application.ActivityLifecycleCallbacks {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    public static final String TAG = "CatchExcep";
    App application;
    Activity currAct = null;

    /**
     * 开发中的时候，不启用异常重启
     */
    private static final boolean DEBUGING = true;

    public CrashHandler(App app) {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        application = app;
        application.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        if (!handleException(ex) && mDefaultHandler != null) {//基本上不会走这里！！！！
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            /*开发周期内 不启用这个设置*/
            if (!DEBUGING) {
                Intent intent = new Intent(application.getApplicationContext(), LaunchActivity.class);
                PendingIntent restartIntent = PendingIntent.getActivity(
                        application.getApplicationContext(), 0, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                //退出程序
                AlarmManager mgr = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                        restartIntent); // 1秒钟后重启应用
            }
            //application.finishActivity();

            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        final String exInfo = writer.getBuffer().toString();
        DLog.exception(CrashHandler.class, exInfo);
        InsertUncatchedException.insert(exInfo);
        ex.printStackTrace();
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(application.getApplicationContext(), "很抱歉,程序出现异常,即将退出.",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        return true;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        DLog.log(getClass(),"created:"+activity.toString());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        DLog.log(getClass(),"started:"+activity.toString());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        DLog.log(getClass(),"resumed:"+activity.toString());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        DLog.log(getClass(),"paused:"+activity.toString());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        DLog.log(getClass(),"stopped:"+activity.toString());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        DLog.log(getClass(),"saveIns:"+activity.toString());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        DLog.log(getClass(),"destroyed:"+activity.toString());
    }
}
