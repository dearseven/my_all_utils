
import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

/**
 * 推送通知的工具类（不是前台服务，前台服务参考MyFrontService）
 */
class NotifyUtil {
    private val CHANNEL_ID = "update_status"
    private val CHANNEL_NAME = "update_status_name"
    //val notifyId = 0

    private constructor()

    private object Holder {
        val instance = NotifyUtil()
    }

    companion object {
        val instance = Holder.instance
    }


    fun sendSimpleNotify(
        context: Context?,
        notifyId: Int,
//        requestCode: Int,
        //title: String,
        desc: String?,  
//        smallIcon: Int,
//        largeIcon: Int  
    ): Int {
        val applicationContext = context ?:WeakHolder.instance.weakApplication.get()!!
        if (applicationContext == null) {
            Log.i("sendSimpleNotify", "applicationContext为空")
            return 0;
        }
        val notificationManager =
            applicationContext.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val packageName = applicationContext.packageName
        //
        val smallIcon = R.mipmap.ic_launcher
        val largeIcon = R.mipmap.ic_launcher
        val title = applicationContext.resources.getString(R.string.app_name_cn)
        //val notifyId = 100// (System.currentTimeMillis() % 10000).toInt()
        val requestCode = notifyId
        val componentName = "com.teetaa.outsourcing.carinspection.activity.TestNotificationActivity"
        //
        __notify(
            applicationContext, notificationManager, packageName,
            notifyId, requestCode,
            title, desc, smallIcon, largeIcon,
            componentName
        )

        return requestCode
    }

    private fun __notify(
        context: Context,
        notificationManager: NotificationManager,
        packageName: String,
        notifyId: Int,
        requestCode: Int,
        title: String,//传入null就不设置
        desc: String?,  //传入null就不设置
        smallIcon: Int,//传入0就不设置
        largeIcon: Int,  //传入0就不设置
        componentName: String
    ) {
        val notification: Notification?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            //设置声音
            //channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/raw/qqqq"),null);
            notificationManager?.createNotificationChannel(channel)
            val com =
                ComponentName(packageName, componentName)
            val intentForeSerive = Intent()
            intentForeSerive.setPackage(packageName)
            intentForeSerive.component = com
            val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                intentForeSerive,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(desc)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
                .setAutoCancel(true)
//                .setLargeIcon(
//                    BitmapFactory.decodeResource(context.getResources(), largeIcon)
//                )
                .setContentIntent(pendingIntent)
                .let {
                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                        it.build();
                    } else {
                        it.getNotification();
                    }
                }
        } else {
            //  val channel = NotificationChannel(CHANNEL_ID, SERVICE_NAME, NotificationManager.IMPORTANCE_HIGH)
            // notificationManager.createNotificationChannel(channel)
            val com = ComponentName(packageName, componentName)
            val intentForeSerive = Intent()
            intentForeSerive.setPackage(packageName)
            intentForeSerive.component = com
            val pendingIntent = PendingIntent.getActivity(
                context, requestCode, intentForeSerive, PendingIntent.FLAG_UPDATE_CURRENT
            )
            notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(desc)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
//                .setLargeIcon(
//                    BitmapFactory.decodeResource(context.getResources(), largeIcon)
//                )
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                //.setSound(Uri.parse("android.resource://" + getPackageName() + "/raw/qqqq"));
                .let {
                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                        it.build();
                    } else {
                        it.getNotification();
                    }
                }
        }

        notification?.also {
            //startForeground(MyFrontService.startForegroundID, notification);
            notificationManager?.notify(notifyId, it)
        }
    }
}