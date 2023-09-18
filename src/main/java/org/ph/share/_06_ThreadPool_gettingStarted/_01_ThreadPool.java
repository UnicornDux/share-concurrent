package org.ph.share._06_ThreadPool_gettingStarted;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class _01_ThreadPool {
    public static void main(String[] args) throws InterruptedException {
        // 单独构建 10 线程执行任务
        newTenThousandThread();
        // 使用线程池执行 10 任务
        doTenThousandTaskWithThreadPool();
    }

    public static void newTenThousandThread() throws InterruptedException {
        Long start = System.currentTimeMillis();
        final Random random = new Random();
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Thread thread = new Thread(() -> {
               list.add(random.nextInt());
            });
            thread.start();
            thread.join();
        }
        System.out.println("new Thread cost: " + (System.currentTimeMillis() - start));
        System.out.println("new Thread size: " + list.size());
    }

    public static void doTenThousandTaskWithThreadPool() throws InterruptedException {
        Long start = System.currentTimeMillis();
        final Random random = new Random();
        final List<Integer> list = new ArrayList<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 100000; i++) {
            executorService.execute(() -> {
                list.add(random.nextInt());
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("Thread pool cost: " + (System.currentTimeMillis() - start));
        System.out.println("Thread pool size: " + list.size());
    }
}
