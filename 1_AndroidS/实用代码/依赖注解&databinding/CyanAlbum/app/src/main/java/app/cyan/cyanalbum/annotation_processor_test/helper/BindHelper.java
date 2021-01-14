package app.cyan.cyanalbum.annotation_processor_test.helper;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import app.cyan.cyanalbum.annotation_processor_test.finder.ActivityFinder;
import app.cyan.cyanalbum.annotation_processor_test.finder.Finder;
import app.cyan.cyanalbum.utils.DLog;

import static com.jap.myprocess.process.TypeUtil.FILE_NAME_END_WITH;


public class BindHelper {

    public BindHelper() {
        throw new AssertionError("No .instances");
    }

    private static final ActivityFinder ACTIVITY_FINDER = new ActivityFinder();

    //private static final ViewFinder VIEW_FINDER = new ViewFinder();

    /**
     *
     */
    private static Map<String, Injector> FINDER_MAP = new HashMap<>();

    public static void bind(Activity activity) {
        bind(activity, activity, ACTIVITY_FINDER);
    }

//    public static void bind(View view) {
//        bind(view, view);
//    }


   // public static void bind(Object host, View view) {
   //     bind(host, view, ACTIVITY_FINDER);
    // }

    public static void unbind(Activity activity){
        FINDER_MAP.clear();
    }
    /**
     * 获取目标类
     *
     * @param host
     * @param source
     * @param finder
     */
    public static void bind(Object host, Object source, Finder finder) {
        String className = host.getClass().getName();
        try {
            Injector injector = FINDER_MAP.get(className);
            if (injector == null) {
                //这里是反射出那个自动注解类，所以注解类的名字在里要和生成的一样
                Class<?> finderClass = Class.forName(className + FILE_NAME_END_WITH);
                //DLog.i(BindHelper.class,"-----"+finderClass.getName());
                injector = (Injector) finderClass.newInstance();
                FINDER_MAP.put(className, injector);
            }
            //执行这个的时候就就是做了绑定，因为实际上生成的类是实习实现了这个接口的一个类，然后是在这个方法里进行的自动注入
            injector.inject(host, source, finder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
