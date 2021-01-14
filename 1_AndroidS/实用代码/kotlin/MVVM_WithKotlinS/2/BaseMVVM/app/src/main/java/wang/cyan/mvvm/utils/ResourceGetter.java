package wang.cyan.mvvm.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * 通过资源名，反射获取ID
 *
 * @author wx 2014-8-12 下午3:07:43
 */
public class ResourceGetter {
    /**
     * 获View的id
     *
     * @param ctx
     * @param idName
     * @return
     */
    public static int getViewID(Context ctx, String idName) {
        int id = 0;
        try {
            Resources res = ctx.getResources();
            id = res.getIdentifier(idName, "id", ctx.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 获取字符串的id
     *
     * @param ctx
     * @param idName 在strings.xml中定义的name
     * @return
     */
    public static int getStringID(Context ctx, String idName) {
        int id = 0;
        try {
            Resources res = ctx.getResources();
            id = res.getIdentifier(idName, "string", ctx.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 获取Drawable的id
     *
     * @param ctx
     * @param idName
     * @return
     */
    public static int getDrawableID(Context ctx, String idName) {
        int id = 0;
        try {
            Resources res = ctx.getResources();
            id = res.getIdentifier(idName, "drawable", ctx.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 获取Mipmap的id
     *
     * @param ctx
     * @param idName
     * @return
     */
    public static int getMipmapID(Context ctx, String idName) {
        int id = 0;
        try {
            Resources res = ctx.getResources();
            id = res.getIdentifier(idName, "mipmap", ctx.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 获取color的id
     *
     * @param ctx
     * @param idName
     * @return
     */
    public static int getColorID(Context ctx, String idName) {
        int id = 0;
        try {
            Resources res = ctx.getResources();
            id = res.getIdentifier(idName, "color", ctx.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * layout
     *
     * @param ctx
     * @param idName
     * @return
     */
    public static int getLayoutID(Context ctx, String idName) {
        int id = 0;
        try {
            Resources res = ctx.getResources();
            id = res.getIdentifier(idName, "layout", ctx.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
