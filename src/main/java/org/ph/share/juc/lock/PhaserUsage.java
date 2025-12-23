package org.ph.share.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.Phaser;

@Slf4j
public class PhaserUsage {


    public static void main(String[] args) {

        final Phaser phaser = new Phaser(){
            // 重写该方法来实现各阶段完成后执行的操作
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                // 参与者数量
                int staff = registeredParties - 1;
                switch (phase) {
                    case 0:
                        log.info("都到公司了，出发去公园: 人数: {}", staff);
                        break;
                    case 1:
                        log.info("都到公园了，出发去餐厅: 人数: {}", staff);
                        break;
                    case 2:
                        log.info("都到餐厅了，开始用餐: 人数: {}", staff);
                        break;
                    default:
                        log.info("");
                }
                return registeredParties == 1;
            }
        };

        final StaffTask staffTask = new StaffTask();

        // 主线程参与
        phaser.register();

        // 全程参与的线程
        for (int i = 0; i < 3; i++) {
            // 子线程参与
            phaser.register();
            new Thread(() -> {


                try {
                    staffTask.step1();
                    phaser.arriveAndAwaitAdvance();

                    staffTask.step2();
                    phaser.arriveAndAwaitAdvance();

                    staffTask.step3();
                    phaser.arriveAndAwaitAdvance();

                    staffTask.step4();
                    // 注销
                    phaser.arriveAndDeregister();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        // 参加活动中途退出
        for (int i = 0; i < 2; i++) {
            phaser.register();
            new Thread(() -> {

                try {
                    staffTask.step1();
                    phaser.arriveAndAwaitAdvance();

                    staffTask.step2();
                    phaser.arriveAndAwaitAdvance();
                    log.info("离开，直接回家。。。。。。。。。。。。。。。。。。。。");
                    phaser.arriveAndDeregister();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        while(!phaser.isTerminated()) {
            // 中途加入
            int phase = phaser.arriveAndAwaitAdvance();
            if (phase == 2) {
                for (int i = 0; i < 4; i++) {
                    phaser.register();
                    new Thread(() -> {
                        try {
                            staffTask.step3();
                            phaser.arriveAndAwaitAdvance();

                            staffTask.step4();
                            phaser.arriveAndDeregister();

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }).start();
                }
            }
        }

    }

    final static Random random = new Random();

    static class StaffTask {
        //
        public void step1() throws InterruptedException {
            log.info("线程: [ {} ] 从家出发", Thread.currentThread().getName());
            Thread.sleep(random.nextInt(1000));
            log.info("线程: [ {} ] 到达公司", Thread.currentThread().getName());
        }
        public void step2() throws InterruptedException {
            log.info("线: [ {} ] 从公司出发", Thread.currentThread().getName());
            Thread.sleep(random.nextInt(1000));
            log.info("线程: [ {} ] 到达公园", Thread.currentThread().getName());
        }
        public void step3() throws InterruptedException {
            log.info("线: [ {} ] 从公园出发", Thread.currentThread().getName());
            Thread.sleep(random.nextInt(1000));
            log.info("线程: [ {} ] 到达餐厅", Thread.currentThread().getName());
        }
        public void step4() throws InterruptedException {
            log.info("线: [ {} ] 开始就餐", Thread.currentThread().getName());
            Thread.sleep(random.nextInt(1000));
            log.info("线程: [ {} ] 聚会结束", Thread.currentThread().getName());
        }
    }
}
