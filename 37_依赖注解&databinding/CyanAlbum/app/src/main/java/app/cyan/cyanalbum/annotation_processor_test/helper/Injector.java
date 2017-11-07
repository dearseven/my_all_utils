package app.cyan.cyanalbum.annotation_processor_test.helper;

import app.cyan.cyanalbum.annotation_processor_test.finder.Finder;

public interface Injector<T> {
    /**
     * @param exActivity   目标，因为不管怎么用肯定是有一个activity的
     * @param activityOrView 控件来源
     * @param finder
     */
    void inject(T exActivity, Object activityOrView, Finder finder);
}
