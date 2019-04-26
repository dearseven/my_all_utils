package app.cyan.cyanalbum.annotation_processor_test.finder;

import android.app.Activity;
import android.content.Context;
import android.view.View;


public class ActivityFinder implements Finder {
//    @Override
//    public Context getContext(Object viewOrActivity) {
//        return (Activity) viewOrActivity;
//    }

    @Override
    public View findView(Object activityOrView, int id) {
        return ((Activity) activityOrView).findViewById(id);
    }
}
