package wc.c.libbase.utils;

public interface IDoAny<T, R> {
    public R doT2R(T t);

    public void doSomeThing(T t);

    public T doT(T t);

}

