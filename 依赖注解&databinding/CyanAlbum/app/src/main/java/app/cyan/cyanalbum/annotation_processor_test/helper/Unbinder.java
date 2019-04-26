package app.cyan.cyanalbum.annotation_processor_test.helper;

import android.support.annotation.UiThread;

/**
 * Created by mingwei on 12/20/16.
 */
@Deprecated
public interface Unbinder {

    @UiThread
    void unbind();

    Unbinder EMPTY = new Unbinder() {
        @Override
        public void unbind() {

        }
    };
}
