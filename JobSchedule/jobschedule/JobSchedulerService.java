package cc.m2u.hidrogen.jobschedule;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.widget.Toast;
import cc.m2u.hidrogen.activity.LaunchActivity;
import cc.m2u.hidrogen.activity.MainActivity;
import cc.m2u.hidrogen.utils.DLog;
import cc.m2u.hidrogen.utils.flood.TotalAsynRun;

/**
 * Created by wx on 2017/7/11.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//API需要在21及以上
public class JobSchedulerService extends JobService {
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(JobSchedulerService.this, "MyJobService", Toast.LENGTH_SHORT).show();
            JobParameters param = (JobParameters) msg.obj;
            jobFinished(param, true);
//            Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

            //如果要执行耗时任务 请开始线程
            //....TotalAsynRun.Companion.getInstance()._run()

            // 重新运行，其实第一次调用时在mainactivity里
            DLog.log(getClass(), "准备启动job");
            JobScheduler mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            mJobScheduler.cancelAll();
            JobInfo.Builder builder = new JobInfo.Builder(1,
                    new ComponentName(getPackageName(),
                            JobSchedulerService.class.getName()));
            //15s运行一次,最迟30秒
            builder.setMinimumLatency(15 * 1000);
            builder.setOverrideDeadline(30 * 1000);
            builder.setPersisted(true);

            //有网络就运行
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            if (mJobScheduler.schedule(builder.build()) <= 0) {
                //If something goes wrong
                DLog.log(getClass(), "job启动失败");
            } else {
                DLog.log(getClass(), "job启动成功");
            }
            return true;
        }
    });

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DLog.log(getClass(), "i am job:" + this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        DLog.log(getClass(), "onStartJob " + this);
        Message m = Message.obtain();
        m.obj = params;
        handler.sendMessage(m);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        DLog.log(getClass(), "onStopJob " + this);
        handler.removeCallbacksAndMessages(null);
        return false;
    }
}
