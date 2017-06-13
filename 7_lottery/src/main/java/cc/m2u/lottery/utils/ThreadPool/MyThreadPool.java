package cc.m2u.lottery.utils.ThreadPool;

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
