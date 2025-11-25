package org.ph.share.juc.pool;


import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PoolShutdown {
    // 线程池的关闭


    public static void main(String[] args) throws InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(5);


        for (int i = 0; i < 10; i++) {
            service.submit(() -> {
                try {
                    // 执行任务
                    log.info("{} 正在执行任务", Thread.currentThread().getName());
                    Thread.sleep(5000);
                }catch (InterruptedException e) {
                    // 重新设置中断状态
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }finally {
                    log.info("{} 任务执行完毕", Thread.currentThread().getName());
                }
            });
        }
        service.shutdown();

        boolean b = service.awaitTermination(3, TimeUnit.SECONDS);
        if (!b) {
            List<Runnable> runnables = service.shutdownNow();
            log.warn("还有 {} 个任务没有完成，线程池强制退出", runnables.size());
        }
    }
}
