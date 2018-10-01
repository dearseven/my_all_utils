package chat.sh.orz.cyan.orz.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

/**
 * 手机的尺寸信息
 */
public class LayoutSizeInfoTeller {
    private final static String TAG = "LayoutSizeInfoTeller";
    //手机的屏幕尺寸
    private final static float SCREEN_INCH = 6f;

    public static void show(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        Log.i(TAG, "widthPixels:" + dm.widthPixels);
        Log.i(TAG, "heightPixels:" + dm.heightPixels);
        //获取屏幕尺寸
        Log.i(TAG, "screenWidthInch:" + String.valueOf(((float) dm.widthPixels) / dm.xdpi));
        Log.i(TAG, "screenHeightInch:" + String.valueOf(((float) dm.heightPixels) / dm.ydpi));
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        Log.i(TAG, "screenSizeInch:" + String.valueOf(Math.sqrt(x + y)));
        //获取屏幕分辨率
        Log.i(TAG, "x px:" + String.valueOf(dm.widthPixels));
        Log.i(TAG, "y px:" + String.valueOf(dm.heightPixels));
        //物理像素
        Log.i(TAG, "xdpi:" + String.valueOf(dm.xdpi));
        Log.i(TAG, "ydpi:" + String.valueOf(dm.ydpi));
        Log.i(TAG, "densityDpi:" + String.valueOf(dm.densityDpi));
        Log.i(TAG, "density:" + String.valueOf(dm.density));
        //DIP
        float dipW = (((float) dm.widthPixels) * 160.0f) / ((float) dm.densityDpi);
        float dipH = (((float) dm.heightPixels) * 160.0f) / ((float) dm.densityDpi);
        Log.i(TAG, "mDIPWidth:" + String.valueOf(dipW));
        Log.i(TAG, "mDIPHeight:" + String.valueOf(dipH));
        //建议
        Log.i(TAG, "layout" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        Log.i(TAG, "layout-land" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        Log.i(TAG, "layout" + getSmallestWidthString((int) dipW, (int) dipH));
        Log.i(TAG, "values" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        Log.i(TAG, "values-land" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        Log.i(TAG, "values" + getSmallestWidthString((int) dipW, (int) dipH));
        //statusbar and navigationbar
        Log.i(TAG, String.valueOf(getStatusBarHeight(activity)));
        Log.i(TAG, String.valueOf(getNavigationBarHeight(activity)));

        String fitInfo = "LayoutSizeInfoTeller fit layout dpi:" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels);
        String fitInfoWithScreenInch = "LayoutSizeInfoTeller fit With ScreenInch:" + SCREEN_INCH + "英寸,layout dpi:" +
                (Math.sqrt(dm.widthPixels * dm.widthPixels + dm.heightPixels * dm.heightPixels) / SCREEN_INCH);
        new AlertDialog.Builder(activity)
                .setMessage(fitInfo + ";" + fitInfoWithScreenInch)
                .create()
                .show();
    }


    //获得导航栏的高度
    private static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android"); //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    //获取状态栏的高度
    private static int getStatusBarHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;  //屏幕宽
        int height = dm.heightPixels;  //屏幕高
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;  //状态栏高
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentTop - statusBarHeight; //标题栏高

        return titleBarHeight;
    }

    private static String densityDpiToString(int densityDpi) {
        String str;
        switch (densityDpi) {
            case 120:
                str = "ldpi";
                break;
            case 160:
                str = "mdpi";
                break;
            case 213:
                str = "tvdpi";
                break;
            case 240:
                str = "hdpi";
                break;
            case 320:
                str = "xhdpi";
                break;
            case 480:
                str = "xxhdpi";
                break;
            case 640:
                str = "xxxhdpi";
                break;
            default:
                str = "N/A";
                break;
        }
        return densityDpi + " (" + str + ")";
    }

    private static String getResolutionString(int rw, int rh) {
        return rw > rh ? "-" + rw + "x" + rh : "-" + rh + "x" + rw;
    }

    private static String getSmallestWidthString(int dipWidth, int dipHeight) {
        StringBuilder stringBuilder = new StringBuilder("-sw");
        if (dipWidth >= dipHeight) {
            dipWidth = dipHeight;
        }
        return stringBuilder.append(dipWidth).append("dp").toString();
    }
}
