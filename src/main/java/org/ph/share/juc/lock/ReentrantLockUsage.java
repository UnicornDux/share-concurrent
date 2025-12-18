package org.ph.share.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockUsage {

    public static void main(String[] args) {
        // baseApi();
        // trylockApi();
        interruptApi();
    }


    public static void baseApi() {
        for (int i = 0; i < 10; i++) {
            new Thread(() ->{
                try {
                    sell();
                } catch (InterruptedException e) {
                    log.error("interrupted");
                }
            }).start();
        }
    }

    private static final ReentrantLock lock = new ReentrantLock();
    private static int tickets = 8;

    public static void sell() throws InterruptedException {

        try{
            lock.lock();
            TimeUnit.MICROSECONDS.sleep(10);
            if (tickets > 0) {
                log.info("sell out {}", tickets--);
            }else {
                log.info("票卖完了");
            }
        } finally {
            // 一定要在 finally 里面释放锁，
            // ReentrantLock 不像 Synchronized, 出现异常不会自动释放锁
            //
            lock.unlock();
        }
    }

    public static void tryLockApi(String[] args) {
        try {
            new Thread(() -> {
                log.info("t1 线程开始运行.......");
                /**
                 * 直接尝试，立即返回
                if (!lock.tryLock()) {
                    log.info("{} 获取锁失败", Thread.currentThread().getName());
                }
                */

                // 带超时时间的锁获取
                try {
                    if (lock.tryLock(3, TimeUnit.SECONDS)) {
                        log.info("t1 等待 3s 内获取到锁");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }, "t1").start();

            // 主线程获取锁
           lock.lock();
           log.info("主线程获取到锁");

           Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static  void  interruptApi()  {

        try {
            Thread t2 = new Thread(() -> {
                log.info("t2 线程开始运行.......");

                try {
                    // 阻塞等待
                    lock.lockInterruptibly();
                    try {
                        log.info("t2 获取到了锁");
                        Thread.sleep(1000);
                    }finally {
                        lock.unlock();
                        log.info("t2 释放了锁");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.info("t2 线程收到中断信号");
                    // 重新恢复中断信号
                    Thread.currentThread().interrupt();
                }catch (Exception e) {
                    // 处理其他异常
                    e.printStackTrace();
                }
            }, "t2");

            // 主线程获取锁
            lock.lock();
            log.info("主线程获取到锁");

            t2.start();
            Thread.sleep(1000);

            log.info("触发 t2 线程中断");
            t2.interrupt();

            t2.join(5000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            log.info("main 线程释放了锁");
        }
    }
}