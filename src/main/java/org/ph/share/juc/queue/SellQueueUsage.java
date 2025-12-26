package org.ph.share.juc.queue;

import lombok.extern.slf4j.Slf4j;
import org.ph.share.model.Result;
import org.ph.share.model.UserRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class SellQueueUsage {

    // 启动 10 个线程
    // 库存 6 个
    // 生成一个合并队列
    // 每个用户都能拿到自己的请求响应
    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        SellDemo demo = new SellDemo();

        // 合并扣减库存
        demo.mergeJob();
        Thread.sleep(2000);

        CountDownLatch latch = new CountDownLatch(10);
        //
        log.info("------------库存初始数量: ({})------------------", demo.stock);
        List<Future<Result>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final Long orderId = i + 100L;
            final Long userId = Long.valueOf(i);
            UserRequest request = new UserRequest(orderId, userId, 1);

            Future<Result> future = executorService.submit(() -> {
                latch.countDown();
                latch.await(1, TimeUnit.SECONDS);
                return demo.operate(request);
            });
            futures.add(future);
        }

        log.info("------------获取到结果-------------------------");
        futures.forEach(future -> {
            try {
               Result result = future.get(3000, TimeUnit.MILLISECONDS);
               log.info("{}", result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        demo.stopMerge();

        // 关闭线程池
        executorService.shutdown();
    }

    static class SellDemo {

        // 模拟数量
        public static Integer stock = 6;
        private static volatile boolean running = true;

        private BlockingQueue<RequestPromise> queue = new LinkedBlockingQueue<>(10);

        public Result operate(UserRequest request) throws InterruptedException {
            // 阈值判断
            // 队列的创建
            RequestPromise promise = new RequestPromise(request);
            synchronized (promise) {
                boolean success = queue.offer(promise, 100, TimeUnit.MILLISECONDS);
                if (!success) {
                    return new Result(false, "系统繁忙");
                }
                try {
                    promise.wait(200);
                    if (promise.getResult() == null){
                        return new Result(false, "等待超时");
                    }
                } catch (InterruptedException e) {
                    return new Result(false, "被中断");
                }
            }
            return promise.getResult();
        }

        // 由于合并线程一直在运行，因此程序不会结束
        public void mergeJob(){
            new Thread(() -> {
                List<RequestPromise> list = new ArrayList<>();
                while(running) {
                    if (queue.isEmpty()) {
                        try {
                            Thread.sleep(10);
                            continue;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    while (queue.peek() != null) {
                        list.add(queue.poll());
                    }

                    int sum = list.stream().mapToInt(e -> e.getRequest().getCount()).sum();
                    // 两种情况
                    if (sum <= stock) {
                        stock -= sum;
                        // notify user
                        list.forEach(promise -> {
                            promise.setResult(new Result(true, "成功"));
                            synchronized (promise) {
                                promise.notify();
                            }
                        });
                        list.clear();
                        continue;
                    }
                    for (RequestPromise promise : list) {
                        int count = promise.getRequest().getCount();
                        if (count <= stock) {
                            stock -= count;
                            promise.setResult(new Result(true, "成功"));
                        }else {
                            promise.setResult(new Result(false, "库存不足"));
                        }
                        synchronized (promise) {
                            promise.notify();
                        }
                    }
                    list.clear();
                }

            }, "merger").start();
        }

        public void stopMerge() {
            running = false;
        }
    }


    static class RequestPromise {

        private UserRequest request;

        private Result result;

        public RequestPromise(UserRequest request) {
            this.request = request;
        }

        public UserRequest getRequest() {
            return request;
        }

        public void setRequest(UserRequest request) {
            this.request = request;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }
}
