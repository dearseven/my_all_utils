package wang.cyan.mvvm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * Created by wx on 2016/11/8.
 */

public class NetWorkTester {
    /**
     * 无网络连接
     */
    public static byte NO_INTERNET = 0;
    /**
     * wifi连接
     */
    public static byte WIFI = 1;
    /**
     * 移动网络连接
     */
    public static byte MOBILE = 2;

    /**
     * 返回网络状态,状态值定义在该类的静态属性中
     *
     * @param context
     * @return
     */
    // public static int netstate(Context context) {
    // byte res = NO_INTERNET;
    //
    // // 获取连接管理器
    // ConnectivityManager connManager = (ConnectivityManager) context
    // .getSystemService(Context.CONNECTIVITY_SERVICE);
    // if (connManager.getActiveNetworkInfo() != null) {
    // // 如果有网络，则判断状态
    // if (connManager.getActiveNetworkInfo().isAvailable()) {
    //
    // NetworkInfo info = connManager
    // .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    // State state = info.getState();
    // if (state == State.CONNECTED) {
    // res = WIFI;
    // }
    // }
    // }
    //
    // return res;
    // }

    /**
     * 返回网络状态,状态值定义在该类的静态属性中
     *
     * @param context
     * @return
     */
    public static byte getNetState(Context context) {
        byte res = NO_INTERNET;
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                if (mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if (mNetworkInfo.isConnected()
                            && mNetworkInfo.isAvailable()) {
                        // DebugLog.log(null, "TYPE_MOBILE 可用",
                        // NetWorkTester.class);
                        res = MOBILE;
                    } else {
                        // DebugLog.log(null, "TYPE_MOBILE 不可用",
                        // NetWorkTester.class);

                    }
                } else if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    if (mNetworkInfo.isConnected()
                            && mNetworkInfo.isAvailable()) {
                        res = WIFI;
                        // DebugLog.log(null, "TYPE_WIFI 可用",
                        // NetWorkTester.class);
                    } else {
                        // DebugLog.log(null, "TYPE_WIFI 不可用",
                        // NetWorkTester.class);

                    }
                }
            }
            // else {
            // no internet
            // }
        }
        //DebugLog.log(null, "网络判定A："+res, NetWorkTester.class);
        //再加一层wifi判定
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (TextUtils.equals(info[i].getTypeName(), "WIFI")
                            && info[i].isConnected()) {
                        res = WIFI;
                    }
                }
            }
        }
        //DebugLog.log(null, "网络判定B："+res, NetWorkTester.class);

        return res;
    }

}
