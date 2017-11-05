package com.jap.myprocess.model;


import com.jap.myprocess.annotations.BindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * 被BindView注解标记的字段的模型类
 */
public class BindViewField {

    private VariableElement mNeedInjectViewElmt;

    private int mResId;

    public BindViewField(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("Only field can be annotated with @%s",
                    BindView.class.getSimpleName()));
        }
        mNeedInjectViewElmt = (VariableElement) element;
        BindView bindView = mNeedInjectViewElmt.getAnnotation(BindView.class);
        mResId = bindView.value();
        if (mResId < 0) {
            throw new IllegalArgumentException(String.format("value() in %s for field % is not valid",
                    BindView.class.getSimpleName(), mNeedInjectViewElmt.getSimpleName()));
        }
    }

    /**
     * 这里返回的就是那个需要被注入的view的变量名
     * @return
     */
    public Name getMNeedInjectViewElmtName() {
        return mNeedInjectViewElmt.getSimpleName();
    }

    public int getResId() {
        return mResId;
    }

    /**
     * 这里返回的就是那个需要被注入的view的变量类型
     * @return
     */
    public TypeMirror getMNeedInjectViewElmtNameType() {
        return mNeedInjectViewElmt.asType();
    }
}