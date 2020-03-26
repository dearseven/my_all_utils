
public class RunMap<T , R> {
	private T _t;
	private R _r;

	public RunMap(T t) {
		this._t = t;
	}

	private RunMap(T t, R r) {
		this._t = t;
		this._r = r;
	}

	public T runSelf(F1<T> func) {
		_t = func.apply(_t);
		return _t;
	}

	public R runIt(F2<T, R> func) {
		_r = func.apply(_t);
		return _r;
	}

	public RunMap<T, R> runSelfMap(F1<T> func) {
		func.apply(_t);
		return new RunMap<T, R>(_t);
	}

	public RunMap<T, R> runItMap(F2<T, R> func) {
		_r = func.apply(_t);
		return new RunMap<T, R>(_t, _r);
	}

	public RunMap<T, R> runT2R(F2<T, R> func) {
		_r = func.apply(_t);
		return new RunMap(_r);
	}

	public T getSelf() {
		return _t;
	}

	public R getTo() {
		return _r;
	}

	public interface F1<T> {
		T apply(T t);
	}

	public interface F2<T, R> {
		R apply(T t);
	}

	@Override
	public String toString() {
		return "RunMap [_t=" + _t + ", _r=" + _r + "]";
	}

}
