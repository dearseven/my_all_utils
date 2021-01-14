
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class SimpleCacheMine<K, V> {
    private Lock lock = new ReentrantLock(); //该锁功能类似于synchronized不过比synchronized更强大，效率更高
    private final int maxCapacity; //最大容量
    //采用map结构存储数据
    private final Map<K, V> emap; //极限池
    private final Map<K, V> lmap;  //缓存池

    public SimpleCacheMine(int maxCapacity) {
        this.maxCapacity = maxCapacity;  //设置最大容量
        /*
         * ConcurrentHashMap该类是线程安全的一个map，效率比hashtable高
         * 该类的实现原理是切割成多个小map，然后在需要同步的地方同步
         * */
        emap = new ConcurrentHashMap<K, V>(maxCapacity);
        /*
         * WeakHashMap 内部采用引用机制来改变自身存储的对象
         * 如果发现对象不再被引用了该类会清楚该对象，这样缓存用该类实现可以不用考虑清除机制的问题。
         * */
        lmap = new WeakHashMap<K, V>(maxCapacity);
    }

    public V get(K k) {
        V v = emap.get(k); //先从极限池取
        if (v == null) { //没取到 然后从缓存池中取,因为lmap是线程不安全的，所以要进行同步。
            try {
                lock.lock();
                v = lmap.get(k); //从缓存池中取
            } catch (Exception e) {

            } finally {
                lock.unlock(); //注意使用lock进行同步一定要用try catch代码块,在finally中释放
            }
            if (v != null) {
                emap.put(k, v); //如果找到了，从缓存池中放入极限池。
            }
        }

        return v;
    }

    public void put(K k, V v) {
        if (emap.size() >= maxCapacity) { //如果此时极限池装满了就往缓存池里面装
            try {
                lock.lock();
                lmap.putAll(emap);
            } catch (Exception e) {
            } finally {
                lock.unlock();
            }

            emap.clear();  //清空极限池
        }
        emap.put(k, v); //放入极限池
    }
}