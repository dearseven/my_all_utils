package com.jap.myprocess.process;

import com.squareup.javapoet.ClassName;

/**
 *ClassName的都是在写java文件的时候使用
 * FILE_NAME_END_WITH
 */
public class TypeUtil {
    public static final ClassName FINDER = ClassName.get("app.cyan.cyanalbum.annotation_processor_test.finder", "Finder");
    public static final ClassName INJECTOR = ClassName.get("app.cyan.cyanalbum.annotation_processor_test.helper", "Injector");
    public static final ClassName ONCLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");

    public static final String FILE_NAME_END_WITH="$$_Injector";
}
