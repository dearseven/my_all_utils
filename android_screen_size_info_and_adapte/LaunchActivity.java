package cc.m2u.alternator_monitor1.activity;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import cc.m2u.alternator_monitor1.R;
import cc.m2u.alternator_monitor1.constants.DeviceSize;
import cc.m2u.alternator_monitor1.utils.DebugLog;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        //获取屏幕尺寸
        DebugLog.i(getClass(), "screenWidthInch:" + String.valueOf(((float) dm.widthPixels) / dm.xdpi));
        DebugLog.i(getClass(), "screenHeightInch:" + String.valueOf(((float) dm.heightPixels) / dm.ydpi));
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        DebugLog.i(getClass(), "screenSizeInch:" + String.valueOf(Math.sqrt(x + y)));
        //获取屏幕分辨率
        DebugLog.i(getClass(), "x px:" + String.valueOf(dm.widthPixels));
        DebugLog.i(getClass(), "y px:" + String.valueOf(dm.heightPixels));
        //物理像素
        DebugLog.i(getClass(), "xdpi:" + String.valueOf(dm.xdpi));
        DebugLog.i(getClass(), "ydpi:" + String.valueOf(dm.ydpi));
        DebugLog.i(getClass(), "densityDpi:" + String.valueOf(dm.densityDpi));
        DebugLog.i(getClass(), "density:" + String.valueOf(dm.density));
        //DIP
        float dipW = (((float) dm.widthPixels) * 160.0f) / ((float) dm.densityDpi);
        float dipH = (((float) dm.heightPixels) * 160.0f) / ((float) dm.densityDpi);
        DebugLog.i(getClass(), "mDIPWidth:" + String.valueOf(dipW));
        DebugLog.i(getClass(), "mDIPHeight:" + String.valueOf(dipH));
        //建议
        DebugLog.i(getClass(),"layout" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        DebugLog.i(getClass(),"layout-land" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        DebugLog.i(getClass(),"layout" + getSmallestWidthString((int) dipW, (int) dipH));
        DebugLog.i(getClass(),"values" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        DebugLog.i(getClass(),"values-land" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        DebugLog.i(getClass(),"values" + getSmallestWidthString((int) dipW, (int) dipH));
        //statusbar and navigationbar
        DebugLog.i(getClass(),String.valueOf(getStatusBarHeight()));
        DebugLog.i(getClass(),String.valueOf(getNavigationBarHeight()));

    }

    //获得导航栏的高度
    public int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android"); //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    //获取状态栏的高度
    public int getStatusBarHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;  //屏幕宽
        int height = dm.heightPixels;  //屏幕高
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;  //状态栏高
        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentTop - statusBarHeight; //标题栏高

        return titleBarHeight;
    }

    private String densityDpiToString(int densityDpi) {
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

    private String getResolutionString(int rw, int rh) {
        return rw > rh ? "-" + rw + "x" + rh : "-" + rh + "x" + rw;
    }

    private String getSmallestWidthString(int dipWidth, int dipHeight) {
        StringBuilder stringBuilder = new StringBuilder("-sw");
        if (dipWidth >= dipHeight) {
            dipWidth = dipHeight;
        }
        return stringBuilder.append(dipWidth).append("dp").toString();
    }
}
