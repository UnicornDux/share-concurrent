package org.ph.share.juc.threads;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class ThreadCreate {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // Runnable
        // run() 没有返回值
        new Thread(() -> {
            log.info("{} running", Thread.currentThread().getName());

            // 线程中产生的异常无法在外部线程中被捕获
            throw new RuntimeException("test");
        }).start();

        // Callable
        // 可以获取 线程返回的结果或者 捕获线程发生的异常
        FutureTask<String> task = new FutureTask<>(() -> {
            log.info("{} running", Thread.currentThread().getName());
            return "result";
        });
        new Thread(task).start();
        log.info("Callable task result: {}", task.get());
    }
}
