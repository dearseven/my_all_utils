package wc.c.libbase.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LList<T> {
    private List<T> _this = null;

    public LList put(T item) {
        _this.add(item);
        return this;
    }

    public T get(int i) {
        return _this.get(i);
    }

    public LList() {
        _this = new ArrayList<>();
    }

    public List<T> get(){
        return _this;
    }
}