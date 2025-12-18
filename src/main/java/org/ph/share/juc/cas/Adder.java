package org.ph.share.juc.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
public class Adder {

    public static void main(String[] args)  {
        add();
    }

    //
    public static void add() {
        testLongAdder(10, 10000);
        log.info("======================================");
        testLongAdder(10, 200000);
        log.info("======================================");
        testLongAdder(100, 200000);
    }

    public static void testLongAdder(int thread, int times)  {
        try {
            long start = System.currentTimeMillis();
            longAdder(thread, times);
            long end = System.currentTimeMillis() - start;
            log.debug("条件 >>>>> 线程数: {}, 单线程操作计数: {}", thread, times);
            log.debug("结果 >>>>> LongAdder  方式增加计数 : {} 次耗时: {}ms ", thread * times, end);

            long start2 = System.currentTimeMillis();
            atomicLong(thread, times);
            long end2 = System.currentTimeMillis() - start2;
            log.debug("结果 >>>>> AtomicLong  方式增加计数 : {} 次耗时: {}ms", thread * times, end2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void atomicLong(int thread, int times) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(thread);
        AtomicLong atomicLong = new AtomicLong(0);
        for (int i = 0; i < thread; i++) {
            new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    atomicLong.incrementAndGet();
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }


    public static void longAdder(int thread, int times) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(thread);
        LongAdder longAdder = new LongAdder();
        for (int i = 0; i < thread; i++) {
            new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    longAdder.add(1);
                }
                latch.countDown();
            }).start();
        }
        latch.await();
    }
}
