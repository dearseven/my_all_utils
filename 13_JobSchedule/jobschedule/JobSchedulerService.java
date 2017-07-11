import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.widget.Toast;


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
            Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
