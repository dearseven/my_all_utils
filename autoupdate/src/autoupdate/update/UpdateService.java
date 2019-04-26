
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


import java.io.File;


/**
 * app更新下载后台服务
 * Created by wuxl_
 * time：2016/8/8 16:09
 */
public class UpdateService extends Service {

    private String apkURL;
    private String filePath;
    private NotificationManager notificationManager;

    private static final String TAG = "TAG";

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String folder = Environment.getExternalStorageDirectory() + "/m2u_update/" + getPackageName() + "/";
        try {//如果存在就删掉
            File file = new File(folder);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception ex) {

        }
        filePath = folder + "new_version.apk";
        DLog.log(getClass(), "下载新版本:" + filePath);
        try {//如果存在就删掉
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {

        }
        if (intent == null) {
            notifyUser(getString(R.string.update_download_failed), getString(R.string.update_download_failed_msg), 0);
            stopSelf();
        }
        apkURL = intent.getStringExtra("ttfh_main");
        DLog.log(getClass(), "下载地址: " + apkURL);
        notifyUser(getString(R.string.update_download_start), getString(R.string.update_download_start), 0);
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload() {
        UpdateManager.getInstance().startDownloads(apkURL, filePath, new UpdateDownloadListener() {

            @Override
            public void onStarted() {
            }

            @Override
            public void onProgressChanged(int progress, String downloadUrl) {
                DLog.log(getClass(), "下载ing: " + progress);
            }

            @Override
            public void onFinished(int completeSize, String downloadUrl) {
				UpdateManager.getInstance().removeRequest();
                DLog.log(getClass(), "下载完成: " + filePath);
                File apkFile = new File(filePath);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()), "application/vnd.android.package-archive");
                startActivity(intent);
                stopSelf();
            }

            @Override
            public void onFailure() {
				UpdateManager.getInstance().removeRequest();
                DLog.log(getClass(), "下载失败: ");
                stopSelf();
            }
        });
    }

    /**
     * 更新notification来告知用户下载进度
     *
     * @param result
     * @param reason
     * @param progress
     */
    private void notifyUser(String result, String reason, int progress) {
        Notification mNotification;
        NotificationCompat.Builder build = new NotificationCompat.Builder(this);
        build.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name));
        if (progress > 0 && progress < 100) {
            build.setProgress(100, progress, false);
        } else {
            build.setProgress(0, 0, false);
        }

        build.setAutoCancel(false);
        build.setWhen(System.currentTimeMillis());
        build.setTicker(result);
        build.setContentIntent(progress >= 100 ? getContentIntent() : PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        mNotification = build.build();
        notificationManager.notify(0, mNotification);
    }

    public PendingIntent getContentIntent() {
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()), "application/vnd.android.package-archive");
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

