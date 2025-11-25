package org.ph.share.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class LockSupportModel {

    public static void main(String[] args) throws InterruptedException {

        Thread parkThread = new Thread(() -> {
            log.info("{}, 开始执行", "ParkThread");
            // 当没有许可时，当前线程暂停运行，有了许可时，用掉这个许可，当前线程恢复运行.
            LockSupport.park();
            log.info("{}, 执行完成", "ParkThread");
        });
        parkThread.start();

        Thread.sleep(3000);
        log.info("唤醒: {}", "parkThread");

        // 给 parkThread 线程发放许可证（多次连续调用 unpark 只会发放一个许可证)
        LockSupport.unpark(parkThread);
    }
}
