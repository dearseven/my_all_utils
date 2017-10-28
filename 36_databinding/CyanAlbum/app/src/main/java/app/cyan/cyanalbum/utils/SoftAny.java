package app.cyan.cyanalbum.utils;

import java.lang.ref.WeakReference;

/**
 *
 * Created by Cyan on 2017/10/28.
 */

public class SoftAny  {
    private WeakReference<Object> weakReference = null;

    public SoftAny(Object any) {
        weakReference = new WeakReference<Object>(any);
    }

    public <T> T get(Class<T> claz) {
        try {
            return claz.cast(weakReference.get());
        } catch (Exception ex) {
            return null;
        }
    }
}
