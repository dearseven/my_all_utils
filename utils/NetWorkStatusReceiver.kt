
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.NetworkInfo.State
import android.os.Build
import android.text.TextUtils


/**
 * 监听用户的网络状态
 *
 *  <intent-filter>
 * <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
 * </intent-filter>
 * Created by wx on 2017/7/11.
 */

class NetWorkStatusReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var statu1 = 0 //0 无 1wifi 2手机
        var statu2 = 0//0 无 1wifi 2手机
        var wifiState: State? = null
        var mobileState: State? = null
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        val mConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //判断1
        val mNetworkInfo = mConnectivityManager
                .activeNetworkInfo
        if (mNetworkInfo != null) {
            if (mNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                if (mNetworkInfo.isConnected && mNetworkInfo.isAvailable) {
                    // DebugLog.log(null, "TYPE_WIFI 可用",
                    // NetWorkTester.class);
                    statu1 = 1
                }
            } else if (mNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                if (mNetworkInfo.isConnected && mNetworkInfo.isAvailable) {
                    // DebugLog.log(null, "TYPE_MOBILE 可用",
                    // NetWorkTester.class);
                    statu1 = 2
                }
            }
        }
        //判断2
        var infos: Array<NetworkInfo>? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            infos = mConnectivityManager.allNetworks.map {
                mConnectivityManager.getNetworkInfo(it)
            }.toTypedArray()
        } else {
            infos = mConnectivityManager.allNetworkInfo;
        }
        if (infos != null) {
            for (i in 0..infos.size - 1) {
                if (TextUtils.equals(infos[i].getTypeName(), "WIFI") && infos[i].isConnected()) {
                    statu2 = 1
                }
            }
        }
        //判断优先级 如果status2为1时则是wifi ，其次如果status1为1时为wifi ，再次 status1为2则是手机网络
        if (statu2 == 1) {

        } else if (statu1 == 1) {

        } else if (statu1 == 2) {

        }
    }

}
