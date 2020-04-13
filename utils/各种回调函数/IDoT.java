
/**
 *  用这个抽象类就实现 public T doT(T t);返回一个T
 */
public  abstract class IDoT<T> implements IDoAny<T, T> {
    @Override
    public T doT2R(T t) {
        return null;
    }

    @Override
    public void doSomeThing(T t) {

    }


}
