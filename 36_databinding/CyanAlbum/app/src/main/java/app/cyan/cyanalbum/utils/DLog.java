package app.cyan.cyanalbum.utils;

import android.os.Debug;
import android.util.Log;

/**
 * Created by Cyan on 2017/10/28.
 */

public class DLog {
    public static void i(Class clz,String log){
        Log.i("CyanAlbum",clz.getName());
        Log.i("CyanAlbum",log);
    }
}
