package org.ph.share.juc.lock;

import lombok.extern.slf4j.Slf4j;
import org.ph.share.SmallTool;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class SemaphoreUsage {

    private static final Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {

        semaphore.acquire();
        SmallTool.printTimeAndThread("主线程获取信号");
        try {
            Thread t2 = new Thread(() -> {
                /*
                try {
                    semaphore.acquire();
                    // handler you work
                } catch (InterruptedException e) {
                    // 中断导致的唤醒, 之后直接执行了 finally, 进行了释放，导致了非正常的程序运行
                    e.printStackTrace();
                    SmallTool.printTimeAndThread("t2 被中断");
                    return;
                }
                finally {
                    semaphore.release();
                    SmallTool.printTimeAndThread("t2 释放 信号");
                }
                */
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SmallTool.printTimeAndThread("t2 被中断");
                    return;
                }
                try {
                    // 将休眠和任务执行分开
                    // handler you work
                    work();
                } finally {
                    semaphore.release();
                    SmallTool.printTimeAndThread("t2 释放 信号");
                }
            }, "t2");

            t2.start();

            SmallTool.printTimeAndThread("给 t2 发送中断信号");
            t2.interrupt();

            TimeUnit.MINUTES.sleep(1);

        } finally {
            semaphore.release();
            SmallTool.printTimeAndThread("主线程释放信号");
        }
    }

    //
    public static void work() {
        SmallTool.printTimeAndThread("正在进行计算...");
    }
}

// 作为限流器使用
@Slf4j
class SemaphoreLimit {

    private static final Semaphore semaphore = new Semaphore(1);

    private static final Executor executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
           executor.execute(() -> getProductInfo());
        }
    }

    // 阻塞等待服务
    public static String getProductInfo() {
        try {
            semaphore.acquire();
        }catch (InterruptedException e) {
            e.printStackTrace();
            log.warn("{} 线程被中断", Thread.currentThread().getName());
            return null;
        }
        try {
            log.info("请求服务");
            SemaphoreUsage.work();
        }finally {
            semaphore.release();
        }
        return "ProductInfo";
    }

    // 没有令牌直接失败返回
    public static String userInfo() {
        if (!semaphore.tryAcquire()){
            log.error("request limited");
            return "request limited";
        }
        try {
            SemaphoreUsage.work();
        }finally {
            semaphore.release();
        }
        return "User info";
    }
}


// 控制同时在线的登录人数
@Slf4j
class LimitLogin {

    static final int  MAX_PERMIT_LOGIN_ACCOUNT = 10;
    static final LoginService loginService = new LoginService(MAX_PERMIT_LOGIN_ACCOUNT);
    public static void main(String[] args) {
        IntStream.range(0, 20).forEach(item -> {
            new Thread(() -> {
                boolean login = loginService.login();
                if (!login) {
                    log.info("{} is refused to exceed max online account", Thread.currentThread().getName());
                    return;
                }
                try {
                    // 简单模拟在线用户的操作
                    simulateWork();
                }finally {
                     loginService.logout();
                }
            }, "user" + item).start();
        });
    }

    public static void simulateWork() {
        log.info("login user working........");
    }
}




