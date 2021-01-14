
/**
 *  用这个抽象类就实现 public T doT2R(T t)，返回一个R
 */
public  abstract class IDoTR<T,R> implements IDoAny<T,R>{
    @Override
    public void doSomeThing(T t) {
    }

    @Override
    public T doT(T t) {
        return null;
    }
}
