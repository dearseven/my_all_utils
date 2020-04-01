
public class RunMap {
    private Object _t;
    private Object _r;

    public RunMap(Object t) {
        this._t = t;
    }


    private RunMap(Object t, Object r) {
        this._t = t;
        this._r = r;
    }

    public <T>T  runSelf(F1 func) {
        _t = func.apply(_t);
        return (T)_t;
    }

    public <T>T runIt(F2 func) {
        _r = func.apply(_t);
        return (T)_r;
    }

    public RunMap runSelfMap(F1 func) {
        func.apply(_t);
        return new RunMap(_t);
    }

    public RunMap runItMap(F2 func) {
        _r = func.apply(_t);
        return new RunMap(_t, _r);
    }

    public RunMap runT2R(F2 func) {
        _r = func.apply(_t);
        return new RunMap(_r);
    }

    public <T> T getSelf(Class<T> c) {
        return (T) c.cast(_t);
    }

    public <T> T getTo(Class<T> c) {
        return (T) c.cast(_r);
    }

    public interface F1<T> {
        T apply(T t);
    }

    public interface F2<T,R> {
        R apply(T t);
    }

    @Override
    public String toString() {
        return "RunMap [_t=" + _t + ", _r=" + _r + "]";
    }

}
