package wc.c.libbase.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.TrafficStats;
import android.util.Log;

public class NetWorkSpeedUtil {
    public static String speedStr = "";
    private static final String TAG = NetWorkSpeedUtil.class.getSimpleName();
    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

    public String tellMeSpeed(Context ctx) {
        //PackageManager pm = ctx.getPackageManager();
        ApplicationInfo ai = ctx.getApplicationInfo();
//        try {
//            ai = pm.getApplicationInfo("com.xxx.xxx", PackageManager.GET_ACTIVITIES);
//        } catch (PackageManager.NameNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }

        long nowTotalRxBytes = getTotalRxBytes(ai.uid);
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        if (speed > 1024l) {
            speedStr = String.format("%.2f", speed / 1024d) + " MB/S";
        } else {
            speedStr = speed + " KB/S";
        }
        //Log.i("speedStr", "speedStr=" + speedStr);
        return speedStr;
    }

    private long getTotalRxBytes(int uid) {
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB
    }
}
