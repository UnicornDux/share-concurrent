package org.ph.share.juc.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class ABA {

    public static void main(String[] args) {
        // showABA();
        referenceStamp();
    }

    public static void showABA() {
         AtomicInteger value = new AtomicInteger(1);
         new Thread(() -> {
            int val = value.get();
            log.debug("{} read value: {}", Thread.currentThread().getName(), val);

            LockSupport.parkNanos(1000000000);
            if (value.compareAndSet(1, 3)) {
                log.debug("{} change value from {} to {}", Thread.currentThread().getName(), val, 3);
            }else {
                log.error("{} change value failed", Thread.currentThread().getName());
            }
        }, "Thread1").start();

        new Thread(() -> {
            int val = value.get();
            log.debug("{} read value: {}", Thread.currentThread().getName(), val);
            if(value.compareAndSet(1, 2)) {
                log.debug("{} change value from {} to {}", Thread.currentThread().getName(), val, 2);
                val = value.get();
                log.debug("{} read value: {}", Thread.currentThread().getName(), val);

                if (value.compareAndSet(2, 1)) {
                    log.debug("{} change value from {} to {}", Thread.currentThread().getName(), val, 1);
                };
            }
        }, "Thread2").start();
    }


    /**
     *  Jdk 中使用了类似乐观锁的设计，添加一个版本号的方式来避免 ABA 问题
     */


    public static void referenceStamp() {

        // 定义 AtomicStampReference, Pair.reference = 1; Pair.stamp = 1;
        AtomicStampedReference reference = new AtomicStampedReference(1, 1);

        new Thread(() -> {
           int[] stampHolder = new int[1];

           // 获取到当前的版本和数据
           int value = (int) reference.get(stampHolder);
           int stamp = stampHolder[0];
            log.debug("{} read value: {}, stamp: {}", Thread.currentThread().getName(), value, stamp);
           //
            LockSupport.parkNanos(1000000000L);
            // Thread 通过 CAS 修改值为 3, stamp 是版本，每次修改都让版本 +1 保证版本的唯一性
            if (reference.compareAndSet(value, 3, stamp, stamp + 1)) {
                log.debug("{} change value from {} to {}", Thread.currentThread().getName(), value, 3);
            }else {
                log.debug("{} change the value 3 failed.", Thread.currentThread().getName());
            }
        }, "Thread1").start();

        new Thread(() -> {

            int[] stampHolder = new int[1];

            int value = (int) reference.get(stampHolder);
            int stamp = stampHolder[0];
            log.debug("{} read value: {}, stamp: {}", Thread.currentThread().getName(), value, stamp);
            // 线程通过 CAS 修改 value 值
            if (reference.compareAndSet(value, 2, stamp, stamp + 1)) {
                log.debug ("{} update from: {} to {}", Thread.currentThread().getName(), value, 2);
            }

            value = (int) reference.get(stampHolder);
            stamp = stampHolder[0];
            log.debug("{} read value: {}, stamp: {}", Thread.currentThread().getName(), value, stamp);
            if (reference.compareAndSet(value, 1, stamp, stamp + 1)) {
                log.debug("{} update from {} to {}", Thread.currentThread().getName(), value, 1);
            }
        }, "Thread2").start();
    }
}
