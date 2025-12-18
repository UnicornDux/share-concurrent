package org.ph.share.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ConditionUsage {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    private static volatile boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Waiter(),  "waiter").start();
        Thread.sleep(2000);
        new Thread(new Signaler(), "signaler").start();
    }

    static class Waiter implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try{
                while(!flag){
                    log.info("{} 当前条件不满足等待", Thread.currentThread().getName());
                    try {
                        condition.await();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                log.info("{} 接收到通知条件满足", Thread.currentThread().getName());
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }

    static class Signaler implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
               flag = true;
               log.info("{} 唤醒 waiter", Thread.currentThread().getName());
               condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
