package com.jap.myprocess.process;

import com.jap.myprocess.model.BindViewField;
import com.jap.myprocess.model.OnClickMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static com.jap.myprocess.process.TypeUtil.FILE_NAME_END_WITH;

/**
 * 这个类负责写文件
 */
public class AnnotatedClass {

    /**
     * 调用注解了那个类型，在例子是某个activity
     */
    public TypeElement mClassElement;
    /**
     * 在这个例子里是需要自动注入的view的列表
     */
    public List<BindViewField> mFiled;
    /**
     * 在这个例子里是需要自动注入的click事件的列表
     */
    public List<OnClickMethod> mMethod;
    /**
     * 元素帮助类
     */
    public Elements mElementUtils;

    public AnnotatedClass(TypeElement classElement, Elements elementUtils) {
        this.mClassElement = classElement;
        this.mElementUtils = elementUtils;
        this.mFiled = new ArrayList<>();
        this.mMethod = new ArrayList<>();
    }

    public String getFullClassName() {
        return mClassElement.getQualifiedName().toString();
    }

    public void addField(BindViewField field) {
        mFiled.add(field);
    }

    public void addMethod(OnClickMethod method) {
        mMethod.add(method);
    }


    public JavaFile generateFinder() {
        /**
         * 构建方法,这个是实现Injector接口需要实现的方法
         */
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "activity", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "activityOrView")
                .addParameter(TypeUtil.FINDER, "finder");
        /**
         * 遍历写需要自动注入的view的列表
         * 注意这里使用了Finder接口View findView(Object activityOrView, int id);
         * 其实这个finder的实例是在app里的进行BindHelper.bind(this)方法的时候，传进来的
         */
        for (BindViewField field : mFiled) {
            methodBuilder.addStatement("activity.$N=($T)finder.findView(activityOrView,$L)", field.getMNeedInjectViewElmtName()
                    , ClassName.get(field.getMNeedInjectViewElmtNameType()), field.getResId());
        }

        /**
         * 遍历需要生成的方法
         */
        int i=0;
        for (OnClickMethod method : mMethod) {
            /**
             * 声明Listener
             */
            if (mMethod.size() > 0) {
                methodBuilder.addStatement("$T listener"+i, TypeUtil.ONCLICK_LISTENER);
            }
            TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(TypeUtil.ONCLICK_LISTENER)
                    .addMethod(MethodSpec.methodBuilder("onClick")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .addParameter(TypeUtil.ANDROID_VIEW, "view")
                            .addStatement("activity.$N()", method.getMethodName())
                            .build())
                    .build();
            methodBuilder.addStatement("listener"+i+" = $L ", listener);
            for (int id : method.ids) {
                methodBuilder.addStatement("finder.findView(activityOrView,$L).setOnClickListener(listener"+i+")", id);
            }
            i++;
        }
        /**
         * 构建类
         */
        String packageName = getPackageName(mClassElement);
        String className = getClassName(mClassElement, packageName);
        ClassName bindClassName = ClassName.get(packageName, className);
        TypeSpec finderClass = TypeSpec.classBuilder(bindClassName.simpleName() + FILE_NAME_END_WITH)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.INJECTOR, TypeName.get(mClassElement.asType())))
                .addMethod(methodBuilder.build())
                .build();
        return JavaFile.builder(packageName, finderClass).build();
    }


    public String getPackageName(TypeElement type) {
        return mElementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }


}
