package com.cyaninject.processor;

import android.view.View;

import com.cyaninject.annotions.CyanView;
import com.cyaninject.processor.interfaces.IProcessor;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

/**
 * InjectViewProcessor类用来解释Field属性
 */
public class InjectViewProcessor implements IProcessor<Field> {
    @Override
    public boolean accept(AnnotatedElement t) {
        //如果当前这个AnnotatedElement实例加有InjectView注解，则返回true
        return t.isAnnotationPresent(CyanView.class);
    }


    @Override
    public void process(Object object, View view, Field ao) {
        //如果存在该元素的指定类型的注释，则返回这些注释，否则返回 null
        CyanView iv = ao.getAnnotation(CyanView.class);
        if (iv != null) {
            final int viewId = iv.value(); //获取注解值（找到控件的id）
            final View v = view.findViewById(viewId); //通过根view查找此id对应的view
            ao.setAccessible(true); //设置属性值的访问权限
            try {
                ao.set(object, v); //将查找到的view指定给目标对象object
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
