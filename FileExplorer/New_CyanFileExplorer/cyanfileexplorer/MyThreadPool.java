
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPool extends ThreadPoolExecutor {

    private MyThreadPool(int corePoolSize, int maxPoolSize, long notCoreKeepAliveTime, BlockingQueue<Runnable> sPoolWorkQueue) {
        super(corePoolSize, maxPoolSize, notCoreKeepAliveTime, TimeUnit.SECONDS, sPoolWorkQueue);
    }

    public static MyThreadPool getNewThreadPoll() {
        //cpu数量
        int cpuCount = Runtime.getRuntime().availableProcessors();
        //核心线程数量
        int corePoolSize = cpuCount + 1;
        //连接池最大量
        int maxPoolSize = cpuCount * 2 + 1;
        //非核心线程的超时时长
        long notCoreKeepAliveTime = 1;
        //队列长度
        BlockingQueue<Runnable> sPoolWorkQueue =
                new LinkedBlockingQueue<Runnable>(128);
        return new MyThreadPool(corePoolSize, maxPoolSize, notCoreKeepAliveTime, sPoolWorkQueue);
    }


}