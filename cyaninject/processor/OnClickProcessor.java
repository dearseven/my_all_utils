package com.cyaninject.processor;

import android.view.View;

import com.cyaninject.annotions.CyanClick;
import com.cyaninject.processor.interfaces.IProcessor;

import java.lang.ref.WeakReference;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class OnClickProcessor implements IProcessor<Method> {
    @Override
    public boolean accept(AnnotatedElement e) {
        //这个很简单，就是告诉管理器我响应OnClick注解
        return e.isAnnotationPresent(CyanClick.class);
    }

    @Override
    public void process(Object object, View view, Method method) {
        //先拿到具体的注解对象 ，并拿到里面的值
        final CyanClick oc = method.getAnnotation(CyanClick.class);
        final int[] value = oc.value();
        //遍历id值设置点击事件将方法method与目标对象object传给监听事件
        for (int id : value) {
            view.findViewById(id).setOnClickListener(new InvokeOnClickListener(method, object));
        }
    }

    //这里面的InvokeOnClickListener是一个中间件，注册给系统，系统在得到点击事件后，
    //通知给InvokeOnClickListener，在这个里面再调用你所指定的方法。
    private static class InvokeOnClickListener implements View.OnClickListener {

        public Method method;
        public WeakReference<Object> obj;
        private boolean hasParam;

        InvokeOnClickListener(Method m, Object object) {
            this.method = m;
            //使用一个WeakReference
            this.obj = new WeakReference<Object>(object);
            //先拿到方法的参数，看看有没有参数 ， 没有就纪录下hasParam为false　
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes == null || parameterTypes.length == 0) {
                hasParam = false;
                //如果有参数的话，判断参数个数，并且判断参数类型是否为view
                //isAssignableFrom方法用来判断一个类Class1和另一个类Class2是否相同或是另一个类的子类或接口
            } else if (parameterTypes.length > 1 || !View.class.isAssignableFrom(parameterTypes[0])) {
                throw new IllegalArgumentException(String.format("%s方法只能拥有0个或一个参数，且只接收View", m.getName()));
            } else {
                //有一个合格参数则返回true
                hasParam = true;
            }
        }

        @Override
        public void onClick(View v) {
            //点击事件触发了
            Object o = obj.get();
            if (o != null) {
                try {
                    if (hasParam) {
                        method.invoke(o, v); //有参数，就把view设置过去
                    } else {
                        method.invoke(o); //没有参数就直接调
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
