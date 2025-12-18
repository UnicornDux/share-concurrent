package org.ph.share.juc.lock;


import lombok.extern.slf4j.Slf4j;
import org.ph.share.SmallTool;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

@Slf4j
public class CyclicBarrierUsage {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

        // 定义 barrier, 值为 11
        CyclicBarrier barrier = new CyclicBarrier(11, null);
        // 创建 10 个线程
        for (int i = 0; i < 10; i++) {
            new Thread(new Tourist(i, barrier)).start();
        }
        // 主线程阻塞，等待所有的旅客上打包
        barrier.await();
        log.info("所有游客都上车了");

        Thread.sleep(1000);

        // 到达景点
        // 主线程等待所有的游客都下车
        barrier.await();
        log.info("所有的游客都下车了");

    }


    private static class Tourist implements  Runnable {
        private final int tourId;
        private final CyclicBarrier barrier;

        public Tourist(int tourId, CyclicBarrier barrier){
            this.tourId = tourId;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            log.info(" 游客: {} 乘坐旅游大巴", tourId);
            // 模拟坐车
            this.spendSeveralSeconds();
            // 上车后等待其他人
            this.waitAndPrint("游客: %s 上车，等待其他游客上车", tourId);

            log.info("游客: {} 到达目的地.", tourId);
            //
            this.spendSeveralSeconds();
            this.waitAndPrint("游客: %s 下车，等待其他人下车", tourId);
        }

        private void waitAndPrint(String s, int tourId) {
            SmallTool.printTimeAndThread(String.format(s, tourId));
            try {
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                // ignore
            }

        }

        private void spendSeveralSeconds() {
            try {
                TimeUnit.SECONDS.sleep(current().nextInt(10));
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}
