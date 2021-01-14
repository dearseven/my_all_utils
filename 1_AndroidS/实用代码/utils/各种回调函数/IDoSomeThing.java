
/**
 *  用这个抽象类就实现 public void doSomeThing(T t);一般用于做回调，不适合方法直接返回结果的情况
 */
public abstract class IDoSomeThing<T> implements IDoAny<T,T> {

    @Override
    public T doT2R(T t) {
        return null;
    }

    @Override
    public T doT(T t) {
        return null;
    }
}
