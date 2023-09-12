package org.ph.share._06_ThreadPool_gettingStarted;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class _02_InternalThreadPool {

    public static void main(String[] args) {
        internalThreadPool();
    }
    public static void internalThreadPool() {
        ExecutorService executorService = null;
        ScheduledExecutorService scheduledExecutorService = null;
        try {
            // 创建一个单线程的 Executor, 确保任务对了，串行执行
            // executorService = Executors.newSingleThreadExecutor();

            // 创建一个可以缓存的线程池，如果当前线程池的规模超出了处理需求，将回收空的线程，
            // 当需求增加时, 会增加线程数量，线程池规模无限制
            // executorService = Executors.newCachedThreadPool();

            // 固定长度的线程池，当到达最大线程数量时，线程池的规模将不再变化
            executorService = Executors.newFixedThreadPool(3);
            for (int i = 0; i < 100; i++) {
                executorService.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "办理业务");
                });
            }

            // 可以定时执行的线程池
            scheduledExecutorService = Executors.newScheduledThreadPool(2);
            for (int i = 0; i < 20; i++) {
                scheduledExecutorService.schedule(() -> {
                    System.out.println(Thread.currentThread().getName() + " > Schedule thread pool execute");
                }, 5, TimeUnit.SECONDS);
            }
        }catch (Exception e) {

            System.out.println("代码执行异常，异常原因: " + e.getMessage());
        }finally {
            // 线程池一定要关闭, 并且关闭代码需要放在 finally 代码块中，防止程序异常导致无法关闭
            // 占用系统资源, 从而导致内存泄露等问题
            if (executorService != null) {
                // shutdown 不会停止正在执行的线程,等待线程执行完成后关闭(不会阻塞等待)
                executorService.shutdown();
                // 判断线程池是否真正关闭，线程池关闭代表线程也已经都执行完毕了
                System.out.println("executorService1: " + executorService.isTerminated());

                // 只会等待当前正在执行的线程完成，不会等待线程池中所有任务都执行完成.
                executorService.shutdownNow();
                System.out.println("executorService2: " + executorService.isTerminated());
            }
            if (scheduledExecutorService != null) {
                scheduledExecutorService.shutdown();
                // 判断线程池是否真正关闭，并且代表线程也已经都执行完毕了
                System.out.println("scheduleExecutor1 : "  + scheduledExecutorService.isTerminated());

                try {
                    // 等待线程池关闭，这就意味着线程池中所有的线程执行完成，可以传入等待时间，表示允许等待多久
                    scheduledExecutorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("scheduleExecutor2 : "  + scheduledExecutorService.isTerminated());
            }
        }
    }
}
