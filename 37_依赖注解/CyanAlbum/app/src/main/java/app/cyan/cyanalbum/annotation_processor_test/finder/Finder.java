package app.cyan.cyanalbum.annotation_processor_test.finder;

import android.app.Activity;
import android.content.Context;
import android.view.View;


public interface Finder {

//    /**
//     * 根据source获取Context
//     *
//     * @param viewOrActivity 空间来源，例如Activity，View
//     * @return
//     */
//    Activity getContext(Object viewOrActivity);

    /**
     * 根据id找控件，其实也可以不做这个接口，但是为什么要做呢，因为这样的话，findview就可以在代码里写，比较方便。
     * 自动注入的那个类只要调用这个接口就好了
     *
     * @param activityOrView 控件来源
     * @param id     目标控件ID
     * @return
     */
    View findView(Object activityOrView, int id);
}
