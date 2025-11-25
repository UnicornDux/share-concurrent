package org.ph.share.juc.twophase;

import lombok.extern.slf4j.Slf4j;

/**
 * 利用两阶段终止模式来完成监控线程的优雅结束
 */
@Slf4j
public class GracefulMonitor extends Thread {

    private volatile boolean terminated = false;

    @Override
    public void run() {
        while(!Thread.interrupted() && !terminated) {
            log.info("监控程序正在运行");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.warn("监控线程被中断， 准备退出....");
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
        log.info("监控线程正在执行清理退出操作...");
        releaseResource();
    }

    public void terminate() {
        // 设置标志变量为 true, 并等待一段时间
        terminated = true;
        try {
            join(5000); // 等待 5s, 期间监控线程会检查 terminated 状态
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releaseResource() {
        log.info("监控线程正在释放资源，进行必要的清理工作...");
    }


    public static void main(String[] args) throws InterruptedException {

        GracefulMonitor t = new GracefulMonitor();
        // 启动线程
        t.start();
        // 主线程休眠时间，监控线程在执行监控操作
        Thread.sleep(10000);
        // 终止监控线程
        t.interrupt();

        // 避免主线程执行结束
        Thread.sleep(100000);
    }

}
