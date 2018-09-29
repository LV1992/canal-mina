package canalmina.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 *
 * @author yihang.lv 2018/9/29、10:25
 */
public class ThreadPoolUtil {

    private ThreadPoolUtil() {
    }

    /**
     * int corePoolSize 核心线程数 即使线程处于闲置状态也不受keepAliveTime的影响，除非设置 allowThreadTimeOut 为true
     * int maximumPoolSize 线程池所能容纳的最大线程数，超出这个数量将会阻塞，当任务队列为没有大小的LinkedBlockingDequeue时这个值无效
     * long keepAliveTime 非核心线程的闲置线程超出这个值的时候将会被回收
     * TimeUnit unit 指定keepAliveTime的单位，如TimeUnit.SECONDS。当将allowCoreThreadTimeOut设置为true时对corePoolSize生效。
     * BlockingQueue<Runnable> workQueue 线程池的任务队列 常用的有三种队列，SynchronousQueue,LinkedBlockingDeque,ArrayBlockingQueue。
     * ThreadFactory threadFactory 默认的有 DefaultThreadFactory 可以自定义线程工厂
     * RejectedExecutionHandler handler 当线程池的资源被全部用完，添加新线程被拒绝时，会调用该接口
     */
    private static final int corePoolSize = Runtime.getRuntime().availableProcessors();
    private static final int maximumPoolSize = corePoolSize * 16 + 1;
    private static final long keepAliveTime = 10L;

    public static ThreadPoolExecutor getInstance() {
        return PoolLoader.threadPool;
    }

    private static class PoolLoader {
        private final static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<>(maximumPoolSize));
    }
}
