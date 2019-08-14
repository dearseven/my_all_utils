package com.cyaninject.main;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.cyaninject.processor.InjectViewProcessor;
import com.cyaninject.processor.OnClickProcessor;
import com.cyaninject.processor.interfaces.IProcessor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class CyanInjector {
    private static List<? extends IProcessor<? extends AccessibleObject>> chain
            = Arrays.asList(new InjectViewProcessor(), new OnClickProcessor());

    public static void injectActivity(Activity act) {
        //传入activity实例和rootview
        inject(act, act.getWindow().getDecorView());
    }

    public static void injectFragment(Fragment fragment,View rootview) {
        //传入fragment实例和rootview
        inject(fragment, rootview);
    }



    private static void inject(Object obj, View rootView) {
        final Class<?> aClass = obj.getClass();
        //获取当前活动中所有声明的属性，包括私有属性
        final Field[] declaredFields = aClass.getDeclaredFields();
        for (Field f : declaredFields) {
            doChain(obj, f, rootView);
        }
        //获取当前活动中所有声明的方法，包括私有方法
        final Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method m : declaredMethods) {
            doChain(obj, m, rootView);
        }
    }

    //把每个遍历到的方法或者属性，甚至是构造方法，类等等通过处理器链来询问这个注解你accept吗？接受则交给它来处理
    private static void doChain(Object obj, AccessibleObject ao, View rootView) {
        for (IProcessor p : chain) {
            //判断当前AccessibleObject(Field,Method都继承了此类)是否为ProcessorIntf具体子类类型的注解
            if (p.accept(ao)) {
                p.process(obj, rootView, ao);
            }
        }
    }

}
