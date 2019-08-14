package com.cyaninject.annotions;

import androidx.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CyanClick {
    //接收一个int[]，表示可以接收多个view的id,绑定到同一个click执行方法上
    public @IdRes int[] value();

}
