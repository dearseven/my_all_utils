
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池规则:<br></>
 * 当进入任务时(execute方法提交)，若核心线程没满，则核心运行。<br></>
 * 若核心全满但是sPoolWorkQueue未满，则加入队列<br></>
 * 若以上全满，则开启非核心线程运行<br></>
 * 若开启非核心达到最大线程池量，责开始拒绝任务<br><br/>
 * <p>
 * -----------
 * <p>
 * 1.shutDown()  关闭线程池，不影响已经提交的任务<br><br/>
 * 2.shutDownNow() 关闭线程池，并尝试去终止正在执行的线程<br><br/>
 * 3.allowCoreThreadTimeOut(boolean value) 允许核心线程闲置超时时被回收<br><br/>
 * 4.submit 一般情况下我们使用execute来提交任务，但是有时候可能也会用到submit，使用submit的好处是submit有返回值<br><br/>
 * Created by wx on 2017/5/14.
 */
 
 /**
 *下面都假设任务队列没有大小限制：
如果线程数量<=核心线程数量，那么直接启动一个核心线程来执行任务，不会放入队列中。
如果线程数量>核心线程数，但<=最大线程数，并且任务队列是LinkedBlockingDeque的时候，超过核心线程数量的任务会放在任务队列中排队。
如果线程数量>核心线程数，但<=最大线程数，并且任务队列是SynchronousQueue的时候，线程池会创建新线程执行任务，这些任务也不会被放在任务队列中。这些线程属于非核心线程，在任务完成后，闲置时间达到了超时时间就会被清除。
如果线程数量>核心线程数，并且>最大线程数，当任务队列是LinkedBlockingDeque，会将超过核心线程的任务放在任务队列中排队。也就是当任务队列是LinkedBlockingDeque并且没有大小限制时，线程池的最大线程数设置是无效的，他的线程数最多不会超过核心线程数。
如果线程数量>核心线程数，并且>最大线程数，当任务队列是SynchronousQueue的时候，会因为线程池拒绝添加任务而抛出异常。
任务队列大小有限时

当LinkedBlockingDeque塞满时，新增的任务会直接创建新线程来执行，当创建的线程数量超过最大线程数量时会抛异常。
SynchronousQueue没有数量限制。因为他根本不保持这些任务，而是直接交给线程池去执行。当任务数量超过最大线程数时会直接抛异常。
--------------------- 
 /

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