
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Toast;


import com.teetaa.fblib.Debug;

import uex.InsertShowSize;
import uex.md5.MD5;

/**
 * 显示尺寸
 * Created by wx on 2017/7/31.
 */

public class ShowSizeUtil {

    public void showSize(Activity ctx) {
        StringBuffer sb = new StringBuffer();
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        //获取屏幕尺寸
        Debug.i(getClass(), "screenWidthInch:" + String.valueOf(((float) dm.widthPixels) / dm.xdpi));
        Debug.i(getClass(), "screenHeightInch:" + String.valueOf(((float) dm.heightPixels) / dm.ydpi));
        sb.append("屏幕尺寸 screenWidthInch:" + String.valueOf(((float) dm.widthPixels) / dm.xdpi) + ",");
        sb.append("屏幕尺寸 screenHeightInch:" + String.valueOf(((float) dm.heightPixels) / dm.ydpi) + ",");

        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        Debug.i(getClass(), "screenSizeInch:" + String.valueOf(Math.sqrt(x + y)));
        sb.append("screenSizeInch:" + String.valueOf(Math.sqrt(x + y)) + ",");

        //获取屏幕分辨率
        Debug.i(getClass(), "x px:" + String.valueOf(dm.widthPixels));
        Debug.i(getClass(), "y px:" + String.valueOf(dm.heightPixels));
        sb.append("屏幕分辨率:x px:" + String.valueOf(dm.widthPixels) + ",");
        sb.append("屏幕分辨率:y px:" + String.valueOf(dm.heightPixels));

        //物理像素
        Debug.i(getClass(), "xdpi:" + String.valueOf(dm.xdpi));
        Debug.i(getClass(), "ydpi:" + String.valueOf(dm.ydpi));
        Debug.i(getClass(), "densityDpi:" + String.valueOf(dm.densityDpi));
        Debug.i(getClass(), "density:" + String.valueOf(dm.density));
        sb.append("物理像素:xdpi:" + String.valueOf(dm.xdpi) + ",");
        sb.append("物理像素:ydpi:" + String.valueOf(dm.ydpi) + ",");
        sb.append("物理像素:densityDpi:" + String.valueOf(dm.densityDpi) + ",");
        sb.append("物理像素:density:" + String.valueOf(dm.density) + ",");

        //DIP
        float dipW = (((float) dm.widthPixels) * 160.0f) / ((float) dm.densityDpi);
        float dipH = (((float) dm.heightPixels) * 160.0f) / ((float) dm.densityDpi);
        Debug.i(getClass(), "mDIPWidth:" + String.valueOf(dipW));
        Debug.i(getClass(), "mDIPHeight:" + String.valueOf(dipH));
        sb.append("dip,mDIPWidth:" + String.valueOf(dipW) + ",");
        sb.append("dip,mDIPHeight:" + String.valueOf(dipH) + ",");

        //建议
        Debug.i(getClass(), "layout" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        Debug.i(getClass(), "layout-land" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        Debug.i(getClass(), "layout" + getSmallestWidthString((int) dipW, (int) dipH));
        Debug.i(getClass(), "values" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        Debug.i(getClass(), "values-land" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels));
        Debug.i(getClass(), "values" + getSmallestWidthString((int) dipW, (int) dipH));

        sb.append("建议suggustion，layout" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels) + ",");
        sb.append("建议suggustion，layout-land" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels) + ",");
        sb.append("建议suggustion，layout" + getSmallestWidthString((int) dipW, (int) dipH) + ",");
        sb.append("建议suggustion，values" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels) + ",");
        sb.append("建议suggustion，values-land" + getSmallestWidthString((int) dipW, (int) dipH) + getResolutionString(dm.widthPixels, dm.heightPixels) + ",");
        sb.append("建议suggustion，values" + getSmallestWidthString((int) dipW, (int) dipH) + ",");
        //statusbar and navigationbar
        Debug.i(getClass(), String.valueOf(getStatusBarHeight(ctx)));
        Debug.i(getClass(), String.valueOf(getNavigationBarHeight(ctx)));


        sb.append("状态栏高度," + String.valueOf(getStatusBarHeight(ctx)) + ",");
        sb.append("导航栏高度高度," + String.valueOf(getNavigationBarHeight(ctx)) + "。");

        String info = sb.toString();
        //String uniqueId = MD5.md5AndHex(info);
    }


    //获得导航栏的高度
    public int getNavigationBarHeight(Activity ctx) {
        Resources resources = ctx.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android"); //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    //获取状态栏的高度
    public int getStatusBarHeight(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;  //屏幕宽
        int height = dm.heightPixels;  //屏幕高
        Rect frame = new Rect();
        ctx.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;  //状态栏高
        int contentTop = ctx.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
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
