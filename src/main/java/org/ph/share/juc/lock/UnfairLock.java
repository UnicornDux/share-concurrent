package org.ph.share.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

// ReentrantLock 的公平与非公平
@Slf4j
public class UnfairLock {

    // 默认就是非公平(通过传入的参数来控制)
    private static ReentrantLock lock = new ReentrantLock(false);

    public static void main(String[] args) {
        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                try {
                    lock.lock();
                    TimeUnit.MILLISECONDS.sleep(20);
                    log.info("running thread {}", "-----------");
                } catch (InterruptedException e) {

                } finally {
                    lock.unlock();
                }
            }, "thread-"+i).start();
        }

        try {
            Thread.sleep(200);
        }catch (InterruptedException e){}

        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                try {
                    lock.lock();
                    Thread.sleep(10);
                    log.info("insert thread {} ", ">>>>>>>>>>");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            },"select-"+i).start();
        }
    }
}
