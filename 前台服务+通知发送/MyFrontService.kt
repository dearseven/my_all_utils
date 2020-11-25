
import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
@AndroidEntryPoint
class MyFrontService : Service() {
   
    //----------------------------services的一些基本东西-----------------------------------------
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.also {
            it.action?.also { action ->
                if (action.equals("stop")) {
                    stopForeground(true)
                    stopSelf()
                } else if (action.equals("start")) {
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    //------------------------------------------------------下面都是开启前台服务------------------------------------------------------------
    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    var notification: Notification? = null
    var notificationManager: NotificationManager? = null
    val notifyId = 1001

    companion object {
        val NAME = "com.teetaa.outsourcing.carinspection.frontservice.FrontLocateService"

        //
        val SERVICE_ID = "FrontLocateService1"
        val SERVICE_NAME = "FrontLocateService2"
        val PACKAGE_NAME = "com.teetaa.outsourcing.carinspection"
        val CHANNEL_ID = "FrontLocateService1"
        val startForegroundID = 10001

        //轮询
        val POLLING = "POLLING_CHAT"

        var isFront=false
        //开启前台服务，只开启前台，不包含业务
        fun startFrontServices(context: Context) {
            isFront=true

            Intent(context, MyFrontService::class.java).also {
                it.action = "start"
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(it)
                } else {
                    context.startService(it)
                }
            }
        }

        //关闭前台服务
        fun stopFrontServices(context: Context) {
            isFront=false

            
            //
            Intent(context, MyFrontService::class.java).also {
                it.action = "stop"
                context.startService(it)
            }
        }

    }

    override fun onDestroy() {
        Log.i("MyFrontService", "onDestroy")
        super.onDestroy()
    }

    override fun onCreate() {
        Log.i("MyFrontService", "onCreate")
        //-----------------
        super.onCreate()


        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //var notification: Notification? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8
            val channel =
                NotificationChannel(CHANNEL_ID, SERVICE_NAME, NotificationManager.IMPORTANCE_HIGH)

            notificationManager?.createNotificationChannel(channel)
            val com =
                ComponentName(
                    packageName,
                    "com.teetaa.outsourcing.carinspection.activity.MainActivity"
                )
            val intentForeSerive = Intent()
            intentForeSerive.setPackage(packageName)
            intentForeSerive.component = com
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intentForeSerive,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("车检")
                .setContentText("Running")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        getResources(),
                        R.mipmap.ic_launcher
                    )
                )
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
            val com = ComponentName(
                packageName,
                "com.teetaa.intellivusdemo3.activities.TransferProgressActivity"
            )
            val intentForeSerive = Intent()
            intentForeSerive.setPackage(packageName)
            intentForeSerive.component = com
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intentForeSerive,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("车检")
                .setContentText("Running")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        getResources(),
                        R.mipmap.ic_launcher
                    )
                )
                .setContentIntent(pendingIntent).let {
                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                        it.build();
                    } else {
                        it.getNotification();
                    }
                }
        }

        notification?.also {
            startForeground(startForegroundID, notification);
            //notificationManager?.notify(notifyId, it)
        }
    }


}
