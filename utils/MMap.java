package wc.c.libbase.utils;


import java.util.HashMap;
import java.util.Map;

public final class MMap<KT, VT> {
    private Map<KT, VT> _this = null;

    public  MMap put(KT k, VT v) {
        _this.put(k, v);
        return this;
    }

    public VT get(KT k) {
        return _this.get(k);
    }

    public MMap() {
        _this = new HashMap<>();
    }

    public Map<KT, VT> get(){
        return _this;
    }
}