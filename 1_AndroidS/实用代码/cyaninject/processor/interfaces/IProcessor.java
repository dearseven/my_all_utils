package com.cyaninject.processor.interfaces;

import android.view.View;

import java.lang.reflect.AnnotatedElement;
//----注解的解释类
//这个泛型接口接收一个AnnotatedElement的子类，它是什么呢？
//在Java中，Field,Method,Constructor…一切可注解的对象都实现了AnnotatedElement接口
public interface IProcessor<T extends AnnotatedElement> {
    /*
     * 每个不同的处理器都会通过这个方法来告诉最终的调度者，这个注解是否由我来处理
     */
    public boolean accept(AnnotatedElement t);

    /*
     *第一个object是目标对象，
     *第二个view是根view
     *第三个是加上注解的值
     */
    public void process(Object object, View view, T ao);

}
