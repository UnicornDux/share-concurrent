package org.ph.share._06_ThreadPool_gettingStarted;


import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池 ( 7 个参数的含义)
 * -----------------------------------------------------------
 * > corePoolSize : 核心线程数
 * > maximumPoolSize : 最大线程数
 * > keepAliveTime : 非核心线程的空闲状态的存活时间
 * > Timeunit : 配合 keepAliveTime, 作为时间单位
 * > workQueue : 工作队列，阻塞队列
 * > ThreadFactory : 创建线程的工厂
 * > RejectExecutionHandler : 拒绝策略对应的处理器
 */
public class _04_CustomThreadPool {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                10,
                20,
                100,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        try {
            for (int i = 0; i < 100; i++) {
                MyTask task = new MyTask(i);
                threadPool.execute(task);
            }
        } catch (Exception e){
           e.printStackTrace();
        }finally{
           threadPool.shutdown();
        }
    }
}
class MyTask implements Runnable {
    private int i = 0;
    public MyTask(int i) {
        this.i = i;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "程序员做第" + i + "个项目");
        try {
            Thread.sleep(3000L); // 业务逻辑
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
