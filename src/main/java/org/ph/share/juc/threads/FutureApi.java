package org.ph.share.juc.threads;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.FutureTask;

@Slf4j
public class FutureApi {

    public static void main(String[] args) {
        // Callable
        // 可以获取 线程返回的结果或者 捕获线程发生的异常
        FutureTask<String> task = new FutureTask<>(() -> {
            log.info("{} running", Thread.currentThread().getName());
            return "result";
        });
        new Thread(task).start();

        try {
            // 判断任务是否已经完成, 需要注意的是，不管任务是否正常结束，只要结束了都会返回 true
            while (!task.isDone()) {
                log.info("task is running");
                // 做一些其他工作，然后再次判定是否结束了
                // do smt else
            }
            // 判断任务是否被取消
            if (!task.isCancelled()) {
                // 阻塞式获取结果
                log.info("Callable task result: {}", task.get());
            }else {
                log.info("task is cancelled");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}