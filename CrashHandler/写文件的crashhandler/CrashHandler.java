
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.LauncherActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;



import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.Calendar;




/**
 * Created by wx on 2016/6/15.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler/*, Application.ActivityLifecycleCallbacks*/ {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    public static final String TAG = "CatchExcep";
    APP application;
    Activity currAct = null;

    /**
     * 开发中的时候，不启用异常重启
     */
    private static final boolean DEBUGING = true;

    public CrashHandler(APP app) {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        application = app;
        //application.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        if (!handleException(ex) && mDefaultHandler != null) {//基本上不会走这里！！！！
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//            }
            /*开发周期内 不启用这个设置*/
            if (!DEBUGING) {
                Intent intent = new Intent(application.getApplicationContext(), LauncherActivity.class);
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
        Log.i("CrashHandler",exInfo);
        if (!exInfo.contains("#0xffffffff")) {
            log(application.getApplicationContext(), "", exInfo);
        }
        //InsertUncatchedException.insert(exInfo);
        ex.printStackTrace();
        //使用Toast来显示异常信息
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                Toast.makeText(application.getApplicationContext(), "很抱歉,程序出现异常,即将退出.",
//                        Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//        }.start();
        return true;
    }

    public static final void log(Context ctx, String userId, String info) {
        try {
            Calendar c = Calendar.getInstance();
            StringBuilder sb = new StringBuilder();
            sb.append(c.get(Calendar.YEAR)).append("_").append(c.get(Calendar.MONTH) + 1).append("_").append(c.get(Calendar.DAY_OF_MONTH));
            String ymd = sb.toString();
            sb.setLength(0);

            String fileName = ymd + "_" + userId + ".txt";
            String folder = Environment.getExternalStorageDirectory() + "/cyans_log/"+ctx.getPackageName()+"/";
            File f = new File(folder);
            if (f.exists() == false) {
                f.mkdir();
            }
            File logFile = new File(folder, fileName);
            if (logFile.exists() == false) {
                logFile.createNewFile();
            }
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(logFile, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            sb.append("\n\n-------------------------------------------------------------------");
            sb.append("\nlog in date and Time:").append(ymd).append(" ").append(c.get(Calendar.HOUR_OF_DAY)).append(":").append(c.get(Calendar.MINUTE)).append(":").append(c.get(Calendar.SECOND));
            sb.append("\n");
            sb.append(info);
            randomFile.writeUTF(new String(sb.toString().getBytes(), "utf-8"));
            randomFile.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
